package com.hyejin.counselor.core.kafka;

import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.repository.ChatRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchMessageConsumer {

    private final ChatRepository chatRepository;

    // 채팅방별 최신 메시지 캐시 (Short Polling용)
    private final Map<String, ConcurrentLinkedQueue<Chat>> recentMessagesCache = new ConcurrentHashMap<>();

    private final List<Chat> messageBuffer = Collections.synchronizedList(new ArrayList<>());
    private final List<Acknowledgment> ackBuffer = Collections.synchronizedList(new ArrayList<>());

    private static final int BATCH_SIZE = 1000;
    private static final int FLUSH_INTERVAL_MS = 5000; // 5초
    private static final int CACHE_MAX_SIZE = 100; // 캐시 최대 메시지 수
    private static final long CACHE_EXPIRE_MS = 60000; // 1분

    @KafkaListener(topics = "chat-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(Chat chat, Acknowledgment acknowledgment) {
        log.debug("Consumed message: roomId={}, senderId={}", chat.getCounselId(), chat.getSenderId());

        // 1. 최신 메시지 캐시에 추가 (Short Polling용)
        recentMessagesCache.computeIfAbsent(chat.getCounselId(), k -> new ConcurrentLinkedQueue<>()).add(chat);

        // 캐시 크기 제한
        ConcurrentLinkedQueue<Chat> cache = recentMessagesCache.get(chat.getCounselId());
        while (cache.size() > CACHE_MAX_SIZE) {
            cache.poll();
        }

        // 2. 버퍼에 추가 (배치 처리를 위해)
        messageBuffer.add(chat);
        ackBuffer.add(acknowledgment);

        // 3. 버퍼 크기가 배치 크기에 도달하면 즉시 flush
        if (messageBuffer.size() >= BATCH_SIZE) {
            flushMessages();
        }
    }

    // Short Polling으로 최신 메시지 조회
    public List<Chat> getRecentMessages(String counselId, String lastMessageId) {
        ConcurrentLinkedQueue<Chat> messages = recentMessagesCache.getOrDefault(counselId, new ConcurrentLinkedQueue<>());

        if (messages.isEmpty()) {
            return Collections.emptyList();
        }

        // 이미 새로운 메시지가 있다면 즉시 응답
        List<Chat> newMessages = messages.stream().filter(m -> {
            // lastMessageId가 비어있으면 모든 메시지 반환
            if (lastMessageId == null || lastMessageId.isEmpty()) {
                return true;
            }
            // compareTo() > 0 이면 m.getMessageId()가 더 최신
            return m.getMessageId().compareTo(lastMessageId) > 0;
        }).collect(Collectors.toList());

        return newMessages;
    }

    // 캐시 정리 (1분마다)
    @Scheduled(fixedDelay = CACHE_EXPIRE_MS)
    public void cleanupCache() {
        recentMessagesCache.forEach((roomId, messages) -> {
            if (messages.isEmpty()) {
                recentMessagesCache.remove(roomId);
            }
        });
    }

    @Scheduled(fixedDelay = FLUSH_INTERVAL_MS)
    public void scheduledFlush() {
        if (!messageBuffer.isEmpty()) {
            flushMessages();
        }
    }

    private synchronized void flushMessages() {
        if (messageBuffer.isEmpty()) {
            return;
        }

        // 버퍼의 모든 메시지를 복사
        List<Chat> messagesToSave = new ArrayList<>(messageBuffer);
        List<Acknowledgment> acksToCommit = new ArrayList<>(ackBuffer);

        // 버퍼 비우기
        messageBuffer.clear();
        ackBuffer.clear();

        try {
            // MongoDB에 벌크 insert
            log.info("Flushing {} messages to MongoDB", messagesToSave.size());
            chatRepository.saveAll(messagesToSave);

            // Kafka offset commit
            if (!acksToCommit.isEmpty()) {
                acksToCommit.get(acksToCommit.size() - 1).acknowledge();
                log.debug("Acknowledged {} messages", acksToCommit.size());
            }

            log.info("Successfully saved {} messages to MongoDB", messagesToSave.size());
        } catch (Exception e) {
            log.error("Failed to save messages to MongoDB", e);
            // 실패한 메시지를 다시 버퍼에 추가 (재시도 로직)
            messageBuffer.addAll(messagesToSave);
            ackBuffer.addAll(acksToCommit);
            throw new RuntimeException("Failed to flush messages", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down - flushing remaining messages");
        flushMessages();
    }
}
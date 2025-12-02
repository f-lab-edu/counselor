package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.common.enums.ErrorCode;
import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.kafka.KafkaProducer;
import com.hyejin.counselor.core.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final KafkaProducer kafkaProducer;
    private final SimpMessagingTemplate messagingTemplate; //socket
    private final ChatRepository chatRepository;
    private final Map<String, List<Chat>> chatStore = new ConcurrentHashMap<>();
    private final Map<String, List<DeferredResult<List<Chat>>>> waiters = new ConcurrentHashMap<>();

    //kafka 사용
    public void sendMessage(Chat chat) {
        kafkaProducer.sendMessage(chat);
    }

//    // socket 사용
//    public void chatSave(Chat chat) {
//        if (chat.getCounselId() == null || chat.getCounselId().trim().isEmpty()) {
//            throw new IllegalArgumentException(ErrorCode.INVALID_CHAT_INFO.getCode());
//        }
//        String destination = "/sub/chat/" + chat.getCounselId();
//        messagingTemplate.convertAndSend(destination, chat.getMsg());
//
//        // db 저장
//        chat.setRegDate(nowDate());
//        chatRepository.save(chat);
//
//    }

    private Chat chatSave(Chat chat) {

        if (chat.getCounselId() == null || chat.getCounselId().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_CHAT_INFO.getCode());
        }

        // db 저장
        chat.setRegDate(nowDate());
        return chatRepository.save(chat);

    }

    public List<Chat> chatList(Chat chat) {
        return chatRepository.findAllByCounselIdOrderByRegDateDesc(chat.getCounselId());
    }

    public List<Chat> waitMessage(String counselId, String lastMessageId) {
        List<Chat> messages = chatStore.getOrDefault(counselId, new ArrayList<>());

        // 이미 새로운 메시지가 있다면 즉시 응답
        List<Chat> newMessages = messages.stream().filter(m -> {
            // lastMessageId가 비어있으면 모든 메시지 반환
            if (lastMessageId == null || lastMessageId.isEmpty()) {
                return true;
            }
            // compareTo() > 0 이면 m.getId()가 더 최신
            return m.getId().compareTo(lastMessageId) > 0;
        }).collect(Collectors.toList());

        return newMessages;
    }

    public void addMessage(Chat chat) {
        // 메세지 DB 저장
        chatSave(chat);

        // 메모리에 저장
        List<Chat> list = chatStore.computeIfAbsent(chat.getCounselId(), id -> new ArrayList<>());
        list.add(chat);
    }
}


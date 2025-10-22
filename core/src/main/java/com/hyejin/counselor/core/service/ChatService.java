package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.common.enums.ErrorCode;
import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.kafka.KafkaProducer;
import com.hyejin.counselor.core.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final KafkaProducer kafkaProducer;
    private final SimpMessagingTemplate messagingTemplate; //socket
    private final ChatRepository chatRepository;

    public void sendMessage(Chat chat) {
        kafkaProducer.sendMessage(chat);
    }

    public void chatSave(Chat chat) {
        if (chat.getCounselId() == null || chat.getCounselId().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_CHAT_INFO.getCode());
        }
        String destination = "/sub/chat/" + chat.getCounselId();
        messagingTemplate.convertAndSend(destination, chat.getMsg());

        // db 저장
        chat.setRegDate(nowDate());
        chatRepository.save(chat);

    }
}


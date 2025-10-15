package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final KafkaProducer kafkaProducer;

    public void sendMessage(Chat chat) {
        kafkaProducer.sendMessage(chat);
    }

}


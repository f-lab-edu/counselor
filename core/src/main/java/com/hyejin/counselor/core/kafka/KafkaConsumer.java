package com.hyejin.counselor.core.kafka;

import com.hyejin.counselor.core.entity.Chat;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = "chat-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(Chat chat) {
        System.out.println("==========received message=============");
        System.out.println("  - msg: " + chat.getMessage());

        // db에 저장
        // 소켓연결
    }
}

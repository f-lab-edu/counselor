package com.hyejin.counselor.core.kafka;

import com.hyejin.counselor.core.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Chat> kafkaTemplate;
    private static final String TOPIC_NAME = "chat-topic"; // 사용할 토픽 이름

    public void sendMessage(Chat chat) {
        System.out.println("Producer sending message to topic '" + TOPIC_NAME + "': " + chat.getMessage());
        // 메시지 전송
        kafkaTemplate.send(TOPIC_NAME, chat);
    }
}

package com.hyejin.counselor.core.kafka;

import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Chat> kafkaTemplate;
    private static final String TOPIC_NAME = "chat-topic"; // 사용할 토픽 이름
    private final ChatRepository chatRepository;

    public void sendMessage(Chat chat) {
        System.out.println("Producer sending message to topic '" + TOPIC_NAME + "': " + chat.getMsg());
        chat.setRegDate(nowDate());

        // 메시지 전송
        kafkaTemplate.send(TOPIC_NAME, chat.getCounselId(), chat)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Message sent successfully: offset={}",
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send message to Kafka", ex);
                    }
                });

    }
}

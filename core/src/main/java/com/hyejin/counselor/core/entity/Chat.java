package com.hyejin.counselor.core.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    private String id;
    private String counselId; // 상담 ID
    private String senderId;
    private String senderType;
    private String msg;
}

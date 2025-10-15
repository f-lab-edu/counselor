package com.hyejin.counselor.core.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat") // 실제 몽고 DB 컬렉션 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    private String id;
    private String message;
}

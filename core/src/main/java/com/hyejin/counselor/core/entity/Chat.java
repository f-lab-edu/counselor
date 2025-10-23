package com.hyejin.counselor.core.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "counselId_regDate_idx", def = "{'counselId': -1, 'regDate': -1}")
})
public class Chat {
    @Id
    private String id;
    private String counselId; // 상담 ID
    private String senderId;
    private String senderType;
    private String msg;
    private String regDate;
}

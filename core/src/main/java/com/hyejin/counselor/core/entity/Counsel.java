package com.hyejin.counselor.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counsel") // 실제 몽고 DB 컬렉션 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Counsel {
    /**
     * "counselId":91,
     *  "userId": 23,
     *  "counselorId":null,
     *  "status": "R",
     *  "regDate": yyyyMMddHHmmss
     */
    @Id
    private String id;
    private String userId;
    private String counselorId;
    private String status;
    private String regDate;

}

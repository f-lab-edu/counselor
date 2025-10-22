package com.hyejin.counselor.core.repository;

import com.hyejin.counselor.core.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    List<Chat> findAllByCounselId(String counselId);
}


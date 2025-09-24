package com.hyejin.counselor.core.repository;

import com.hyejin.counselor.core.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TestRepository extends MongoRepository<User, String> {
    User findUserByName(String name);

}

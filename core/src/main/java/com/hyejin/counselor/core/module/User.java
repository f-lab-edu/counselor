package com.hyejin.counselor.core.module;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.hyejin.counselor.core.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class User {
    private final UserRepository repository;

    public Object save(com.hyejin.counselor.core.entity.User user) {
        return repository.save(user);
    }
}

package com.hyejin.counselor.core.module;

import com.hyejin.counselor.core.entity.User;
import org.springframework.stereotype.Component;
import com.hyejin.counselor.core.repository.TestRepository;

@Component
public class Test {
    private TestRepository repository;

    public void save(User user) {
        repository.save(user);
    }
}

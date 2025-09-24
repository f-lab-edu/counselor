package com.hyejin.counselor.core.module;

import com.hyejin.counselor.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hyejin.counselor.core.repository.TestRepository;

@Component
@RequiredArgsConstructor
public class Test {
    private final TestRepository repository;

    public void save(User user) {
        repository.save(user);
    }
}

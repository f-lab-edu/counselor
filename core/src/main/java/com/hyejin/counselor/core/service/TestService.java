package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.entity.User;
import com.hyejin.counselor.core.module.Test;
import com.hyejin.counselor.core.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    private final Test testModule;

    public void save(User user) {
        testModule.save(user);
    }
}

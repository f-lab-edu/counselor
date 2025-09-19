package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.entity.User;
import com.hyejin.counselor.core.module.Test;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private Test testModule;

    public void save(User user) {
        testModule.save(user);
    }
}

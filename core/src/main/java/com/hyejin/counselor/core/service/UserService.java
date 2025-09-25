package com.hyejin.counselor.core.service;

import com.hyejin.counselor.core.module.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final User userModule;

    public boolean save(com.hyejin.counselor.core.entity.User user) {
        boolean result = false;
        if(userModule.save(user)!=null) result = true;
        return result;
    }
}

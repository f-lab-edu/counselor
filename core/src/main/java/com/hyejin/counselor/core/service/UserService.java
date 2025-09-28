package com.hyejin.counselor.core.service;


import com.hyejin.counselor.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public boolean save(com.hyejin.counselor.core.entity.User user) {
        boolean result = false;
        if(repository.save(user)!=null) result = true;
        return result;
    }
}

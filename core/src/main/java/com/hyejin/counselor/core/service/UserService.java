package com.hyejin.counselor.core.service;


import com.hyejin.counselor.core.entity.User;
import com.hyejin.counselor.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }
}

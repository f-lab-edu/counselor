package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.core.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hyejin.counselor.core.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;


    @GetMapping("/save/{name}/{phone}")
    public void test(@PathVariable("name") String name, @PathVariable("phone") String phone) {
        User user = new User(name,phone);
        testService.save(user);
    }

}

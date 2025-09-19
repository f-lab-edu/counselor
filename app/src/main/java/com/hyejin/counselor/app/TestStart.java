package com.hyejin.counselor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hyejin")
public class TestStart {
    public static void main(String[] args) {
        SpringApplication.run(TestStart.class, args);
    }
}
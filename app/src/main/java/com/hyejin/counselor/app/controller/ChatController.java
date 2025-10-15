package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.app.common.ApiResponse;
import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> chatSave(@RequestBody Chat chat) {
        chatService.sendMessage(chat);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}

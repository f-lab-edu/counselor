package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.app.common.ApiResponse;
import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

//    @PostMapping("/chat")
//    public ResponseEntity<ApiResponse<Object>> chatSave(@RequestBody Chat chat) {
//        chatService.sendMessage(chat);
//        return ResponseEntity.ok(ApiResponse.success(null));
//    }

    @MessageMapping("/chat")
    public void chatSave(@Payload Chat chat) throws Exception {
        chatService.chatSave(chat);
    }

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<Object>> chatList(@ModelAttribute Chat chat) {
        List<Chat> list = chatService.chatList(chat);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

}

package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println("연결!!!!!!!" + chat.getId() + "/ " + chat.getCounselId() + "/ " + chat.getMsg());

    }


}

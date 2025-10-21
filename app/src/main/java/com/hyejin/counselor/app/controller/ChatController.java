package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.app.common.ApiResponse;
import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> chatSave(@RequestBody Chat chat) {
        chatService.sendMessage(chat);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @MessageMapping("/enter")
    public void counselEnter(@Payload Chat chat) throws Exception {

        if (chat == null) {
            System.out.println("chat 객체가 null!!!");
            return;
        }
        System.out.println("연결!!!!!!!" + chat.getId() + "/ " + chat.getCounselId() + "/ " + chat.getMsg());

        String destination = "/sub/chat/" + chat.getCounselId();
        chat.setMsg(chat.getId() + " 님이 입장하셨습니다.");

        messagingTemplate.convertAndSend(destination, chat);
    }


}

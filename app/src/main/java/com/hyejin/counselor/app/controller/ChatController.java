package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.app.common.ApiResponse;
import com.hyejin.counselor.core.entity.Chat;
import com.hyejin.counselor.core.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

//  /*kafka 사용시*/
//    @PostMapping("/chat")
//    public ResponseEntity<ApiResponse<Object>> chatSave(@RequestBody Chat chat) {
//        chatService.sendMessage(chat);
//        return ResponseEntity.ok(ApiResponse.success(null));
//    }
//
//    /**
//     * 채팅입력 웹소켓
//     * @param chat
//     * @throws Exception
//     */
//    @MessageMapping("/chat")
//    public void chatSave(@Payload Chat chat) throws Exception {
//        chatService.chatSave(chat);
//    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> chatSave(@RequestBody Chat chat) {
        chatService.addMessage(chat);
        return ResponseEntity.ok(ApiResponse.success(chat));
    }

    /**
     * 채팅 조회
     * @param chat
     * @return
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> chatList(@ModelAttribute Chat chat) {
        List<Chat> list = chatService.chatList(chat);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/poll")
    public DeferredResult<List<Chat>> pollMessages(
            @RequestParam String counselId,
            @RequestParam String lastMessageId
    ) {
        return chatService.waitMessage(counselId, lastMessageId);
    }

}

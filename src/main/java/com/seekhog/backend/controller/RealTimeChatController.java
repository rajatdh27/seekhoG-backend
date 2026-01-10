package com.seekhog.backend.controller;

import com.seekhog.backend.model.Message;
import com.seekhog.backend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private MessageRepository messageRepository;

    // Handle Private Messages
    @MessageMapping("/private-message")
    public void processPrivateMessage(@Payload Message message) {
        message.setStatus(Message.MessageStatus.SENT);
        Message savedMsg = messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/conversation." + message.getConversationId(), savedMsg);
    }

    // Handle Read Receipts
    // Client sends to: /app/mark-read
    @MessageMapping("/mark-read")
    public void markAsRead(@Payload ReadReceiptRequest request) {
        // 1. Update Database
        int count = messageRepository.markMessagesAsRead(request.getConversationId(), request.getReaderId());

        if (count > 0) {
            // 2. Broadcast "Read Receipt" event to the conversation
            // The sender will receive this and update their UI (Blue Ticks)
            messagingTemplate.convertAndSend("/topic/conversation." + request.getConversationId(), 
                new ReadReceiptEvent("READ_RECEIPT", request.getConversationId(), request.getReaderId()));
        }
    }

    // DTOs for Read Receipts
    @Data
    @AllArgsConstructor
    static class ReadReceiptRequest {
        private Long conversationId;
        private String readerId;
    }

    @Data
    @AllArgsConstructor
    static class ReadReceiptEvent {
        private String type; // "READ_RECEIPT"
        private Long conversationId;
        private String readerId;
    }
}
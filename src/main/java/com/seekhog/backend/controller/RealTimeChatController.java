package com.seekhog.backend.controller;

import com.seekhog.backend.model.Conversation;
import com.seekhog.backend.model.Message;
import com.seekhog.backend.repository.ConversationRepository;
import com.seekhog.backend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class RealTimeChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @MessageMapping("/private-message")
    public void processPrivateMessage(@Payload Message message) {
        message.setStatus(Message.MessageStatus.SENT);
        Message savedMsg = messageRepository.save(message);
        
        Conversation conversation = conversationRepository.findById(message.getConversationId()).orElse(null);
        if (conversation != null) {
            conversation.setLastMessageContent(message.getContent());
            conversation.setLastMessageAt(LocalDateTime.now());
            conversationRepository.save(conversation);
        }

        messagingTemplate.convertAndSend("/topic/conversation." + message.getConversationId(), savedMsg);
    }

    @MessageMapping("/mark-read")
    public void markAsRead(@Payload ReadReceiptRequest request) {
        int count = messageRepository.markMessagesAsRead(request.getConversationId(), request.getReaderId());

        if (count > 0) {
            messagingTemplate.convertAndSend("/topic/conversation." + request.getConversationId(), 
                new ReadReceiptEvent("READ_RECEIPT", request.getConversationId(), request.getReaderId()));
        }
    }

    @Data
    @AllArgsConstructor
    static class ReadReceiptRequest {
        private String conversationId; // Changed Long to String
        private String readerId;
    }

    @Data
    @AllArgsConstructor
    static class ReadReceiptEvent {
        private String type;
        private String conversationId; // Changed Long to String
        private String readerId;
    }
}
package com.seekhog.backend.controller;

import com.seekhog.backend.model.ChatMessage;
import com.seekhog.backend.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatMessageRepository repository;

    // 1. Real-time Message Handling
    // When a client sends a message to "/app/sendMessage", this method is called.
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public") // The return value is broadcast to all subscribers of "/topic/public"
    public ChatMessage sendMessage(ChatMessage message) {
        // Save to database
        return repository.save(message);
    }

    // 2. REST API for History
    // When a user joins, they can call this to get old messages.
    @GetMapping("/api/chat/history")
    public List<ChatMessage> getChatHistory() {
        return repository.findAll();
    }
}
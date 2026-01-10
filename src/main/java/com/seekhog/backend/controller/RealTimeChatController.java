package com.seekhog.backend.controller;

import com.seekhog.backend.model.Message;
import com.seekhog.backend.repository.MessageRepository;
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
    // Client sends to: /app/private-message
    @MessageMapping("/private-message")
    public void processPrivateMessage(@Payload Message message) {
        // 1. Save to Database
        Message savedMsg = messageRepository.save(message);

        // 2. Send to Recipient (via their private queue)
        // The recipient subscribes to: /user/queue/messages
        // We need to know WHO the recipient is. 
        // In a real app, the 'Message' object should have a 'recipientId' field, 
        // or we look up the participants of the conversation.
        
        // For simplicity, let's assume the frontend sends the recipientId in the message content or a separate DTO.
        // But since our Message model only has conversationId, we need to fetch participants.
        
        // TODO: In a production app, you would fetch the other participant of the conversation
        // and send it to them specifically.
        
        // For now, we will broadcast to the specific Conversation Topic
        // This is easier: Everyone in Conversation 123 subscribes to /topic/conversation.123
        messagingTemplate.convertAndSend("/topic/conversation." + message.getConversationId(), savedMsg);
    }
}
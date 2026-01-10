package com.seekhog.backend.controller;

import com.seekhog.backend.model.Conversation;
import com.seekhog.backend.model.ConversationParticipant;
import com.seekhog.backend.model.Message;
import com.seekhog.backend.repository.ConversationParticipantRepository;
import com.seekhog.backend.repository.ConversationRepository;
import com.seekhog.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
public class AdvancedChatController {

    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ConversationParticipantRepository participantRepository;
    @Autowired
    private MessageRepository messageRepository;

    // 1. Create or Get Private Conversation
    @PostMapping("/create/private/{targetUserId}")
    public Conversation createPrivateChat(@RequestParam String myUserId, @PathVariable String targetUserId) {
        // Check if conversation already exists
        Optional<Conversation> existing = conversationRepository.findDirectConversation(myUserId, targetUserId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Create new conversation
        Conversation conversation = new Conversation();
        conversation.setType(Conversation.ConversationType.DIRECT);
        conversation = conversationRepository.save(conversation);

        // Add participants
        ConversationParticipant p1 = new ConversationParticipant();
        p1.setConversationId(conversation.getId());
        p1.setUserId(myUserId);
        participantRepository.save(p1);

        ConversationParticipant p2 = new ConversationParticipant();
        p2.setConversationId(conversation.getId());
        p2.setUserId(targetUserId);
        participantRepository.save(p2);

        return conversation;
    }

    // 2. Get My Conversations
    @GetMapping("/conversations")
    public List<ConversationParticipant> getMyConversations(@RequestParam String userId) {
        return participantRepository.findByUserId(userId);
        // Note: In a real app, you would join this with the Conversation table to get names/last messages
    }

    // 3. Get Message History for a Conversation
    @GetMapping("/history/{conversationId}")
    public List<Message> getHistory(@PathVariable Long conversationId, @RequestParam(defaultValue = "0") int page) {
        return messageRepository.findByConversationIdOrderBySentAtDesc(conversationId, PageRequest.of(page, 20));
    }
}
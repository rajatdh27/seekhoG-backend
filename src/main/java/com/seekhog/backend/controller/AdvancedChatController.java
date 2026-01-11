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

    @PostMapping("/create/private/{targetUserId}")
    public Conversation createPrivateChat(@RequestParam String myUserId, @PathVariable String targetUserId) {
        Optional<Conversation> existing = conversationRepository.findDirectConversation(myUserId, targetUserId);
        if (existing.isPresent()) {
            return existing.get();
        }

        Conversation conversation = new Conversation();
        conversation.setType(Conversation.ConversationType.DIRECT);
        conversation = conversationRepository.save(conversation);

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

    @GetMapping("/conversations")
    public List<ConversationParticipant> getMyConversations(@RequestParam String userId) {
        return participantRepository.findByUserId(userId);
    }

    @GetMapping("/history/{conversationId}")
    public List<Message> getHistory(@PathVariable String conversationId, @RequestParam(defaultValue = "0") int page) { // Changed Long to String
        return messageRepository.findByConversationIdOrderBySentAtDesc(conversationId, PageRequest.of(page, 20));
    }
}
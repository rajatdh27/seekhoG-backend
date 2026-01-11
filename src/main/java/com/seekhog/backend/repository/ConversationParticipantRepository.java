package com.seekhog.backend.repository;

import com.seekhog.backend.model.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, String> { // Changed Long to String
    List<ConversationParticipant> findByUserId(String userId);
    List<ConversationParticipant> findByConversationId(String conversationId); // Changed Long to String
}
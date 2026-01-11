package com.seekhog.backend.repository;

import com.seekhog.backend.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> { // Changed Long to String

    @Query("SELECT c FROM Conversation c " +
           "JOIN ConversationParticipant p1 ON c.id = p1.conversationId " +
           "JOIN ConversationParticipant p2 ON c.id = p2.conversationId " +
           "WHERE c.type = 'DIRECT' AND p1.userId = :user1 AND p2.userId = :user2")
    Optional<Conversation> findDirectConversation(String user1, String user2);
}
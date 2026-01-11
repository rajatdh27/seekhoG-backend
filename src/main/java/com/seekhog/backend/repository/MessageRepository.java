package com.seekhog.backend.repository;

import com.seekhog.backend.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> { // Changed Long to String
    
    List<Message> findByConversationIdOrderBySentAtDesc(String conversationId, Pageable pageable); // Changed Long to String

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.status = 'READ' WHERE m.conversationId = :conversationId AND m.senderId != :readerId AND m.status != 'READ'")
    int markMessagesAsRead(String conversationId, String readerId); // Changed Long to String
}
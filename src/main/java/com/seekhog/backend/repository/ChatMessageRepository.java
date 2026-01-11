package com.seekhog.backend.repository;

import com.seekhog.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // This repository is for the OLD ChatMessage entity (Global Chat).
    // We are keeping it as Long for now since we deprecated it, or we can update it too.
    // Let's leave it as is to avoid breaking the old global chat if it's still used.
}
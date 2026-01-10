package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender; // Username of sender
    private String content; // The message text
    
    // For now, we will just have a global chat room, so no "conversationId" needed yet.
    // If you want private chats later, we can add it.

    @CreationTimestamp
    private LocalDateTime timestamp;
}
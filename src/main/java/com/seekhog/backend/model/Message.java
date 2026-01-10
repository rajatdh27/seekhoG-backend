package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;
    private String senderId;
    
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type; // TEXT, IMAGE, CODE

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT; // Default to SENT

    @CreationTimestamp
    private LocalDateTime sentAt;

    public enum MessageType {
        TEXT, IMAGE, CODE
    }

    public enum MessageStatus {
        SENT, DELIVERED, READ
    }
}
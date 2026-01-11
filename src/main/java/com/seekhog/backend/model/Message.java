package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages", indexes = {
    @Index(name = "idx_msg_convo", columnList = "conversationId"),
    @Index(name = "idx_msg_sender", columnList = "senderId")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    private String id; // UUID

    private String conversationId; // Changed from Long to String
    private String senderId;
    
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT;

    @CreationTimestamp
    private LocalDateTime sentAt;

    public enum MessageType {
        TEXT, IMAGE, CODE
    }

    public enum MessageStatus {
        SENT, DELIVERED, READ
    }

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
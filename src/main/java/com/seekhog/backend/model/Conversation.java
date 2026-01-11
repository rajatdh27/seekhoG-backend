package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    @Id
    private String id; // UUID

    @Enumerated(EnumType.STRING)
    private ConversationType type;

    private String name;

    private String lastMessageContent;
    private LocalDateTime lastMessageAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum ConversationType {
        DIRECT, GROUP
    }

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
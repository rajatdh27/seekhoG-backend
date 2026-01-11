package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversation_participants", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"conversationId", "userId"})
    },
    indexes = {
        @Index(name = "idx_part_user", columnList = "userId"),
        @Index(name = "idx_part_convo", columnList = "conversationId")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationParticipant {

    @Id
    private String id; // UUID

    private String conversationId; // Changed from Long to String
    private String userId;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
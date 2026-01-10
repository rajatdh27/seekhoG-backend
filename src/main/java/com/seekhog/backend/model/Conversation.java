package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConversationType type; // DIRECT, GROUP

    private String name; // Null for DIRECT, required for GROUP

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum ConversationType {
        DIRECT, GROUP
    }
}
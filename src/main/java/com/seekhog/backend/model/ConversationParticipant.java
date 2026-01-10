package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_participants", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"conversationId", "userId"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;
    private String userId; // Linking to User ID

    @CreationTimestamp
    private LocalDateTime joinedAt;
}
package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "friendships", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"requesterId", "addresseeId"})
    },
    indexes = {
        @Index(name = "idx_friend_req", columnList = "requesterId"),
        @Index(name = "idx_friend_addr", columnList = "addresseeId")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {

    @Id
    private String id; // UUID

    private String requesterId;
    private String addresseeId;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum FriendshipStatus {
        PENDING, ACCEPTED, BLOCKED
    }

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
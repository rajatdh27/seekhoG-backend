package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendships", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"requesterId", "addresseeId"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterId; // The user who sent the request
    private String addresseeId; // The user receiving the request

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status; // PENDING, ACCEPTED, BLOCKED

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum FriendshipStatus {
        PENDING, ACCEPTED, BLOCKED
    }
}
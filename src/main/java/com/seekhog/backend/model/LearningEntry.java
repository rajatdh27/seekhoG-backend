package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "learning_entries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningEntry {

    @Id
    private String id; // UUID

    private String userId;

    private String topic;
    private String subTopic;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer difficulty;
    
    private String referenceLink;
    private String referenceTitle;
    private String platform;

    private LocalDate learningDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
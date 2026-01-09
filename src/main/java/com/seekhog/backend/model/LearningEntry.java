package com.seekhog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_entries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // Linking to the User ID (String UUID)

    private String topic;
    private String subTopic;

    @Column(columnDefinition = "TEXT") // Allows large text content
    private String content;

    private Integer difficulty; // 1-5 scale?
    
    private String referenceLink;
    private String referenceTitle;
    private String platform; // e.g., LeetCode, YouTube

    private LocalDate learningDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
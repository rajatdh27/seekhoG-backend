package com.seekhog.backend.repository;

import com.seekhog.backend.model.LearningEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningEntryRepository extends JpaRepository<LearningEntry, Long> {
    // Find all entries for a specific user, ordered by date
    List<LearningEntry> findByUserIdOrderByLearningDateDesc(String userId);
}
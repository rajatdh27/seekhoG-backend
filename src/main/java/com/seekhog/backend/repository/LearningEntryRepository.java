package com.seekhog.backend.repository;

import com.seekhog.backend.dto.LeaderboardEntryDTO;
import com.seekhog.backend.model.LearningEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningEntryRepository extends JpaRepository<LearningEntry, Long> {
    
    // Find all entries for a specific user, ordered by date
    List<LearningEntry> findByUserIdOrderByLearningDateDesc(String userId);

    // Count logs for a specific user
    Long countByUserId(String userId);

    // Leaderboard Query
    @Query("SELECT new com.seekhog.backend.dto.LeaderboardEntryDTO(u.username, COUNT(l), MAX(l.learningDate)) " +
           "FROM LearningEntry l, User u " +
           "WHERE l.userId = u.id " +
           "GROUP BY u.username " +
           "ORDER BY COUNT(l) DESC")
    List<LeaderboardEntryDTO> findTopUsers(Pageable pageable);
}
package com.seekhog.backend.controller;

import com.seekhog.backend.dto.LeaderboardEntryDTO;
import com.seekhog.backend.repository.LearningEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LearningEntryRepository repository;

    @GetMapping
    public List<LeaderboardEntryDTO> getLeaderboard() {
        // Fetch top 10 users
        return repository.findTopUsers(PageRequest.of(0, 10));
    }
}
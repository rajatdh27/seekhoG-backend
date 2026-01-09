package com.seekhog.backend.controller;

import com.seekhog.backend.model.LearningEntry;
import com.seekhog.backend.repository.LearningEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journey")
public class LearningEntryController {

    @Autowired
    private LearningEntryRepository repository;

    // 1. Get all entries for a specific user
    @GetMapping("/{userId}")
    public List<LearningEntry> getUserJourney(@PathVariable String userId) {
        return repository.findByUserIdOrderByLearningDateDesc(userId);
    }

    // 2. Add a new entry
    @PostMapping
    public LearningEntry addEntry(@RequestBody LearningEntry entry) {
        return repository.save(entry);
    }
    
    // 3. Delete an entry
    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
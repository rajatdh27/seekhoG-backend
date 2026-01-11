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

    @GetMapping("/{userId}")
    public List<LearningEntry> getUserJourney(@PathVariable String userId) {
        return repository.findByUserIdOrderByLearningDateDesc(userId);
    }

    @PostMapping
    public LearningEntry addEntry(@RequestBody LearningEntry entry) {
        return repository.save(entry);
    }
    
    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable String id) { // Changed Long to String
        repository.deleteById(id);
    }
}
package com.seekhog.backend.controller;

import com.seekhog.backend.model.Friendship;
import com.seekhog.backend.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    @Autowired
    private FriendshipRepository repository;

    // 1. Send Friend Request
    @PostMapping("/request")
    public Friendship sendRequest(@RequestParam String requesterId, @RequestParam String addresseeId) {
        // Check if relationship exists
        if (repository.findRelationship(requesterId, addresseeId).isPresent()) {
            throw new RuntimeException("Relationship already exists");
        }

        Friendship friendship = new Friendship();
        friendship.setRequesterId(requesterId);
        friendship.setAddresseeId(addresseeId);
        friendship.setStatus(Friendship.FriendshipStatus.PENDING);
        return repository.save(friendship);
    }

    // 2. Accept Request
    @PostMapping("/accept/{id}")
    public Friendship acceptRequest(@PathVariable Long id) {
        Friendship friendship = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        return repository.save(friendship);
    }

    // 3. Get My Friends
    @GetMapping("/{userId}")
    public List<Friendship> getFriends(@PathVariable String userId) {
        return repository.findAllFriends(userId);
    }

    // 4. Get Pending Requests (Incoming)
    @GetMapping("/pending/{userId}")
    public List<Friendship> getPendingRequests(@PathVariable String userId) {
        return repository.findByAddresseeIdAndStatus(userId, Friendship.FriendshipStatus.PENDING);
    }
}
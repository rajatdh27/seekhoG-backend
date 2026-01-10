package com.seekhog.backend.controller;

import com.seekhog.backend.dto.FriendshipResponse;
import com.seekhog.backend.model.Friendship;
import com.seekhog.backend.model.User;
import com.seekhog.backend.repository.FriendshipRepository;
import com.seekhog.backend.repository.LearningEntryRepository;
import com.seekhog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    @Autowired
    private FriendshipRepository repository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LearningEntryRepository learningRepository;

    // 1. Send Friend Request
    @PostMapping("/request")
    public Friendship sendRequest(@RequestParam String requesterId, @RequestParam String targetUsername) {
        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String addresseeId = targetUser.getId();

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

    // 3. Get My Friends (Accepted)
    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getFriends(@PathVariable String userId) {
        List<Friendship> friendships = repository.findAllFriends(userId);
        return ResponseEntity.ok(mapToDTO(friendships, userId));
    }

    // 4. Get Pending Requests (Inbox)
    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getPendingRequests(@PathVariable String userId) {
        List<Friendship> requests = repository.findByAddresseeIdAndStatus(userId, Friendship.FriendshipStatus.PENDING);
        return ResponseEntity.ok(mapToDTO(requests, userId));
    }

    // 5. Get Sent Requests (Outbox)
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getSentRequests(@PathVariable String userId) {
        List<Friendship> sent = repository.findByRequesterIdAndStatus(userId, Friendship.FriendshipStatus.PENDING);
        return ResponseEntity.ok(mapToDTO(sent, userId));
    }

    // Helper to convert Entity List -> DTO List
    private List<FriendshipResponse> mapToDTO(List<Friendship> list, String myUserId) {
        return list.stream().map(f -> {
            User requester = userRepository.findById(f.getRequesterId()).orElse(null);
            User addressee = userRepository.findById(f.getAddresseeId()).orElse(null);
            
            // Determine who the "Friend" is (the other person)
            String friendId = f.getRequesterId().equals(myUserId) ? f.getAddresseeId() : f.getRequesterId();
            
            // Fetch stats for the friend
            Long totalLogs = learningRepository.countByUserId(friendId);

            return new FriendshipResponse(
                f, 
                requester != null ? requester.getUsername() : "Unknown",
                addressee != null ? addressee.getUsername() : "Unknown",
                totalLogs
            );
        }).collect(Collectors.toList());
    }
}
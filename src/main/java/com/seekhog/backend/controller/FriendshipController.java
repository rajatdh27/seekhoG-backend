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

    @PostMapping("/accept/{id}")
    public Friendship acceptRequest(@PathVariable String id) { // Changed Long to String
        Friendship friendship = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        return repository.save(friendship);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getFriends(@PathVariable String userId) {
        List<Friendship> friendships = repository.findAllFriends(userId);
        return ResponseEntity.ok(mapToDTO(friendships, userId));
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getPendingRequests(@PathVariable String userId) {
        List<Friendship> requests = repository.findByAddresseeIdAndStatus(userId, Friendship.FriendshipStatus.PENDING);
        return ResponseEntity.ok(mapToDTO(requests, userId));
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getSentRequests(@PathVariable String userId) {
        List<Friendship> sent = repository.findByRequesterIdAndStatus(userId, Friendship.FriendshipStatus.PENDING);
        return ResponseEntity.ok(mapToDTO(sent, userId));
    }

    private List<FriendshipResponse> mapToDTO(List<Friendship> list, String myUserId) {
        return list.stream().map(f -> {
            User requester = userRepository.findById(f.getRequesterId()).orElse(null);
            User addressee = userRepository.findById(f.getAddresseeId()).orElse(null);
            
            String friendId = f.getRequesterId().equals(myUserId) ? f.getAddresseeId() : f.getRequesterId();
            Long totalLogs = learningRepository.countByUserId(friendId);

            // Use getName() instead of getUsername() for display
            String reqName = requester != null ? (requester.getName() != null ? requester.getName() : requester.getUsername()) : "Unknown";
            String addrName = addressee != null ? (addressee.getName() != null ? addressee.getName() : addressee.getUsername()) : "Unknown";

            return new FriendshipResponse(
                f, 
                reqName,
                addrName,
                totalLogs
            );
        }).collect(Collectors.toList());
    }
}
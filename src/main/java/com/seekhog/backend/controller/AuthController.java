package com.seekhog.backend.controller;

import com.seekhog.backend.model.User;
import com.seekhog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 1. Sign Up
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        
        user.setId(UUID.randomUUID().toString());
        user.setRole("USER");
        
        // If name is not provided, default to username
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getUsername());
        }
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found. Please Sign Up first.");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect password");
        }
        
        return ResponseEntity.ok(user);
    }

    // 3. Anonymous Login
    @PostMapping("/anonymous")
    public ResponseEntity<User> anonymousLogin() {
        User anonUser = new User();
        anonUser.setId(UUID.randomUUID().toString());
        anonUser.setUsername("Guest_" + anonUser.getId().substring(0, 5));
        anonUser.setName("Guest User");
        anonUser.setRole("ANONYMOUS");
        return ResponseEntity.ok(anonUser);
    }
}
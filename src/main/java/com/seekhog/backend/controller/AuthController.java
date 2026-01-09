package com.seekhog.backend.controller;

import com.seekhog.backend.model.User;
import com.seekhog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired // Inject the repository (Dependency Injection)
    private UserRepository userRepository;

    // 1. Sign Up
    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        // Check if user already exists in DB
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        
        user.setId(UUID.randomUUID().toString());
        user.setRole("USER");
        return userRepository.save(user); // Save to H2 Database
    }

    // 2. Login
    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
            return userOpt.get();
        }
        throw new RuntimeException("Invalid credentials");
    }

    // 3. Anonymous Login
    @PostMapping("/anonymous")
    public User anonymousLogin() {
        User anonUser = new User();
        anonUser.setId(UUID.randomUUID().toString());
        anonUser.setUsername("Guest_" + anonUser.getId().substring(0, 5));
        anonUser.setRole("ANONYMOUS");
        // We usually DON'T save anonymous users to DB to save space, 
        // but we return them so the frontend has a session.
        return anonUser;
    }
}
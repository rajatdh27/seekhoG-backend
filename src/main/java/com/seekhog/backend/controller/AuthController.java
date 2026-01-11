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

    @Autowired
    private UserRepository userRepository;

    // 1. Sign Up
    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        
        user.setId(UUID.randomUUID().toString());
        user.setRole("USER");
        
        // If name is not provided, default to username
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getUsername());
        }
        
        return userRepository.save(user);
    }

    // 2. Login
    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found. The database might have reset. Please Sign Up again.");
        }

        if (!userOpt.get().getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        
        return userOpt.get();
    }

    // 3. Anonymous Login
    @PostMapping("/anonymous")
    public User anonymousLogin() {
        User anonUser = new User();
        anonUser.setId(UUID.randomUUID().toString());
        anonUser.setUsername("Guest_" + anonUser.getId().substring(0, 5));
        anonUser.setName("Guest User");
        anonUser.setRole("ANONYMOUS");
        return anonUser;
    }
}
package com.seekhog.backend.controller;

import com.seekhog.backend.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Simulating a database in memory
    private List<User> userDatabase = new ArrayList<>();

    // 1. Sign Up
    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        // Check if user already exists
        for (User u : userDatabase) {
            if (u.getUsername().equals(user.getUsername())) {
                throw new RuntimeException("User already exists");
            }
        }
        
        user.setId(UUID.randomUUID().toString());
        user.setRole("USER");
        userDatabase.add(user);
        return user;
    }

    // 2. Login
    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        for (User u : userDatabase) {
            if (u.getUsername().equals(loginRequest.getUsername()) && 
                u.getPassword().equals(loginRequest.getPassword())) {
                return u;
            }
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
        // We don't save anonymous users to the database usually, but we return a session object
        return anonUser;
    }
}
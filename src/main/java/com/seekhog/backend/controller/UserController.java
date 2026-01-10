package com.seekhog.backend.controller;

import com.seekhog.backend.model.User;
import com.seekhog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }
}
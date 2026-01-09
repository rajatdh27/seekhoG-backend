package com.seekhog.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String password; // In a real app, this would be encrypted!
    private String role; // "USER" or "ANONYMOUS"
}
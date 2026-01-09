package com.seekhog.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 1. Tells JPA to make a table called "users"
@Table(name = "users") // Optional: explicitly name the table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id // 2. Primary Key
    private String id;
    
    private String username;
    private String password;
    private String role;
}
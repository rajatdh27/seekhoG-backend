package com.seekhog.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_username", columnList = "username")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    private String id;
    
    private String username; // Used for Login (Email)
    private String name;     // Display Name (e.g., "Rajat Thakur")
    private String password;
    private String role;
}
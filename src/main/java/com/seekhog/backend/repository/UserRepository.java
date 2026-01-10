package com.seekhog.backend.repository;

import com.seekhog.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Magic Method: Spring automatically implements this!
    // It translates to: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // Search for users containing the query string (case insensitive)
    List<User> findByUsernameContainingIgnoreCase(String query);
}
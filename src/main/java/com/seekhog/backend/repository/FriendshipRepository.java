package com.seekhog.backend.repository;

import com.seekhog.backend.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, String> { // Changed Long to String

    @Query("SELECT f FROM Friendship f WHERE " +
           "(f.requesterId = :user1 AND f.addresseeId = :user2) OR " +
           "(f.requesterId = :user2 AND f.addresseeId = :user1)")
    Optional<Friendship> findRelationship(String user1, String user2);

    @Query("SELECT f FROM Friendship f WHERE " +
           "(f.requesterId = :userId OR f.addresseeId = :userId) AND f.status = 'ACCEPTED'")
    List<Friendship> findAllFriends(String userId);

    List<Friendship> findByAddresseeIdAndStatus(String addresseeId, Friendship.FriendshipStatus status);

    List<Friendship> findByRequesterIdAndStatus(String requesterId, Friendship.FriendshipStatus status);
}
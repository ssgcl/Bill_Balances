package com.ssgc.springbootjwt.dao;

import org.springframework.data.jpa.repository.JpaRepository; // Import for JPA repository
import org.springframework.data.jpa.repository.Modifying; // Import for modifying queries
import org.springframework.transaction.annotation.Transactional;

import com.ssgc.springbootjwt.model.RefreshToken; // Import for the RefreshToken model class

import java.util.Optional; // Import for Optional type

/**
 * Repository interface for managing RefreshToken entities.
 * It extends JpaRepository to provide basic CRUD operations.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    /**
     * Finds a RefreshToken by its token string.
     *
     * @param token the token string to search for
     * @return an Optional containing the found RefreshToken, or empty if not found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Deletes all RefreshTokens associated with a specific username.
     * This method is marked with @Modifying to indicate it performs a modifying operation.
     *
     * @param username the username for which the refresh tokens should be deleted
     */
    @Modifying
    @Transactional // This ensures a transaction is created for the repository method
    void deleteByUsername(String username);  // To invalidate old refresh tokens
}

package com.ssgc.springbootjwt.dao;

import org.springframework.data.repository.CrudRepository; // Import for CRUD operations
import org.springframework.stereotype.Repository; // Import for the Repository annotation

import com.ssgc.springbootjwt.model.BANKUSER; // Import for the BANKUSER model class
import com.ssgc.springbootjwt.model.UserDTO; // Import for the UserDTO model class

/**
 * Repository interface for managing BANKUSER entities.
 * It extends CrudRepository to provide basic CRUD operations for BANKUSER.
 */
@Repository
public interface UserDao extends CrudRepository<BANKUSER, Integer> {

    /**
     * Finds a BANKUSER by their username.
     *
     * @param username the username to search for
     * @return the BANKUSER associated with the given username
     */
    BANKUSER findByUsername(String username);

    /**
     * Saves a UserDTO object to the database.
     *
     * @param user the UserDTO object to save
     * @return the saved BANKUSER object
     */
    UserDTO save(UserDTO user);
}
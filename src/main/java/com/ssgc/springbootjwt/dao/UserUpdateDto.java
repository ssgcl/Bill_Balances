package com.ssgc.springbootjwt.dao;

/**
 * Data Transfer Object (DTO) for updating user information.
 * This class is used to encapsulate the data needed to update a user.
 */
public class UserUpdateDto {
    
    // Field to store the username of the user
    private String username;
    
    // Field to store the password of the user
    private String password;

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

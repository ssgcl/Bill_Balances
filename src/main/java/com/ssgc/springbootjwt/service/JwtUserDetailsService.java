package com.ssgc.springbootjwt.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssgc.springbootjwt.dao.UserDao;
import com.ssgc.springbootjwt.dao.UserUpdateDto;
import com.ssgc.springbootjwt.exception.DuplicateUserException;
import com.ssgc.springbootjwt.exception.NonExistingUserException;
import com.ssgc.springbootjwt.exception.WrongPasswordException;
import com.ssgc.springbootjwt.model.BANKUSER;
import com.ssgc.springbootjwt.model.UserDTO;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        BANKUSER user = userDao.findByUsername(username);
	        
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found with username: " + username);
	        }
	        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
	                new ArrayList<>());
	    }

	 public void authenticateUser(String username, String password) {
	        BANKUSER user = userDao.findByUsername(username);

	        if (user == null) {
	            throw new NonExistingUserException("User not found with username: " + username);
//	            throw new UsernameNotFoundException("User not found with username: " + username);
	        }

	        // Verify password
	        else if (!bcryptEncoder.matches(password, user.getPassword())) {
	            throw new WrongPasswordException("Incorrect password for username: " + username);
	        }
	    }
	
	public BANKUSER save(UserDTO user) {
		
		// Update user details
		 BANKUSER existingUser = userDao.findByUsername(user.getUsername());
		
        if (existingUser != null) {
        	 throw new DuplicateUserException("User already exists. Please try different username");
        }
		
		BANKUSER newUser = new BANKUSER();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userDao.save(newUser);
	}
	
	public BANKUSER updateUser(String username, UserUpdateDto userUpdateDto) {
        BANKUSER user = userDao.findByUsername(username);
        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
        	 throw new NonExistingUserException("User not found with username: " + username);
        }
       

        // Update user details
        if (userUpdateDto.getUsername() != null) {
            String encodedPassword = new BCryptPasswordEncoder().encode(userUpdateDto.getPassword());
            user.setPassword(encodedPassword);
        }

        return userDao.save(user);
    }

	
}
package com.ssgc.springbootjwt.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ssgc.springbootjwt.model.BANKUSER;
import com.ssgc.springbootjwt.model.UserDTO;


@Repository
public interface UserDao extends CrudRepository<BANKUSER, Integer> {

	BANKUSER findByUsername(String username);

	UserDTO save(UserDTO user);
	
}
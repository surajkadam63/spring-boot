package com.springboot.jwt.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.jwt.entity.User;
import com.springboot.jwt.repository.UserRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=userRepository.findByUsername(username);
		
		if(username.equalsIgnoreCase(user.getUsername())) {
		System.out.println("in loadUser()");
			return user;//predefined class can use cutom class user also
		}else
			throw new UsernameNotFoundException("User not Found");
	

	//return new User("suraj","welcome123",new ArrayList<>());//predefined class can use cutom class user also
	}
}

package com.springboot.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jwt.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username );
}

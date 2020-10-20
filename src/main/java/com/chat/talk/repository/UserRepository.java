package com.chat.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.talk.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}

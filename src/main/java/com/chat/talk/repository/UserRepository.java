package com.chat.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chat.talk.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	  @Query(value = "from User where username= :username")
	  User findByUsername(@Param("username") String username);
	  
}

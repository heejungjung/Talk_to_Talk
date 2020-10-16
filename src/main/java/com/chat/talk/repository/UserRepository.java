package com.chat.talk.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.chat.talk.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	  @Query(value = "from User where userid= :userid and password= :password")
	  User findByUsername(@Param("userid") String userid, @Param("password") String password);
}

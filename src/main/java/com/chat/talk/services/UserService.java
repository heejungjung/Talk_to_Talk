package com.chat.talk.services;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.chat.talk.model.User;
import com.chat.talk.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	private final UserRepository userRepository;
	public UserService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	
	public void saveMyUser(User user) {
		userRepository.save(user);
	}
	public User loginUser(User user1){
		User tmp = this.userRepository.findByUsername(user1.getUserid(), user1.getPassword());
	    System.out.print(user1.getUserid()+","+user1.getPassword()+"\n");
	    if (tmp == null) {
	      throw new RuntimeException("user not found");
	    }
	    User user = new User();
	    user.setId(tmp.getId());
	    user.setUserid(tmp.getUserid());
	    user.setPassword(tmp.getPassword());
	    user.setNickname(tmp.getNickname());
	    user.setSex(tmp.getSex());
	    user.setBirthday(tmp.getBirthday());
	    user.setEmail(tmp.getEmail());
	    user.setRegdt(tmp.getRegdt());
	    
	    return user;
	}
}

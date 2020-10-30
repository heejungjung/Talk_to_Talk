package com.chat.talk.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(unique = true)
    private String username;   
    private String password;
    private String nickname;
    private String sex;
    private String birthday;
    private String email;
    private String regdt;
    private Boolean enabled;

//	public User(User user) {
//		this.id = user.getId();
//		this.username = user.getUsername();
//		this.password = user.getPassword();
//		this.nickname = user.getNickname();
//		this.sex = user.getSex();
//		this.birthday = user.getBirthday();
//		this.email = user.getEmail();
//		this.regdt = user.getRegdt();
//		this.enabled = user.getEnabled();
//	}
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

}

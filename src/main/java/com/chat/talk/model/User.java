package com.chat.talk.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	
	@Id
	private int id;
	private String userid;
	private String password;
	private String nickname;
	private String sex;
	private String birthday;
	private String email;
	private String regdt;
	
	public User() {}
	
	public User(String userid, String password, String nickname, String sex, String birthday, String email) {
		this.userid = userid;
		this.password = password;
		this.nickname = nickname;
		this.sex = sex;
		this.birthday = birthday;
		this.email = email;
	}
	public User(String userid, String password, String nickname, String email) {
		this.userid = userid;
		this.password = password;
		this.nickname = nickname;
	
		this.email = email;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegdt() {
		return regdt;
	}

	public void setRegdt(String regdt) {
		this.regdt = regdt;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userid=" + userid + ", password=" + password + ", nickname=" + nickname + ", sex="
				+ sex + ", birthday=" + birthday + ", email=" + email + ", regdt=" + regdt + "]";
	}

	
}

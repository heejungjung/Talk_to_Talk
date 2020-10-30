package com.chat.talk.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DBRoom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long lno;
	
	String title;
	int people;
	String notice;
	
}

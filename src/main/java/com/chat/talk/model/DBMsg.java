package com.chat.talk.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "dbmsg")
public class DBMsg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mno;
	
	private String msgContent;
	private String regdt;
}

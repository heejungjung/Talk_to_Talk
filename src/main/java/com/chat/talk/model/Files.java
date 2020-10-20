package com.chat.talk.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="profile")
public class Files {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long fno;
   
   String username;
   String filename;
   String file1;
   String fileurl;
   String message;

}
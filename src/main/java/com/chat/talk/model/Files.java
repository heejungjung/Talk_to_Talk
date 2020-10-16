package com.chat.talk.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="profile")
public class Files {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   int fno;
   
   String filename;
   String file1;
   String fileurl;
   String message;

   
   public Files() {}
   
   public int getFno() {
      return fno;
   }
   public void setFno(int fno) {
      this.fno = fno;
   }
   public String getFilename() {
      return filename;
   }
   public void setFilename(String filename) {
      this.filename = filename;
   }
   
   public String getFile1() {
      return file1;
   }
   public void setFile1(String file1) {
      this.file1 = file1;
   }
   public String getFileurl() {
      return fileurl;
   }
   public void setFileurl(String fileurl) {
      this.fileurl = fileurl;
   }

   public String getMessage() {
      return message;
   }
   public void setMessage(String message) {
      this.message = message;
   }
   
}
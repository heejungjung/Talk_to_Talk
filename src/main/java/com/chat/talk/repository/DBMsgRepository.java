package com.chat.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.talk.model.DBMsg;

public interface DBMsgRepository extends JpaRepository<DBMsg, Long> {

}

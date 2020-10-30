package com.chat.talk.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.talk.model.DBRoom;

public interface SearchListRepository extends JpaRepository<DBRoom, Long> {

   List<DBRoom> findByTitleContains(String search);
}
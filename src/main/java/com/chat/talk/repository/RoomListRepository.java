package com.chat.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chat.talk.model.DBRoom;

public interface RoomListRepository extends JpaRepository<DBRoom, Long> {

	  @Query(value = "from DBRoom where title= :title")
	  DBRoom findByTitle(@Param("title") String title);
}

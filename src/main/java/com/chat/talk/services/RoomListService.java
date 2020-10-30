package com.chat.talk.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.talk.model.ChatRoom;
import com.chat.talk.model.DBRoom;
import com.chat.talk.repository.RoomListRepository;

@Service
public class RoomListService {
	
	@Autowired
	RoomListRepository roomListRepository;
	
    public DBRoom addroom(ChatRoom chatRoom) {
    	DBRoom room = new DBRoom();
    	room.setTitle(chatRoom.getRoomid());
    	room.setPeople(1);
        return roomListRepository.save(room);
    }
	
    public List<ChatRoom> List_All() {
        List<ChatRoom> result = new ArrayList<ChatRoom>();
    	List<DBRoom> rooms = roomListRepository.findAll();
    	
    	for(DBRoom r : rooms)
    	{
        	ChatRoom chatroom = new ChatRoom();
    		chatroom.setRoomid(r.getTitle());
    		result.add(chatroom);
    	}
        return result;
    }

    public void Join(String roomid) {
    	DBRoom room = roomListRepository.findByTitle(roomid);
    	increment(room);
    }

    public void Leave(String curroomid) {
    	DBRoom room = roomListRepository.findByTitle(curroomid);
    	decrement(room);
    }
    
    public void increment(DBRoom room) {
		room.setPeople(room.getPeople() + 1); 
		roomListRepository.save(room);
    }

    public void decrement(DBRoom room) {
		room.setPeople(room.getPeople() - 1);
    	int people = room.getPeople();
    	if(people==0) {
    		roomListRepository.delete(room);
    	}
    	else {
    		roomListRepository.save(room);
    	}
    }
    
    public void Notice(String roomid,String notice) {
    	DBRoom room = roomListRepository.findByTitle(roomid);
    	room.setNotice(notice);
		roomListRepository.save(room);
    }
}
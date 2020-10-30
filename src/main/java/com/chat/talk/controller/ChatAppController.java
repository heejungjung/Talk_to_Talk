package com.chat.talk.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.chat.talk.model.ChatRoom;
import com.chat.talk.model.Message;
import com.chat.talk.services.FilesService;
import com.chat.talk.services.RoomListService;

@Controller
public class ChatAppController
{
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private RoomListService roomListService;

    @Autowired
    private FilesService filesService;
	
    List<ChatRoom> rooms;


    @Autowired
    public void ChatController()
    {
        rooms = roomListService.List_All();
    }
    
    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId, @Payload Message chatMessage) {
    	addmessage(roomId,chatMessage);
    	chatMessage.setPic(filesService.profile(chatMessage.getSenderid()));
        messagingTemplate.convertAndSend("/subscribe/chat/room/"+roomId,chatMessage);
    }
    
    @MessageMapping("/chat/{roomId}/addUser")
    public void addUser(@DestinationVariable String roomId, @Payload Message chatMessage,SimpMessageHeaderAccessor headerAccessor) {
    	String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
        if (currentRoomId != null) {
            Message leaveMessage = new Message();
            leaveMessage.setType(Message.MessageType.LEAVE);
            leaveMessage.setSender(chatMessage.getSender());
            addmessage(currentRoomId,chatMessage);
            messagingTemplate.convertAndSend("/subscribe/chat/room/"+currentRoomId, leaveMessage);
            roomListService.Leave(currentRoomId);
        }

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        addmessage(roomId,chatMessage);
        messagingTemplate.convertAndSend("/subscribe/chat/room/"+roomId, chatMessage);
        roomListService.Join(chatMessage.getRoomid());
    }


    @SubscribeMapping("/chat/rooms")
    public List<ChatRoom> listOfRoom()
    {
    	System.out.println("Rooms size: "+rooms.size());
        return rooms;
    }

    @MessageMapping("/chat/rooms")
    public void addRoom(@Payload ChatRoom chatRoom)
    {
    	String roomId = chatRoom.getRoomid();
        int flag=0;
        for(ChatRoom room:rooms)
        {
            if(room.getRoomid().equals(roomId))
            {
                flag = 1;
                break;
            }
        }
        if(flag==0)
        {
            List<Message> messages = new ArrayList<Message>();
            chatRoom.setMessages(messages);
            rooms.add(chatRoom);
            roomListService.addroom(chatRoom);
        }
        messagingTemplate.convertAndSend("/subscribe/chat/rooms", rooms);
    }


    @MessageMapping("/chat/{roomId}/leaveuser")
    public void leaveRoom(@DestinationVariable String roomId, @Payload Message chatMessage,SimpMessageHeaderAccessor headerAccessor)
    {
    	String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
        if (currentRoomId != null) {
            Message leaveMessage = new Message();
            leaveMessage.setType(Message.MessageType.LEAVE);
            leaveMessage.setSender(chatMessage.getSender());
            addmessage(currentRoomId,chatMessage);
            messagingTemplate.convertAndSend("/subscribe/chat/room/"+currentRoomId, leaveMessage);
            roomListService.Leave(chatMessage.getRoomid());
        }
    }

    private void addmessage(String roomid, Message message)
    {
    	for(ChatRoom room: rooms)
        {
            if(room.getRoomid().equals(roomid))
            {
                List<Message> messages = room.getMessages();
                messages.add(message);
                room.setMessages(messages);
                break;
            }
        }
    }

    @SubscribeMapping("chat/{roomId}/getPrevious")
    public List<Message> getPreviousMessages(@DestinationVariable String roomId)
    {
        List<Message> messages = new ArrayList<Message>();
        for(ChatRoom room: rooms)
        {
            if(room.getRoomid().equals(roomId))
            {
            	messages = room.getMessages();
                break;
            }
        }
        return messages;
    }

    @MessageMapping("/chat/{roomId}/notice")
    public void notice(@DestinationVariable String roomId, @Payload Message chatMessage,SimpMessageHeaderAccessor headerAccessor)
    {
        String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
        if (currentRoomId != null) {
            addmessage(currentRoomId,chatMessage);
            roomListService.Notice(chatMessage.getRoomid(),chatMessage.getContent());
            messagingTemplate.convertAndSend("/subscribe/chat/room/"+currentRoomId, chatMessage);
        }
    }
}
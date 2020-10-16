package com.chat.talk.model;

public class Message {
    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
    
	private String roomid;
    private MessageType messageType;
    private String content;
    private String sender;

    public String getRoomid() {
		return roomid;
	}

	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}

    public MessageType getType() {
        return messageType;
    }

    public void setType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

	@Override
	public String toString() {
		return "Message [roomid=" + roomid + ", messageType=" + messageType + ", content=" + content + ", sender="
				+ sender + "]";
	}

}

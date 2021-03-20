package com.example.us.Message;

import com.example.us.Room;
import com.example.us.Room_item;
import com.example.us.User_list_item;

import java.util.ArrayList;

/**
 *
 * @Title: Server.Server.Message.java
 * @Description: TODO 封装通讯消息
 * @author ZhangJing   https://github.com/Laity000/ChatRoom-JavaFX
 * @date 2017年5月17日 上午11:22:16
 *
 */
public class Message {

	private int id;
	private MessageType type;

	private String from;
	private String to;

	private String content;

	private int room_Id;

	private String time;
	private ArrayList<User_list_item> userInfoArrayList;

	private ArrayList<Room_item> room_list;

	public ArrayList<Room_item> getRoom_list() {
		return room_list;
	}

	public void setRoom_list(ArrayList<Room_item> room_list) {
		this.room_list = room_list;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setUserInfoArrayList(ArrayList<User_list_item> userInfoArrayList) {
		this.userInfoArrayList = userInfoArrayList;
	}
	public ArrayList<User_list_item> getUserInfoArrayList() {
		return userInfoArrayList;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	public int getRoom_Id() {
		return room_Id;
	}

	public void setRoom_Id(int room_Id) {
		this.room_Id = room_Id;
	}

	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}




}

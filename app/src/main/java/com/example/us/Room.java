package com.example.us;

import com.example.us.Message.Message;

import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Room {

    private int id;

    private String title;

    private ArrayList<User_list_item> room_user_list ;
    private LinkedList<Socket> socketLinkedList;

    private Message last_message ;

    public void setLast_message(Message last_message) {
        this.last_message = last_message;
    }

    public Message getLast_message() {
        return last_message;
    }

    public Room(int id, ArrayList<User_list_item> room_user_list ){
        this.id = id;
        this.room_user_list = room_user_list;
    }

    public Room(int id) throws SQLException {
        this.id = id;

        get_user_list_db(id);
    }
    public Room() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRoom_user_list(ArrayList<User_list_item> room_user_list) {
        this.room_user_list = room_user_list;
    }

    public void setRoom_user_list_db() {
        ArrayList<User_list_item> room_user_list_db = new ArrayList<User_list_item>();

        room_user_list_db = room_user_list;
    }

    public void setSocketLinkedList(LinkedList<Socket> socketLinkedList) {
        this.socketLinkedList = socketLinkedList;
    }

    public LinkedList<Socket> getSocketLinkedList() {
        return socketLinkedList;
    }

    public ArrayList<User_list_item> getRoom_user_list() {
        return room_user_list;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void get_user_list_db(int id) throws SQLException {

        MyDB_test mydb = null;
        mydb = new MyDB_test(id);
        this.room_user_list = mydb.room_user_list(id);

    }
}

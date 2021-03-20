package com.example.us;

import com.example.us.Message.Message;

import java.util.ArrayList;

public class Room_item {
    int id;
    String title;
    Message last_message;
    ArrayList<User_list_item> user_list_itemArrayList;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLast_message(Message last_message) {
        this.last_message = last_message;
    }

    public void setUser_list_itemArrayList(ArrayList<User_list_item> user_list_itemArrayList) {
        this.user_list_itemArrayList = user_list_itemArrayList;
    }

    public Message getLast_message() {
        return last_message;
    }

    public ArrayList<User_list_item> getUser_list_itemArrayList() {
        return user_list_itemArrayList;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}

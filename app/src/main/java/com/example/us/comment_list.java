package com.example.us;

import com.google.gson.annotations.SerializedName;

public class comment_list {

    private int id;

    private int user_id;

    private String username;

    private String content;

//    @SerializedName("create_time")
//    private Date create_time;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public void setCreate_time(Date create_time) {
//        this.create_time = create_time;
//    }


    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

//    public Date getCreate_time() {
//        return create_time;
//    }


    public String getUsername() {
        return username;
    }

}

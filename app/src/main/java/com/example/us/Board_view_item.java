package com.example.us;

import com.google.gson.annotations.SerializedName;

public class Board_view_item {

    @SerializedName("id")
    int id;
    @SerializedName("user_id")
    int user_id;
    @SerializedName("content")
    String content;
    @SerializedName("create_time")
    String create_time;
    @SerializedName("user_name")
    String user_name;
    @SerializedName("user_email")
    String user_email;
    @SerializedName("title")
    String title;
    @SerializedName("user_img")
    String user_img;
    @SerializedName("category")
    String category;



//Board_view_item


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUser_img() {
        return user_img;
    }
}

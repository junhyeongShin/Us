package com.example.us;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Board_list {


    //TODO : 레트로핏에 post 대신 현 클래스 대입 후 테스트.
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("views")
    private int views;
    @SerializedName("category")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("create_time")
    private Date create_time;

    public void setViews(int views) {
        this.views = views;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getCategory() {
        return category;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getViews() {
        return views;
    }

    public String getTitle() {
        return title;
    }
}

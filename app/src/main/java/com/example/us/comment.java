package com.example.us;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class comment {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("board_id")
    private int board_id;

    @SerializedName("username")
    private String username;

    @SerializedName("content")
    private String content;

    @SerializedName("create_time")
    private Date create_time;

    @SerializedName("is_recomment")
    int is_recomment;

    @SerializedName("img_path")
    private String img_url;

    @SerializedName("title")
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setIs_recomment(int is_recomment) {
        this.is_recomment = is_recomment;
    }

    public int getIs_recomment() {
        return is_recomment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

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

    public Date getCreate_time() {
        return create_time;
    }

    public int getBoard_id() {
        return board_id;
    }

    public String getUsername() {
        return username;
    }
}

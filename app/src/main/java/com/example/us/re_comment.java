package com.example.us;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class re_comment {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("comment_id")
    private int comment_id;

    @SerializedName("username")
    private String username;

    @SerializedName("content")
    private String content;

    @SerializedName("img_path")
    private String img_url;

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }
    //    @SerializedName("create_time")
//    private Date create_time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String user_name) {
        this.username = user_name;
    }

    public int getComment_id() {
        return comment_id;
    }

//    public Date getCreate_time() {
//        return create_time;
//    }

    public String getContent() {
        return content;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getId() {
        return id;
    }

//    public void setCreate_time(Date create_time) {
//        this.create_time = create_time;
//    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }
}

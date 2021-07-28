package com.example.us;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Board_list {

    @SerializedName("id")
    private int id;
    @SerializedName("writer")
    private int writer;
    @SerializedName("views")
    private int views;
    @SerializedName("content")
    private String content;
    @SerializedName("category")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("email")
    private String email;
    @SerializedName("create_time")
    private Date create_time;
    @SerializedName("intro_profile")
    private String user_intro;
    @SerializedName("img_profile")
    private String user_img;
    @SerializedName("img_url")
    private String img_url;
    @SerializedName("username")
    private String user_name;


    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getUser_intro() {
        return user_intro;
    }

    public String getUser_img() {
        return user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public void setUser_intro(String user_intro) {
        this.user_intro = user_intro;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWriter() {
        return writer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWriter(int writer) {
        this.writer = writer;
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


    public int getViews() {
        return views;
    }

    public String getTitle() {
        return title;
    }
}

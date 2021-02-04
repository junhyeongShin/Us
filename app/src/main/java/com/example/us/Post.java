package com.example.us;

import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("user_id")
    private int user_id;
    @SerializedName("category")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

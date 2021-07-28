package com.example.us.wowza;

import com.google.gson.annotations.SerializedName;

public class live_streams {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String name;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("play_hls")
    private String play_hls;

    @SerializedName("username")
    private String user_name;
    private int user_id;
    @SerializedName("img_path")
    private String user_img;



    public String getPlay_hls() {
        return play_hls;
    }

    public void setPlay_hls(String play_hls) {
        this.play_hls = play_hls;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_img() {
        return user_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void change_user_name(){
        user_id = Integer.valueOf(user_name);
    }


}

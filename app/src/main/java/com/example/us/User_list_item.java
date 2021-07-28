package com.example.us;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User_list_item {

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String user_name;

    @SerializedName("intro_profile")
    private String user_intro;

    @SerializedName("img_path")
    private String user_img;


    public User_list_item(int id,String username, String profile_intro, String img_path){
        super();
        this.id = id;
        this.user_name = username;
        this.user_intro = profile_intro;
        this.user_img =img_path;
    }

    public User_list_item(){
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_intro(String user_intro) {
        this.user_intro = user_intro;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
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

    public int getId() {
        return id;
    }

}

package com.example.us;

import com.google.gson.annotations.SerializedName;

public class friend_list_item {

    //or index
    @SerializedName("friend_id")
    private int friend_id;

    @SerializedName("username")
    private String user_name;

    @SerializedName("intro_profile")
    private String user_intro;

    @SerializedName("img_path")
    private String user_img;


    public friend_list_item(int friend_id, String username, String profile_intro, String img_path){
        super();
        this.friend_id = friend_id;
        this.user_name = username;
        this.user_intro = profile_intro;
        this.user_img =img_path;
    }


    public friend_list_item(){
        super();
    }

    public void setId(int id) {
        this.friend_id = id;
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
        return friend_id;
    }
}

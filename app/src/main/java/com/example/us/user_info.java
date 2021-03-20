package com.example.us;

public class user_info {

    String user_ID ;
    String user_intro_profile;
    String user_name ;
    String img_path;

    int user_index_number;
    int user_guild_list;
    int user_img_profile;
    int user_favorites_list;
    int user_chat_room_list;
    int user_friend_list;


    public static user_info getOurInstance() {
        return ourInstance;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    //싱글톤 패턴 ~
    private static final user_info ourInstance = new user_info();

    public static user_info getInstance() {
        return ourInstance;
    }

    private user_info() {
    }
    // ~ 싱글톤 패턴


    public user_info(String user_ID ,String user_name,int user_index_number){
         this.user_ID = user_ID;
         this.user_name = user_name;
         this.user_index_number = user_index_number;
    }

    public user_info(String user_ID, String user_name, String user_intro_profile, int user_index_number, int user_chat_room_list,
                     int user_favorites_list, int user_friend_list, int user_guild_list, int user_img_profile){

        this.user_ID = user_ID;
        this.user_intro_profile = user_name ;
        this.user_name  = user_intro_profile ;
        this.user_index_number = user_index_number ;
        this.user_guild_list = user_chat_room_list ;
        this.user_img_profile = user_favorites_list ;
        this.user_favorites_list = user_friend_list ;
        this.user_chat_room_list = user_guild_list ;
        this.user_friend_list = user_img_profile ;

    }

    public int getUser_chat_room_list() {
        return user_chat_room_list;
    }

    public int getUser_favorites_list() {
        return user_favorites_list;
    }

    public int getUser_friend_list() {
        return user_friend_list;
    }

    public int getUser_guild_list() {
        return user_guild_list;
    }

    public int getUser_img_profile() {
        return user_img_profile;
    }

    public int getUser_index_number() {
        return user_index_number;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public String getUser_intro_profile() {
        return user_intro_profile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_chat_room_list(int user_chat_room_list) {
        this.user_chat_room_list = user_chat_room_list;
    }

    public void setUser_favorites_list(int user_favorites_list) {
        this.user_favorites_list = user_favorites_list;
    }

    public void setUser_friend_list(int user_friend_list) {
        this.user_friend_list = user_friend_list;
    }

    public void setUser_guild_list(int user_guild_list) {
        this.user_guild_list = user_guild_list;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public void setUser_img_profile(int user_img_profile) {
        this.user_img_profile = user_img_profile;
    }

    public void setUser_index_number(int user_index_number) {
        this.user_index_number = user_index_number;
    }

    public void setUser_intro_profile(String user_intro_profile) {
        this.user_intro_profile = user_intro_profile;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}



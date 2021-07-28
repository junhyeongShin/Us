package com.example.us.wowza;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class live_stream {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String name;
    @SerializedName("player_embed_code")
    @Expose
    private String player_embed_code;
    @SerializedName("player_id")
    @Expose
    private String player_id;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnail_url;
    @SerializedName("play_hls")
    @Expose
    private String player_hls_playback_url;

    @SerializedName("username")
    private String user_name;
    private int user_id;
    @SerializedName("img_path")
    private String user_img;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setPlayer_embed_code(String player_embed_code) {
        this.player_embed_code = player_embed_code;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getState() {
        return state;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public String getPlayer_embed_code() {
        return player_embed_code;
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

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPlayer_hls_playback_url() {
        return player_hls_playback_url;
    }

    public void setPlayer_hls_playback_url(String player_hls_playback_url) {
        this.player_hls_playback_url = player_hls_playback_url;
    }

}

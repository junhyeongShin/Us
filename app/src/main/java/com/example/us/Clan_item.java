package com.example.us;

import com.google.gson.annotations.SerializedName;

public class Clan_item {

    @SerializedName("id")
    private int id;
    @SerializedName("member_count")
    private int member_count;
    @SerializedName("img_path")
    private String clan_img;
    @SerializedName("master")
    private int master;
    @SerializedName("category")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("clan_introduce")
    private String clan_introduce;

    public int getMember_count() {
        return member_count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public Clan_item(int id) {
        id=this.id;
    }

    public int getMaster() {
        return master;
    }

    public String getClan_introduce() {
        return clan_introduce;
    }

    public String getClan_img() {
        return clan_img;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setClan_img(String clan_img) {
        this.clan_img = clan_img;
    }

    public void setClan_introduce(String clan_introduce) {
        this.clan_introduce = clan_introduce;
    }

    public void setMaster(int master) {
        this.master = master;
    }


}

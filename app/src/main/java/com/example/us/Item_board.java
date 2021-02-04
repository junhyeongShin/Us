package com.example.us;

public class Item_board {

    private int index;
    private String title;
    private String writer;
    private String time;
    private int views;

    public Item_board(int index, String title, String writer, String time,int views) {
        this.index = index;
        this.title = title;
        this.writer = writer;
        this.time = time;
        this.views = views;
    }

    public int getIndex() {
        return index;
    }

    public String getWriter() {
        return writer;
    }

    public String getTime() {
        return time;
    }

    public int getViews() {
        return views;
    }

    public String getTitle() {
        return title;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setViews(int views) {
        this.views = views;
    }


}

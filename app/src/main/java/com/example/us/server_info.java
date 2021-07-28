package com.example.us;

public class server_info {

//    private final String URL = "http://3.36.182.81";
//    private final String URL_node = "https://us-broadcast.p-e.kr:3000/";
//    private final String URL_service = "3.36.182.81";
    private final String URL = "http://192.168.56.1";
    private final String URL_node = "http://192.168.56.1:3000";
    private final String URL_service = "192.168.56.1";
    private final String URL_VIDEO = URL+"/Data/video/";
    private final String URL_IMG = URL+"/Data/img_file/";
    public int ROOM_ID ;

    public String getURL_node() {
        return URL_node;
    }

    public String getURL_VIDEO() {
        return URL_VIDEO;
    }

    public String getURL_IMG() {
        return URL_IMG;
    }

    //싱글톤 패턴 ~
    private static final server_info ourInstance = new server_info();

    public static server_info getInstance() {
        return ourInstance;
    }

    public server_info() { //원래는 private 이지만 Login 클래스 때문에 public 으로 변경.
    }
    // ~ 싱글톤 패턴


    public int getROOM_ID() {
        return ROOM_ID;
    }

    public void setROOM_ID(int ROOM_ID) {
        this.ROOM_ID = ROOM_ID;
    }

    public String getURL() {
        return URL;
    }

    public String getURL_service() {
        return URL_service;
    }
}

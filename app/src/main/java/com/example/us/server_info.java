package com.example.us;

public class server_info {

//    private final String URL = "http://13.125.205.208/comunity_app";
    private final String URL = "http://192.168.56.1";

    //싱글톤 패턴 ~
    private static final server_info ourInstance = new server_info();

    public static server_info getInstance() {
        return ourInstance;
    }

    public server_info() { //원래는 private 이지만 Login 클래스 때문에 public 으로 변경.
    }
    // ~ 싱글톤 패턴


    public String getURL() {
        return URL;
    }



}

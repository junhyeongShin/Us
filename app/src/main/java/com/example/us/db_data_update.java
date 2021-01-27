package com.example.us;

import com.android.volley.Response;
import com.android.volley.request.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class db_data_update extends StringRequest {                 ///extends  StringRequest 추가

    server_info server_info = new server_info();

    final static private String URL = com.example.us.server_info.getInstance().getURL();

//    final static private String URL =  "http://localhost/comunity_app";
//    final static private String URL =  "http://13.125.205.208/comunity_app";

    private Map<String,String> parameters;

    public db_data_update(String url,String column , String content, Response.Listener<String> listener) {

        //자신이 보낼 데이터
        super(Method.POST,URL+url, listener, null); //Method 방식

        System.out.println("db_data_update start to : "+URL+url);


        parameters = new HashMap<>();
        parameters.put("column",column);                            //데이터 변수명
        parameters.put("content",content);

        System.out.println("parameters : "+parameters);

        System.out.println("db_data_update end");

    }

    public db_data_update(String url,String column , String content,String column_2 , String content_2, Response.Listener<String> listener) {

        //자신이 보낼 데이터
        super(Method.POST,URL+url, listener, null); //Method 방식

        System.out.println("db_data_update start to : "+URL+url);

        parameters = new HashMap<>();
        parameters.put("column",column);                            //데이터 변수명
        parameters.put("column_2",column_2);
        parameters.put("content",content);
        parameters.put("content_2",content_2);

        System.out.println("parameters : "+parameters);

        System.out.println("db_data_update end");

    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
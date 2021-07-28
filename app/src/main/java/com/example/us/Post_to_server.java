package com.example.us;

import android.os.Build;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Post_to_server {

    server_info server_info = com.example.us.server_info.getInstance();

    private static final String TAG = "Post_to_server";

    String base_url = server_info.getURL();

    public Post_to_server(){

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String post_data(String url, String column, String content){
        InputStream Post_is = null;
        String result = "";

        try {
            String post_url = base_url + url;

            URL urlCon = new URL(post_url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("column", column);
            jsonObject.accumulate("content", content);


            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("json : "+json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");
            httpCon.setRequestProperty("charset","utf-8");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes(StandardCharsets.UTF_8));

            os.flush();

            // receive response as inputStream
            // http 연결 후 입력받은 json 데이터 판별
            try {
                Post_is = httpCon.getInputStream();
                // convert inputstream to string

                if(Post_is != null){
                    result = convertInputStreamToString(Post_is);

                    JSONObject jsonObject_result = new JSONObject(result.toString());
                    System.out.println(TAG+"jsonObject_result : "+jsonObject_result);

                    //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
                    //배열로된 자료를 가져올때
                    String json_chek_login = jsonObject_result.getString("result_check");//배열의 이름
                    System.out.println(TAG+"json_chek_login : "+json_chek_login);

                    if(json_chek_login.equals("OK")){
                        System.out.println("성공");
                    }else {
                        System.out.println("실패");
                    }
                    System.out.println(result);
                }
                //입력이 비었을경우
                else
                    result = "Did not work!";

            }

            //json 관련 오류
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //http 연결 제거
            finally {
                httpCon.disconnect();
            }

        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("POST_http 종료");
        System.out.println("POST_http result : "+result);

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String post_data_get_id(String url,String user_id){
        InputStream Post_is = null;
        String result = "";

        try {
            String post_url = base_url + url;

            URL urlCon = new URL(post_url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", user_id);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("json : "+json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();

            // receive response as inputStream
            // http 연결 후 입력받은 json 데이터 판별
            try {
                Post_is = httpCon.getInputStream();
                // convert inputstream to string

                if(Post_is != null){
                    result = convertInputStreamToString(Post_is);

                    JSONObject jsonObject_result = new JSONObject(result.toString());
                    System.out.println(TAG+"jsonObject_result : "+jsonObject_result);

                    //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
                    //배열로된 자료를 가져올때
                    String json_chek_login = jsonObject_result.getString("result_check");//배열의 이름
                    System.out.println(TAG+"json_chek_login : "+json_chek_login);

                    if(json_chek_login.equals("OK")){
                        System.out.println("성공");
                    }else {
                        System.out.println("실패");
                    }
                    System.out.println(result);
                }
                //입력이 비었을경우
                else
                    result = "Did not work!";

            }

            //json 관련 오류
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //http 연결 제거
            finally {
                httpCon.disconnect();
            }

        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("POST_http 종료");
        System.out.println("POST_http result : "+result);

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String post_get_img_id(String url,int user_id){
        InputStream Post_is = null;
        String result = "";

        try {
            String post_url = base_url + url;

            URL urlCon = new URL(post_url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", user_id);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("json : "+json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();

            // receive response as inputStream
            // http 연결 후 입력받은 json 데이터 판별
            try {
                Post_is = httpCon.getInputStream();
                // convert inputstream to string

                if(Post_is != null){
                    result = convertInputStreamToString(Post_is);

                    JSONObject jsonObject_result = new JSONObject(result.toString());
                    System.out.println(TAG+"jsonObject_result : "+jsonObject_result);

                    //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
                    //배열로된 자료를 가져올때
                    String json_chek_login = jsonObject_result.getString("result_check");//배열의 이름
                    System.out.println(TAG+"json_chek_login : "+json_chek_login);

                    if(json_chek_login.equals("OK")){
                        System.out.println("성공");
                    }else {
                        System.out.println("실패");
                    }
                    System.out.println(result);
                }
                //입력이 비었을경우
                else
                    result = "Did not work!";

            }

            //json 관련 오류
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //http 연결 제거
            finally {
                httpCon.disconnect();
            }

        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("POST_http 종료");
        System.out.println("POST_http result : "+result);

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String post_board_add(String url, int user_index,String title, String category, String board_content){
        InputStream Post_is = null;
        String result = "";

        try {
            String post_url = base_url + url;

            URL urlCon = new URL(post_url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("user_index", user_index);
            jsonObject.accumulate("title", title);
            jsonObject.accumulate("category", category);
            jsonObject.accumulate("content", board_content);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("json : "+json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");
            httpCon.setRequestProperty("charset","utf-8");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();

            // receive response as inputStream
            // http 연결 후 입력받은 json 데이터 판별
            try {
                Post_is = httpCon.getInputStream();

                // convert inputstream to string

                if(Post_is != null){
                    result = convertInputStreamToString(Post_is);

                    JSONObject jsonObject_result = new JSONObject(result.toString());
                    System.out.println(TAG+"jsonObject_result : "+jsonObject_result);

                    //배열로된 자료를 가져올때
                    String json_chek = jsonObject_result.getString("result_check");//배열의 이름
                    System.out.println(TAG+"json_chek_login : "+json_chek);

                    if(json_chek.equals("OK")){
                        System.out.println("성공");
                    }else {
                        System.out.println("실패");
                    }
                    System.out.println(result);
                }
                //입력이 비었을경우
                else
                    result = "Did not work!";

            }

            //json 관련 오류
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //http 연결 제거
            finally {
                httpCon.disconnect();
            }

        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("POST_http 종료");
        System.out.println("POST_http result : "+result);

        return result;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String post_data_id(String url,String user_id, String column, String content){
        InputStream Post_is = null;
        String result = "";

        try {
            String post_url = base_url + url;

            URL urlCon = new URL(post_url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", user_id);
            jsonObject.accumulate("column", column);
            jsonObject.accumulate("content", content);


            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("json : "+json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");
            httpCon.setRequestProperty("charset","utf-8");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();

            // receive response as inputStream
            // http 연결 후 입력받은 json 데이터 판별

            try {
                Post_is = httpCon.getInputStream();
                // convert inputstream to string

                if(Post_is != null){
                    result = convertInputStreamToString(Post_is);

                    JSONObject jsonObject_result = new JSONObject(result.toString());
                    System.out.println(TAG+"jsonObject_result : "+jsonObject_result);

                    //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
                    //배열로된 자료를 가져올때
                    String json_chek_login = jsonObject_result.getString("result_check");//배열의 이름
                    System.out.println(TAG+"json_chek_login : "+json_chek_login);

                    if(json_chek_login.equals("OK")){
                        System.out.println("성공");
                    }else {
                        System.out.println("실패");
                    }
                    System.out.println(result);
                }
                //입력이 비었을경우
                else
                    result = "Did not work!";

            }

            //json 관련 오류
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //http 연결 제거
            finally {
                httpCon.disconnect();
            }

        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("POST_http 종료");
        System.out.println("POST_http result : "+result);

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String convertInputStreamToString(InputStream is) {

        final char[] buffer = new char[8192];
        final StringBuilder result = new StringBuilder();

        // InputStream -> Reader
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            int charsRead;
            while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
                result.append(buffer, 0, charsRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(TAG+" : convertInputStreamToString e : "+e);
        }

        System.out.println(TAG+" : result.toString() : "+result.toString());

        return result.toString();

    }

}
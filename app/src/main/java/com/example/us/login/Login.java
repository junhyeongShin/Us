package com.example.us.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.us.*;
import com.example.us.wowza.live_stream_list;
import com.example.us.wowza.live_streams;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
//import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {


    private static final String TAG = "Login";
    private static String login_error = "";
    public static final String url = com.example.us.server_info.getInstance().getURL();

    private static boolean check = false;

    Button btn_login; //로그인버튼
    Button btn_signup; //회원가입 버튼
    EditText edit_text_id;
    EditText edit_text_pw;
    public static String USER_ID;

    @Override
    protected void onResume() {

        check = false;

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);//로그인버튼
        btn_signup = findViewById(R.id.btn_signup);//회원가입 버튼

        edit_text_id=findViewById(R.id.edit_text_id);
        edit_text_pw=findViewById(R.id.edit_text_pw);



        //로그인버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id;
                final String pw;
                id = edit_text_id.getText().toString();
                pw = edit_text_pw.getText().toString();
                Log.i("login_id : ",id);
                Log.i("login_pw : ",pw);

                Thread thread_url = new Thread(){
                    String user_id = id;
                    String user_passwd = pw;

                    public void run(){
                        try{
                            POST_login(url+"/Login/login_data_check.php",user_id,user_passwd);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                //http 연결 및 json 데이터 수신
                thread_url.start();
                System.out.println(TAG+" : thread_url.start()");

                //스레드 종료될때까지 대기
                try {
                    thread_url.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(TAG+" : thread_url.join()");

                System.out.println(TAG+" : check : "+check);
                if(check){
                    Toast.makeText(Login.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                    edit_text_id.setText("로그인 성공");

                    Intent intent = new Intent(getApplicationContext(), Activity_home.class);
                    startActivity(intent);
                    finish();
                }
                //예외처리
                else {
                    Toast.makeText(Login.this, login_error, Toast.LENGTH_LONG).show();
                    System.out.println("로그인 실패");
                }

                System.out.println(TAG+" : btn_login_click : end");
            }
        });
        //회원가입으로 이동
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_left,R.anim.out_right);
            }
        });

        System.out.println("http_post");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<String> task) {
                        if(!task.isSuccessful()){
                            System.out.println("실패 : task :"+task.getException());
                            return;
                        }

                        String token = task.getResult();

                        System.out.println("token : "+token);
                    }
                });

    }



    // JSon 오브젝트 생성후
    // http에 응답 요청 post방식 json데이터사용
    // 보내는 데이터 : 로그인 정보
    // 받는 데이터 true or false
    //
    public String POST_login(String url, String user_id, String user_passwd){

        InputStream Post_is = null;
        String result = "";

        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", user_id);
            jsonObject.accumulate("user_passwd", user_passwd);


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
                System.out.println("로그인 시도");

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

                    String json_user_data = jsonObject_result.getString("result_data");//배열의 이름
                    System.out.println(TAG+"json_user_data : "+json_user_data);

                    JSONObject jsonObject_data = new JSONObject(json_user_data);
                    System.out.println(TAG+"jsonObject_result : "+jsonObject_data);


                    if(json_chek_login.equals("OK")){

                        check = true;
                        System.out.println("로그인 성공");

                        //TODO:시연때는 LOGIN에서 정보 입력 해주고 종료
                        user_info login_user_info = user_info.getInstance();
                        login_user_info.setUser_ID(jsonObject_data.getString("email"));
                        login_user_info.setUser_name(jsonObject_data.getString("username"));
                        login_user_info.setUser_intro_profile(jsonObject_data.getString("intro_profile"));
                        login_user_info.setUser_index_number(jsonObject_data.getInt("index"));
                        login_user_info.setUser_img_profile(jsonObject_data.getInt("img_profile"));

                        System.out.println(TAG+" - email : "+jsonObject_data.getString("email"));
                        System.out.println(TAG+" - username : "+jsonObject_data.getString("username"));
                        System.out.println(TAG+" - intro_profile : "+jsonObject_data.getString("intro_profile"));
                        System.out.println(TAG+" - index_number : "+user_info.getInstance().getUser_index_number());



                        // 서버에 데이터 요청 후, 각 이미지 및 텍스트에 적용하는 쓰레드
                        new Thread(new Runnable() {
                            @Override
                            public void run() {


                                final Post_to_server post_to_server =new Post_to_server();

                                //포스트로 데이터 요청.
                                String result_user_data = post_to_server.post_data_get_id("/Data/user_data_get.php",user_info.getInstance().getUser_ID());

                                try {
                                    JSONObject jsonObject = new JSONObject(result_user_data);
                                    String result_img_data_decode =  jsonObject.getString("result_img");
                                    JSONObject jsonObject_img_decode = new JSONObject(result_img_data_decode);


                                    //이미지 부분 json 파싱된 uri를 이용해 이미지 표시
                                    login_user_info.setImg_path(jsonObject_img_decode.getString("img_path"));



                                    System.out.println("img url : " + Uri.parse(server_info.getInstance().getURL()+"/Data/img_file/"+jsonObject_img_decode.getString("img_path")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();




                    }else if(json_chek_login.equals("E_id")){
                        login_error = "없는 ID입니다.";
                    }else if(json_chek_login.equals("E_pw")){
                        login_error = "비밀번호가 틀렸습니다.";
                    }else {
                        System.out.println("로그인 실패");
                        System.out.println(result);
                    }

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



        System.out.println(TAG+" : POST_http 종료");
        return result;

    }

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


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    //아이디 비밀번호 일치 확인
    //로그인 edit_text에서 들고온 데이터
    //db - user/table 에서 들고온 데이터
//    private boolean loginValidation(String id, String password) {
//
//        pref = getSharedPreferences("ID",0);
//
//
//        Log.i("pref id : ",pref.getString("id"+"id",""));
//        Log.i("pref pw : ",pref.getString("id"+"pw",""));
//        System.out.println("login ID : "+pref.getString("id"+"id",""));
//        System.out.println("login PW : "+pref.getString("id"+"pw",""));
//
//        //입력값이 없을때
//        if(Objects.equals(id, "") || Objects.equals(password, "")){
//            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//        //로그인 성공시
//        if(pref.getString(id+"id","").equals(id) && pref.getString(id+"pw","").equals(password)) {
//            // login success
//            return true;
//        }
//        // 일치하는 ID가 없는경우
//        else if (pref.getString(id+"id", "") == null){
//            // sign in first
//            Toast.makeText(getApplicationContext(), "없는 ID입니다.", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        // 비밀번호가 틀린경우
//        else {
//            // login failed
//            Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
//            return false;
//        }
//    }
}
package com.example.us;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class Activity_clan_edit extends AppCompatActivity {

    private static final String TAG = "Activity_edit_profile";
    ImageView imageView_clan_add;
    Boolean img_update = false;
    String img_path;
    int img_index;

    String clan_name = "";
    String clan_intro = "";

    String user_id = user_info.getInstance().getUser_ID();

    server_info server_info = new server_info();

    String server_info_url = server_info.getURL();

    EditText Edittext_edit_profile_name;
    EditText Edittext_edit_profile_intro;

    //액티비티 실행시 필요한 데이터 (유저 아이디, 이름, 자기소개, 프로필 사진)
    //요청해서 각 레이아웃에 적용
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void init(){


        final Post_to_server post_to_server =new Post_to_server();


        // 서버에 데이터 요청 후, 각 이미지 및 텍스트에 적용하는 쓰레드
        new Thread(new Runnable() {
            @Override
            public void run() {

                clan_name = "";
                clan_intro = "";
                String clan_img_path = "";

                String result_user_data = post_to_server.post_data_get_id("/Data/user_data_get.php",user_id);
                try {
                    JSONObject jsonObject = new JSONObject(result_user_data);
                    String result_user_data_decode =  jsonObject.getString("result_data");
                    JSONObject jsonObject_decode = new JSONObject(result_user_data_decode);
                    clan_name = jsonObject_decode.getString("username");
                    clan_intro = jsonObject_decode.getString("intro_profile");

                    String result_img_data_decode =  jsonObject.getString("result_img");
                    JSONObject jsonObject_img_decode = new JSONObject(result_img_data_decode);

                    //이미지 부분 json 파싱된 uri를 이용해 이미지 표시
                    clan_img_path = jsonObject_img_decode.getString("img_path");

                    Glide.with(imageView_clan_add.getContext()).load(server_info.getInstance().getURL_IMG()+clan_img_path).into(imageView_clan_add);


                    System.out.println("img url : " + Uri.parse(server_info_url+"/Data/img_file/"+clan_img_path));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        final Handler mhandler = new Handler();
        Thread mthread = new Thread(new Runnable(){
            @Override
            public void run() {
                mhandler.post(new Runnable(){
                    @Override
                    public void run() {

                        Edittext_edit_profile_name = findViewById(R.id.Edittext_edit_profile_name);
                        Edittext_edit_profile_name.setText(clan_name);

                        Edittext_edit_profile_intro = findViewById(R.id.Edittext_edit_profile_intro);
                        Edittext_edit_profile_intro.setText(clan_intro);
                    }
                });
            }
        });
        mthread.start();

    }


    @Override
    protected void onResume() {
        super.onResume();


//        imageView_edit_profile.setBackground(new ShapeDrawable(new OvalShape()));
//        imageView_edit_profile.setClipToOutline(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_add);

        imageView_clan_add = findViewById(R.id.imageView_clan_add);

        Button btn_clan_add_img = findViewById(R.id.btn_clan_add_img);
        btn_clan_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);
            }
        });


        Button btn_clan_add_input = findViewById(R.id.btn_clan_add_input);
        btn_clan_add_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //프로필 수정 완료 버튼을 누르면
                // 1. 이미지 업데이트 요청
                // 2. 닉네임, 소개 업데이트 요청


                Thread thread_url_imageUpload = new Thread(){

                    public void run(){
                        try{
                            imageUpload(img_path);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                //http 연결 및 json 데이터 수신
                thread_url_imageUpload.start();
                System.out.println(TAG+" : thread_url_profile.start()");

                Thread thread_url_profile = new Thread(){

                    public void run(){
                        try{
                            // post_to_server 클래스를 이용해서 각 edit_text의 값을 서버로 전송한다.
                            // 아무것도 입력하지 않았을경우 if 문으로 제외

//                            Edittext_edit_profile_name = findViewById(R.id.Edittext_clan_add_name);
//                            clan_name = Edittext_edit_profile_name.getText().toString();
//
//                            Edittext_edit_profile_intro = findViewById(R.id.Edittext_clan_add_name_intro);
//                            clan_name = Edittext_edit_profile_intro.getText().toString();

                            Post_to_server post_to_server = new Post_to_server();
                            if(!clan_name.equals("")){
                                post_to_server.post_data_id("/Data/user_data_edit.php",user_id,"clan_name", clan_name);
                                com.example.us.user_info.getInstance().setUser_name(clan_name);
                            }
                            if(!clan_intro.equals("")){
                                post_to_server.post_data_id("/Data/user_data_edit.php",user_id,"intro_profile", clan_intro);
                                com.example.us.user_info.getInstance().setUser_intro_profile(clan_intro);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                //http 연결 및 json 데이터 수신
                thread_url_profile.start();
                System.out.println(TAG+" : thread_url_profile.start()");

                //스레드 종료될때까지 대기
                try {
                    thread_url_imageUpload.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                init();

                finish();

            }
        });

        //상단 툴바 <- 버튼 클릭시
        ImageButton ibtn_clan_add_back = findViewById(R.id.ibtn_clan_add_back);
        ibtn_clan_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        // 서버에서 데이터 불러오기
        init();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView_clan_add.setImageBitmap(img);

                    // 이미지 업로드 준비
                    System.out.println("data.getData() : "+data.getData());
                    img_update = true;
                    img_path = getRealPathFromURI(data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //업로드 하기위한 img_path로 변경
    public String getRealPathFromURI(Uri uri) {
        int index = 0;
        System.out.println("getRealPathFromURI uri : "+uri);
        String[] proj = {MediaStore.Images.Media.DATA}; // 이미지 경로로 해당 이미지에 대한 정보를 가지고 있는 cursor 호출
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null); // 데이터가 있으면(가장 처음에 위치한 레코드를 가리킴)
        if (cursor.moveToFirst()) { // 해당 필드의 인덱스를 반환하고, 존재하지 않을 경우 예외를 발생시킨다.
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        Log.d("getRealPathFromURI", "getRealPathFromURI: " + cursor.getString(index));
        return cursor.getString(index);
    }

    private void imageUpload(final String imagePath) {
        //이미지 업로드용 멀티파트 요청
        //서버에서 받은 요청중 이미지 index값을 user 데이터에 업데이트

        System.out.println("imageUpload start");
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, server_info_url+"/Data/img_file/img_upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONObject jObj_response = new JSONObject(response);
                            String message = jObj_response.getString("result_index");

                            JSONObject jObj_max = new JSONObject(message);
                            final String max_img_id = jObj_max.getString("MAX(id)");
                            System.out.println("max_img_id"+max_img_id);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            //TODO : 이미지 업로드 후 받은 max_id 값으로 클랜 생성
                            Thread thread_url = new Thread(){

                                public void run(){
                                    try{
                                        Post_to_server post_to_server = new Post_to_server();
                                        System.out.println("user_info.user_ID : "+user_id);
                                        //프로필 사진 업데이트
                                        post_to_server.post_data_id("/Data/user_data_edit.php",user_id,"img_profile", max_img_id);
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


                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        smr.addFile("image", imagePath);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smr);


        System.out.println("imageUpload end");
    }
}
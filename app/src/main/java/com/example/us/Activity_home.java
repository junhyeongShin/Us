package com.example.us;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Response;
import com.android.volley.request.StringRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Activity_home extends AppCompatActivity {

    private static final String TAG = "Activity_home";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //TODO:시연때는 LOGIN에서 정보 입력 해주고 종료
        user_info login_user_info = user_info.getInstance();
        login_user_info.setUser_ID("test_id");
        login_user_info.setUser_name("test_name");
        login_user_info.setUser_intro_profile("test_intro");
        login_user_info.setUser_index_number(8);

        System.out.println(TAG+"test_id : "+"ggga");
        System.out.println(TAG+"test_id : "+"test_ggg");
        System.out.println(TAG+"test_id : "+"test_intro");
        System.out.println(TAG+"test_id : "+"index_number : "+user_info.getInstance().getUser_index_number());

        Button btn_test_imgupload = findViewById(R.id.btn_test_imgupload);
        btn_test_imgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(mIntent);

            }
        });

        //하단 네비게이션바 버튼
        //각 버튼별로 이동할 엑티비티가 정해져있고, 지금 열려있는 엑티비티는 버튼 리스너 없음.
        Button btn_bottom_guild = findViewById(R.id.btn_bottom_guild);
        btn_bottom_guild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_guild.class);
                startActivity(mIntent);
                finish();
            }
        });
        Button btn_bottom_chat = findViewById(R.id.btn_bottom_chat);
        btn_bottom_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_chat.class);
                startActivity(mIntent);
                finish();
            }
        });
        Button btn_bottom_board = findViewById(R.id.btn_bottom_board);
        btn_bottom_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_board.class);
                startActivity(mIntent);
                finish();
            }
        });
        Button btn_bottom_more = findViewById(R.id.btn_bottom_more);
        btn_bottom_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_more.class);
                startActivity(mIntent);
                finish();
            }
        });



    }




}
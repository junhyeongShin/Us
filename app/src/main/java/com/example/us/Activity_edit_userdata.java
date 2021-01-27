package com.example.us;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

//TODO:초기화 작업필요함 : db 유저 정보에서 데이터 가져와서 뿌려주기
//TODO:비밀번호 수정 버튼 클릭시 비밀번호 변경 서버로 요청
public class Activity_edit_userdata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userdata);

        ImageButton ibtn_edit_back = findViewById(R.id.ibtn_edit_back);
        ibtn_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_edit_profile.class);
                startActivity(mIntent);
            }
        });

    }
}
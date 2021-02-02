package com.example.us;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Popup_board_link extends AppCompatActivity {

    EditText edit_text_popup_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_board_link);

        Button btn_popup_link = findViewById(R.id.btn_popup_link);
        btn_popup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClose(v);
            }
        });


    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 가져오기
        edit_text_popup_link = findViewById(R.id.edit_text_popup_link);

        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("link", edit_text_popup_link.getText().toString());
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
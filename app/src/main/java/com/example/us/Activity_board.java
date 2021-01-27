package com.example.us;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Activity_board extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Activity_board";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);


        //하단 네비게이션바 버튼
        //각 버튼별로 이동할 엑티비티가 정해져있고, 지금 열려있는 엑티비는 버튼 리스너 없음.
        Button btn_bottom_home = findViewById(R.id.btn_bottom_home);
        btn_bottom_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_home.class);
                startActivity(mIntent);
                finish();

            }
        });
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

        Button btn_bottom_more = findViewById(R.id.btn_bottom_more);
        btn_bottom_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_more.class);
                startActivity(mIntent);
                finish();
            }
        });

        //최상단 카테고리 리스너
        //어뎁터를 이용해서 value - string - spinner_category 지정해놓은 값 불러옴.
        Spinner spinner_board_category = findViewById(R.id.spinner_board_category);
        ArrayAdapter<CharSequence> adapter_spinner = ArrayAdapter.createFromResource(this,
                R.array.spinner_category, R.layout.spinner_item_category);
        adapter_spinner.setDropDownViewResource(R.layout.spinner_dropdown_category);
        spinner_board_category.setAdapter(adapter_spinner);
        spinner_board_category.setOnItemSelectedListener(this);


    }

    //스피너에서 선택된 값을
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 0 : 파오캐
        // 1 : 카오스
        // 2 : 원랜디
        // 3 : 뿔레전쟁
        // 4 : 기타

        String category_selected = "";

        if(id==0){
            category_selected = "파오캐";
        }else if(id==1){
            category_selected = "카오스";
        }else if(id==2){
            category_selected = "원랜디";
        }else if(id==3){
            category_selected = "뿔레전쟁";
        }else {
            category_selected = "기타";
        }

        System.out.println(TAG+" : onItemSelected category_selected : "+category_selected);
//        System.out.println(TAG+" : onItemSelected position : "+position);
        System.out.println(TAG+" : onItemSelected id : "+id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println(TAG+"onNothingSelected");
    }
}
package com.example.us;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;

public class Activity_re_comment_add extends AppCompatActivity {

    //뒤로가기 버튼 (댓글 -> 게시판)
    ImageButton ibtn_re_comment_add_back;

    //댓글 달 댓글의 내용
    TextView textview_re_comment_content;

    //추가 할 댓글 내용
    EditText edit_text_re_comment_add;

    //댓글 추가 버튼
    Button btn_re_comment_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_comment_add);

        Intent mIntent =getIntent();
        final int comment_id = mIntent.getIntExtra("comment_id",0);
        String comment_content = mIntent.getStringExtra("content");


        textview_re_comment_content = findViewById(R.id.textview_re_comment_content);
        textview_re_comment_content.setText(comment_content);

        if(comment_id == 0){
            System.out.println(" intent error : ");
        }

        ibtn_re_comment_add_back = findViewById(R.id.ibtn_re_comment_add_back);
        ibtn_re_comment_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_re_comment_add = findViewById(R.id.btn_re_comment_add);
        btn_re_comment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //유저정보, 댓글내용, 게시판 아이디 필요
                edit_text_re_comment_add= findViewById(R.id.edit_text_re_comment_add);
                String content = edit_text_re_comment_add.getText().toString();

                if(content.equals("")){
                    Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT).show();
                }else {

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(server_info.getInstance().getURL())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

                    HashMap<String, Object> input = new HashMap<>();
                    input.put("user_id", user_info.getInstance().getUser_index_number());
                    input.put("comment_id", comment_id);
                    input.put("content", content);

                    System.out.println("input : " + input);

                    retrofit_api.postData_re_comment_add(input).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                String data = response.body();
                                System.out.println("성공");
                                System.out.println(data);

                                finish();
                            }
                            System.out.println("성공 response : " + response);
                            System.out.println("성공 call : " + call);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            System.out.println("실패 : call : " + call);
                            System.out.println("실패 : Throwable : " + t);
                        }
                    });


                }


    }

});
    }
}
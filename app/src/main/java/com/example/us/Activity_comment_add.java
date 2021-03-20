package com.example.us;
import android.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class Activity_comment_add extends AppCompatActivity {

    //뒤로가기 버튼 (댓글 -> 게시판)
    ImageButton ibtn_board_view_back;

    //댓글 달 게시물의 제목
    TextView textview_comment_title;

    //댓글 내용
    EditText edit_text_comment_add;

    //댓글 추가 버튼
    Button btn_comment_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_add);


        Intent mIntent =getIntent();
        final int board_id = mIntent.getIntExtra("board_id",0);

        if(board_id == 0){
            System.out.println(" intent error : ");
        }

        ImageButton ibtn_comment_add_back = findViewById(R.id.ibtn_comment_add_back);
        ibtn_comment_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_comment_add = findViewById(R.id.btn_comment_add);
        btn_comment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 댓글 추가하기 POST
                //유저정보, 댓글내용, 게시판 아이디 필요
                edit_text_comment_add= findViewById(R.id.edit_text_comment_add);
                String content = edit_text_comment_add.getText().toString();

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
                    input.put("board_id", board_id);
                    input.put("content", content);

                    System.out.println("input : " + input);

                    retrofit_api.postData_comment_add(input).enqueue(new Callback<String>() {
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
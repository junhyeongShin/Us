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

public class Activity_comment_edit extends AppCompatActivity {

    // 댓글 수정할 내용
    String content;

    // 수정할 댓글의 게시물 제목
    String title;

    // 댓글의 index
    int id;

    // 게시판의 index
    int board_id;

    // 댓글 수정할 내용 적는곳
    EditText edit_text_comment_edit;

//    // 게시물 제목
//    TextView textview_comment_title;

    // 댓글 수정 버튼
    Button btn_comment_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_edit);

        Intent mIntent = getIntent();
//        title = mIntent.getStringExtra("title");
        id = mIntent.getIntExtra("id",0);
        board_id =  mIntent.getIntExtra("board_id",0);
        content = mIntent.getStringExtra("content");


        edit_text_comment_edit = findViewById(R.id.edit_text_comment_edit);
        edit_text_comment_edit.setText(content);

//        textview_comment_title= findViewById(R.id.textview_comment_title);
//        textview_comment_title.setText(title);


        // 댓글 수정 버튼
        btn_comment_edit = findViewById(R.id.btn_comment_edit);
        btn_comment_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : 댓글 수정

                content = edit_text_comment_edit.getText().toString();


                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(server_info.getInstance().getURL())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

                HashMap<String, Object> input = new HashMap<>();
                input.put("id", id);
                input.put("content", content);

                retrofit_api.postData_comment_edit(input).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String data = response.body();
                            System.out.println("성공");
                            System.out.println(data);
                            Toast.makeText(getApplicationContext(), "게시물 수정 완료.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println("call : "+call);
                        System.out.println("Throwable :"+t);
                        System.out.println("실패");
                    }
                });
            }
        });

        //뒤로가기 버튼
        ImageButton ibtn_comment_edit_back = findViewById(R.id.ibtn_comment_edit_back);
        ibtn_comment_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



}
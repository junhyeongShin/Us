package com.example.us;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Activity_guild extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_guild);


        ImageButton ibtn_clan_search = findViewById(R.id.ibtn_clan_search);
        ibtn_clan_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("클랜 찾아보기 클릭");

//                Intent mIntent = new Intent(getApplicationContext(), Activity_clan_search.class);
//                startActivity(mIntent);

            }
        });

        ImageButton ibtn_clan_add = findViewById(R.id.ibtn_clan_add);
        ibtn_clan_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클랜 만들기 클릭");
                Intent mIntent = new Intent(getApplicationContext(), Activity_clan_add.class);
                startActivity(mIntent);

            }
        });


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

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(server_info.getInstance().getURL())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);
//
//        retrofit_api.(id).enqueue(new Callback<List<comment>>() {
//            @Override
//            public void onResponse(Call<List<comment>> call, Response<List<comment>> response) {
//                System.out.println("onResponse : call" + call);
//                System.out.println("onResponse : response" + response);
//                if (response.isSuccessful()) {
//                    List<comment> board_list = response.body();
//                    System.out.println("성공");
//
//                    for (int i = 0; i < board_list.size(); i++) {
//                        System.out.println("ID : " + board_list.get(i).getId());
//                    }
//                    mData = (ArrayList<comment>) board_list;
//
//                    RecyclerView recyclerView = findViewById(R.id.recyclerview_board_view);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(mData);
//                    recyclerView.setAdapter(recyclerAdapter);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<comment>> call, Throwable t) {
//                System.out.println("getData_comment_list 실패 Throwable : " + t.toString());
//                System.out.println("getData_comment_list 실패 call : " + call.toString());
//            }
//        });
//    }

}
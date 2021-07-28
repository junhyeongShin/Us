package com.example.us;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class Activity_my_comment extends AppCompatActivity {

    private ArrayList<comment> comment_list_recycler = new ArrayList<>();
    private static final String TAG = "Activity_my_comment : ";

    private Adapter_comment_my Adapter_comment_my;
    private RecyclerView recyclerview_comment_my;

    Button btn_del_comment_my;
    boolean is_del = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_comment);

        ImageButton ibtn_back_comment_my = findViewById(R.id.ibtn_back_comment_my);
        ibtn_back_comment_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_del_comment_my= findViewById(R.id.btn_del_comment_my);
        btn_del_comment_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("대댓글 삭제버튼 클릭");
                Adapter_comment_my.del_item();

            }
        });

        ImageButton ibtn_my_comment_del = findViewById(R.id.ibtn_my_comment_del);
        ibtn_my_comment_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_del){

                    is_del = false;
                    Adapter_comment_my.setIs_del(is_del);
                    Adapter_comment_my.clear_checked();
                    btn_del_comment_my.animate()
                            .translationY(btn_del_comment_my.getHeight())
                            .alpha(0.0f)
                            .setDuration(500);
                    btn_del_comment_my.setVisibility(View.GONE);

                }else {

                    is_del = true;
                    Adapter_comment_my.setIs_del(is_del);
                    btn_del_comment_my.setVisibility(View.INVISIBLE);
                    btn_del_comment_my.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(500);
                    btn_del_comment_my.setVisibility(View.VISIBLE);

                }

            }
        });

        // 리사이클러뷰 부분
        recyclerview_comment_my = findViewById(R.id.recyclerview_comment_my);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_comment_my.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            retrofit_api();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void retrofit_api() throws UnsupportedEncodingException {

        //TODO:서버에서 제대로 못 불러옴 php 에서 확인해볼것것

        System.out.println(TAG+" - retrofit_api : start");
        System.out.println(TAG+" - retrofit_api_url : "+server_info.getInstance().getURL());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.comment_list_my(user_info.getInstance().getUser_index_number()).enqueue(new Callback<List<comment>>() {
            @Override
            public void onResponse(Call<List<comment>> call, Response<List<comment>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<comment> board_list = response.body();
                    System.out.println("성공");
                    comment_list_recycler = (ArrayList<comment>) board_list;
                    init(comment_list_recycler);
                }
            }

            @Override
            public void onFailure(Call<List<comment>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }

        });

    }

    //초기화
    private void init(ArrayList<comment> array_list_comment) {

        System.out.println("Adapter start : "+array_list_comment);
        Adapter_comment_my = new Adapter_comment_my(array_list_comment);
        Adapter_comment_my.notifyDataSetChanged();
        recyclerview_comment_my.setAdapter(Adapter_comment_my);

    }



}
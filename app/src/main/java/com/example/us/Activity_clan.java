package com.example.us;

import android.content.Intent;
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
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Activity_clan extends AppCompatActivity {

    private static final String TAG = Class.class.getName()+" : ";

    RecyclerView recyclerview_clan_list;

    private ArrayList<Clan_item> clan_list_recycler = new ArrayList<>();


    @Override
    protected void onResume() {
        super.onResume();

        try {
            retrofit_api();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan);

        ImageButton ibtn_clan_search = findViewById(R.id.ibtn_clan_search);
        ibtn_clan_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("클랜 찾아보기 클릭");
                Intent mIntent = new Intent(getApplicationContext(), Activity_clan_search.class);
                startActivity(mIntent);
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

        recyclerview_clan_list = findViewById(R.id.recyclerview_clan_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_clan_list.setLayoutManager(linearLayoutManager);


    }

    //초기화
    private void init(ArrayList<Clan_item> array_list) {

        System.out.println("Adapter start : "+array_list);
        clan_list_adapter itemAdapter = new clan_list_adapter(array_list);
        itemAdapter.notifyDataSetChanged();
        recyclerview_clan_list.setAdapter(itemAdapter);

    }

    private void retrofit_api() throws UnsupportedEncodingException {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getClan_list_user(user_info.getInstance().getUser_index_number()).enqueue(new Callback<List<Clan_item>>() {
            @Override
            public void onResponse(Call<List<Clan_item>> call, Response<List<Clan_item>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<Clan_item> clan_list = response.body();
                    System.out.println("성공");

                    clan_list_recycler = (ArrayList<Clan_item>) clan_list;

                    init(clan_list_recycler);

                }
            }

            @Override
            public void onFailure(Call<List<Clan_item>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }

}
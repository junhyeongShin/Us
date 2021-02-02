package com.example.us;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_board extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Activity_board / ";

    String category_selected = "";

    private ArrayList<Item_board> array_item_board = new ArrayList<>();
    private Adapter itemAdapter;
    private RecyclerView RecyclerView_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Button btn_write_board = findViewById(R.id.btn_write_board);
        btn_write_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_board_add.class);
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


        // 리사이클러뷰 부분
        // 하단에 '+ 아이템 추가하기' 텍스트뷰 - 눌렀을 때 아이템 추가되는 기능 구현
        RecyclerView_main = findViewById(R.id.recyclerview_board);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView_main.setLayoutManager(linearLayoutManager);

        //TODO:전체보기로 추가

        init(array_item_board);

        Button btn_all_board = findViewById(R.id.btn_all_board);
        btn_all_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(TAG+"-btn_all_board- : onclick");

                retrofit_api("all");
                System.out.println(TAG+"retrofit_api(all)");

                //TODO:전체보기로 추가
//                init(array_item_board);
            }
        });

        Button btn_all_views = findViewById(R.id.btn_all_views);
        btn_all_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Item_board> array_item_board_selected = new ArrayList<>();

                //TODO:인기순
                init(array_item_board_selected);

            }
        });

        Button btn_all_favorites = findViewById(R.id.btn_all_favorites);
        btn_all_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Item_board> array_item_board_selected = new ArrayList<>();

                //TODO:즐겨찾기
                user_info.getInstance().getUser_favorites_list();
                init(array_item_board_selected);
            }
        });


    }

    private void retrofit_api(String request_list){

        System.out.println(TAG+" - retrofit_api : start");
        System.out.println(TAG+" - retrofit_api_url : "+server_info.getInstance().getURL());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getData_board(user_info.getInstance().getUser_index_number(), category_selected,request_list).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> data = response.body();
                    System.out.println("성공");
                    System.out.println(data.get(0).getTitle());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("실패 : "+t);
            }
        });

    }
//초기화
    private void init(ArrayList<Item_board> array_item_board) {

        System.out.println("Adapter start : "+array_item_board);
        itemAdapter = new Adapter(array_item_board);
        RecyclerView_main.setAdapter(itemAdapter);

        Button btn_test_recycle = findViewById(R.id.btn_test_recycle);
        btn_test_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(TAG+"onClick");
                itemAdapter.addItem(new Item_board(1,"아이템","test","test",0));
                itemAdapter.notifyDataSetChanged();
            }
        });

    }

    //스피너에서 선택된 값을
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 0 : 파오캐
        // 1 : 카오스
        // 2 : 원랜디
        // 3 : 뿔레전쟁
        // 4 : 기타

        if(id==0){
            category_selected = "파오캐";
        }else if(id==1){
            category_selected = "카오스";
        }else if(id==2){
            category_selected = "원랜디";
        }else if(id==3){
            category_selected = "뿔레전쟁";
        }else if(id==4) {
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
package com.example.us;
import android.os.Handler;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_board extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @Override
    protected void onStart() {
        super.onStart();

        try {
            retrofit_api();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static final String TAG = "Activity_board / ";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(0);
    }

    String category_selected = "전체";


    private ArrayList<Board_list> board_list_recycler = new ArrayList<>();
    private ArrayList<Board_list> board_list_recycler_search = new ArrayList<>();


    private Adapter itemAdapter;
    private RecyclerView RecyclerView_main;

    @Override
    protected void onResume() {
        super.onResume();
        init(board_list_recycler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //검색창
        final EditText edit_text_find_board = findViewById(R.id.edit_text_find_board);
        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        edit_text_find_board.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = edit_text_find_board.getText().toString();
                search(text);
            }
        });


        FloatingActionButton fab_add_board = findViewById(R.id.fab_add_board);
        fab_add_board.setOnClickListener(new View.OnClickListener() {
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
                clan_check();
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

        try {
            retrofit_api();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        전체 인기순 버튼 주석 처리

//        Button btn_all_board = findViewById(R.id.btn_all_board);
//        btn_all_board.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(TAG+"-btn_all_board- : onclick");
//
//                try {
//                    retrofit_api("all");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(TAG+"retrofit_api(all)");
//
//                init(board_list_recycler);
//            }
//        });
//
//        Button btn_all_views = findViewById(R.id.btn_all_views);
//        btn_all_views.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                System.out.println(TAG+"-btn_all_views- : onclick");
//
//                try {
//                    retrofit_api("views");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(TAG+"retrofit_api(views)");
//
//
//
//            }
//        });


    }



    private void retrofit_api() throws UnsupportedEncodingException {

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

        retrofit_api.getData_board_all().enqueue(new Callback<List<Board_list>>() {
            @Override
            public void onResponse(Call<List<Board_list>> call, Response<List<Board_list>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<Board_list> board_list = response.body();
                    System.out.println("성공");

                    board_list_recycler = (ArrayList<Board_list>) board_list;

                    init(board_list_recycler);

                }
            }

            @Override
            public void onFailure(Call<List<Board_list>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }

    private void retrofit_api_category(String category_selected) throws UnsupportedEncodingException {

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

        retrofit_api.getData_board_category(category_selected).enqueue(new Callback<List<Board_list>>() {
            @Override
            public void onResponse(Call<List<Board_list>> call, Response<List<Board_list>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<Board_list> board_list = response.body();
                    System.out.println("성공");

                    board_list_recycler = (ArrayList<Board_list>) board_list;

                    init(board_list_recycler);

                }
            }

            @Override
            public void onFailure(Call<List<Board_list>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }
    //초기화
    private void init(ArrayList<Board_list> array_list_board) {

        System.out.println("Adapter start : "+array_list_board);
        itemAdapter = new Adapter(array_list_board);
        itemAdapter.notifyDataSetChanged();
        RecyclerView_main.setAdapter(itemAdapter);

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        board_list_recycler_search = new ArrayList<>();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            System.out.println("문자 입력이 없을때");
            init(board_list_recycler);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < board_list_recycler.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (board_list_recycler.get(i).getTitle().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    board_list_recycler_search.add(board_list_recycler.get(i));
                }
            }
            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
            init(board_list_recycler_search);
        }
    }

    //스피너에서 선택된 값을
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 0 : 파오캐
        // 1 : 카오스
        // 2 : 원랜디
        // 3 : 뿔레전쟁
        // 4 : 기타

        if(id==0) {
            category_selected = "전체";
            try {
                retrofit_api();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if(id==1){
            category_selected = "파오캐";
            try {
                retrofit_api_category(category_selected);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(id==2){
            category_selected = "카오스";
            try {
                retrofit_api_category(category_selected);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(id==3){
            category_selected = "원랜디";
            try {
                retrofit_api_category(category_selected);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(id==4){
            category_selected = "뿔레";
            try {
                retrofit_api_category(category_selected);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(id==5) {
            category_selected = "기타";
            try {
                retrofit_api_category(category_selected);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        System.out.println(TAG+" : onItemSelected category_selected : "+category_selected);
//        System.out.println(TAG+" : onItemSelected position : "+position);
        System.out.println(TAG+" : onItemSelected id : "+id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println(TAG+"onNothingSelected");
    }


    void clan_check(){
        int user_id = user_info.getInstance().getUser_index_number();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);


        retrofit_api.getClan_check(user_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String data = response.body();
                    assert data != null;
                    if(data.equals("true")){
                        System.out.println("getClan_check: true");

                        Intent mIntent = new Intent(getApplicationContext(), Activity_clan.class);
                        startActivity(mIntent);
                    }else {
                        System.out.println("getClan_check: "+data);

                        Intent mIntent = new Intent(getApplicationContext(), Activity_no_clan.class);
                        startActivity(mIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("getClan_check 실패 call : "+call.toString());
                System.out.println("getClan_check 실패 Throwable : "+t.toString());
            }
        });


    }
}
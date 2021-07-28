package com.example.us;
import android.os.Handler;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Activity_my_board extends AppCompatActivity {

    private ArrayList<Board_list> board_list_recycler = new ArrayList<>();
    private ArrayList<Board_list> board_list_recycler_search = new ArrayList<>();

    private Adapter_board_my Adapter_board_my;
    private RecyclerView recyclerview_board_my;

    private static final String TAG = Class.class.getName()+" : ";
    boolean is_del = false;

    Button btn_del_board_my;



    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_board);

        ImageButton ibtn_back_board_my = findViewById(R.id.ibtn_back_board_my);
        ibtn_back_board_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //검색창
        final EditText edit_text_find_board_my = findViewById(R.id.edit_text_find_board_my);
        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        edit_text_find_board_my.addTextChangedListener(new TextWatcher() {
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
                String text = edit_text_find_board_my.getText().toString();
                search(text);
            }
        });

        btn_del_board_my = findViewById(R.id.btn_del_board_my);
        btn_del_board_my.setVisibility(View.GONE);
        btn_del_board_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Adapter_board_my.check_del(Adapter_board_my.getChecked_list());

            }
        });

        ImageButton ibtn_my_board_del = findViewById(R.id.ibtn_my_board_del);
        ibtn_my_board_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_del){

                    is_del = false;
                    Adapter_board_my.setIs_del(is_del);
                    Adapter_board_my.clear_checked();
                    btn_del_board_my.animate()
                            .translationY(btn_del_board_my.getHeight())
                            .alpha(0.0f)
                            .setDuration(500);
                    btn_del_board_my.setVisibility(View.GONE);




                }else {

                    is_del = true;
                    Adapter_board_my.setIs_del(is_del);

                    final Handler mhandler = new Handler();
                    Thread mthread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            mhandler.post(new Runnable(){
                                @Override
                                public void run() {

                                    btn_del_board_my.setVisibility(View.VISIBLE);
                                    btn_del_board_my.animate()
                                            .translationY(0)
                                            .alpha(1.0f)
                                            .setDuration(500);

                                }
                            });
                        }
                    });
                    mthread.start();

                }
            }
        });

        // 리사이클러뷰 부분
        recyclerview_board_my = findViewById(R.id.recyclerview_board_my);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_board_my.setLayoutManager(linearLayoutManager);


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

        retrofit_api.get_board_list_my(user_info.getInstance().getUser_index_number()).enqueue(new Callback<List<Board_list>>() {
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
        Adapter_board_my = new Adapter_board_my(array_list_board);
        Adapter_board_my.notifyDataSetChanged();
        recyclerview_board_my.setAdapter(Adapter_board_my);

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
}
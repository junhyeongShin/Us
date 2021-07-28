package com.example.us;
import android.os.Handler;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Delayed;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_friend_list extends AppCompatActivity {

    private static final String TAG = "Activity_friend_list : ";
    ArrayList<User_list_item> User_list_item ;
    ArrayList<User_list_item> User_list_item_search ;
    private BasicAdapter basicAdapter;
    private RecyclerView recyclerView_friend;

    EditText edit_text_find_friend;

    //친구 리스트를 한번에 리스트로 받고
    //최초 20개 보여주고 아래로 스크롤링 하면 리스너로 하단위치 받아서
    //한번에 20개씩 리사이클러뷰에 추가
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        try {
            retrofit_api();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        edit_text_find_friend = findViewById(R.id.edit_text_find_friend);
        recyclerView_friend = findViewById(R.id.recyclerView_friend);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_friend.setLayoutManager(linearLayoutManager);

        ImageButton ibtn_board_friend_back = findViewById(R.id.ibtn_board_friend_back);
        ibtn_board_friend_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        edit_text_find_friend.addTextChangedListener(new TextWatcher() {
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
                String text = edit_text_find_friend.getText().toString();
                search(text);
            }
        });



    }

    private void init(ArrayList<User_list_item> user_list_item){
        System.out.println("Adapter start init");

        final ArrayList<User_list_item> user_list_item_total = user_list_item;
        ArrayList<User_list_item> user_list_item_view = new ArrayList<>();


        int current_page = 0;
        final int last_page = user_list_item_total.size();

        if(user_list_item_total.size()<20){
            System.out.println("총 아이템 수 20 이하일때");

            basicAdapter = new BasicAdapter(user_list_item);
            basicAdapter.notifyDataSetChanged();
            recyclerView_friend.setAdapter(basicAdapter);

        }else {
            System.out.println("총 아이템 수 20 이상일때");

            final ArrayList<com.example.us.User_list_item> finalUser_list_item_view = user_list_item_view;


                for(int i=0; i<20; i++) {
                    finalUser_list_item_view.add(user_list_item_total.get(i));
                }

                basicAdapter = new BasicAdapter(finalUser_list_item_view);
                basicAdapter.notifyDataSetChanged();
                recyclerView_friend.setAdapter(basicAdapter);


            recyclerView_friend.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    System.out.println("onScrollStateChanged newState: "+newState);

                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    System.out.println("onScrollStateChanged dx: "+dx);
                    System.out.println("onScrollStateChanged dy: "+dy);


                    int lastPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
                    int totalCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();

                    System.out.println("onScrollStateChanged lastPosition: "+lastPosition);
                    System.out.println("onScrollStateChanged totalCount: "+totalCount);

                    if(lastPosition+1 == totalCount){
                        System.out.println("페이징 마직막에 도달");

                        if(last_page< finalUser_list_item_view.size() +20){
                            System.out.println("페이징 마직막 수가 +20보다 작을때");
                            for(int i = lastPosition; i<last_page; i++) {
                                basicAdapter.add_item(user_list_item_total.get(i));
                            }
                        }else {
                            System.out.println("페이징 마직막 수가 +20보다 클때");
                            for(int i = lastPosition; i<lastPosition+20; i++) {
                                  basicAdapter.add_item(user_list_item_total.get(i));
                            }
                        }

                    }

                }
            });

        }

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        User_list_item_search = new ArrayList<>();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            System.out.println("문자 입력이 없을때");
            init(User_list_item);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < User_list_item.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (User_list_item.get(i).getUser_name().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    User_list_item_search.add(User_list_item.get(i));
                }
            }
            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
            init(User_list_item_search);
        }
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

        retrofit_api.getFriend_list_not(user_info.getInstance().getUser_index_number()).enqueue(new Callback<List<User_list_item>>() {
            @Override
            public void onResponse(Call<List<User_list_item>> call, Response<List<User_list_item>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<User_list_item> list = response.body();
                    System.out.println("성공");

                    User_list_item = (ArrayList<com.example.us.User_list_item>) list;
                    init(User_list_item);

                    System.out.println("유저 리스트 사이즈 : "+User_list_item.size());

                }
            }

            @Override
            public void onFailure(Call<List<User_list_item>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }
}
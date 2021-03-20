package com.example.us;

import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
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
import java.util.Objects;

public class Activity_clan_search extends AppCompatActivity {

    private static final String TAG = Class.class.getName()+" : ";
    ArrayList<Clan_item> clan_item ;
    ArrayList<Clan_item> clan_item_search ;
    private Clan_recycler_adapter basicAdapter;
    private RecyclerView recyclerView_clan;

    EditText edit_text_find_clan;

    //친구 리스트를 한번에 리스트로 받고
    //최초 20개 보여주고 아래로 스크롤링 하면 리스너로 하단위치 받아서
    //한번에 20개씩 리사이클러뷰에 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_search);

        try {
            retrofit_api();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        edit_text_find_clan = findViewById(R.id.edit_text_find_clan);
        recyclerView_clan = findViewById(R.id.recyclerView_clan_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_clan.setLayoutManager(linearLayoutManager);

        ImageButton ibtn_clan_search_back = findViewById(R.id.ibtn_clan_search_back);
        ibtn_clan_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        edit_text_find_clan.addTextChangedListener(new TextWatcher() {
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
                String text = edit_text_find_clan.getText().toString();
                try {
                    search(text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });











    }

    private void init(final ArrayList<Clan_item> clan_list_item){
        System.out.println("Adapter start init");

        final ArrayList<Clan_item> clan_list_item_total = clan_list_item;
        ArrayList<Clan_item> clan_list_item_view = new ArrayList<>();


        int current_page = 0;
        final int last_page = clan_list_item_total.size();

        if(clan_list_item_total.size()<20){
            System.out.println("총 아이템 수 20 이하일때");

            basicAdapter = new Clan_recycler_adapter(clan_list_item,this.getApplicationContext());
            basicAdapter.notifyDataSetChanged();
            recyclerView_clan.setAdapter(basicAdapter);
            basicAdapter.size = getDeviceSize();

        }else {
            System.out.println("총 아이템 수 20 이상일때");
            System.out.println("총 아이템 수 : "+clan_list_item_total.size());

            final ArrayList<com.example.us.Clan_item> finalClan_list_item_view = clan_list_item_view;


            for(int i=0; i<20; i++) {
                finalClan_list_item_view.add(clan_list_item_total.get(i));
            }

            basicAdapter = new Clan_recycler_adapter(finalClan_list_item_view,this.getApplicationContext());
            basicAdapter.notifyDataSetChanged();
            recyclerView_clan.setAdapter(basicAdapter);
            basicAdapter.size = getDeviceSize();


            recyclerView_clan.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

//                    System.out.println("onScrollStateChanged newState: "+newState);

                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

//                    System.out.println("onScrollStateChanged dx: "+dx);
//                    System.out.println("onScrollStateChanged dy: "+dy);


                    int lastPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
                    int totalCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();

//                    System.out.println("onScrollStateChanged lastPosition: "+lastPosition);
//                    System.out.println("onScrollStateChanged totalCount: "+totalCount);

                    if(lastPosition+1 == totalCount){
                        System.out.println("페이징 마직막에 도달");
                        if(clan_list_item.size()>0){

                        if(last_page <= lastPosition +19){
                            System.out.println("페이징 마직막 수가 +20보다 작을때");
                            System.out.println("last_page : "+last_page);
                            System.out.println("finalClan_list_item_view.size() : + "+finalClan_list_item_view.size());
                            for(int i = lastPosition; i<last_page; i++) {
                                basicAdapter.add_item(clan_list_item_total.get(i));
                            }
                        }else {
                            System.out.println("페이징 마직막 수가 +20보다 클때");
                            System.out.println("last_page : "+last_page);
                            System.out.println("finalClan_list_item_view.size() : + "+finalClan_list_item_view.size());
                            for(int i = lastPosition; i<lastPosition+20; i++) {
                                basicAdapter.add_item(clan_list_item_total.get(i));
                            }
                        }

                        }else {
                            System.out.println("clan_list_item=null");
                        }


                    }

                }
            });

        }

    }

    // 검색을 수행하는 메소드
    public void search(String charText) throws UnsupportedEncodingException {

        clan_item_search = new ArrayList<>();

        retrofit_api_search(charText);

//        // 문자 입력이 없을때는 모든 데이터를 보여준다.
//        if (charText.length() == 0) {
//            System.out.println("문자 입력이 없을때");
//            init(clan_item);
//        }
//        // 문자 입력을 할때..
//        else
//        {
//            if(null!=clan_item) {
//                // 리스트의 모든 데이터를 검색한다.
//                for (int i = 0; i < clan_item.size(); i++) {
//                    // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
//                    if (clan_item.get(i).getTitle().toLowerCase().contains(charText)) {
//                        // 검색된 데이터를 리스트에 추가한다.
//                        clan_item_search.add(clan_item.get(i));
//                    }
//                }
//                System.out.println("데이터 추가 완료");
//                // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
//                init(clan_item_search);
//            }else {
//                System.out.println("clan_item 이 비어있음.");
//
//            }
//
//        }
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

        retrofit_api.getClan_list().enqueue(new Callback<List<Clan_item>>() {
            @Override
            public void onResponse(Call<List<Clan_item>> call, Response<List<Clan_item>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<Clan_item> list = response.body();

                    clan_item = (ArrayList<Clan_item>) list;

                    for(int i=0; i<clan_item.size(); i++) {

                    }

                    init(clan_item);



                    System.out.println("클랜 리스트 사이즈 : "+clan_item.size());

                }
            }

            @Override
            public void onFailure(Call<List<Clan_item>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }

    private void retrofit_api_search(String search) throws UnsupportedEncodingException {

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

        retrofit_api.getClan_list_search(search).enqueue(new Callback<List<Clan_item>>() {
            @Override
            public void onResponse(Call<List<Clan_item>> call, Response<List<Clan_item>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<Clan_item> list = response.body();

                    clan_item = (ArrayList<Clan_item>) list;

                    for(int i=0; i<clan_item.size(); i++) {

                    }

                    init(clan_item);



                    System.out.println("클랜 리스트 사이즈 : "+clan_item.size());

                }
            }

            @Override
            public void onFailure(Call<List<Clan_item>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }

    // 디바이스 가로 세로 사이즈 구하기
    // getRealSize()는 status bar 등 system insets을
    // 포함한 스크린 사이즈를 가져오는 방법이고,
    // getSize()는 status bar 등 insets를
    // 제외한 부분에 대한 사이즈만 가져오는 함수이다.
    // 단위는 픽셀
    public Point getDeviceSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        //현재 프로젝트에서는 쓰지 않지만 가로와 세로 길이를 이렇게 빼서 사용한다
        int width = size.x;
        int height = size.y;

        return size;
    }

}
package com.example.us;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.us.wowza.live_stream;
import com.example.us.wowza.live_stream_list;
import com.example.us.wowza.live_streams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_streaming_list extends AppCompatActivity {
    RecyclerView recyclerview_streaming;
    SwipeRefreshLayout SwipeRefreshLayout_streaming_list;
    streaming_list_adapter streaming_list_adapter;
    ArrayList<live_stream> live_stream_list;
    int size ;
    int position_size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_list);

        recyclerview_streaming = findViewById(R.id.recyclerview_streaming);


//        SwipeRefreshLayout_streaming_list = findViewById(R.id.SwipeRefreshLayout_streaming_list);
//        SwipeRefreshLayout_streaming_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                System.out.println("onRefresh start");
//
//                //새로고침해줄 코드 작성
//                //현재 방송 리스트 가져오기
//                //방송 아이디 목록을 가져오고, 그 방송 ID값을 서버에 조회 한다.
//                //서버에 조회해서 현재 방송이 진행중이면 리사이클러뷰에 추가
//                //방송 이름, 아이디, 현재 시청자 수, 방송 시작 시간, 방송 카테고리, 유저의 id / 썸네일, 현재 상태는 따로 서버에 조회
//                //유저의 id DB에 조회해서 유저 정보도 추가해줌
//                //유저의 정보 : 프로필사진, 유저 id, 유저 네임, 유저 소개
//                //유저의 정보도 리사이클러뷰에 추가해주기.
//                //
//
//                SwipeRefreshLayout_streaming_list.setRefreshing(false);
//                System.out.println("onRefresh end");
//            }
//        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_streaming.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
        live_stream_list= new ArrayList<>();
//        get_stream_list();


    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");

    }

//    public void get_stream_list(){
//        System.out.println("get_stream_list");
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(server_info.getInstance().getURL_node())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);
//
//        retrofit_api.get_live_streams().enqueue(new Callback<List<live_streams>>() {
//            @Override
//            public void onResponse(@NotNull Call<List<live_stream>>call, @NotNull Response<List<live_streams>> response) {
//                if(response.isSuccessful()){
//                    List<live_streams> list = response.body();
//                    System.out.println("성공");
//
//                    for(int i=0; i<list.size();i++) {
//                        System.out.println(list.get(i).getId());
////                        getThumbnail(list.get(i).getId());
//                    }
//
//                    size = list.size();
//
//                }
//            }
//
//
//            @Override
//            public void onFailure(@NotNull Call<List<live_streams>> call, @NotNull Throwable t) {
//                System.out.println("Clan_del 실패 call : "+call.toString());
//                System.out.println("Clan_del 실패 Throwable : "+t.toString());
//            }
//        });
//    }

    public void getThumbnail(live_stream live_stream, String id){
        System.out.println("getThumbnail");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL_node()+"?id="+id)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.get_thumbnail(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                System.out.println(response);
                live_stream.setThumbnail_url(response.body());
                live_stream_list.add(live_stream);

                position_size ++;

                if(position_size==size){
                    //리사이클러뷰 적용시켜줌.
                    streaming_list_adapter = new streaming_list_adapter(live_stream_list);
                    recyclerview_streaming.setAdapter(streaming_list_adapter);
                    System.out.println("streaming_list_adapter 적용 시켜 줌");
                }

                System.out.println("live_stream_list Thumbnail_url : "+live_stream.getThumbnail_url());
                System.out.println("live_stream_list id : "+live_stream.getId());
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                System.out.println("Clan_del 실패 call : "+call.toString());
                System.out.println("Clan_del 실패 Throwable : "+t.toString());
            }
        });

    }


}
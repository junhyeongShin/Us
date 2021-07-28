package com.example.us;
import android.app.AlertDialog;
import android.content.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.request.StringRequest;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import com.example.us.wowza.live_stream;
import com.example.us.wowza.live_streams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_home extends AppCompatActivity {

    private static final String TAG = "Activity_home";

    Socket_service mService;
    boolean mBound = false;

    RecyclerView recyclerview_streaming;
    SwipeRefreshLayout SwipeRefreshLayout_streaming_list;
    streaming_list_adapter streaming_list_adapter;
    ArrayList<live_stream> live_stream_list;
    int size ;
    int position_size = 0;
    boolean is_first = false;


    @Override
    protected void onResume() {
        super.onResume();

        live_stream_list = new ArrayList<>();
        get_stream_list();
        if(is_first){
            streaming_list_adapter.notifyDataSetChanged();
        }


//        SharedPreferences spf = getSharedPreferences("MASSAGE",0);
//        SharedPreferences.Editor editor = spf.edit();
//        spf.getString("11","0");
//        editor.putString("11","0");
//
//        editor.apply();

//        String tmp_ = "2021-04-29 06:59:03";
//        String tmp_2 = "2021-04-29 06:59:02";
        //tmp_2 가 1초 클때 = -1
        //tmp_2 가 1주일 클때 = -1
        //tmp_2 가 1일 작을때 = 1
        //tmp_2 가 1초 작을때 = 1


//        System.out.println("tmp_.compareTo(tmp_2) : "+tmp_.compareTo(tmp_2));


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind to LocalService
        Intent intent = new Intent(this, Socket_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        intent.putExtra("id",user_info.getInstance().getUser_index_number());
        startService(intent);

        setContentView(R.layout.activity_home);
        recyclerview_streaming = findViewById(R.id.recyclerview_streaming);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_streaming.setLayoutManager(linearLayoutManager);

        SwipeRefreshLayout swipe_layout_streaming = findViewById(R.id.swipe_layout_streaming);
        swipe_layout_streaming.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                live_stream_list = new ArrayList<>();
                get_stream_list();
                streaming_list_adapter.notifyDataSetChanged();


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_layout_streaming.setRefreshing(false);
                    }
                }, 2000);


            }
        });



//        Button btn_more_streaming = findViewById(R.id.btn_more_streaming);
//        btn_more_streaming.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mIntent = new Intent(getApplicationContext(), Activity_streaming_list.class);
//                startActivity(mIntent);
//            }
//        });


        //하단 네비게이션바 버튼
        //각 버튼별로 이동할 엑티비티가 정해져있고, 지금 열려있는 엑티비티는 버튼 리스너 없음.
        Button btn_bottom_guild = findViewById(R.id.btn_bottom_guild);
        btn_bottom_guild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_data_server();

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

    @Override
    protected void onStop() {
        super.onStop();
    }

    void get_data_server(){
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
//                    assert data != null;
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


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get Service instance
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println(TAG);
            Socket_service.Socket_binder binder = (Socket_service.Socket_binder) service;
            System.out.println("Socket_service.Socket_binder binder = (Socket_service.Socket_binder) service;");
            mService = binder.getService();
            System.out.println("mService = binder.getService();");
            mBound = true;
            System.out.println("mBound = true");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            System.out.println("홈 액티비티 - 서비스 연결 종료");

        }
    };

    public void get_stream_list(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.get_live_streams().enqueue(new Callback<List<live_stream>>() {
            @Override
            public void onResponse(@NotNull Call<List<live_stream>>call, @NotNull Response<List<live_stream>> response) {
                if(response.isSuccessful()){
                    ArrayList<live_stream> list = (ArrayList<live_stream>) response.body();
                    System.out.println("성공 get_live_streams");

                    for(int i=0; i<list.size();i++) {
                        get_state(list.get(i),list.get(i).getId());
                    }

                    size = list.size();


                }
            }


            @Override
            public void onFailure(@NotNull Call<List<live_stream>> call, @NotNull Throwable t) {
                System.out.println("get_stream_list 실패 call : "+call.toString());
                System.out.println("get_stream_list 실패 Throwable : "+t.toString());
            }
        });
    }


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

                if(live_stream.getState().equals("started")){
                    live_stream_list.add(live_stream);
                }

                position_size ++;

                if(position_size==size){
                    //리사이클러뷰 적용시켜줌.
                    streaming_list_adapter = new streaming_list_adapter(live_stream_list);
                    recyclerview_streaming.setAdapter(streaming_list_adapter);
                    System.out.println("streaming_list_adapter 적용 시켜 줌");
                    is_first = true;
                }

                System.out.println("live_stream_list Thumbnail_url : "+live_stream.getThumbnail_url());
//                System.out.println("live_stream_list id : "+live_stream.getId());
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                System.out.println("getThumbnail 실패 call : "+call.toString());
                System.out.println("getThumbnail 실패 Throwable : "+t.toString());
            }
        });

    }

    public void get_state(live_stream live_stream, String id){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL_node()+"?id="+id)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.state_live_stream(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String>call, @NotNull Response<String> response) {
                if(response.isSuccessful()){
//                    live_stream live_stream = response.body();
                    System.out.println("성공");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONObject jsonObject_ = new JSONObject(jsonObject.getString("live_stream"));
                        live_stream.setState(jsonObject_.getString("state"));
                        System.out.println(TAG+" : "+jsonObject_.getString("state"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getThumbnail(live_stream,id);

                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                System.out.println("get_stream 실패 call : "+call.toString());
                System.out.println("get_stream 실패 Throwable : "+t.toString());
            }
        });
    }



}
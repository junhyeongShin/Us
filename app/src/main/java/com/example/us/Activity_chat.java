package com.example.us;

import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Activity_chat extends AppCompatActivity {


    Socket_service mService;
    boolean mBound = false;
    Adapter_room Adapter;
    RecyclerView rv;
    ArrayList<Room_item> room_list;


    @Override
    protected void onStart() {
        super.onStart();

        // Bind to LocalService
        Intent intent = new Intent(this, Socket_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        room_list = new ArrayList<>();


        rv = findViewById(R.id.recyclerview_room);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);




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

        //스와이프 핼프 리스너

        MySwipeHelper swipeHelper= new MySwipeHelper(Activity_chat.this,rv,300) {
            @Override
            public void instantiatrMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(Activity_chat.this,
                        "Delete",
                        30,
                        R.drawable.ic_baseline_delete_48,
                        Color.parseColor("#FF3C30"),
                        pos -> {

                            System.out.println("스와이프 삭제 : "+viewHolder.getAdapterPosition());
                            room_list.remove(viewHolder.getAdapterPosition());                // 해당 항목 삭제
                            Adapter.notifyItemRemoved(viewHolder.getAdapterPosition());    // Adapter에 알려주기.
                        }));
            }
        };// swipeHelper


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //초기화
    private void init(ArrayList<Room_item> list) {

        System.out.println("Adapter start");
        Adapter = new Adapter_room(list);
        Adapter.notifyDataSetChanged();
        rv.setAdapter(Adapter);

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

    /** Defines callbacks for service binding, passed to bindService() */
    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get Service instance
            Socket_service.Socket_binder binder = (Socket_service.Socket_binder) service;
            System.out.println("Socket_service.Socket_binder binder = (Socket_service.Socket_binder) service;");
            mService = binder.getService();
            System.out.println("mService = binder.getService();");
            mBound = true;
            System.out.println("mBound = true");

            try {
                get_room_list();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            System.out.println("서비스 연결 종료");
        }
    };

    public void get_room_list() throws IOException {
        System.out.println("get_room_list");


        if(!mBound){
           return ;
        }


                    Message message = new Message();
                    message.setType(MessageType.ROOMLIST);

                    User_list_item user = new User_list_item();
                    user.setId(user_info.getInstance().getUser_index_number());
                    user.setUser_name(user_info.getInstance().getUser_name());
                    user.setUser_img(user_info.getInstance().getImg_path());
                    user.setUser_intro(user_info.getInstance().getUser_intro_profile());

                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    userlist.add(user);
                    message.setUserInfoArrayList(userlist);

        System.out.println(message.getType());
        System.out.println(message.getUserInfoArrayList().get(0).getUser_name());
        System.out.println("mService test : "+mService.getRandomNumber());

                    mService.send_message(message);

        System.out.println("get_room_list end");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        get_room_list_service();

    }
    public void get_room_list_service(){
        room_list = mService.get_room_list();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        init(room_list);
    }
}
package com.example.us;
import android.app.AlertDialog;
import android.content.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.request.StringRequest;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_home extends AppCompatActivity {

    private static final String TAG = "Activity_home";

    Socket_service mService;
    boolean mBound = false;

    @Override
    protected void onResume() {
        super.onResume();

        // Bind to LocalService
        Intent intent = new Intent(this, Socket_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        intent.putExtra("id",user_info.getInstance().getUser_index_number());
        startService(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Button button = findViewById(R.id.btn_test_for_service);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    System.out.println("mBound is true");

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

                    try {
                        mService.send_message(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else {
                    System.out.println("mBound is not");
                }
            }
        });

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




}
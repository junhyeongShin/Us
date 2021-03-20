package com.example.us;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;

import java.io.IOException;
import java.util.ArrayList;

public class Activity_chat_room extends AppCompatActivity {

    Socket_service mService;
    boolean mBound = false;
    Adapter_room Adapter;
    RecyclerView rv;
    ArrayList<Message> msg_list;

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onStart() {
        super.onStart();

        // Bind to LocalService
        Intent intent = new Intent(this, Socket_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


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
//        msg_list = mService.get_room_list();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        init(msg_list);
    }
}
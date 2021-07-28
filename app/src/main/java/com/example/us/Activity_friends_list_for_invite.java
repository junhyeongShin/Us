package com.example.us;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity_friends_list_for_invite extends AppCompatActivity {
    Socket_service mService;
    boolean mBound = false;
    IBinder iBinder;
    int Room_id;
    private static Gson gson = new Gson();
    private Messenger mServiceMessenger = null;

    private static final String TAG = "Activity_friend_list : ";
    ArrayList<friend_list_item> friend_list_item;
    ArrayList<friend_list_item> friend_list_item_search;
    private friend_list_adapter_for_chat friend_list_adapter_for_invite;
    private RecyclerView recyclerView_friend_invite;
    ArrayList<friend_list_item> checked_list = new ArrayList<>();

    Message msg_chat;


    int count_item = 0;

    EditText edit_text_invite_friend;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mIntent = new Intent(getApplicationContext(), Activity_chat_room.class);
        mIntent.putExtra("id",Room_id);
        startActivity(mIntent);

        finish();
    }

    //친구 리스트를 한번에 리스트로 받고
    //최초 10개 보여주고 아래로 스크롤링 하면 리스너로 하단위치 받아서
    //한번에 10개씩 리사이클러뷰에 추가
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list_for_invite);

        Intent mIntent = getIntent();
        Room_id = mIntent.getIntExtra("id",0);


        try {
            retrofit_api();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ImageButton ibtn_friend_invite_back = findViewById(R.id.ibtn_friend_invite_back);
        ibtn_friend_invite_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btn_add_invite = findViewById(R.id.btn_add_invite);
        btn_add_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 선택된 유저 리스트 가지고 채팅방 생성하기.
                // 1. 선택된 유저 리스트 가져오기
                // 2. 핸들러로 서비스에 보내기
                // 3. 서버에 전송 완료되면 응답 받기 (채팅방 번호 받기)
                // 4. 응답 받으면 해당 채팅방으로 이동하기.

                try {
                    invite();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        edit_text_invite_friend = findViewById(R.id.edit_text_invite_friend);
        recyclerView_friend_invite = findViewById(R.id.recyclerView_friend_invite);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_friend_invite.setLayoutManager(linearLayoutManager);

        ImageButton ibtn_search_invite_friend = findViewById(R.id.ibtn_search_invite_friend);
        ibtn_search_invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        edit_text_invite_friend.addTextChangedListener(new TextWatcher() {
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
                String text = edit_text_invite_friend.getText().toString();
                search(text);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Bind to LocalService
        Intent intent = new Intent(this, Socket_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void init(final ArrayList<friend_list_item> friend_list_item) {
        System.out.println("Adapter start init");

        final ArrayList<friend_list_item> friend_list_item_total = friend_list_item;
        ArrayList<friend_list_item> friend_list_item_view = null;


        final int current_page = 0;
        final int last_page = friend_list_item_total.size();

        if (friend_list_item_total.size() < 20) {
            System.out.println("총 아이템 수 20 이하일때");

            friend_list_item_view = friend_list_item_total;

            friend_list_adapter_for_invite = new friend_list_adapter_for_chat(friend_list_item);
            friend_list_adapter_for_invite.notifyDataSetChanged();
            recyclerView_friend_invite.setAdapter(friend_list_adapter_for_invite);

        } else {
            System.out.println("총 아이템 수 20 이상일때");

            final ArrayList<com.example.us.friend_list_item> final_friend_list_item_view = friend_list_item_view;
            recyclerView_friend_invite.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    int totalCount = recyclerView.getAdapter().getItemCount();

                    if (lastPosition == totalCount) {
                        System.out.println("페이징 마직막에 도달");
                        if (last_page < current_page + 20) {
                            System.out.println("페이징 마직막 수가 +20보다 작을때");


                            for (int i = current_page; i < last_page; i++) {
                                final_friend_list_item_view.add(friend_list_item_total.get(current_page));
                            }

                        } else {
                            System.out.println("페이징 마직막 수가 +20보다 클때");

                            int page = current_page + 20;

                            for (int i = current_page; i < page; i++) {
                                final_friend_list_item_view.add(friend_list_item_total.get(current_page));
                            }

                        }

                        friend_list_adapter_for_invite = new friend_list_adapter_for_chat(final_friend_list_item_view);
                        friend_list_adapter_for_invite.notifyDataSetChanged();
                        recyclerView_friend_invite.setAdapter(friend_list_adapter_for_invite);


                    }

                }
            });


        }

    }


    // 선택된 유저 리스트 가지고 채팅방 생성하기.
    // 1. 선택된 유저 리스트 가져오기
    // 2. 핸들러로 서비스에 보내기
    // 3. 서버에 전송 완료되면 응답 받기 (채팅방 번호 받기)
    // 4. 응답 받으면 해당 채팅방으로 이동하기.
    public void invite() throws IOException {

        checked_list = friend_list_adapter_for_invite.get_checked_list();
        System.out.println("checked_list.size : " + checked_list.size());

        if (checked_list.size() == 0) {
            Toast.makeText(this.getApplicationContext(), "선택된 유저가 없습니다.", Toast.LENGTH_SHORT).show();

            return;
        }


        //메시지 만들어서 핸들러로 서비스에 보내기
        msg_chat = new Message();
        msg_chat.setTime(time());
        msg_chat.setType(MessageType.INVITE);
        msg_chat.setRoom_Id(Room_id);

        //메시지에 넣을 유저 리스트 생성
        ArrayList<User_list_item> userlist = new ArrayList<>();

        //현재 유저의 정보 추가
        User_list_item user_1st = new User_list_item();
        user_1st.setId(user_info.getInstance().getUser_index_number());
        userlist.add(user_1st);
        user_1st = null;

        for (int i = 0; i < checked_list.size(); i++) {
            User_list_item user = new User_list_item();
            user.setId(checked_list.get(i).getId());
            user.setUser_name(checked_list.get(i).getUser_name());
            userlist.add(user);
            user = null;
        }

        msg_chat.setUserInfoArrayList(userlist);

        // 스트링으로 변경
        String messagesString = gson.toJson(msg_chat);

        //핸들러에게 전송하기.
        mService.send_message(msg_chat);


//                4. 응답 받으면 해당 채팅방으로 이동하기.
//                int room_id = 0;
//
//
//
//                Intent mIntent = new Intent(getApplicationContext(), Activity_chat_room.class);
//                mIntent.putExtra("id",room_id);
//                //현재 액티비티 스택에 쌓지 않기
//                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                startActivity(mIntent);

    }


    // 검색을 수행하는 메소드
    public void search(String charText) {

        friend_list_item_search = new ArrayList<>();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            System.out.println("문자 입력이 없을때");
            init(friend_list_item);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < friend_list_item.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (friend_list_item.get(i).getUser_name().toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    friend_list_item_search.add(friend_list_item.get(i));
                }
            }
            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
            init(friend_list_item_search);
        }
    }

    private void retrofit_api() throws UnsupportedEncodingException {

        System.out.println(TAG + " - retrofit_api : start");
        System.out.println(TAG + " - retrofit_api_url : " + server_info.getInstance().getURL());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.get_friend_list_for_invite(user_info.getInstance().getUser_index_number(),Room_id).enqueue(new Callback<List<friend_list_item>>() {
            @Override
            public void onResponse(Call<List<friend_list_item>> call, Response<List<friend_list_item>> response) {
                System.out.println("onResponse : call" + call);
                System.out.println("onResponse : response" + response);
                if (response.isSuccessful()) {
                    List<friend_list_item> list = response.body();
                    System.out.println("친구 리사이클러뷰 데이터 수신 성공");

                    friend_list_item = (ArrayList<com.example.us.friend_list_item>) list;
                    init(friend_list_item);

//                    for(int i=0; i<User_list_item.size(); i++) {
//                        System.out.println(User_list_item.get(i).getId());
//                        System.out.println(User_list_item.get(i).getUser_img());
//                        System.out.println(User_list_item.get(i).getUser_name());
//                    }

                }
            }

            @Override
            public void onFailure(Call<List<friend_list_item>> call, Throwable t) {
                System.out.println("실패 Throwable : " + t.toString());
                System.out.println("실패 call : " + call.toString());
            }
        });

    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Thread thread = new Thread() {
                public void run() {
                    try {
                        //바인더 호출
                        Socket_service.Socket_binder binder = (Socket_service.Socket_binder) service;
                        mService = binder.getService();
                        mBound = true;
                        iBinder = Socket_service.mMessenger_bind();
                        mServiceMessenger = new Messenger(iBinder);
                        try {
                            android.os.Message msg = android.os.Message.obtain(null, Room_id);
                            msg.replyTo = mMessenger;
                            mServiceMessenger.send(msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            System.out.println("서비스 연결 종료");
        }
    };

    public static String time() {
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();

        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);

        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat SimpleDateFormat_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // nowDate 변수에 값을 저장한다.
        return SimpleDateFormat_time.format(date);
    }

    /**
     * Service 로 부터 message를 받음
     */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull android.os.Message msg) {
            System.out.println("act : what " + msg.what);
            String revString = msg.getData().getString("msg");
            Message message_Get = gson.fromJson(revString, Message.class);

            switch (message_Get.getType()) {
                case MSG:

                    System.out.println("mMessenger : MSG");
                    break;

                case INVITE:

                    System.out.println("mMessenger : INVITE");
                    Intent mIntent = new Intent(getApplicationContext(), Activity_chat_room.class);
                    mIntent.putExtra("id",Room_id);
                    startActivity(mIntent);

                    finish();

                    break;
            }
            return false;
        }

    }));

}
package com.example.us;
import android.content.Intent;

import android.content.*;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity_chat extends AppCompatActivity {

    Socket_service mService;
    IBinder iBinder;
    private Messenger mServiceMessenger = null;
    private static Gson gson = new Gson();
    boolean mBound = false;
    Adapter_room Adapter;
    RecyclerView rv;
    ArrayList<Room_item> room_list;
    ArrayList<Room_item> room_list_search;
    public static Context context_chat;
    boolean is_activity = false;
    boolean is_first = true;

    private static final String TAG = Activity_chat.class.getName()+" : ";

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println(TAG+"onStart");

        // Bind to LocalService
        Intent intent = new Intent(this, Socket_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        try {
            send_client();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        if(!is_first){
            try {
                get_room_list();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        context_chat = this;
        room_list = new ArrayList<>();


        rv = findViewById(R.id.recyclerview_room);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);

        ImageButton ibtn_search_room = findViewById(R.id.ibtn_search_room);
        ibtn_search_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //채팅방 검색

            }
        });

        ImageButton ibtn_add_room = findViewById(R.id.ibtn_add_room);
        ibtn_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //유저 리스트 불러와서 채팅방 만들 수 있게
            // 인텐트로 이동
                Intent mIntent = new Intent(getApplicationContext(), Activity_freinds_list_for_chat.class);
//                mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
                            System.out.println("스와이프 삭제 room_id : "+room_list.get(viewHolder.getAdapterPosition()).getId());

                            //서버로 보내주기.
                            out_chat(room_list.get(viewHolder.getAdapterPosition()).getId());

                            room_list.remove(viewHolder.getAdapterPosition());                // 해당 항목 삭제
                            Adapter.notifyItemRemoved(viewHolder.getAdapterPosition());    // Adapter에 알려주기.


                        }));
            }
        };// swipeHelper


        EditText edit_text_find_room = findViewById(R.id.edit_text_find_room);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        edit_text_find_room.addTextChangedListener(new TextWatcher() {
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
                String text = edit_text_find_room.getText().toString();
                search(text);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(TAG+"onResume");

        try {
            check_msg();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(is_activity){
        Adapter.line_item();
        }



    }

    //초기화
    private void init(ArrayList<Room_item> list) {

        System.out.println("Adapter start");
        Adapter = new Adapter_room(list);
        Adapter.notifyDataSetChanged();
        rv.setAdapter(Adapter);

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        room_list_search = new ArrayList<>();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            System.out.println("문자 입력이 없을때");
            init(room_list);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < room_list.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (room_list.get(i).getTitle().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    room_list_search.add(room_list.get(i));
                }
            }
            // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
            init(room_list_search);
        }
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
            Thread thread = new Thread(){
                public void run(){
                    try{
                        //바인더 호출
                        Socket_service.Socket_binder binder = (Socket_service.Socket_binder) service;
                        System.out.println("채팅방 서비스");
                        System.out.println("Socket_service.Socket_binder binder = (Socket_service.Socket_binder) service;");
                        mService = binder.getService();
                        System.out.println("mService = binder.getService();");
                        mBound = true;
                        System.out.println("mBound = true");
                        iBinder = Socket_service.mMessenger_bind();
                        mServiceMessenger = new Messenger(iBinder);
                        System.out.println("mServiceMessenger");
                        android.os.Message msg = android.os.Message.obtain(null, 0);
                        msg.replyTo = mMessenger;
                        mServiceMessenger.send(msg);
                        System.out.println("mServiceMessenger.send(msg)");



                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

            try {
                thread.join();
                get_room_list();
                is_first = false;
            } catch (IOException | InterruptedException e) {
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

        mService.send_message(message);

    }

    /** Service 로 부터 message를 받음 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull android.os.Message msg) {
            System.out.println("act : what "+msg.what);
            String revString = msg.getData().getString("msg");
            Message message_Get = gson.fromJson(revString, Message.class);

            switch (message_Get.getType()) {
                case MSG:
                case IMG:
                case VIDEO:

                    boolean room_check = false;

                    if(message_Get.getType()==MessageType.MSG){
                        System.out.println("mMessenger : MSG");
                    }else if(message_Get.getType()==MessageType.IMG){
                        System.out.println("mMessenger : IMG");
                    }else {
                        System.out.println("mMessenger : VIDEO");
                    }

                    //룸이 있는지 없는지 부터 판별해야함.
                    //룸이 없을경우
                    System.out.println("revString : "+revString);
                    for(int i=0; i<room_list.size(); i++) {
                        if(room_list.get(i).getId()==message_Get.getRoom_list().get(0).getId()){
                           //해당 룸이 있습니다.
                            room_check = true;

                           break;
                        }
                    }
                    //
                    if(room_check){
                    //룸이 있을경우
                    Adapter.update_item(message_Get);
                    //N표시 해줘야함
                    Adapter.alarm_item(message_Get);

                    Adapter.line_item();

                    }else {

                        System.out.println("revString : "+revString);

                        //룸 리스트에 마지막 룸 값을 넣어주자
                        room_list.add(message_Get.getRoom_list().get(0));
                        init(room_list);
                        is_activity = true;

                        try {
                            check_msg();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Adapter.line_item();

                    }

                    room_check = false;
                    break;


                case CREATEROOM:
                    System.out.println("mMessenger : CREATEROOM");
                case INVITE:
                    System.out.println("mMessenger : NO CREATEROOM INVITE");
                    break;


                case ROOMLIST:

                    System.out.println("mMessenger : ROOMLIST");

                    room_list = message_Get.getRoom_list();

                    for(int i=0; i<room_list.size(); i++) {
                        String tmp_ =  room_list.get(i).getLast_message().getContent();
                        System.out.println(i+1+"번째 룸의 라스트 메시지 : "+tmp_);
                        System.out.println(i+1+"번째 룸의 라스트 타입 : "+room_list.get(i).getLast_message().getType());
                    }


                    init(room_list);
                    is_activity = true;
                    try {
                        check_msg();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        Adapter.line_item();

                    break;

                default:
                    System.out.println("default");
                    break;
            }
            return false;
        }

    }));

    public void send_client() throws RemoteException {
        iBinder = Socket_service.mMessenger_bind();
        mServiceMessenger = new Messenger(iBinder);
        System.out.println("mServiceMessenger");
        android.os.Message msg = android.os.Message.obtain(null, 0);
        msg.replyTo = mMessenger;
        mServiceMessenger.send(msg);
        System.out.println("mServiceMessenger.send(msg)");
    }

    public void check_msg() throws ParseException {
        SharedPreferences sharedPreferences = getSharedPreferences("MESSAGE", 0); // 0 :MODE_Privte

        for(int i=0; i<room_list.size(); i++) {
            String tmp_ = sharedPreferences.getString(String.valueOf(room_list.get(i).getId()), "0"); //값 , 비어있을때
//            System.out.println("tmp_ : "+tmp_);
//            System.out.println("tmp_ : room_list ID : "+room_list.get(i).getId());
            if(tmp_.equals("0")){
                System.out.println("쉐어드에 저장된 값이 없습니다. 룸 아이디 : "+room_list.get(i).getId());
                room_list.get(i).setAlarm(true);
                Adapter.notifyItemChanged(i);
            }
            else if(tmp_.equals("null")){
                System.out.println("쉐어드에 저장된 값이 null 없습니다. 룸 아이디 : "+room_list.get(i).getId());
            }
            else {

                //채팅방 룸의 라스트 메시지 가져오기

                Date date_ = new Date();
                SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date_ = beforeFormat.parse(room_list.get(i).getLast_message().getTime());

                //쉐어드에 저장되어잇던 msg 가져오기
                Message msg_ = new Message();
                msg_ = gson.fromJson(tmp_,Message.class);
                Date date_2 = new Date();
                SimpleDateFormat beforeFormat_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date_2 = beforeFormat_2.parse(msg_.getTime());


                int compare = date_.compareTo(date_2);

                //두 날짜가 같을때
                if(compare==0){
                    System.out.println("날짜 같음");

                    int position_ = Adapter.find_room_id(msg_);
                    Adapter.no_alarm_item(position_);
                    Adapter.notifyItemChanged(position_);

                }
                //라스트 메시지의 값이 더 클때
                else if(compare>0){
                    System.out.println("라스트 메시지의 값이 더 클때");

                    // N 표시 해줘야함
                    room_list.get(i).setAlarm(true);
                    Adapter.notifyItemChanged(i);
                }
                //쉐어드의 메시지 값이 더 클때
                else {
                    System.out.println("쉐어드의 메시지 값이 더 클때");
//                    Adapter.update_item(msg_);

                    if(msg_.getType()!=MessageType.NOTIFICATION){
                    room_list.get(i).setLast_message(msg_);
                    Adapter.notifyItemChanged(i);

                    int position_ = Adapter.find_room_id(msg_);
                    Adapter.no_alarm_item(position_);
                    Adapter.notifyItemChanged(position_);
                    }

                }


            }
        }
    }

    public void out_chat(int room_id) throws IOException {

        Message msg = new Message();
        msg.setType(MessageType.OUT);
        msg.setRoom_Id(room_id);
        msg.setTime(time());

        User_list_item user = new User_list_item();
        user.setId(user_info.getInstance().getUser_index_number());
        user.setUser_name(user_info.getInstance().getUser_name());
        user.setUser_img(user_info.getInstance().getImg_path());
        user.setUser_intro(user_info.getInstance().getUser_intro_profile());

        ArrayList<User_list_item> userlist = new ArrayList<>();
        userlist.add(user);
        msg.setUserInfoArrayList(userlist);

        mService.send_message(msg);


    }

    public static String time() {
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();

        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);

        // 시간을 나타냇 포맷을 정한다 ( yyyy-MM-dd 같은 형태로 변형 가능 )
        java.text.SimpleDateFormat SimpleDateFormat_time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // nowDate 변수에 값을 저장한다.
        return SimpleDateFormat_time.format(date);
    }
}
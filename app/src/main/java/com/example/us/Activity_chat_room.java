package com.example.us;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;

import android.os.*;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


//채팅방 액티비티

//enter_room : 채팅방에 입장 했다는 메시지전송(채팅방 번호 알려줘야함)
//get_msg_list : 채팅방의 메시지 리스트 받아오는 메시지전송
//send_text : 채팅 내용을 서버로 전송하는 메시지 보내기
//send_file : 이미지나 동영상을 서버로 전송하는 메시지 보내기
// * 채팅방에 메시지가 오면 서비스에서 핸들러로 수정해주기

// 리사이클러뷰 포지션 이동 메소드
// scrollToPosition
// 마지막으로 이동하게 하려면
// rv.scrollToPosition(messageData.size()-1);

public class Activity_chat_room extends AppCompatActivity {

    Socket_service mService;
    IBinder iBinder;
    boolean mBound = false;
    Adapter_chatting Adapter_chatting;
    RecyclerView rv;
    ArrayList<Message> msg_list;
    EditText edit_text_chat_room;
    Button btn_send_msg_chat_room;
    private static Gson gson = new Gson();
    int Room_id;
    private static final String TAG = Activity_chat_room.class.getName()+" : ";
    private Messenger mServiceMessenger = null;
    Message last_msg;
    String img_path;
    String video_path;
    int position;
    boolean is_activity = false;

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("onResume");

        if(position!=-1){
        rv.scrollToPosition(msg_list.size()-1);
        }

        try {
            send_client();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(is_activity){
            try {
                change_room(Room_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("채팅방 onStart");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent mIntent = getIntent();
        Room_id = mIntent.getIntExtra("id",0);

        server_info.getInstance().setROOM_ID(Room_id);


        // Bind to LocalService
        Intent intent = new Intent(this, Socket_service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        System.out.println("들어간 방 번호 : "+Room_id);

        //노티피케이션 제거
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // 해당룸의 Notification 알림 제거
        notificationManager.cancel(Room_id);


    }

    @Override
    protected void onStop() {
        super.onStop();

        is_activity=true;

        System.out.println("채팅방 onStop");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        edit_text_chat_room = findViewById(R.id.edit_text_chat_room);

        position = - 1;

        rv = findViewById(R.id.recyclerView_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);

        ImageButton ibtn_chat_room_back = findViewById(R.id.ibtn_chat_room_back);
        ibtn_chat_room_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_send_msg_chat_room = findViewById(R.id.btn_send_msg_chat_room);
        btn_send_msg_chat_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("채팅 메시지 보내기 클릭");
                try {
                    send_text();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_send_msg_chat_room.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    System.out.println("채팅 메시지 보내기 엔터 사용");
                    try {
                        send_text();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        ImageButton ibtn_chat_room_setting = findViewById(R.id.ibtn_chat_room_setting);
        ibtn_chat_room_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("채팅 설정 버튼 클릭");


                //메뉴창 생성
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);

                popupMenu.getMenuInflater().inflate(R.menu.popup_chat, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_chat_invite) {
                            System.out.println(TAG + "친구초대 클릭");

                            //초대
                            Intent mIntent = new Intent(getApplicationContext(), Activity_friends_list_for_invite.class);
                            mIntent.putExtra("id",Room_id);
                            startActivity(mIntent);

                            return true;
                        } else if (item.getItemId() == R.id.menu_chat_out) {
                            System.out.println(TAG + "채팅방 나가기 클릭");

                            //나가기 메시지 보내기

                            try {
                                out_chat();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        ImageButton chat_room_menu_file = findViewById(R.id.chat_room_menu_file);
        chat_room_menu_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("파일선택 메뉴 클릭");

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_chat_room_file, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_chat_room_image) {
                            System.out.println(TAG + "menu_chat_room_image");

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(intent, 1);

                            is_activity=false;

                            return true;
                        } else if (item.getItemId() == R.id.menu_chat_room_video) {
                            System.out.println(TAG + "menu_chat_room_video");
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(intent, 2);

                            is_activity=false;

                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });



    }

    //초기화
    private void init(ArrayList<Message> list) {

        ArrayList<Message> list_adapter = new ArrayList<>();
        list_adapter = list;
        System.out.println("Adapter start");
        Adapter_chatting = new Adapter_chatting(list_adapter);
        Adapter_chatting.notifyDataSetChanged();
        rv.setAdapter(Adapter_chatting);
        // 메시지 목록이 없을경우
        if(list_adapter.size()<1){
           return;
        }
        rv.scrollToPosition(list_adapter.size()-1);

    }

    private void send_text() throws IOException {

        //입력창의 텍스트 가져오기
        String text = edit_text_chat_room.getText().toString();
        if(text.equals("")){
           Toast.makeText(this.getApplicationContext(), "입력내용이 없습니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        // 메시지 생성
        // 메시지 타입, 룸 아이디, 텍스트, 유저정보
        Message msg = new Message();
        msg.setType(MessageType.MSG);
        msg.setRoom_Id(Room_id);
        msg.setContent(text);
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

        Adapter_chatting.add_item(msg);
        rv.scrollToPosition(msg_list.size()-1);

        //다시 초기화
        edit_text_chat_room.setText("");
    }

        @Override
        protected void onPause() {

            System.out.println("채팅방 onPause");

            //쉐어드로 마지막 메시지 값 적용해주기.
            // 쉐어드 프리퍼런스에
            // 키 : 채팅+id값
            // 벨류 : gson을 사용한 json String 으로 저장 마지막 메시지
            last_msg = Adapter_chatting.last_item();
            String tmp_ = gson.toJson(last_msg);

            SharedPreferences spf = getSharedPreferences("MESSAGE",0); // 0 :MODE_Privte
            SharedPreferences.Editor editor = spf.edit();
            editor.putString(String.valueOf(Room_id),tmp_); // 필요한값 저장할것
            System.out.println("SharedPreferences : tmp_"+tmp_);
            editor.commit(); // 저장 완료시다음진행, apply 저장하면서 다음진행


        //엑티비티에서 나갈때 room_id 초기화 해주기
            mServiceMessenger = new Messenger(iBinder);
            try {
                System.out.println("mServiceMessenger");
                android.os.Message msg = android.os.Message.obtain(null, 0);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);
                System.out.println("mServiceMessenger.send(msg)");
            }
            catch (RemoteException e) {
            }

            if(msg_list.size()>1){
               position=rv.getScrollState();
            }

            super.onPause();
        }

        public void change_room(int input_ch) throws IOException {
        System.out.println("채팅방 변경 알려줌");
        String input_ch_string = String.valueOf(input_ch);
        Message message = new Message();
        message.setContent(input_ch_string);
        message.setRoom_Id(Room_id);

        User_list_item user = new User_list_item();
        user.setId(user_info.getInstance().getUser_index_number());
        user.setUser_name(user_info.getInstance().getUser_name());
        user.setUser_img(user_info.getInstance().getImg_path());
        user.setUser_intro(user_info.getInstance().getUser_intro_profile());

        ArrayList<User_list_item> userlist = new ArrayList<>();

        userlist.add(user);
        message.setUserInfoArrayList(userlist);
        message.setTime(time());
        message.setType(MessageType.CHANGE);
        System.out.println("채팅방 변경 메시지 전송 : "+message.getContent());
        mService.send_message(message);

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
                        try {
                            System.out.println("mServiceMessenger");
                            android.os.Message msg = android.os.Message.obtain(null, Room_id);
                            msg.replyTo = mMessenger;
                            mServiceMessenger.send(msg);
                            System.out.println("mServiceMessenger.send(msg)");
                        }
                        catch (RemoteException e) {
                        }

                        try {
                            change_room(Room_id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }catch(Exception e){
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

        // 시간을 나타냇 포맷을 정한다 ( yyyy-MM-dd 같은 형태로 변형 가능 )
        SimpleDateFormat SimpleDateFormat_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // nowDate 변수에 값을 저장한다.
        return SimpleDateFormat_time.format(date);
    }

    /** Service 로 부터 message를 받음 */
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull android.os.Message msg) {
                System.out.println("act : what "+msg.what);
                String revString = msg.getData().getString("msg");
                Message message_Get = gson.fromJson(revString, Message.class);

                switch (message_Get.getType()) {
                    case VIDEO:
                    case IMG:
                    case MSG:
                    case NOTIFICATION:

                        if(message_Get.getRoom_Id()==Room_id){
                            System.out.println("mMessenger : "+message_Get.getType());
                            Adapter_chatting.add_item(message_Get);
                            rv.scrollToPosition(msg_list.size()-1);
                        }else {
                            System.out.println("여기 메시지 아님");
                        }

                        break;

                    case MSG_LIST:

                        System.out.println("mMessenger : MSG_LIST");
                        msg_list = message_Get.getMsg_list();
                        init(msg_list);

                        break;

                    default:
                        System.out.println("채팅룸 서비스-핸들러 디폴트값 생성");
                        break;
                }
                return false;
            }

        }));

    public void send_client() throws RemoteException {
        iBinder = Socket_service.mMessenger_bind();
        mServiceMessenger = new Messenger(iBinder);
        android.os.Message msg = android.os.Message.obtain(null, Room_id);
        msg.replyTo = mMessenger;
        System.out.println("서비스에 바인더 접속시켜주기 방번호 : "+Room_id);
        mServiceMessenger.send(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String img_url;

        //이미지 삽입 버튼 클릭시
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
//여기선 bitmap 안쓰는 액티비티
//                    // 선택한 이미지에서 비트맵 생성
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//
                    // 이미지 uri 받아오기
                    System.out.println("data.getData() : " + data.getData());
                    img_url = getRealPathFromURI(data.getData());

                    //이미지 업로드
                    String finalImg_url1 = img_url;

                    Thread thread = new Thread(){
                        public void run(){
                            try{

                    imageUpload(finalImg_url1);

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //동영상 삽입 버튼 클릭시
        if (requestCode == 2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 동영상 uri 받아오기
                    System.out.println("data.getData() : " + data.getData());
                    img_url = getRealPathFromURI(data.getData());

                    //동영상 업로드
                    String finalImg_url = img_url;
                    Thread thread = new Thread(){
                        public void run(){
                            try{
                    videoUpload(finalImg_url);


                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void imageUpload(final String imagePath) {
        //이미지 업로드용 멀티파트 요청
        //서버에서 받은 요청중 이미지 index값을 user 데이터에 업데이트

        System.out.println("imageUpload start");
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, server_info.getInstance().getURL()+ "/Data/img_file/img_upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONObject jObj_response = new JSONObject(response);
                            String message = jObj_response.getString("img_name");
                            img_path = message;

                            System.out.println("이미지 업로드 : message : "+message);


                            //여기서 메시지 보내줘야함.
                            send_img(img_path);

//                            // 이미지 표시
//                            System.out.println("mEditor img_url : " + server_info.getInstance().getURL() + "/Data/img_file/" + message);
//
//                            mEditor.focusEditor();
//                            mEditor.insertImage(server_info.getInstance().getURL() + "/Data/img_file/" + img_path, img_path, 320, 320);
//                            System.out.println("mEditor.insertImage");

                        } catch (JSONException | IOException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        smr.addFile("image", imagePath);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smr);

        System.out.println("imageUpload end");
    }

    private void videoUpload(final String videoPath) {
        //이미지 업로드용 멀티파트 요청
        //서버에서 받은 요청중 이미지 index값을 user 데이터에 업데이트

        System.out.println("videoUpload start");
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, server_info.getInstance().getURL() + "/Data/video/video_upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONObject jObj_response = new JSONObject(response);
                            String message = jObj_response.getString("video_name");
                            video_path = message;

                            System.out.println("이미지 업로드 : message : "+message);

                            //여기서 메시지 보내줘야함.
                            send_video(video_path);

//                            // 동영상 표시
//                            System.out.println("mEditor video_url : " + server_info.getInstance().getURL() + "/Data/video/" + message);
//
//                            mEditor.focusEditor();
//                            mEditor.insertVideo(server_info.getInstance().getURL() + "/Data/video/" + video_path, 320, 320);
//                            System.out.println("mEditor.insertVideo");

                        } catch (JSONException | IOException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        smr.addFile("video", videoPath);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smr);

        System.out.println("videoUpload end");
    }

    //업로드 하기위한 경로로 변경
    public String getRealPathFromURI(Uri uri) {
        int index = 0;
        System.out.println("getRealPathFromURI uri : " + uri);
        String[] proj = {MediaStore.Images.Media.DATA}; // 이미지 경로로 해당 이미지에 대한 정보를 가지고 있는 cursor 호출
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null); // 데이터가 있으면(가장 처음에 위치한 레코드를 가리킴)
        if (cursor.moveToFirst()) { // 해당 필드의 인덱스를 반환하고, 존재하지 않을 경우 예외를 발생시킨다.
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        Log.d("getRealPathFromURI", "getRealPathFromURI: " + cursor.getString(index));
        return cursor.getString(index);
    }

    public void send_img(String img_name) throws IOException {
        // 메시지 생성
        // 메시지 타입, 룸 아이디, 텍스트, 유저정보
        Message msg = new Message();
        msg.setType(MessageType.IMG);
        msg.setRoom_Id(Room_id);
        msg.setContent(img_name);
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

    public void send_video(String video_name) throws IOException {
        // 메시지 생성
        // 메시지 타입, 룸 아이디, 텍스트, 유저정보
        Message msg = new Message();
        msg.setType(MessageType.VIDEO);
        msg.setRoom_Id(Room_id);
        msg.setContent(video_name);
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

    public void out_chat() throws IOException {

        Message msg = new Message();
        msg.setType(MessageType.OUT);
        msg.setRoom_Id(Room_id);
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

        finish();


    }

    }

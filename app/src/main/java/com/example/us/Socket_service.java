package com.example.us;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
//      유저의 id값으로 비교 하게끔 변경해야하고, 콘솔 룸 채팅처럼 동작하도록 채팅방 엑티비티 수정 해야함.
//      그래서 유저의 id값, 채팅방의 id값 을 비교해서 현재 채팅방일 경우 리사이클러뷰에 데이터 추가해주기. (핸들러?)
//      1. 단순 핸들러를 이용해서 TextView  추가해보기 (레이아웃 스크롤 뷰 로 변경해야함).
//      2. 리사이클러뷰에 아이템 추가하는 방식으로 해보기.

public class Socket_service extends Service {

    public static Handler handler;
    NotificationManager Notifi_M;
    public static boolean is_conn;
    public static int room_id;
    public static ArrayList<Room_item> Room_list;
    public static Messenger mClient = null;   // Activity 에서 가져온 Messenger

//    SharedPreferences spf = getSharedPreferences("noti", 0); // 0 :MODE_Privte

    private static final String TAG = Socket_service.class.getName()+" : ";
    private static Gson gson = new Gson();


    // Binder given to clients
    private final IBinder binder = new Socket_binder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class Socket_binder extends Binder {
        Socket_service getService() {
            // Return this instance of LocalService so clients can call public methods
            return Socket_service.this;
        }
    }

    public Socket_service() {
    }

    public void close(){
        is_conn=false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!is_conn){

            Client_thread thread = new Client_thread();
            thread.start();

        }
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        handler = new myServiceHandler();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static IBinder mMessenger_bind(){
       return mMessenger.getBinder();
    }

    //클라이언트용 메서드 제작해서 사용
    public int getRandomNumber() {
        return 0;
    }


    public void send_message(Message message) throws IOException {

        Thread thread = new Thread(){
            public void run(){
                try{
                    Client_thread.ListenForInput_test(message);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }



    public static void read_message(Message message) {
        // 내 아이디
        int user_id = user_info.getInstance().getUser_index_number();
        // 보낸 유저의 아이디
        int from_user = message.getUserInfoArrayList().get(0).getId();

        //내 아이디와 보낸 유저의 아이디가 같을경우 내 메시지
        if(user_id == from_user){
            String read =message.getContent() + " / " + message.getTime();
            System.out.println(read);


        //다를 경우 상대방 메시지
        } else {
            String read = message.getUserInfoArrayList().get(0).getUser_name() + " : "
                    + message.getContent() + " / " + message.getTime();
            System.out.println(read);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public static String time() {
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();

        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);

        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        @SuppressLint
        ("SimpleDateFormat") SimpleDateFormat SimpleDateFormat_time
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // nowDate 변수에 값을 저장한다.
        return SimpleDateFormat_time.format(date);
    }

    private static String getServerIp() {

        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (local == null) {
            return "";
        } else {
            String ip = local.getHostAddress();
            return ip;
        }

    }

    public ArrayList<Room_item> get_room_list(){

        System.out.println("get_room_list : "+Room_list.size());

        if(Room_list.size()==0){
            return null;
        }

        return Room_list;
    }

    private static class Client_thread extends Thread {

        private static Socket s;
        private static BufferedReader br = null;
        private static PrintStream ps = null;
        private static Gson gson = new Gson();
        private static ArrayList<Message> messageArrayList = new ArrayList<>();
        private static User_list_item user;

        @Override
        public void run() {
            super.run();

            s = new Socket();
            SocketAddress endpoint = new InetSocketAddress(server_info.getInstance().getURL_service(), 12345);
            System.out.println(server_info.getInstance().getURL_service());
//        SocketAddress endpoint = new InetSocketAddress(server_info.getInstance().getURL_service(), 12345);
            try {
                s.connect(endpoint, 5 * 1000);
                br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
                ps = new PrintStream(s.getOutputStream());
                is_conn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //새로운 메시지 생성 (유저리스트에 유저정보, 시간, 타입, 접속 ip,
            Message message = new Message();

            String String_time = time();

            user = new User_list_item();
            user.setId(user_info.getInstance().getUser_index_number());
            user.setUser_name(user_info.getInstance().getUser_name());
            user.setUser_img(user_info.getInstance().getImg_path());
            user.setUser_intro(user_info.getInstance().getUser_intro_profile());

            ArrayList<User_list_item> userlist = new ArrayList<>();
            userlist.add(user);
            message.setUserInfoArrayList(userlist);

            message.setType(MessageType.CONNECT);
            message.setContent(getServerIp());

            message.setTime(String_time);
//            System.out.println("------------- 최초 서비스 생성시 메시지 체크 -------------");
//            System.out.println("유저 정보 : "+user.getUser_name());
//            System.out.println("시간 : "+message.getTime());
//            System.out.println("메시지 타입 : "+message.getType());
//            System.out.println("----------------------------------------------------------------------------");
            String messagesString = gson.toJson(message);
            System.out.println("messagesString : " + messagesString);
            ps.println(messagesString);

            Thread thread = new Thread(() -> {
                try {
                    while (s.isConnected()&&is_conn) {
                        String revString = br.readLine();
                        if (revString != null) {
                            Message message_Get = gson.fromJson(revString, Message.class);
                            System.out.println("message_Get");

                            switch (message_Get.getType()) {
                                case SUCCESS:
                                    System.out.println("SUCCESS");
                                    break;
                                case FAIL:
                                    System.out.println("FAIL");
                                    break;

                                case VIDEO:
                                case IMG:
                                case MSG:
                                case NOTIFICATION:

                                    System.out.println(message_Get.getType());

                                    //if 현재 채팅방이면 노티피케이션을 안주고
                                    //현재 채팅방이 아니면 노티피케이션을 준다.
                                    //핸들러에게 메세지를 보낸다.
                                    System.out.println("message_Get.getRoom_Id() id : "+message_Get.getRoom_Id());
                                    System.out.println("room id : "+room_id);

                                    //바인드된 액티비티가 있을 경우 보냄.
                                    if(mClient!=null){
                                    System.out.println("mClient!=null 입니다.");
                                    sendMsgToActivity(message_Get);
                                    }


                                    //해당 룸에 있을때는 노티피케이션을 보내지 않음
                                    if(room_id!=message_Get.getRoom_Id() && message_Get.getType()!=MessageType.NOTIFICATION){
                                    System.out.println("방 번호가 달라 노티피 케이션을 보냅니다.");
                                    send_msg_to_notification(message_Get);
                                    }

                                    break;

                                case USERLIST:
                                    System.out.println("USERLIST");
                                    if (message_Get.getType() == MessageType.USERLIST) {
                                        System.out.println(message_Get.getContent());
                                    }
                                    break;

                                case ROOMLIST:
                                    System.out.println("ROOMLIST");
//                                    read_room_list(message_Get);

                                    for(int i=0; i<message_Get.getRoom_list().size(); i++) {
                                        System.out.println(i+"번째 룸 아이디 : "+message_Get.getRoom_list().get(i).getId());
                                        System.out.println(i+"번째 룸 제목 : "+message_Get.getRoom_list().get(i).getTitle());
                                        System.out.println(i+"번째 룸 메시지 : "+message_Get.getRoom_list().get(i).getLast_message().getContent());
                                        System.out.println(i+"번째 룸 시간 : "+message_Get.getRoom_list().get(i).getLast_message().getTime());
                                    }

                                    sendMsgToActivity(message_Get);

                                    break;
                                case CHANGE:
                                    System.out.println("CHANGE");

                                    break;

                                case CREATEROOM:
                                    System.out.println("CREATEROOM");
                                    sendMsgToActivity(message_Get);

                                    break;

                                case MSG_LIST:
                                    System.out.println("MSG_LIST");
                                    //잘 왔는지 확인하는 코드
                                    System.out.println("message_Get.getMsg_list().size() : "+message_Get.getMsg_list().size());
                                    sendMsgToActivity(message_Get);


                                    break;

                                case INVITE:
                                    System.out.println("INVITE");
                                    sendMsgToActivity(message_Get);


                                    break;

                                case OUT:
                                    System.out.println("OUT");
                                    sendMsgToActivity(message_Get);


                                    break;

                                default:
                                    System.out.println("default");
                                    break;
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();

        }

        public static void ListenForInput_test(Message message_get) throws IOException {

            ps = new PrintStream(s.getOutputStream());


            if (message_get != null) {

                switch (message_get.getType()) {
                    case SUCCESS:
                        System.out.println("SUCCESS");
                        break;

                    case FAIL:
                        System.out.println("FAIL");
                        break;

                    case VIDEO:
                    case IMG:
                    case MSG:
                        System.out.println("MSG");

                        String tmp_messagesString = gson.toJson(message_get);
                        System.out.println("보내는 데이터 : " + tmp_messagesString);
                        ps.println(tmp_messagesString);

                        break;

                    case CREATEROOM:

                        System.out.println("CREATEROOM");
                        String tmp_create_string = gson.toJson(message_get);
                        System.out.println("보내는 데이터 : " + tmp_create_string);
                        ps.println(tmp_create_string);

                        break;

                    case USERLIST:
                        System.out.println("USERLIST");

                        break;

                    case NOTIFICATION:
                        System.out.println("NOTIFICATION");

                        break;

                    case ROOMLIST:
                        System.out.println("ROOMLIST");

                        String msg_room_list = gson.toJson(message_get);
                        System.out.println("서버로 보내는 데이터 : " + msg_room_list);
                        ps.println(msg_room_list);

                        break;

                    case OUT:
                        System.out.println("OUT");
                        String tmp_create_string_ = gson.toJson(message_get);
                        System.out.println("보내는 데이터 : " + tmp_create_string_);
                        ps.println(tmp_create_string_);
                        break;


                    case INVITE:
                        System.out.println("INVITE");
                        String tmp_create_string__ = gson.toJson(message_get);
                        System.out.println("보내는 데이터 : " + tmp_create_string__);
                        ps.println(tmp_create_string__);
                        break;

                    case CHANGE:
                        System.out.println("CHANGE");
                        room_id = message_get.getRoom_Id();
                        String ch_messagesString = gson.toJson(message_get);
                        ps.println(ch_messagesString);

                        break;

                    default:
                        System.out.println("default");
                        break;
                }
            }


        }

        public void destroy() {
            is_conn = false;
            System.out.println("destroy");
            new Thread(() -> {
                try {
                    if (br != null) {
                        br.close();
                        br = null;
                    }
                    if (ps != null) {
                        ps.close();
                        ps = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        public void read_room_list(Message ms){

            Room_list = new ArrayList<>();

            System.out.println("read_room_list / 룸 리스트 받기");

            System.out.println("ms.getRoom_list().size() : " + ms.getRoom_list().size());

            //룸리스트의 각 룸들을 반복문으로 호출
            for (int i = 0; i < ms.getRoom_list().size(); i++) {
//                System.out.println("i "+i);
                //임시로 룸 과 유저 리스트 생성
                ArrayList<User_list_item> tmp_user_list = new ArrayList<User_list_item>();
                Room_item tmp_item = new Room_item();

                tmp_item.setId(ms.getRoom_list().get(i).getId());
                tmp_item.setTitle(ms.getRoom_list().get(i).getTitle());

                //룸이 가지고 있는 유저 리스트를 대입해줌
                //각 유저들을 반복문으로 호출 및 임시 유저에 대입해서
                //룸의 유저리스트에 추가.
                for(int j=0; j<ms.getRoom_list().get(i).getUser_list_itemArrayList().size(); j++) {

//                    System.out.println("j : "+j);
//                    System.out.println("roomArrayList.get(i).getRoom_user_list().size()"+ms.getRoom_list().get(i).getUser_list_itemArrayList().size());

                    int tmp_id = ms.getRoom_list().get(i).getUser_list_itemArrayList().get(j).getId();
                    String tmp_name = ms.getRoom_list().get(i).getUser_list_itemArrayList().get(j).getUser_name();
                    String tmp_profie = ms.getRoom_list().get(i).getUser_list_itemArrayList().get(j).getUser_intro();
                    String tmp_img = ms.getRoom_list().get(i).getUser_list_itemArrayList().get(j).getUser_img();

                    User_list_item tmp_user = new User_list_item();
                    tmp_user.setUser_name(tmp_name);
                    tmp_user.setId(tmp_id);
                    tmp_user.setUser_img(tmp_img);
                    tmp_user.setUser_intro(tmp_profie);

                    tmp_user_list.add(tmp_user);


                }
                tmp_item.setUser_list_itemArrayList(tmp_user_list);
                Room_list.add(tmp_item);

            }


            System.out.println(TAG+"Room_list end");
            System.out.println(TAG+"Room_list size : "+Room_list.size());

        }


        }


    /** activity로부터 binding 된 Messenger */
    public static Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {
            //엑티비티 와 서비스 핸들러 연결
            if (msg.what != 0) {
                System.out.println("액티비티와 바인더 연결 방번호 : "+ msg.what);
                mClient = msg.replyTo;  // activity로부터 가져온
                room_id = msg.what;
            } else {
                //if room_id == 0 일때만
                room_id = 0;
                System.out.println("액티비티와 바인더 연결 채팅방 목록 ");
                mClient = msg.replyTo;  // activity로부터 가져온
            }
            return false;
        }
    }));


    public static void sendMsgToActivity(Message message) {
        System.out.println("sendMsgToActivity");
        Bundle bundle = new Bundle();
        String messagesString = gson.toJson(message);
        bundle.putString("msg",messagesString);
        android.os.Message msg = android.os.Message.obtain(null, 0);
        msg.setData(bundle);
        System.out.println("sendMsgToActivity mClient send(msg) try");
        try {
            mClient.send(msg);      // msg 보내기
        } catch (RemoteException e) {

        }
    }

    public static void send_msg_to_notification(Message message) {

//        if(!str.equals("OK")){
//           return;
//        }

        System.out.println("send_msg_to_notification");
        Bundle bundle = new Bundle();
        String messagesString = gson.toJson(message);
        bundle.putString("msg",messagesString);
        android.os.Message msg = android.os.Message.obtain(null, 0);
        msg.setData(bundle);
        System.out.println("send_msg_to_notification mClient send(msg) try");
        //노티피케이션 핸들러에 메시지 보내기
        handler.sendMessage(msg);     // msg 보내기
    }



    class myServiceHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {

            System.out.println("handleMessage");

            String revString = msg.getData().getString("msg");
            Message message_Get = gson.fromJson(revString, Message.class);

            Intent intent = new Intent(Socket_service.this, Activity_chat_room.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("id",message_Get.getRoom_Id() ); //전달할 값
            PendingIntent pendingIntent = PendingIntent.getActivity(Socket_service.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Socket_service.this, String.valueOf(message_Get.getRoom_Id()))
                    .setSmallIcon(R.drawable.ic_baseline_games_24)  // 작은 아이콘
                    .setContentTitle("War3 App")  // 제목
                    .setContentText(message_Get.getUserInfoArrayList().get(0).getUser_name()+"님의 메시지가 왔습니다.")  // 본문 텍스트
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 알림 우선순위

                    //위에서 생성한 intent 사용해서 작성
                    .setContentIntent(pendingIntent) //알림의 탭 작업 설정
                    .setAutoCancel(true); // 사용자가 탭하면 자동으로 알림을 삭제

            //OREO API 26 이상에서는 채널 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                System.out.println("OREO API 26");

                CharSequence channelName  = "channelName";

                String description = "오레오 이상을 위한 채널설정";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                //노티피케이션에 체널 및 체널 이름 등을 설정해줌.
                //메시지의 룸 아이디 값을 체널ID로 설정
                NotificationChannel channel = new NotificationChannel(String.valueOf(message_Get.getRoom_Id()), channelName , importance);
                channel.setDescription(description);

                // 노티피케이션 채널을 시스템에 등록
                assert Notifi_M != null;
                Notifi_M.createNotificationChannel(channel);

            }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

            //알림 진짜 띄우게 하는거 (알림 표시)
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(message_Get.getRoom_Id(), builder.build()); // 앞에숫자 : 고유 아이디 입력

        }


    };

    }

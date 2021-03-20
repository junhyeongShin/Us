package com.example.us;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
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
import java.util.Scanner;

public class Socket_service extends Service {

    public static boolean is_conn;
    public static ArrayList<Room_item> Room_list;


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

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //클라이언트용 메서드 제작해서 사용
    public int getRandomNumber() {
        return 0;
    }


    public void send_message(Message message) throws IOException {
        System.out.println("서비스 메세지 보내기");

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
        String read = message.getUserInfoArrayList().get(0).getUser_name() + " : " + message.getContent() + " / " + message.getTime();

        System.out.println(read);
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
        SimpleDateFormat SimpleDateFormat_time = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");

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
        System.out.println("get_room_list 1st : "+Room_list.get(0).getTitle());
        return Room_list;
    }

    private static class Client_thread extends Thread {

        private static Socket s;
        private static BufferedReader br = null;
        private static PrintStream ps = null;
        private static Gson gson = new Gson();
        private static int room_id;
        private static ArrayList<Message> messageArrayList = new ArrayList<>();
        private static User_list_item user;

        @Override
        public void run() {
            super.run();

            s = new Socket();
//            SocketAddress endpoint = new InetSocketAddress("192.168.56.1", 12345);
        SocketAddress endpoint = new InetSocketAddress("13.125.205.208", 12345);
            try {
                s.connect(endpoint, 5 * 1000);
                br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
                ps = new PrintStream(s.getOutputStream());
                is_conn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }


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
            String messagesString = gson.toJson(message);
            System.out.println("messagesString : " + messagesString);
            ps.println(messagesString);
            room_id = 0;


            Thread thread = new Thread(() -> {
                try {
                    while (s.isConnected()&&is_conn) {

                        String revString = br.readLine();

                        if (revString != null) {

                            Message message_Get = gson.fromJson(revString, Message.class);

                            switch (message_Get.getType()) {
                                case SUCCESS:
                                    System.out.println("SUCCESS");
                                    break;
                                case FAIL:
                                    System.out.println("FAIL");
                                    break;
                                case MSG:
                                    System.out.println("MSG");
                                    if (message_Get.getTo().equals(String.valueOf(room_id))) {
                                        if (message_Get.getUserInfoArrayList().get(0).getId() != user.getId()) {
                                            read_message(message_Get);
                                        } else {
                                            System.out.println("my MSG");
                                        }
                                    } else {
                                        System.out.println("not room");
//                                    System.out.println(revString);
                                    }

                                    break;
                                case USERLIST:
                                    System.out.println("USERLIST");
                                    if (message_Get.getType() == MessageType.USERLIST) {
                                        System.out.println(message_Get.getContent());
                                    }
                                    break;
                                case NOTIFICATION:
                                    System.out.println("NOTIFICATION");
                                    break;

                                case ROOMLIST:
                                    System.out.println("ROOMLIST");
                                    read_room_list(message_Get);


                                    break;
                                default:
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

        public static void ListenForInput() throws IOException {
            Scanner console = new Scanner(System.in);
            ps = new PrintStream(s.getOutputStream());

            System.out.println("입력하세요");
            System.out.println("타입");

            while (console.hasNextLine()) {
                System.out.println("타입");

                String String_time = time();

                String input = console.nextLine();

                if (input.toLowerCase().equals("quit")) {
                    //나가기

                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    userlist.add(user);

                    Message message = new Message();
                    message.setUserInfoArrayList(userlist);
                    message.setType(MessageType.DISCONNECT);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);
                    break;
                }

                if (input.toLowerCase().equals("join")) {
                    s = new Socket();
                    SocketAddress endpoint = new InetSocketAddress("192.168.56.1", 12345);
                    s.connect(endpoint, 5 * 1000);

                    br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
                    ps = new PrintStream(s.getOutputStream());


                    Message message = new Message();

                    String_time = time();


                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    userlist.add(user);
                    message.setUserInfoArrayList(userlist);

                    message.setType(MessageType.CONNECT);
                    message.setContent(getServerIp());

                    message.setTime(String_time);
                    String messagesString = gson.toJson(message);
                    System.out.println("messagesString : " + messagesString);
                    ps.println(messagesString);

                }

                if (input.toLowerCase().equals("change room")) {
                    System.out.println("변경할 룸");
                    String input_ch = console.nextLine();

                    Message message = new Message();
                    message.setContent(input_ch);

                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    userlist.add(user);
                    message.setUserInfoArrayList(userlist);

                    message.setTime(time());
                    message.setRoom_Id(Integer.parseInt(input_ch));


                    message.setType(MessageType.CHANGE);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);

                    room_id = Integer.parseInt(input_ch);
                    System.out.println("방 변경 : " + room_id);


                }
                if (input.toLowerCase().equals("user list")) {
                    Message message = null;
                    message = new Message();


                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    userlist.add(user);
                    message.setUserInfoArrayList(userlist);

                    message.setContent(input);
                    message.setType(MessageType.USERLIST);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);
                }

                if (input.toLowerCase().equals("room list")) {
                    Message message = null;
                    message = new Message();


                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    userlist.add(user);
                    message.setUserInfoArrayList(userlist);

                    message.setContent(input);
                    message.setType(MessageType.ROOMLIST);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);
                }

                if (input.toLowerCase().equals("msg")) {
                    //그냥 말하기
                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    userlist.add(user);

//                System.out.println("보내는 방번호");
//                String input_from = console.nextLine();
//                System.out.println("받는 방 번호");
//                String input_to = console.nextLine();
                    System.out.println("보낼 내용");
                    String input_content = console.nextLine();

                    Message message = new Message();
                    message.setFrom(String.valueOf(room_id));
//                message.setTo(input_to);
                    message.setTo(String.valueOf(room_id));
                    message.setTime(String_time);
                    message.setContent(input_content);
                    message.setUserInfoArrayList(userlist);
                    message.setType(MessageType.MSG);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);
                }

                if (input.toLowerCase().equals("create room")) {
                    //방 생성
                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    userlist.add(user);

                    user = new User_list_item();
                    user.setId(2);
                    user.setUser_name("user_name_2");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro_2");
                    userlist.add(user);

                    user = new User_list_item();
                    user.setId(3);
                    user.setUser_name("user_name_3");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro_3");
                    userlist.add(user);

                    user = new User_list_item();
                    user.setId(4);
                    user.setUser_name("user_name_4");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro_4");
                    userlist.add(user);

                    System.out.println("보낼 내용");
                    String input_content = console.nextLine();

                    Message message = new Message();
                    message.setFrom(String.valueOf(room_id));
                    message.setTo(String.valueOf(room_id));
                    message.setTime(String_time);
                    message.setContent(input_content);
                    message.setUserInfoArrayList(userlist);
                    message.setType(MessageType.CREATEROOM);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);
                }

                if (input.toLowerCase().equals("invite")) {
                    //방 초대
                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    userlist.add(user);

                    user = new User_list_item();
                    user.setId(7);
                    user.setUser_name("user_name_7");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro_7");
                    userlist.add(user);

                    System.out.println("보낼 내용");
                    String input_content = console.nextLine();

                    Message message = new Message();
                    message.setFrom(String.valueOf(room_id));
                    message.setTo(String.valueOf(room_id));
                    message.setTime(String_time);
                    message.setContent(input_content);
                    message.setUserInfoArrayList(userlist);
                    message.setType(MessageType.INVITE);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);
                }


                if (input.toLowerCase().equals("out")) {
                    //방 초대
                    ArrayList<User_list_item> userlist = new ArrayList<>();
                    user = new User_list_item();
                    user.setId(0);
                    user.setUser_name("user_name_1");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro");
                    userlist.add(user);

                    user = new User_list_item();
                    user.setId(7);
                    user.setUser_name("user_name_7");
                    user.setUser_img("basic.jpg");
                    user.setUser_intro("user_intro_7");
                    userlist.add(user);

                    System.out.println("보낼 내용");
                    String input_content = console.nextLine();

                    Message message = new Message();
                    message.setFrom(String.valueOf(room_id));
                    message.setTo(String.valueOf(room_id));
                    message.setTime(String_time);
                    message.setContent(input_content);
                    message.setUserInfoArrayList(userlist);
                    message.setType(MessageType.OUT);
                    String tmp_messagesString = gson.toJson(message);
                    System.out.println("보내는 데이터 : " + tmp_messagesString);
                    ps.println(tmp_messagesString);
                }
            }

            try {
                Thread.sleep(1L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }

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

                    case MSG:
                        System.out.println("MSG");

                        String tmp_messagesString = gson.toJson(message_get);
                        System.out.println("보내는 데이터 : " + tmp_messagesString);
                        ps.println(tmp_messagesString);

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
                        System.out.println("보내는 데이터 : " + msg_room_list);
                        ps.println(msg_room_list);

                        break;

                    case OUT:
                        System.out.println("OUT");

                        break;



                    case INVITE:
                        System.out.println("INVITE");

                        break;

                    default:

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

            System.out.println("룸 리스트 받기");

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
//                    System.out.println("roomArrayList.get(i).getRoom_user_list().size()"+ms.getRoom_list().get(j).getUser_list_itemArrayList().size());

                    int tmp_id = ms.getRoom_list().get(j).getUser_list_itemArrayList().get(j).getId();
                    String tmp_name = ms.getRoom_list().get(j).getUser_list_itemArrayList().get(j).getUser_name();
                    String tmp_profie = ms.getRoom_list().get(j).getUser_list_itemArrayList().get(j).getUser_intro();
                    String tmp_img = ms.getRoom_list().get(j).getUser_list_itemArrayList().get(j).getUser_img();

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
private static final String TAG = Socket_service.class.getName()+" : ";
    }


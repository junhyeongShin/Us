package com.example.us;

import com.example.us.Message.Message;

import java.sql.*;
import java.util.ArrayList;

public class MyDB_test {
    private static final String TAG = MyDB_test.class.getName()+" : ";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //     jdbc 드라이버 주소
    static final String DB_URL = "jdbc:mysql://13.125.205.208:3306/db_us?useSSL=false";
    //     DB 접속 주소 (db_us 에 접속, SSL 미사용)

    static final String USERNAME = "mysql_user";
    static final String PASSWORD = "1q2w3e4r!!";

    int num;

    static Message message = null;

    public MyDB_test(Message message){
        MyDB_test.message = message;
    }

    public MyDB_test(int num){
        this.num = num;
    }

    public boolean conn(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        System.out.print("User Table 접속 : ");
        try {
//      Class.forName(JDBC_DRIVER);
//      Class 클래스의 forName()함수를 이용해서 해당 클래스를 메모리로 로드 하는 것입니다.
//      URL, ID, password를 입력하여 데이터베이스에 접속합니다.
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

//      접속결과를 출력합니다.

            if (conn != null) {
                System.out.println("성공");

                //클라이언트의 메시지를 분석하고 해당 조치를 취합니다.
                switch (message.getType()) {

                    case ROOMLIST:

                        break;

                    case USERLIST:

                        break;

                    case CONNECT:
                        insert_login(message);

                        break;
                    case DISCONNECT:

                        break;

                    case MSG:

                        insert_MSG(message);

                        break;

                    case QUERY:

                        break;

                    case CHANGE:

                        break;

                    default:
                        break;
                }

                return true;
            } else {
                System.out.println("실패");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception : " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }



    public static int insert_MSG(Message message) throws SQLException {

        String query = "insert into messages (user_id, chat_id, content ,type ,time) values ( " +
                + message.getUserInfoArrayList().get(0).getId() + " ,  "
                + message.getTo() + " ,  "
                + " '"+ message.getContent()+"' "+ " ,  "
                +" '"+ message.getType()+"' "+ " ,  "
                + " '"+ message.getTime()+"' " +
                " );";

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        int num = DBExecutor.execUpdate(connection, query);
        connection.close();

        return num;
    }

    public static int insert_login(Message message) throws SQLException {

        String query = "insert into login_log (user_id, ip ,date) values ( " +
                + message.getUserInfoArrayList().get(0).getId() + " ,  "
                + " '"+ message.getContent()+"' " + " ,  "
                + " '"+ message.getTime()+"' " +
                " );";

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        int num = DBExecutor.execUpdate(connection, query);
        connection.close();

        return num;
    }

    public int insert_MSG_file(Message message) throws SQLException {

//            // 현재시간을 msec 으로 구한다.
//            long now = System.currentTimeMillis();
//
//            // 현재시간을 date 변수에 저장한다.
//            Date date = new Date(now);
//
//            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
//            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd/HH:00 ");
//
//            // nowDate 변수에 값을 저장한다.
//            String formatDate = sdfNow.format(date);


        String query = "insert into messages (id, user_id, chat_id, content ,type , messages_file_id ,date) values ( " +
                + message.getUserInfoArrayList().get(0).getId() + " ,  "
                + message.getTo() + " ,  "
                + message.getContent() + " ,  "
                + message.getType() + " ,  "
//                + message.getRoom_Id() + " ,  " 파일 ID 대입
                + " '"+ message.getTime()+"' " +
                " )";

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        int num = DBExecutor.execUpdate(connection, query);
        connection.close();

        return num;
    }

    public ArrayList<Message> get_room_msg(int id) throws SQLException {

        String query = "SELECT * FROM messages LEFT JOIN user u on messages.user_id = u.`index` " +
                "LEFT JOIN image i on i.id = u.img_profile where chat_id ="+id;

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        ArrayList<Message> list =  DBExecutor.DB_select_MSG(connection, query);
        connection.close();

        return list;

    }


    public int insert_chat_room(Message message) throws SQLException {

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("insert_chat_room 의 메세지");
        System.out.println(message.getUserInfoArrayList().size());

        String title = "";

        for(int i=0; i<message.getUserInfoArrayList().size(); i++) {

            if(message.getUserInfoArrayList().size()<i+1){
                title = title + message.getUserInfoArrayList().get(i).getUser_name();
            }else {
                title = title + message.getUserInfoArrayList().get(i).getUser_name() + ", ";
            }

        }

        title = title + "의 채팅방";

        String query = "insert into chat (create_time, title) values ('"+message.getTime()+"','"+title+"');";

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        DBExecutor.execUpdate(connection, query);



        query = "SELECT id FROM chat ORDER BY id DESC limit 1";

        int num = DBExecutor.DB_select_max(connection, query);
        connection.close();

        System.out.println(TAG+"insert_chat_room : "+num);


        insert_chat_user(message,num);

        return num;
    }

    public int insert_chat_room_title(Message message) throws SQLException {

        String query = "insert into chat (create_time, title) values ('"+message.getTime()+"','"+message.getContent()+"');";

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        DBExecutor.execUpdate(connection, query);



        query = "SELECT id FROM chat ORDER BY id DESC limit 1";

        int num = DBExecutor.DB_select_max(connection, query);
        connection.close();

        System.out.println(TAG+"insert_chat_room : "+num);


        insert_chat_user(message,num);

        return num;
    }

    public void insert_chat_user(Message message, int num) throws SQLException {

        String query_1st = "insert into chat_member (join_time, chat_id, user_id, master) values ('"+message.getTime()+"',"
                +num+","+message.getUserInfoArrayList().get(0).getId()+",1);";

        System.out.println(TAG+"query_1st : "+query_1st);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        DBExecutor.execUpdate(connection, query_1st);

        for(int i=1; i<message.getUserInfoArrayList().size(); i++) {

            String query = "insert into chat_member (join_time, chat_id, user_id) values ('"+message.getTime()+"',"
                    +num+","+message.getUserInfoArrayList().get(i).getId()+");";

            System.out.println(TAG+"query : "+query);

            DBExecutor.execUpdate(connection, query);
        }

        connection.close();

    }

    public boolean invite_chat_user(Message message, int num) throws SQLException {

        int tmp = -1;
        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        for(int i=1; i<message.getUserInfoArrayList().size(); i++) {

            String query = "insert into chat_member (join_time, chat_id, user_id) values ('"+message.getTime()+"',"
                    +num+","+message.getUserInfoArrayList().get(i).getId()+");";

            System.out.println(TAG+"query : "+query);

            tmp = DBExecutor.execUpdate(connection, query);
        }

        connection.close();

        if(tmp>0){
            return true;
        }
        return false;
    }



    public boolean check_chat_room(Message message) throws SQLException {

        String query = "SELECT * FROM chat where id = "+Integer.parseInt(message.getContent());

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        boolean check = DBExecutor.DB_select_check(connection, query);
        connection.close();
        System.out.println(TAG+"check_chat_room : "+check);

        return check;
    }

    public ArrayList<Room> get_room_list(Message message) throws SQLException {
        int user_id = message.getUserInfoArrayList().get(0).getId();

        ArrayList<Room> roomArrayList = new ArrayList<Room>();

        String query = "SELECT * From chat c LEFT JOIN chat_member m " +
                "on c.id = m.chat_id WHERE user_id = "+user_id;

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        roomArrayList = DBExecutor.DB_select_room(connection, query);
        connection.close();
        System.out.println(TAG+"room list size : "+roomArrayList.size());

        return roomArrayList ;
    }

    public ArrayList<User_list_item> room_user_list(int id) throws SQLException {
        int room_id = id;

        ArrayList<User_list_item> User_list = null;

        String query = "SELECT * From user u " +
                "LEFT JOIN chat_member m on u.index = m.user_id " +
                "LEFT JOIN image i on i.id = u.img_profile " +
                "WHERE m.chat_id ="+room_id;

        System.out.println(TAG+"query : "+query);

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        User_list = DBExecutor.DB_select_User(connection, query);
        connection.close();
        System.out.println(TAG+"room list size : "+User_list.size());

        return User_list ;
    }

    public boolean out_chat_user(Message message, int num) throws SQLException {

        int tmp = -1;
        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        for(int i=1; i<message.getUserInfoArrayList().size(); i++) {

            String query = "DELETE FROM chat_member where chat_id = "+num+" AND user_id = "+message.getUserInfoArrayList().get(i).getId();

            System.out.println(TAG+"query : "+query);

            tmp = DBExecutor.execUpdate(connection, query);
        }

        connection.close();

        if(tmp>0){
            return true;
        }
        return false;
    }
}

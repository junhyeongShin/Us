package com.example.us;

import com.example.us.Message.Message;
import com.example.us.Message.MessageType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBExecutor {

    public static <T> T execQuery( Connection connection,
                                   String query,
                                   TResultHandler<T> handler)
            throws SQLException
    {
        T value;

        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {
            value = handler.handle(result);
        }
        return value;
    }

    public static int execUpdate(Connection connection, String update)
            throws SQLException
    {
        int count;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(update);
            count = stmt.getUpdateCount();
        }
        return count;
    }

    public static int DB_select_max(Connection connection, String query)
            throws SQLException
    {
        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {

            if(result.next()){
                return result.getInt("id");
            }
        }

        return 0;
    }

    public static ArrayList<Message> DB_select_MSG (Connection connection, String query)
            throws SQLException
    {
        ArrayList<Message> msg_list = new ArrayList<>();


        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {

            if(result.next()){

                Message message_1st = new Message();

                message_1st.setType(MessageType.MSG);
                message_1st.setContent(result.getString("content"));
                message_1st.setTo(String.valueOf(result.getInt("chat_id")));
                message_1st.setTime(result.getString("time"));
                message_1st.setFrom(String.valueOf(result.getInt("user_id")));

                //id , name , intro , img
                User_list_item userInfo_1st = new User_list_item();
                userInfo_1st.setId(result.getInt("index"));
                userInfo_1st.setUser_name(result.getString("username"));
                userInfo_1st.setUser_intro(result.getString("intro_profile"));
                userInfo_1st.setUser_img(result.getString("img_path"));




                ArrayList<User_list_item> user_list_1st = new ArrayList<>();
                user_list_1st.add(userInfo_1st);
                message_1st.setUserInfoArrayList(user_list_1st);



                msg_list.add(message_1st);

                while(result.next()){

                    System.out.println("DB 에서 받은 결과 메세지 : "+result.toString());

                    Message message = new Message();
                    message.setType(MessageType.MSG);
                    message.setContent(result.getString("content"));
                    message.setTo(String.valueOf(result.getInt("chat_id")));
                    message.setTime(result.getString("time"));
                    message.setFrom(String.valueOf(result.getInt("user_id")));

                    //id , name , intro , img
                    User_list_item userInfo = new User_list_item();
                    userInfo.setId(result.getInt("index"));
                    userInfo.setUser_name(result.getString("username"));
                    userInfo.setUser_intro(result.getString("intro_profile"));
                    userInfo.setUser_img(result.getString("img_path"));

                    ArrayList<User_list_item> user_list = new ArrayList<>();
                    user_list.add(userInfo);
                    message.setUserInfoArrayList(user_list);

                    msg_list.add(message);
                }
            }else {
                System.out.println("DB 에서 받은 결과 없음"+result.toString());
            }

            System.out.println("DB 에서 받은 결과 메세지 갯수: "+msg_list.size());

        }

        return msg_list;
    }

    public static ArrayList<User_list_item> DB_select_User (Connection connection, String query)
            throws SQLException
    {
        ArrayList<User_list_item> user_list = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {

            if(result.next()){

                //id , name , intro , img
                User_list_item userInfo_1st = new User_list_item();
                userInfo_1st.setId(result.getInt("index"));
                userInfo_1st.setUser_name(result.getString("username"));
                userInfo_1st.setUser_intro(result.getString("intro_profile"));
                userInfo_1st.setUser_img(result.getString("img_path"));


                user_list.add(userInfo_1st);

                while(result.next()){

                    User_list_item userInfo = new User_list_item();
                    userInfo.setId(result.getInt("index"));
                    userInfo.setUser_name(result.getString("username"));
                    userInfo.setUser_intro(result.getString("intro_profile"));
                    userInfo.setUser_img(result.getString("img_path"));

                    user_list.add(userInfo);
                }

            }else {
                System.out.println("DB 에서 받은 결과 없음"+result.toString());
            }

            System.out.println("DB 에서 받은 결과 유저리스트 갯수: "+user_list.size());

        }

        return user_list;
    }

    public static boolean DB_select_check(Connection connection, String query)
            throws SQLException
    {
        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {

            if(result.next()){
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Room> DB_select_room (Connection connection, String query)
            throws SQLException
    {
        ArrayList<Room> room_list = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {

            if(result.next()){

                Room room_1 = new Room(result.getInt("id"));
                room_list.add(room_1);

                while(result.next()){

                    Room room = new Room(result.getInt("id"));
                    room_list.add(room);
                }

            }else {
                System.out.println("DB 에서 받은 결과 없음"+result.toString());
            }

            System.out.println("DB 에서 받은 결과 룸 갯수: "+room_list.size());

        }

        return room_list;
    }





}
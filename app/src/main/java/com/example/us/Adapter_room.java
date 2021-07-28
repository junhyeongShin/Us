package com.example.us;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;

public class Adapter_room extends RecyclerView.Adapter<Adapter_room.ItemViewHolder> {

    private static final String TAG = "Adapter_room";
    private ArrayList<Room_item> mData;
    Context context;

    public Adapter_room(ArrayList<Room_item> mData) {
        this.mData = mData;

        for(int i=0; i<mData.size(); i++) {
            String tmp_ =  mData.get(i).getLast_message().getContent();
            System.out.println(i+1+"번째 룸의 라스트 메시지 : "+tmp_);
            System.out.println(i+1+"번째 룸의 라스트 타입 : "+mData.get(i).getLast_message().getType());
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        View view_room_item;

        TextView textview_room_item_title;
        TextView textview__room_item_content;
        TextView time_room_item;
        TextView alarm_room_item;
        CircleImageView circleimage_room_item;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            view_room_item = itemView.findViewById(R.id.view_room_item);
            textview_room_item_title = itemView.findViewById(R.id.textview_room_item_title);
            textview__room_item_content = itemView.findViewById(R.id.textview__room_item_content);
            time_room_item = itemView.findViewById(R.id.time_room_item);
            alarm_room_item = itemView.findViewById(R.id.alarm_room_item);
            circleimage_room_item = itemView.findViewById(R.id.circleimage_room_item);

        }

    }

    @Override
    public Adapter_room.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        System.out.println(TAG+"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new Adapter_room.ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter_room.ItemViewHolder holder, final int position) {

//        TextView textview_room_item_title;
//        TextView textview__room_item_content;
//        TextView time_room_item;
//        TextView alarm_room_item;
//        CircleImageView circleimage_room_item;

        String title = "";

        for(int i=0; i<mData.get(position).getUser_list_itemArrayList().size(); i++) {

            String user_name = mData.get(position).getUser_list_itemArrayList().get(i).getUser_name();
            if(i==0){
               title = title + user_name;
            }else {
                title = title +", "+ user_name;
            }
        }

        title = title + "의 채팅방";

        holder.textview_room_item_title.setText(title);

        holder.time_room_item.setText(mData.get(position).getLast_message().getTime());


        if(mData.get(position).getLast_message().getType()== MessageType.IMG){
            holder.textview__room_item_content.setText("내용이 이미지 입니다.");
        }else if(mData.get(position).getLast_message().getType()== MessageType.VIDEO){
            holder.textview__room_item_content.setText("내용이 비디오 입니다.");
        }else {
            holder.textview__room_item_content.setText(mData.get(position).getLast_message().getContent());
        }

//        if(mData.get(position).getLast_message().getType()== MessageType.MSG)

        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL()
                +"/Data/img_file/"+
                mData.get(position).getUser_list_itemArrayList().get(0).getUser_img(),
                holder.circleimage_room_item);
        imageLoadTask.execute();

        if(mData.get(position).getAlarm()){
            holder.alarm_room_item.setVisibility(View.VISIBLE);
            holder.alarm_room_item.setText("N");
        }else {
            holder.alarm_room_item.setVisibility(View.INVISIBLE);
        }


        holder.view_room_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("룸 리사이클러뷰 클릭 포지션 : "+position);

                no_alarm_item(position);
                notifyItemChanged(position);

                //룸 id값이랑 같이 전송
                //서버에 룸id값에따른 데이터 받아오고 표시해주기기
                Intent mIntent = new Intent(v.getContext(),Activity_chat_room.class);
                mIntent.putExtra("id", mData.get(position).id);
                System.out.println("id : "+ mData.get(position).id);
                v.getContext().startActivity(mIntent);

            }
        });

    }

    @Override
    public long getItemId(int position) {
        System.out.println(getClass().getName()+"-getItemId- : "+position);
        return position;
    }

    @Override
    public int getItemCount() {
//        System.out.println(getClass().getName()+"-getItemCount- : "+mData.size());
        return mData.size();
    }

    public void delete_item(int position){

        mData.remove(position);
        notifyDataSetChanged();

//        //데이터 담아서 팝업(액티비티) 호출
//        AlertDialog.Builder ad = new AlertDialog.Builder(context);
//
//        ad.setTitle("채팅방 나가기");       // 제목 설정
//
//        // EditText 삽입하기
//        final EditText et_link = new EditText(context);
//        ad.setView(et_link);
//
//        // 확인 버튼 설정
//        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                System.out.println("채팅방 삭제 확인 버튼 클릭");
//
//                //TODO : 삭제 메시지 서버로 전송하고 응답 되면 삭제하기
////                mData.remove(position);
//
//            }
//        });
//
//        // 취소 버튼 설정
//        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                System.out.println("채팅방 삭제 취소 버튼 클릭");
//                dialog.dismiss();     //닫기
//                // Event
//            }
//        });
//
//        // 창 띄우기
//        ad.show();

    }

    public void update_item(Message msg){
        System.out.println("update_item 시작");

        int position =-1 ;

        //해당룸의 마지막 메시지 업데이트
        for(int i=0; i<mData.size(); i++) {
            //해당룸의 포지션값 받아오기
            if(mData.get(i).getId() == msg.getRoom_Id()){
               position = i;
               break;
            }
        }

        if(position==-1){
            System.out.println("해당 아이디의 룸이 없습니다.");
            return;
        }

        mData.get(position).setLast_message(msg);
        notifyItemChanged(position);

        System.out.println("update_item 끝");

    }

    public void alarm_item(Message msg){
        System.out.println("alarm_item 시작");

        int position = find_room_id(msg);

        if(position==-1){
            System.out.println("해당 아이디의 룸이 없습니다.");
           return;
        }

        mData.get(position).setAlarm(true);
        notifyItemChanged(position);

        System.out.println("update_item 끝");

    }

    public void no_alarm_item(int position){
        System.out.println("alarm_item 시작");

        mData.get(position).setAlarm(false);
        notifyItemChanged(position);

        System.out.println("update_item 끝");

    }

    public void line_item(){
        System.out.println("line_item ");
        Collections.sort(mData);
        notifyDataSetChanged();
    }

    public int find_room_id (Message msg){

        int position =-1 ;

        //해당룸의 포지션값 받아오기
        for(int i=0; i<mData.size(); i++) {
            if(mData.get(i).getId() == msg.getRoom_Id()){
                position = i;
                break;
            }
        }

        if(position==-1){
            System.out.println("해당 아이디의 룸이 없습니다.");
            return -1;
        }

        return position ;
    }

    public int find_room_id (int room_id){

        int position =-1 ;

        //해당룸의 포지션값 받아오기
        for(int i=0; i<mData.size(); i++) {
            if(mData.get(i).getId() == room_id){
                position = i;
                break;
            }
        }

        if(position==-1){
            System.out.println("해당 아이디의 룸이 없습니다.");
            return -1;
        }

        return position ;
    }



}
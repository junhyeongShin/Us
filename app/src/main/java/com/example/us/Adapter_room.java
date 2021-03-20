package com.example.us;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import org.w3c.dom.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_room extends RecyclerView.Adapter<Adapter_room.ItemViewHolder> {

    private static final String TAG = "Adapter_room";
    private ArrayList<Room_item> mData;
    Context context;

    public Adapter_room(ArrayList<Room_item> mData) {
        this.mData = mData;
        System.out.println(TAG+"mData : "+mData);
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

        holder.textview_room_item_title.setText(mData.get(position).getTitle());
//        holder.textview__room_item_content.setText(mData.get(position).getLast_message().getContent());
        holder.time_room_item.setText(mData.get(position).getTitle());
//        holder.alarm_room_item.setText(mData.get(position).getTitle());


        holder.view_room_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("룸 리사이클러뷰 클릭 포지션 : "+position);

                //룸 id값이랑 같이 전송
                //서버에 룸id값에따른 데이터 받아오고 표시해주기기
                Intent mIntent = new Intent(v.getContext(),Activity_chat_room.class);
                mIntent.putExtra("id", mData.get(position).id);
                System.out.println("id : "+ mData.get(position).id);
                context.startActivity(mIntent);

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


        //데이터 담아서 팝업(액티비티) 호출
        AlertDialog.Builder ad = new AlertDialog.Builder(context);

        ad.setTitle("게시물 삭제");       // 제목 설정

        // EditText 삽입하기
        final EditText et_link = new EditText(context);
        ad.setView(et_link);

        // 확인 버튼 설정
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("채팅방 삭제 확인 버튼 클릭");

                //TODO : 삭제 메시지 서버로 전송하고 응답 되면 삭제하기
//                mData.remove(position);


            }
        });

        // 취소 버튼 설정
        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("채팅방 삭제 취소 버튼 클릭");
                dialog.dismiss();     //닫기
                // Event
            }
        });


        // 창 띄우기
        ad.show();


    }

}
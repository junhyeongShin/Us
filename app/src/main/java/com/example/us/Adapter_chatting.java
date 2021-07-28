package com.example.us;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.us.Message.Message;
import com.example.us.Message.MessageType;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_chatting extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //채팅 어뎁터
    //채팅 메시지 타입별로 만들어준다
    //TODO : 파일전송시에 이미지 뷰어 및  동영상 플레이어 생성

    private static final String TAG = "Adapter_chatting";
    private ArrayList<Message> mData;
    Context context;

    public Adapter_chatting(ArrayList<Message> mData) {
        this.mData = mData;
        System.out.println(TAG+"채팅방 리사이클러뷰 어뎁터 생성 : "+mData);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Message last_item(){
        if(mData.size()==0){
           return null;
        }

        for(int i=0; i<mData.size(); i++) {
            if(mData.get(i).getType()==MessageType.MSG){
                return mData.get(mData.size()-i-1);
            }
        }

        return null;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            return new CenterViewHolder(view);

    }



   @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
       // instanceof : 왼쪽의 객체가 오른쪽의 자식 객체인지 확인 => True or False
       // 예시
       // 자신 instanceof 자신 = T
       // 부모 instanceof 자식 = T
       // 자식 instanceof 부모 = F
//       System.out.println("onBindViewHolder position : "+position);
//       System.out.println("onBindViewHolder position content: "+mData.get(position).getContent());


       //공지사항인 경우
       if(mData.get(position).getType()== MessageType.NOTIFICATION){

           System.out.println("공지사항");

           ((CenterViewHolder) holder).chat_center_txt.setText(mData.get(position).getContent());
           ((CenterViewHolder) holder).chat_right_item.setVisibility(View.GONE);
           ((CenterViewHolder) holder).chat_left_item.setVisibility(View.GONE);

       }

       //내 메시지
       else if(mData.get(position).getUserInfoArrayList().get(0).getId()==user_info.getInstance().getUser_index_number()){
           System.out.println("내 메시지");
           // 테스트 이미지인지 동영상인지 구분
           if(mData.get(position).getType()==MessageType.MSG){
               ((CenterViewHolder) holder).chat_right_txt.setText(mData.get(position).getContent());

           }
           else if(mData.get(position).getType()==MessageType.IMG){
               //텍스트 미 표시
               ((CenterViewHolder) holder).chat_right_txt.setVisibility(View.GONE);
               ((CenterViewHolder) holder).chat_box_right.setVisibility(View.GONE);

               System.out.println("채팅 메시지 타입 : MessageType.IMG");
               System.out.println("content : "+mData.get(position).getContent());

                //이미지표시
               ((CenterViewHolder) holder).imageView_chat_room_right.setVisibility(View.VISIBLE);

               //이미지 적용 클래스 사용
               //유저리스트의 이미지 (String) 으로 적용시킴
               System.out.println("이미지 적용 url : "+server_info.getInstance().getURL() + "/Data/img_file/"+mData.get(position).getContent());
//               ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() + "/Data/img_file/"+
//                       mData.get(position).getContent(),
//                       ((CenterViewHolder) holder).imageView_chat_room_right
//               );
//               imageLoadTask.execute();
               Glide.with(((CenterViewHolder) holder).imageView_chat_room_right.getContext())
                       .load(server_info.getInstance().getURL_IMG()+mData.get(position).getContent())
                       .into(((CenterViewHolder) holder).imageView_chat_room_right);


//               Glide.with(holder.itemView.getContext()).load(server_info.getInstance().getURL() + "/Data/img_file/"+mData.get(position).getContent()).into(((CenterViewHolder) holder).imageView_chat_room_right);

               ((CenterViewHolder) holder).imageView_chat_room_right.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       System.out.println("메시지 이미지 클릭");
                       //이미지 전체보기 창 만들기
                       Intent mIntent = new Intent(v.getContext(), ImgViewActivity.class);
                       mIntent.putExtra("img_view", mData.get(position).getContent());
                       v.getContext().startActivity(mIntent);
                   }
               });
           }
           //이미지 타입이 동영상이거나 아닐경우
           else{

               System.out.println("content : "+mData.get(position).getContent());

               //텍스트 미 표시
               ((CenterViewHolder) holder).chat_right_txt.setVisibility(View.GONE);
               ((CenterViewHolder) holder).chat_box_right.setVisibility(View.GONE);


               //동영상표시

               // String을 Uri형식으로 맞추어준다.
               Uri uri = Uri.parse(server_info.getInstance().getURL_VIDEO()+mData.get(position).getContent());
               final SimpleExoPlayer player = new SimpleExoPlayer.Builder(holder.itemView.getContext()).build();
               ((CenterViewHolder) holder).videoView_chat_room_right.setPlayer(player);
               ((CenterViewHolder) holder).videoView_chat_room_right.setVisibility(View.VISIBLE);

               // Build the media item.
               final MediaItem mediaItem = MediaItem.fromUri(uri);
               player.setMediaItem(mediaItem);
               // Prepare the player.
               player.prepare();

               ((CenterViewHolder) holder).videoView_chat_room_right.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       //영상 재생 중일때 클릭하면 멈춤
                       if(player.isPlaying()){
                           player.pause();
                           //영상 멈춰 있을때 클릭하면 재생
                       }else {
                           player.play();
                       }
                       //영상이 다 재생된경우 클릭하면 다시 재생
                       if(player.getPlaybackState()== Player.STATE_ENDED){
                           player.seekTo(0);
                           player.play();
                       }
                   }
               });
               //동영상 표시 끝

           }
           ((CenterViewHolder) holder).chat_right_time.setText(mData.get(position).getTime());
           ((CenterViewHolder) holder).chat_left_item.setVisibility(View.GONE);
           ((CenterViewHolder) holder).chat_center_item.setVisibility(View.GONE);

       }
       //남의 메시지
       else {
           System.out.println("남의 메시지");
           // 테스트 이미지인지 동영상인지 구분
           if(mData.get(position).getType()==MessageType.MSG){
//               System.out.println("채팅 메시지 타입 : MessageType.MSG");
              ((CenterViewHolder) holder).chat_left_txt.setText(mData.get(position).getContent());

           }
           else if(mData.get(position).getType()==MessageType.IMG){
               // content의 형식 확인
               // 앞뒤에 붙여줘야하는 테스트 추가 ex) : /data/
               // 만든 url 뷰에 붙여서 재생 확인
               //
               System.out.println("채팅 메시지 타입 : MessageType.IMG");
               System.out.println("content : "+mData.get(position).getContent());

               //텍스트 미 표시
               ((CenterViewHolder) holder).chat_left_txt.setVisibility(View.GONE);
               ((CenterViewHolder) holder).chat_box_left.setVisibility(View.GONE);


               //이미지 표시
               ((CenterViewHolder) holder).imageView_chat_room_left.setVisibility(View.VISIBLE);
               //이미지 적용 클래스 사용
               //유저리스트의 이미지 (String) 으로 적용시킴
               System.out.println("이미지 적용 url : "+server_info.getInstance().getURL() + "/Data/img_file/"+mData.get(position).getContent());
               ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() + "/Data/img_file/"+
                       mData.get(position).getContent(),
                       ((CenterViewHolder) holder).imageView_chat_room_left
               );
               imageLoadTask.execute();

//               Glide.with(holder.itemView.getContext()).load(server_info.getInstance().getURL() + "/Data/img_file/"+mData.get(position).getContent()).into(((CenterViewHolder) holder).imageView_chat_room_left);


               ((CenterViewHolder) holder).imageView_chat_room_left.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       System.out.println("메시지 이미지 클릭");
                       //이미지 전체보기 창 만들기
                       Intent mIntent = new Intent(v.getContext(), ImgViewActivity.class);
                       mIntent.putExtra("img_view", mData.get(position).getContent());
                       v.getContext().startActivity(mIntent);

                   }
               });

           }
           else {

               //텍스트 미 표시
               ((CenterViewHolder) holder).chat_left_txt.setVisibility(View.GONE);
               ((CenterViewHolder) holder).chat_box_left.setVisibility(View.GONE);

               // content의 형식 확인
               // 앞뒤에 붙여줘야하는 테스트 추가 ex) : /data/
               // 만든 url 뷰에 붙여서 재생 확인
               //
               System.out.println("채팅 메시지 타입 : MessageType.VIDEO");
               System.out.println("content : "+mData.get(position).getContent());


               //동영상 표시
               // String을 Uri형식으로 맞추어준다.
               Uri uri = Uri.parse(server_info.getInstance().getURL_VIDEO()+mData.get(position).getContent());
               final SimpleExoPlayer player = new SimpleExoPlayer.Builder(holder.itemView.getContext()).build();
               ((CenterViewHolder) holder).videoView_chat_room_left.setPlayer(player);
               ((CenterViewHolder) holder).videoView_chat_room_left.setVisibility(View.VISIBLE);

               // Build the media item.
               final MediaItem mediaItem = MediaItem.fromUri(uri);
               player.setMediaItem(mediaItem);
               // Prepare the player.
               player.prepare();

               ((CenterViewHolder) holder).videoView_chat_room_left.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(player.isPlaying()){
                           player.pause();
                       }else {
                           player.play();
                       }

                       if(player.getPlaybackState()== Player.STATE_ENDED){
                           player.seekTo(0);
                           player.play();
                       }
                   }
               });

           }
           ((CenterViewHolder) holder).chat_center_item.setVisibility(View.GONE);
           ((CenterViewHolder) holder).chat_right_item.setVisibility(View.GONE);

        //보낸 유저의 닉네임과 프로필 사진을 받아서 표시해주고
        ((CenterViewHolder) holder).chat_left_name.setText(mData.get(position).getUserInfoArrayList().get(0).getUser_name());
        ((CenterViewHolder) holder).chat_left_time.setText(mData.get(position).getTime());

        //이미지 적용 클래스 사용
        //유저리스트의 이미지 (String) 으로 적용시킴
        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() + "/Data/img_file/"+
                mData.get(position).getUserInfoArrayList().get(0).getUser_img(),
                ((CenterViewHolder) holder).chat_left_img);
        imageLoadTask.execute();

        ((CenterViewHolder) holder).chat_left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("프로필 사진 클릭");
                System.out.println("유저의 아이디 값 : "+mData.get(position).getUserInfoArrayList().get(0).getId());
                //유저 정보 보이게 팝업 액티비티 띄우기
            }
        });

        }


    }



    @Override
    public long getItemId(int position) {
//        System.out.println(getClass().getName()+"-getItemId- : "+position);
        return position;
    }

    @Override
    public int getItemCount() {
//        System.out.println(getClass().getName()+"-getItemCount- : "+mData.size());
        return mData.size();
    }


    //채팅 아이템에서 해당되는 부분만 보여주게 코딩했습니다
    //해당되지 않는 부분은 GONE으로 보이지 않게 처리했습니다.
    public class CenterViewHolder extends RecyclerView.ViewHolder{
        TextView chat_center_txt;
        TextView chat_left_txt;
        TextView chat_right_txt;
        TextView chat_left_name;
        TextView chat_left_time;
        TextView chat_right_time;
        CircleImageView chat_left_img;
        View chat_right_item;
        View chat_left_item;
        View chat_center_item;
        ImageView imageView_chat_room_left;
        ImageView imageView_chat_room_right;
        PlayerView videoView_chat_room_left;
        PlayerView videoView_chat_room_right;
        View chat_box_right;
        View chat_box_left;

        CenterViewHolder(View itemView)
        {
            super(itemView);
            chat_box_right = itemView.findViewById(R.id.chat_box_right);
            chat_box_left = itemView.findViewById(R.id.chat_box_left);
            chat_center_txt = itemView.findViewById(R.id.chat_center_txt);
            chat_left_txt = itemView.findViewById(R.id.chat_left_txt);
            chat_right_txt = itemView.findViewById(R.id.chat_right_txt);
            chat_left_name = itemView.findViewById(R.id.chat_left_name);
            chat_left_time = itemView.findViewById(R.id.chat_left_time);
            chat_right_time = itemView.findViewById(R.id.chat_right_time);
            chat_left_img = itemView.findViewById(R.id.chat_left_img);
            chat_right_item = itemView.findViewById(R.id.chat_right_item);
            chat_left_item = itemView.findViewById(R.id.chat_left_item);
            chat_center_item = itemView.findViewById(R.id.chat_center_item);
            imageView_chat_room_left = itemView.findViewById(R.id.imageView_chat_room_left);
            imageView_chat_room_right = itemView.findViewById(R.id.imageView_chat_room_right);
            videoView_chat_room_left = itemView.findViewById(R.id.videoView_chat_room_left);
            videoView_chat_room_right = itemView.findViewById(R.id.videoView_chat_room_right);

        }
    }

    public void add_item(Message msg){
        mData.add(msg);
        notifyItemInserted(mData.size()-1);
    }
}




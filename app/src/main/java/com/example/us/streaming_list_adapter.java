package com.example.us;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.us.wowza.live_stream;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class streaming_list_adapter extends RecyclerView.Adapter<streaming_list_adapter.ViewHolder>{

    private static final String TAG = Class.class.getName()+" : ";

    private List<live_stream> Items;

    Context context;

    public streaming_list_adapter(List<live_stream> Items) {
        this.Items = Items;
    }


    @NonNull
    @NotNull
    @Override
    public streaming_list_adapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        System.out.println("streaming_list_adapter start");

        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.streaming_list_item, parent, false);    //리사이클러뷰나 뷰페이저에 등록할 아이템뷰

        streaming_list_adapter.ViewHolder vh = new streaming_list_adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull streaming_list_adapter.ViewHolder holder, final int position) {

        holder.id = Items.get(position).getId();
        holder.textview_streaming_item.setText(Items.get(position).getName());
        holder.textview_streaming_username.setText(Items.get(position).getUser_name());

        System.out.println("onBindViewHolder : "+Items.get(position).getName());

        holder.layout_streaming_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(TAG+" 방송 아이템 클릭 ");
                Intent mIntent = new Intent(context, Activity_streaming_view.class);
                mIntent.putExtra("id",Items.get(position).getId());
                mIntent.putExtra("hls_playback_url",Items.get(position).getPlayer_hls_playback_url());
                context.startActivity(mIntent);

            }
        });

        if(Items.get(position).getThumbnail_url().equals("null")){
//            ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/null_streaming.gif",holder.imageView_streaming_item);
//            imageLoadTask.execute();
            Glide.with(holder.imageView_streaming_item.getContext())
                    .load(server_info.getInstance().getURL() +"/Data/img_file/null_streaming.gif")
                    .into(holder.imageView_streaming_item);

        }else {
//            ImageLoadTask imageLoadTask = new ImageLoadTask(Items.get(position).getThumbnail_url(),holder.imageView_streaming_item);
//            imageLoadTask.execute();
            Glide.with(holder.imageView_streaming_item.getContext()).load(Items.get(position).getThumbnail_url()).into(holder.imageView_streaming_item);

        }
//        ImageLoadTask imageLoadTask_2 = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+Items.get(position).getUser_img(),holder.circleimage_streaming_userimg);
//        imageLoadTask_2.execute();
        Glide.with(holder.circleimage_streaming_userimg.getContext()).load(server_info.getInstance().getURL_IMG()+Items.get(position).getUser_img()).into(holder.circleimage_streaming_userimg);


    }


    @Override
    public int getItemCount() {
//        System.out.println(TAG+"Items.size() : "+Items.size());
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String id;
        View layout_streaming_item;
        ImageView imageView_streaming_item;
        TextView textview_streaming_item;
        TextView textview_streaming_username;
        CircleImageView circleimage_streaming_userimg;


//        layout_streaming_item
//        imageView_streaming_item
//        textview_streaming_item


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            layout_streaming_item = itemView.findViewById(R.id.layout_streaming_item);
            imageView_streaming_item = itemView.findViewById(R.id.imageView_streaming_item);
            textview_streaming_item = itemView.findViewById(R.id.textview_streaming_item);
            circleimage_streaming_userimg = itemView.findViewById(R.id.circleimage_streaming_userimg);
            textview_streaming_username = itemView.findViewById(R.id.textview_streaming_username);
        }

    }




}

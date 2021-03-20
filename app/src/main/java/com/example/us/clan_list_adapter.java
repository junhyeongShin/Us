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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class clan_list_adapter  extends RecyclerView.Adapter<clan_list_adapter.ViewHolder>{

    private static final String TAG = Class.class.getName()+" : ";

    private List<Clan_item> Items;

    Context context;

    public clan_list_adapter(List<Clan_item> Items) {
        this.Items = Items;
    }


    @NonNull
    @NotNull
    @Override
    public clan_list_adapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clan_list_item_img, parent, false);    //리사이클러뷰나 뷰페이저에 등록할 아이템뷰

        clan_list_adapter.ViewHolder vh = new clan_list_adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull clan_list_adapter.ViewHolder holder, final int position) {

        holder.id = Items.get(position).getId();
        holder.master = Items.get(position).getMaster();
        holder.textview_clan_item_img.setText(Items.get(position).getTitle());

        holder.item_clan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(TAG+" 아이템 클릭 ");

                Intent mIntent = new Intent(context, Activity_clan_view.class);
                mIntent.putExtra("id",Items.get(position).getId());
                mIntent.putExtra("title",Items.get(position).getTitle());
                mIntent.putExtra("img",Items.get(position).getClan_img());
                mIntent.putExtra("master",Items.get(position).getMaster());
                mIntent.putExtra("intro",Items.get(position).getClan_introduce());
                context.startActivity(mIntent);

            }
        });

        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+Items.get(position).getClan_img(),holder.imageView_clan_item_img);
        imageLoadTask.execute();


    }


    @Override
    public int getItemCount() {
//        System.out.println(TAG+"Items.size() : "+Items.size());
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int id;
        int master;
        View item_clan_img;
        TextView textview_clan_item_img;
        ImageView imageView_clan_item_img;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            item_clan_img = itemView.findViewById(R.id.item_clan_img);
            textview_clan_item_img = itemView.findViewById(R.id.textview_clan_item_img);
            imageView_clan_item_img = itemView.findViewById(R.id.imageView_clan_item_img);
        }

    }




}

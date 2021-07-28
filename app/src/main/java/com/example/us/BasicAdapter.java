package com.example.us;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class BasicAdapter extends RecyclerView.Adapter<BasicAdapter.ViewHolder>{

    private List<User_list_item> Items;

    public BasicAdapter(List<User_list_item> Items) {
        this.Items = Items;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public BasicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_view, parent, false);    //리사이클러뷰나 뷰페이저에 등록할 아이템뷰

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicAdapter.ViewHolder holder, final int position) {
        holder.textview_friend_list_name.setText(Items.get(position).getUser_name());
        holder.textview_friend_list_intro.setText(Items.get(position).getUser_intro());

        //이미지 적용 클래스 사용
        //유저리스트의 이미지 (String) 으로 적용시킴
        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+Items.get(position).getUser_img(),holder.imageview_friend_list_item);
        imageLoadTask.execute();


        holder.imageview_friend_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("이미지 포지션 클릭 / 포지션 값 : "+position);


            }
        });

        holder.ibtn_friend_list_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("이미지 버튼 클릭 / 포지션 값 : "+position);

                        add_friend(position);

                    }
                });


    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textview_friend_list_name;
        TextView textview_friend_list_intro;
        ImageButton ibtn_friend_list_add;
        CircleImageView imageview_friend_list_item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_friend_list_item = itemView.findViewById(R.id.imageview_friend_list_item);
            textview_friend_list_name = itemView.findViewById(R.id.textview_friend_list_name);
            textview_friend_list_intro = itemView.findViewById(R.id.textview_friend_list_intro);
            ibtn_friend_list_add = itemView.findViewById(R.id.ibtn_friend_list_add);
        }



    }

    public void del_item(int position){
        System.out.println("친구 프로필 삭제");
        Items.remove(position);
        notifyDataSetChanged();
    }

    public void add_friend(int position){
        System.out.println("친구 프로필 추가");

        System.out.println("친구 user_id 값 : "+Items.get(position).getId());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);


        retrofit_api.Add_Friend(user_info.getInstance().getUser_index_number(), Items.get(position).getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    System.out.println("Add_Friend: 성공");
                    del_item(position);
                }
                System.out.println("Add_Friend 성공 call : "+call);
                System.out.println("Add_Friend 성공 response : "+response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Add_Friend 실패 call : "+call.toString());
                System.out.println("Add_Friend 실패 Throwable : "+t.toString());
            }
        });

    }

    public void add_item(User_list_item items){
        System.out.println("리사이클러뷰 아이템 추가");
        Items.add(items);
        notifyDataSetChanged();
    }

}

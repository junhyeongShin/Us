package com.example.us;
import android.widget.Toast;

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
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class friend_list_adapter extends RecyclerView.Adapter<friend_list_adapter.ViewHolder>{

    private List<User_list_item> Items;

    public friend_list_adapter(List<User_list_item> Items) {
        this.Items = Items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_del, parent, false);    //리사이클러뷰나 뷰페이저에 등록할 아이템뷰

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        holder.id = Items.get(position).getId();
        holder.textview_friend_list_intro_del.setText(Items.get(position).getUser_intro());
        holder.textview_friend_list_name_del.setText(Items.get(position).getUser_name());

        //이미지 적용 클래스 사용
        //유저리스트의 이미지 (String) 으로 적용시킴
        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+Items.get(position).getUser_img(),holder.imageview_friend_list_item_del);
        imageLoadTask.execute();


        holder.imageview_friend_list_item_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("이미지 포지션 클릭 / 포지션 값 : "+position);



            }
        });

        holder.ibtn_friend_list_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("이미지 버튼 클릭 / 포지션 값 : "+position);
                del_friend(position);

                Toast.makeText(v.getContext(), "친구 삭제",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int id;
        TextView textview_friend_list_name_del;
        TextView textview_friend_list_intro_del;
        ImageButton ibtn_friend_list_del;
        CircleImageView imageview_friend_list_item_del;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_friend_list_item_del = itemView.findViewById(R.id.imageview_friend_list_item_del);
            textview_friend_list_name_del = itemView.findViewById(R.id.textview_friend_list_name_del);
            textview_friend_list_intro_del = itemView.findViewById(R.id.textview_friend_list_intro_del);
            ibtn_friend_list_del = itemView.findViewById(R.id.ibtn_friend_list_del);
        }


    }

    public void del_item(int position){
        System.out.println("친구 프로필 삭제");
        Items.remove(position);
        notifyItemRemoved(position);
    }

    public void del_friend(final int position){
        System.out.println("친구 삭제");

        System.out.println("친구 user_id 값 : "+Items.get(position).getId());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.Del_Friend(user_info.getInstance().getUser_index_number(), Items.get(position).getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String data = response.body();
                    System.out.println("Del_Friend: 성공");
                    del_item(position);

                }
                System.out.println("Del_Friend 성공 call : "+call);
                System.out.println("Del_Friend 성공 response : "+response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Del_Friend 실패 call : "+call.toString());
                System.out.println("Del_Friend 실패 Throwable : "+t.toString());
            }
        });

    }


}

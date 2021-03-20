package com.example.us;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.List;

public class Adapter_join_list extends RecyclerView.Adapter<Adapter_join_list.ViewHolder>{


    private List<User_list_item> Items;
    int id;

    public Adapter_join_list(List<User_list_item> Items, int id) {
        this.Items = Items;
        this.id = id;
    }

    @NonNull
    @Override
    public Adapter_join_list.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_list_item, parent, false);    //리사이클러뷰나 뷰페이저에 등록할 아이템뷰

        return new Adapter_join_list.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_join_list.ViewHolder holder, final int position) {
        holder.id = Items.get(position).getId();
        holder.textview_join_list_name.setText(Items.get(position).getUser_name());
        holder.textview_join_list_intro.setText(Items.get(position).getUser_intro());

        //이미지 적용 클래스 사용
        //유저리스트의 이미지 (String) 으로 적용시킴
        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+Items.get(position).getUser_img(),holder.imageview_join_list_item);
        imageLoadTask.execute();


        holder.imageview_join_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("이미지 포지션 클릭 / 포지션 값 : "+position);


            }
        });

        holder.ibtn_join_list_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("이미지 버튼 클릭 / 포지션 값 : "+position);

                add_friend(position);

                Toast.makeText(v.getContext(), "클랜 가입",Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int id;
        TextView textview_join_list_name;
        TextView textview_join_list_intro;
        ImageButton ibtn_join_list_add;
        CircleImageView imageview_join_list_item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_join_list_item = itemView.findViewById(R.id.imageview_join_list_item);
            textview_join_list_name = itemView.findViewById(R.id.textview_join_list_name);
            textview_join_list_intro = itemView.findViewById(R.id.textview_join_list_intro);
            ibtn_join_list_add = itemView.findViewById(R.id.ibtn_join_list_add);
        }



    }

    public void del_item(int position){
        System.out.println("친구 프로필 삭제");
        Items.remove(position);
        notifyDataSetChanged();
    }

    public void add_friend(final int position){

        System.out.println("----------------------------------------------------------------------");
        System.out.println("클랜 가입 user_id 값 : "+Items.get(position).getId());
        System.out.println("클랜 가입 clan_id 값 : "+id);
        System.out.println("----------------------------------------------------------------------");



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);


        retrofit_api.Add_member( Items.get(position).getId(),id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();

                    assert result != null;
                    if(result.equals("success")){
                        System.out.println("Add_member: 성공 result : "+result);
                        del_item(position);
                    }else {
                        System.out.println("Add_member: 실패 result : "+result);
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Add_member 실패 call : "+call.toString());
                System.out.println("Add_member 실패 Throwable : "+t.toString());
            }
        });

    }

    public void add_item(User_list_item items){
        System.out.println("리사이클러뷰 아이템 추가");
        Items.add(items);
        notifyDataSetChanged();
    }
}

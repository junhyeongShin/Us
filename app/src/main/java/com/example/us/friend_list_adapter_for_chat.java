package com.example.us;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class friend_list_adapter_for_chat extends RecyclerView.Adapter<friend_list_adapter_for_chat.ViewHolder>{

    private List<friend_list_item> Items;
    ArrayList<friend_list_item> checked_list = new ArrayList<>();

    public friend_list_adapter_for_chat(List<friend_list_item> Items) {
        this.Items = Items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_for_chat, parent, false);    //리사이클러뷰나 뷰페이저에 등록할 아이템뷰

        return new ViewHolder(view);
    }

    public ArrayList<friend_list_item> get_checked_list (){
        return checked_list;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        holder.id = Items.get(position).getId();
        holder.textview_friend_list_name_chat.setText(Items.get(position).getUser_name());

        //이미지 적용 클래스 사용
        //유저리스트의 이미지 (String) 으로 적용시킴
        Glide.with(holder.imageview_friend_list_item_chat.getContext()).load(server_info.getInstance().getURL_IMG()+Items.get(position).getUser_img()).into(holder.imageview_friend_list_item_chat);



        holder.imageview_friend_list_item_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("이미지 포지션 클릭 / 포지션 값 : "+position);
            }
        });

        holder.checkbox_friends_for_chat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked_list.add(Items.get(position));
                    System.out.println("체크박스 리스트에 아이템 추가 : "+Items.get(position).getId());

                }else {
                    if(checked_list.contains(Items.get(position).getId())){
                        for(int i=0; i<checked_list.size(); i++) {
                            if(checked_list.get(i).getId() == Items.get(position).getId()){
                                checked_list.remove(i);
                                break;
                            }
                        }
                        System.out.println("체크박스 리스트에 아이템 삭제 : "+Items.get(position).getId());
                    }
                }
                System.out.println("현재 리스트 사이즈 : "+checked_list.size());
            }
        });


    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int id;
        TextView textview_friend_list_name_chat;
        CircleImageView imageview_friend_list_item_chat;
        CheckBox checkbox_friends_for_chat;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview_friend_list_item_chat = itemView.findViewById(R.id.imageview_friend_list_item_chat);
            textview_friend_list_name_chat = itemView.findViewById(R.id.textview_friend_list_name_chat);
            checkbox_friends_for_chat = itemView.findViewById(R.id.checkbox_friends_for_chat);
        }


    }


}

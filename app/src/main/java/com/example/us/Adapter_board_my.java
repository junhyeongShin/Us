package com.example.us;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_board_my extends RecyclerView.Adapter<Adapter_board_my.ItemViewHolder> {

    private static final String TAG = "Adapter";
    private ArrayList<Board_list> mData;
    Context context;
    private ArrayList<Integer> checked_list =  new ArrayList<>();
    boolean is_del = false;

    public Adapter_board_my(ArrayList<Board_list> mData) {
        this.mData = mData;
        System.out.println(TAG+"mData : "+mData);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView TextView_board_itemView_title;
        TextView TextView_board_itemView_time_my;
        TextView TextView_board_itemView_views_my;
        CheckBox checkbox_board_my;
        ImageButton btn_board_item_edit;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            TextView_board_itemView_title = itemView.findViewById(R.id.TextView_board_itemView_title_my);
            TextView_board_itemView_time_my = itemView.findViewById(R.id.TextView_board_itemView_time_my);
            checkbox_board_my = itemView.findViewById(R.id.checkbox_board_my);

        }

        public void onBind(Board_list item_board){
            // System.out.println(TAG+"onBind");

            // 현재시간을 date 변수에 저장한다.
            Date date = new Date(String.valueOf(item_board.getCreate_time()));

            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            SimpleDateFormat sdfNow_year = new SimpleDateFormat("yyyy");
            SimpleDateFormat sdfNow_month = new SimpleDateFormat("MM");
            SimpleDateFormat sdfNow_day = new SimpleDateFormat("dd");

            // nowDate 변수에 값을 저장한다.
            String formatYear = sdfNow_year.format(date);
            String formatMonth = sdfNow_month.format(date);
            String formatDay = sdfNow_day.format(date);

            TextView_board_itemView_title.setText(item_board.getTitle());
            TextView_board_itemView_time_my.setText(formatYear+" / "+formatMonth+" / "+formatDay);

        }
    }

    @Override
    public Adapter_board_my.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        System.out.println(TAG+"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_board_item_my, parent, false);
        return new Adapter_board_my.ItemViewHolder(view);
    }


    // 선택 리스너 추가
    // 해당 게시판의 정보 INTENT로 넘겨줌
    // 유저 정보 : 인덱스값 => DB에서 조회할 수 있게
    // 게시판 정보 : 전부다 넘겨줌 => 바로 표시가능하게게
   @Override
    public void onBindViewHolder(@NonNull Adapter_board_my.ItemViewHolder holder, final int position) {
//        System.out.println(TAG+"onBindViewHolder");
        holder.onBind(mData.get(position));

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //아이템 클릭해서 게시판 열었을때
                //board_list에서 받아온 데이터들중 게시판 내용및 글쓴이 정보를 INTENT 로 넘겨준다
                // 게시판 내용 : id, 제목, 내용
                // 유저 정보 : index, 이름, 아이디

                int index = mData.get(position).getId();
                String title = mData.get(position).getTitle();
                String content = mData.get(position).getContent();

                int user_id = user_info.getInstance().getUser_index_number();
                String user_name = mData.get(position).getUser_name();
                String user_intro = mData.get(position).getUser_intro();
                String user_img = mData.get(position).getUser_img();
                String user_email = mData.get(position).getEmail();

                System.out.println(getClass().getSimpleName()+"_onClick_position : "+position);
                System.out.println(getClass().getSimpleName()+"_onClick_index : "+index);

                Intent mIntent = new Intent(context, Activity_board_view.class);

                mIntent.putExtra("id", index);
                mIntent.putExtra("title", title);
                mIntent.putExtra("content", content);

                mIntent.putExtra("user_id", user_id);
                mIntent.putExtra("user_name", user_name);
                mIntent.putExtra("user_email", user_email);
                mIntent.putExtra("user_intro", user_intro);
                mIntent.putExtra("user_img", user_img);


                context.startActivity(mIntent);

                }



        });

        if(is_del){
            holder.checkbox_board_my.setVisibility(View.VISIBLE);
        }else {
            holder.checkbox_board_my.setVisibility(View.GONE);
        }

        holder.checkbox_board_my.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked_list.add(mData.get(position).getId());
                }else {
                    for(int i=0; i<checked_list.size(); i++) {
                        if(checked_list.get(i)==mData.get(position).getId()){
                            checked_list.remove(i);
                            break;
                        }
                    }
                }
                for(int i=0; i<checked_list.size(); i++) {
                    System.out.println("checked_list "+i+" : "+checked_list.get(i));
                }
            }
        });


//       holder.btn_board_item_edit.setOnClickListener(new OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//
//               // 아이템 클릭해서 수정 액티비티 열었을때
//               // board_list에서 받아온 데이터들중 게시판에서 수정할 내용을 INTENT 로 넘겨준다
//               // 게시판 내용 : id, 제목, 내용
//
//               int index = mData.get(position).getId();
//               String title = mData.get(position).getTitle();
//               String content = mData.get(position).getContent();
//               String category = mData.get(position).getCategory();
//
//               int user_id = mData.get(position).getWriter();
//               String user_name = mData.get(position).getUser_name();
//               String user_intro = mData.get(position).getUser_intro();
//               String user_img = mData.get(position).getUser_img();
//               String user_email = mData.get(position).getEmail();
//
////               System.out.println(getClass().getSimpleName()+"_onClick_position : "+position);
////               System.out.println(getClass().getSimpleName()+"_onClick_index : "+index);
//
//               Intent mIntent = new Intent(context, Activity_board_edit.class);
//               mIntent.putExtra("id", index);
//               System.out.println("id : "+index);
//               mIntent.putExtra("title", title);
//               System.out.println("title : "+title);
//               mIntent.putExtra("content", content);
//               System.out.println("content : "+content);
//               mIntent.putExtra("category", category);
//               System.out.println("category : "+category);
//
//               mIntent.putExtra("user_id", user_id);
//               System.out.println("user_id : "+user_id);
//               mIntent.putExtra("user_name", user_name);
//               System.out.println("user_name : "+user_name);
//               mIntent.putExtra("user_email", user_email);
//               System.out.println("user_email : "+user_email);
//               mIntent.putExtra("user_intro", user_intro);
//               System.out.println("user_intro : "+user_intro);
//               mIntent.putExtra("user_img", user_img);
//               System.out.println("user_img : "+user_img);
//
//               context.startActivity(mIntent);
//
////               System.out.println("btn_board_item_edit position : "+position);
////               System.out.println("btn_board_item_edit Data_id : "+mData.get(position).getId());
//           }
//       });
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


    public void check_del(ArrayList<Integer> list){
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
                Log.v(TAG, "Yes Btn Click");

                // Text 값 받아서 로그 남기기
                String value = et_link.getText().toString();
                Log.v(TAG, value);

                dialog.dismiss();     //닫기
                // Event

                System.out.println(getClass().getName()+"-delete_item- ");
                for(int i=0; i<list.size(); i++) {
                    del(list.get(i));

                }


            }
        });

        // 취소 버튼 설정
        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "No Btn Click");
                dialog.dismiss();     //닫기
                // Event
            }
        });


        // 창 띄우기
        ad.show();
    }


    public void del(int id){

        System.out.println("del :"+id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getData_del_board(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String data = response.body();
                    System.out.println("성공");
                    System.out.println(data);
                    remove_id(id);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("실패");
            }
        });

    }

    public ArrayList<Integer> getChecked_list() {
        return checked_list;
    }

    public void remove_id(int id){
        for(int i=0; i<mData.size(); i++) {
            if(mData.get(i).getId()==id){
                mData.remove(i);
                break;
            }
        }
    }

    public void setIs_del(boolean is_del) {
        this.is_del = is_del;
        if(is_del){

           notifyDataSetChanged();
        }else {

           notifyDataSetChanged();
        }
    }

    public void clear_checked(){
        checked_list = new ArrayList<>();
    }

}




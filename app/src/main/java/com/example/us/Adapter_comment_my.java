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
import java.util.Map;

public class Adapter_comment_my extends RecyclerView.Adapter<Adapter_comment_my.ItemViewHolder> {

    private static final String TAG = "Adapter";
    private ArrayList<comment> mData;
    Context context;
    private ArrayList<comment> checked_list =  new ArrayList<>() ;
    boolean is_del = false ;

    ArrayList<CheckBox> checkboxe_list = new ArrayList<>();

    public Adapter_comment_my(ArrayList<comment> mData) {
        this.mData = mData;
        System.out.println(TAG+"mData : "+mData);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView TextView_comment_content_my;
        TextView TextView_comment_time_my;
        TextView TextView_comment_board_name;
        CheckBox checkbox_comment_my;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            TextView_comment_content_my = itemView.findViewById(R.id.TextView_comment_content_my);
            TextView_comment_time_my = itemView.findViewById(R.id.TextView_comment_time_my);
            TextView_comment_board_name = itemView.findViewById(R.id.TextView_comment_board_name);
            checkbox_comment_my = itemView.findViewById(R.id.checkbox_comment_my);

        }

        public void onBind(comment item_board){
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

            TextView_comment_content_my.setText(item_board.getContent());
            TextView_comment_time_my.setText(formatYear+" / "+formatMonth+" / "+formatDay);
            TextView_comment_board_name.setText(item_board.getTitle());

        }
    }

    @Override
    public Adapter_comment_my.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment_item_my, parent, false);

        return new Adapter_comment_my.ItemViewHolder(view);
    }


    // 선택 리스너 추가
    // 해당 게시판의 정보 INTENT로 넘겨줌
    // 유저 정보 : 인덱스값 => DB에서 조회할 수 있게
    // 게시판 정보 : 전부다 넘겨줌 => 바로 표시가능하게게

   @Override
    public void onBindViewHolder(@NonNull Adapter_comment_my.ItemViewHolder holder, final int position) {

        holder.onBind(mData.get(position));

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //아이템 클릭해서 게시판 열었을때
                //board_list에서 받아온 데이터들중 게시판 내용및 글쓴이 정보를 INTENT 로 넘겨준다
                // 게시판 내용 : id, 제목, 내용
                // 유저 정보 : index, 이름, 아이디


                int index = mData.get(position).getBoard_id();
                int point = mData.get(position).getId();
                int is_recomment = mData.get(position).getIs_recomment();

                System.out.println(getClass().getSimpleName()+"_onClick_position : "+position);
                System.out.println(getClass().getSimpleName()+"_onClick_index : "+index);

                Intent mIntent = new Intent(context, Activity_board_view.class);

                mIntent.putExtra("id", index);
                mIntent.putExtra("point", point);
                mIntent.putExtra("is_recomment", is_recomment);

                context.startActivity(mIntent);

                }

        });

        if(is_del){
           holder.checkbox_comment_my.setVisibility(View.VISIBLE);
       } else {
           holder.checkbox_comment_my.setVisibility(View.GONE);
       }

        holder.checkbox_comment_my.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    checked_list.add(mData.get(position));

                } else {
                    for(int i=0; i<checked_list.size(); i++) {
                        if(checked_list.get(i).getId()==mData.get(position).getId()){
                            checked_list.remove(i);
                            break;
                        }
                    }
                }
                for(int i=0; i<checked_list.size(); i++) {
                    System.out.println("checked_list "+i+" : "+checked_list.get(i).getId());
                }
            }
        });

       checkboxe_list.add(holder.checkbox_comment_my);


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

    public ArrayList<comment> getChecked_list() {
        return checked_list;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        clear_check_box();

    }

    public void del_item(){

        //데이터 담아서 팝업(액티비티) 호출
        AlertDialog.Builder ad = new AlertDialog.Builder(context);

        ad.setTitle("댓글 삭제");       // 제목 설정

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

                for(int i=0; i<checked_list.size(); i++) {

                    if(checked_list.get(i).getIs_recomment()==0){

                        System.out.println("삭제 요청 댓글 : "+checked_list.get(i).getId());
                        System.out.println("삭제 요청 댓글 확인 : "+checked_list.get(i).getIs_recomment());
                        delete_item_comment(checked_list.get(i));

                    } else {

                        System.out.println("삭제 요청 대댓글 : "+checked_list.get(i).getId());
                        delete_item_re(checked_list.get(i));

                    }

                }

                clear_checked();

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

    public void delete_item_comment(comment comment){

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(server_info.getInstance().getURL())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

                retrofit_api.getData_del_comment(comment.getId()).enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            String data = response.body();
                            System.out.println("성공");
                            System.out.println(data);

                            remove_item(comment);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println("실패");
                    }

                });


    }

    public void delete_item_re(comment comment){

        System.out.println(getClass().getName()+"-delete_item- ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getData_del_re_comment(comment.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String data = response.body();
                    System.out.println("대 댓글 삭제 성공 : "+comment.getId());
                    System.out.println(data);

                    remove_item(comment);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("대 댓글 삭제 실패 : "+call);
                System.out.println("대 댓글 삭제 실패 : "+t);
            }
        });



    }

    public void remove_item(comment comment){
        for(int i=0; i<mData.size(); i++) {
            if(mData.get(i).getId()==comment.getId()&&comment.getIs_recomment()==mData.get(i).getIs_recomment()){
               mData.remove(i);
            }
        }
        notifyDataSetChanged();
        System.out.println("remove 아이템 완료");
    }

    public void clear_check_box(){

        for(int i=0; i<checkboxe_list.size(); i++) {
            checkboxe_list.get(i).setChecked(false);
        }

    }





}




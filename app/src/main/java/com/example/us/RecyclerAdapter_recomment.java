package com.example.us;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter_recomment extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<re_comment> mData_re = null ;
    static final String TAG = "RecyclerAdapter_recomment : ";
    Context context;
    int comment_id;

    @NonNull
    @NotNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        context = parent.getContext() ;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.re_comment, parent, false) ;

        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view,0);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull final RecyclerAdapter.ViewHolder holder, final int position) {


        holder.textview_re_comment_content.setText(mData_re.get(position).getContent());
        holder.textview_re_comment_username.setText(mData_re.get(position).getUsername());

        String user_img_path;

        if(mData_re.get(position).getImg_url().equals("0")){
           user_img_path = "basic_img.png";
        }else {
            user_img_path = mData_re.get(position).getImg_url();
        }
        Glide.with(holder.circleimage_re_comment.getContext())
                .load(server_info.getInstance().getURL_IMG() +user_img_path)
                .into(holder.circleimage_re_comment);



        if(mData_re.get(position).getUser_id()==user_info.getInstance().getUser_index_number()) {
            holder.ibtn_re_comment_menu.setVisibility(View.VISIBLE);
        }

        holder.ibtn_re_comment_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("댓글 메뉴 버튼 클릭");

                    final PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_re_comment, popupMenu.getMenu());
                    System.out.println("작성자 와 접속자 동일");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.re_comment_edit) {
                                System.out.println(TAG + "ibtn_comment_edit");

                                Intent mIntent = new Intent(context, Activity_re_comment_edit.class);

                                mIntent.putExtra("id", mData_re.get(position).getId());
                                mIntent.putExtra("content", mData_re.get(position).getContent());

                                context.startActivity(mIntent);
                                return true;
                            } else if (item.getItemId() == R.id.re_comment_delete) {
                                delete_item(position);
                                System.out.println(TAG + "ibtn_comment_delete");
                                return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();

            }
        });

        String str = String.valueOf(position);
        Log.i("onBindViewHoler 대댓글",str);


    }

    public RecyclerAdapter_recomment(ArrayList<re_comment> list,int id) {
        mData_re = list;
        comment_id = id;
    }

    @Override
    public int getItemCount() {
        return mData_re.size();
    }

    public void delete_item(final int position){


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

                System.out.println(getClass().getName()+"-delete_item- ");
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(server_info.getInstance().getURL())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

                retrofit_api.getData_del_re_comment(mData_re.get(position).getId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            String data = response.body();
                            System.out.println("대 댓글 삭제 성공 : "+mData_re.get(position).getId());
                            System.out.println(data);

                            mData_re.remove(mData_re.get(position));
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println("대 댓글 삭제 실패 : "+call);
                        System.out.println("대 댓글 삭제 실패 : "+t);
                    }
                });

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

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
package com.example.us;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<comment> mData = null ;
    static final String TAG = "RecyclerAdapter : ";

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.comment_item, parent, false) ;
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view) ;

        return vh ;
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerAdapter(ArrayList<comment> list) {
        mData = list ;
    }

    //position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.textview_comment_content.setText(mData.get(position).getContent());


        String str = String.valueOf(position);
        Log.i("onBindViewHoler",str);
    }

    //리사이클러뷰 아이템 갯수 설정하는 메소드.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textview_comment_username;
        TextView textview_comment_content;
        TextView textview_comment_etc;
        ImageButton ibtn_comment_edit;
        ImageButton ibtn_comment_delete;



        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 연결
            textview_comment_username =  itemView.findViewById(R.id.textview_comment_username);
            textview_comment_content =  itemView.findViewById(R.id.textview_comment_content);
            textview_comment_etc =  itemView.findViewById(R.id.textview_comment_etc);
            ibtn_comment_edit =  itemView.findViewById(R.id.ibtn_comment_edit);
            ibtn_comment_delete =  itemView.findViewById(R.id.ibtn_comment_delete);

            // 클릭이벤트 추가
            ibtn_comment_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: 버튼 클릭시 수정
//                    v.getId();
                    System.out.println(TAG+"ibtn_comment_edit");

                }
            });

            ibtn_comment_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:버튼 클릭시 삭제
//                    v.getId();
                    System.out.println(TAG+"ibtn_comment_delete");

                }
            });

        }
    }

}




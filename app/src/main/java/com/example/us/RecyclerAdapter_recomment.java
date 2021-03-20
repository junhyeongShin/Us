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



        final RecyclerAdapter.ViewHolder viewHolder = holder;

        holder.textview_re_comment_content.setText(mData_re.get(position).getContent());
        holder.textview_re_comment_username.setText(mData_re.get(position).getUsername());




//        // 접속 유저와 댓글을 작성한 유저가 동일할시에 수정 및 삭제 버튼 표시
//        if(mData_re.get(position).getUser_id()==user_info.getInstance().getUser_index_number()){
//            holder.ibtn_re_comment_edit.setVisibility(0);
//            holder.ibtn_re_comment_delete.setVisibility(0);
//        }



        // 프로필 이미지 적용.
        final Post_to_server post_to_server =new Post_to_server();


        // 서버에 데이터 요청 후, 각 이미지 및 텍스트에 적용하는 쓰레드
        new Thread(new Runnable() {
            @Override
            public void run() {

                String user_img_path = "";

                //포스트로 데이터 요청.
                String result_user_data = post_to_server.post_get_img_id("/Data/get_user_img.php", mData_re.get(position).getUser_id());

                try {
                    JSONObject jsonObject = new JSONObject(result_user_data);
                    String result_img_data_decode =  jsonObject.getString("result_img");
                    JSONObject jsonObject_img_decode = new JSONObject(result_img_data_decode);


                    //이미지 부분 json 파싱된 uri를 이용해 이미지 표시
                    user_img_path = jsonObject_img_decode.getString("img_path");

                    ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+user_img_path,holder.circleimage_re_comment);
                    imageLoadTask.execute();

                    System.out.println("댓글 프로필 이미지 url : "+server_info.getInstance().getURL() +"/Data/img_file/"+user_img_path);

                } catch (JSONException e) {
                    e.printStackTrace();

                    //실패했을시 기본 이미지 적용
                    user_img_path = "basic_img.png";

                    System.out.println("댓글 프로필 이미지 불러오기 실패");
                    System.out.println("댓글 프로필 이미지 url : "+server_info.getInstance().getURL() +"/Data/img_file/"+user_img_path);

                    ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+user_img_path,holder.circleimage_re_comment);
                    imageLoadTask.execute();
                }

            }
        }).start();


//        // 클릭이벤트 추가
//        holder.ibtn_re_comment_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(TAG+"ibtn_comment_edit");
//
//                Intent mIntent = new Intent(context, Activity_re_comment_edit.class);
//
//                mIntent.putExtra("id",mData_re.get(position).getId());
//                mIntent.putExtra("content",mData_re.get(position).getContent());
//
//                context.startActivity(mIntent);
//
//            }
//        });
//
//        holder.ibtn_re_comment_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete_item(position);
//                System.out.println(TAG+"ibtn_comment_delete");
//            }
//        });

//        holder.ibtn_re_comment_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                System.out.println(TAG+"ibtn_commment_re_add");
//
//                Intent mIntent = new Intent(context, Activity_re_comment_add.class);
//                mIntent.putExtra("comment_id",comment_id);
//                mIntent.putExtra("content",mData_re.get(position).getContent());
//                context.startActivity(mIntent);
//
//            }
//        });
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
        Log.i("onBindViewHoler",str);


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
                            System.out.println("성공");
                            System.out.println(data);

                            mData_re.remove(mData_re.get(position));
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println("실패");
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


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
//    public class ViewHolder_recomment extends RecyclerView.ViewHolder{
//        TextView textview_re_comment_username;
//        TextView textview_re_comment_content;
//        CircleImageView circleimage_re_comment;
////        TextView textview_re_comment_etc;
//        ImageButton ibtn_re_comment_edit;
//        ImageButton ibtn_re_comment_delete;
////        ImageButton ibtn_re_comment_add;
//
//
//
//        ViewHolder_recomment(View vh) {
//            super(vh) ;
//            // 뷰 객체에 연결
//            textview_re_comment_username =  vh.findViewById(R.id.textview_re_comment_username);
//            textview_re_comment_content =  vh.findViewById(R.id.textview_re_comment_content);
////            textview_re_comment_etc =  itemView.findViewById(R.id.textview_re_comment_etc);
//            ibtn_re_comment_edit =  vh.findViewById(R.id.ibtn_re_comment_edit);
//            ibtn_re_comment_delete =  vh.findViewById(R.id.ibtn_re_comment_delete);
////            ibtn_re_comment_add = vh.findViewById(R.id.ibtn_re_comment_add);
//
//            circleimage_re_comment = vh.findViewById(R.id.circleimage_re_comment);
//            // 클릭이벤트 추가
//            ibtn_re_comment_edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //TODO: 버튼 클릭시 수정
////                    v.getId();
//                    System.out.println(TAG+"ibtn_comment_edit");
//
//                }
//            });
//
//            ibtn_re_comment_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //TODO:버튼 클릭시 삭제
////                    v.getId();
//                    System.out.println(TAG+"ibtn_comment_delete");
//
//                }
//            });
//
////            ibtn_re_comment_add.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                    System.out.println(TAG+"ibtn_re_comment_add id : "+v.getId());
////
////                }
////            });
//
//        }
//    }

}
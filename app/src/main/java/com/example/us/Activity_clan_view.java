package com.example.us;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.UnsupportedEncodingException;

public class Activity_clan_view extends AppCompatActivity {
    private static final String TAG = Activity_clan_view.class.getName()+" : ";

    boolean is_master = false;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_view);


        //인텐트로 받은 내용 표시시
       Intent mIntent = getIntent();
       int master = mIntent.getIntExtra("master",0);
       id = mIntent.getIntExtra("id",0);
       String img = mIntent.getStringExtra("img");
       String intro = mIntent.getStringExtra("intro");
       String title = mIntent.getStringExtra("title");

        System.out.println("------------------------------------------------------------------------");
        System.out.println(TAG+" intent get master : "+master);
        System.out.println(TAG+" intent get id : "+id);
        System.out.println(TAG+" intent get img : "+img);
        System.out.println(TAG+" intent get intro : "+intro);
        System.out.println(TAG+" intent get title : "+title);
        System.out.println("------------------------------------------------------------------------");


        if(master == user_info.getInstance().getUser_index_number()){
            is_master = true;
            System.out.println(TAG+" is_master : "+is_master);
        }

        TextView textview_clan_view_intro = findViewById(R.id.textview_clan_view_intro);
        textview_clan_view_intro.setText(intro);

        TextView textview_clan_view_title = findViewById(R.id.textview_clan_view_title);
        textview_clan_view_title.setText(title);

        ImageView imageView_clan_view = findViewById(R.id.imageView_clan_view);




        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+img,imageView_clan_view);
        imageLoadTask.execute();



        ImageButton ibtn_clan_view_back = findViewById(R.id.ibtn_clan_view_back);
        ibtn_clan_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ImageButton ibtn_clan_setting = findViewById(R.id.ibtn_clan_setting);
        ibtn_clan_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                System.out.println(TAG+"이미지 버튼 클릭 : ibtn_clan_setting");
                //팝업 메뉴 생성
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);

                if(is_master){

                    //팝업 메뉴 뷰에 연결
                    popupMenu.getMenuInflater().inflate(R.menu.popup_clan_view, popupMenu.getMenu());

                    //메뉴 아이템에 클릭 리스너 추가
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //각 id값을 받아서 아이템 버튼 클릭 확인
                            if (item.getItemId() == R.id.menu_clan_view_join){
                                System.out.println("menu_clan_view_join");
                                //1번 메뉴 클릭시 동작 작성

                                Intent mIntent = new Intent(getApplicationContext(), Activity_clan_join_list.class);
                                mIntent.putExtra("id",id);
                                startActivity(mIntent);



                                return true;
                            }else if (item.getItemId() == R.id.menu_clan_view_member){
                                System.out.println("menu_clan_view_member");
                                //2번 메뉴 클릭시 동작 작성

                                return true;
                            }else if (item.getItemId() == R.id.menu_clan_view_edit) {

                                System.out.println("menu_clan_view_edit");
                                //3 번 메뉴 클릭시 동작 작성

                                return true;
                            }else if (item.getItemId() == R.id.menu_clan_view_setting) {

                                System.out.println("menu_clan_view_setting");
                                //3 번 메뉴 클릭시 동작 작성

                                return true;
                            }else if (item.getItemId() == R.id.menu_clan_view_del) {

                                System.out.println("menu_clan_view_del");
                                //3 번 메뉴 클릭시 동작 작성

                                //데이터 담아서 팝업(액티비티) 호출
                                AlertDialog.Builder ad = new AlertDialog.Builder(v.getContext());

                                ad.setTitle("클랜 해체");       // 제목 설정

                                // 확인 버튼 설정
                                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.v(TAG, "Yes Btn Click");
                                        // Event

                                        try {
                                            clan_delete(id);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

                                        dialog.dismiss();     //닫기

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



                                return true;
                            }
                            return false;
                        }
                    });
                }else {
                    //팝업 메뉴 뷰에 연결
                    popupMenu.getMenuInflater().inflate(R.menu.popup_clan_view_member, popupMenu.getMenu());

                    //메뉴 아이템에 클릭 리스너 추가
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //각 id값을 받아서 아이템 버튼 클릭 확인
                            if (item.getItemId() == R.id.menu_clan_view_member){
                                System.out.println("menu_clan_view_member");
                                //1번 메뉴 클릭시 동작 작성

                                return true;
                            }else if (item.getItemId() == R.id.menu_clan_view_out){
                                System.out.println("menu_clan_view_out");
//                                2번 메뉴 클릭시 동작 작성

                                //데이터 담아서 팝업(액티비티) 호출
                                AlertDialog.Builder ad = new AlertDialog.Builder(v.getContext());

                                ad.setTitle("클랜 탈퇴");       // 제목 설정

                                // 확인 버튼 설정
                                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.v(TAG, "Yes Btn Click");
                                        // Event

                                        try {
                                            member_out(id,user_info.getInstance().getUser_index_number());
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

                                        dialog.dismiss();     //닫기

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

//                                try {
//                                    member_out(id,user_info.getInstance().getUser_index_number());
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }

                                return true;
                            }
                            return false;
                        }
                    });
                }

                popupMenu.show();


            }
        });




    }


    private void member_out(int clan_id, int user_id) throws UnsupportedEncodingException {

        System.out.println("member_out_start----------------------------------------");
        System.out.println("clan_id : "+clan_id);
        System.out.println("user_id : "+user_id);
        System.out.println("--------------------------------------------------------------------------------");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.member_out(user_id,clan_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    String data = response.body();
                    if(data.equals("success")){
                        System.out.println("member_out: true");

                        finish();
                    }else {
                        System.out.println("member_out: "+data);
                    }
                }
                System.out.println("member_out 성공 call : "+call.toString());
                System.out.println("member_out 성공 response : "+response.toString());
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                System.out.println("member_out 실패 call : "+call.toString());
                System.out.println("member_out 실패 Throwable : "+t.toString());
            }
        });


    }

    private void clan_delete(int clan_id) throws UnsupportedEncodingException {

        System.out.println("member_out_start----------------------------------------");
        System.out.println("clan_id : "+clan_id);
        System.out.println("--------------------------------------------------------------------------------");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getClan_del(clan_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    String data = response.body();
                    if(data.equals("success")){
                        System.out.println("Clan_del: true");

                        finish();
                    }else {
                        System.out.println("Clan_del: "+data);
                    }
                }
                System.out.println("Clan_del 성공 call : "+call.toString());
                System.out.println("Clan_del 성공 response : "+response.toString());
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                System.out.println("Clan_del 실패 call : "+call.toString());
                System.out.println("Clan_del 실패 Throwable : "+t.toString());
            }
        });


    }


}
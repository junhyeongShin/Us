package com.example.us;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Activity_more extends AppCompatActivity {

    CircleImageView imageView_more_profile;
    TextView textView_more_id;
    TextView textView_more_name;

//    btn_more_my_board
//    btn_more_my_comment
//    btn_more_alarm

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();

        init();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        imageView_more_profile = findViewById(R.id.imageView_more_profile);
        textView_more_id = findViewById(R.id.textView_more_id);
        textView_more_name = findViewById(R.id.textView_more_name);


//        imageView_more_profile.setBackground(new ShapeDrawable(new OvalShape()));
//        imageView_more_profile.setClipToOutline(true);

        ImageButton btn_more_find_friend = findViewById(R.id.btn_more_find_friend);
        btn_more_find_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_friend, popupMenu.getMenu());
                System.out.println("친구 메뉴 활성화");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_friend_add){
                            System.out.println("친구 찾기 버튼 클릭");

                            Intent mIntent = new Intent(getApplicationContext(), Activity_friend_list.class);
                            startActivity(mIntent);

                            return true;
                        }else if (item.getItemId() == R.id.menu_friend_del){
                            System.out.println("친구 관리 버튼 클릭");

                            Intent mIntent = new Intent(getApplicationContext(), Activity_friend_list_del.class);
                            startActivity(mIntent);

                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();


            }
        });






        Button btn_more_profile = findViewById(R.id.btn_more_profile);
        btn_more_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_edit_userdata.class);
                startActivity(mIntent);
            }
        });

        Button btn_more_my_board = findViewById(R.id.btn_more_my_board);
        btn_more_my_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_my_board.class);
                startActivity(mIntent);
            }
        });

        Button btn_more_my_comment = findViewById(R.id.btn_more_my_comment);
        btn_more_my_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_my_comment.class);
                startActivity(mIntent);
            }
        });

        //하단 네비게이션바 버튼
        //각 버튼별로 이동할 엑티비티가 정해져있고, 지금 열려있는 엑티비는 버튼 리스너 없음.
        Button btn_bottom_home = findViewById(R.id.btn_bottom_home);
        btn_bottom_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_home.class);
                startActivity(mIntent);
                finish();

            }
        });
        Button btn_bottom_guild = findViewById(R.id.btn_bottom_guild);
        btn_bottom_guild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clan_check();
                finish();
            }
        });
        Button btn_bottom_chat = findViewById(R.id.btn_bottom_chat);
        btn_bottom_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_chat.class);
                startActivity(mIntent);
                finish();
            }
        });
        Button btn_bottom_board = findViewById(R.id.btn_bottom_board);
        btn_bottom_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_board.class);
                startActivity(mIntent);
                finish();
            }
        });

    }

    //액티비티 실행시 필요한 데이터 (유저 아이디, 이름, 자기소개, 프로필 사진)
    //요청해서 각 레이아웃에 적용
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void init(){


        final Post_to_server post_to_server =new Post_to_server();


        // 서버에 데이터 요청 후, 각 이미지 및 텍스트에 적용하는 쓰레드
        new Thread(new Runnable() {
            @Override
            public void run() {

                String user_img_path = "";

                //포스트로 데이터 요청.
                String result_user_data = post_to_server.post_data_get_id("/Data/user_data_get.php",user_info.getInstance().getUser_ID());

                try {
                    JSONObject jsonObject = new JSONObject(result_user_data);
                    String result_img_data_decode =  jsonObject.getString("result_img");
                    JSONObject jsonObject_img_decode = new JSONObject(result_img_data_decode);


                    //이미지 부분 json 파싱된 uri를 이용해 이미지 표시
                    user_img_path = jsonObject_img_decode.getString("img_path");

                    ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+user_img_path,imageView_more_profile);
                    imageLoadTask.execute();

                    System.out.println("img url : " + Uri.parse(server_info.getInstance().getURL()+"/Data/img_file/"+user_img_path));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        textView_more_id.setText("ID : " + user_info.getInstance().getUser_ID());
        textView_more_name.setText("name : " + user_info.getInstance().getUser_name());


    }

    void clan_check(){
        int user_id = user_info.getInstance().getUser_index_number();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);


        retrofit_api.getClan_check(user_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String data = response.body();
                    assert data != null;
                    if(data.equals("true")){

                        System.out.println("getClan_check: true");

                        Intent mIntent = new Intent(getApplicationContext(), Activity_clan.class);
                        startActivity(mIntent);
                    }else {
                        System.out.println("getClan_check: "+data);

                        Intent mIntent = new Intent(getApplicationContext(), Activity_no_clan.class);
                        startActivity(mIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("getClan_check 실패 call : "+call.toString());
                System.out.println("getClan_check 실패 Throwable : "+t.toString());
            }
        });


    }
}
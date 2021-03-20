package com.example.us;
import android.net.Uri;
import android.os.Build;
import android.widget.*;
import android.content.Intent;

import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.request.JsonObjectRequest;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONException;
import org.json.JSONObject;

public class Activity_edit_userdata extends AppCompatActivity {

    private static final String TAG = "Activity_edit_userdata";
    String server_info_url = server_info.getInstance().getURL();
    String user_id = user_info.getInstance().getUser_ID();
    CircleImageView imageView_edit;

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println(TAG+"onStop");

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(TAG+"onResume");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();

        init();

        System.out.println(TAG+"onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userdata);

        imageView_edit = findViewById(R.id.imageView_edit);

        ImageButton ibtn_edit_back = findViewById(R.id.ibtn_edit_back);
        ibtn_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_edit_profile.class);
                startActivity(mIntent);
            }
        });

        Button button = findViewById(R.id.btn_edit_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext_edit_new_pw = findViewById(R.id.edittext_edit_new_pw);
                EditText edittext_edit_new_pw_check = findViewById(R.id.edittext_edit_new_pw_check);
                EditText edittext_edit_old_pw =findViewById(R.id.edittext_edit_old_pw);

                final String new_pw = edittext_edit_new_pw.getText().toString();
                String new_pw_check = edittext_edit_new_pw_check.getText().toString();

                final String old_pw = edittext_edit_old_pw.getText().toString();

                // 기존 비밀번호도 일치하고, 새로운 비밀번호도 일치할때
                if(new_pw.equals(new_pw_check)){

                    final Post_to_server post_to_server_update = new Post_to_server();

                    new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    post_to_server_update.post_data_id("/Data/user_pw_update.php",user_info.getInstance().getUser_ID(), "user_pw",new_pw);
                                }
                            }).start();

                    edittext_edit_new_pw_check.setText("");
                    edittext_edit_new_pw.setText("");
                    edittext_edit_old_pw.setText("");

                    Toast.makeText(getApplicationContext(), "비밀번호가 변경 되었습니다.",Toast.LENGTH_SHORT).show();



                }
                // 예외처리 : 기존 비밀번호만 일치하고, 새로운 비밀번호는 일치하지 않을때
                else {
                    Toast.makeText(getApplicationContext(), "새로운 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
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

                String user_name = "";
                String user_intro = "";
                String user_img_path = "";

                //포스트로 데이터 요청.
                String result_user_data = post_to_server.post_data_get_id("/Data/user_data_get.php",user_id);

                System.out.println(TAG+"init : user_id : "+ user_id);

                try {
                    JSONObject jsonObject = new JSONObject(result_user_data);
                    String result_user_data_decode =  jsonObject.getString("result_data");
                    JSONObject jsonObject_decode = new JSONObject(result_user_data_decode);
                    user_name = jsonObject_decode.getString("username");
                    user_intro = jsonObject_decode.getString("intro_profile");

                    TextView TextView_edit_id = findViewById(R.id.TextView_edit_id);
                    TextView_edit_id.setText("ID : "+user_id);

                    TextView TextView_edit_name = findViewById(R.id.TextView_edit_name);
                    TextView_edit_name.setText("닉네임 : "+user_name);

                    TextView TextView_edit_detail = findViewById(R.id.TextView_edit_detail);
                    TextView_edit_detail.setText("소개 : "+user_intro);

                    String result_img_data_decode =  jsonObject.getString("result_img");
                    JSONObject jsonObject_img_decode = new JSONObject(result_img_data_decode);


                    //이미지 부분 json 파싱된 uri를 이용해 이미지 표시
                    user_img_path = jsonObject_img_decode.getString("img_path");

                    ImageLoadTask imageLoadTask = new ImageLoadTask(server_info_url+"/Data/img_file/"+user_img_path,imageView_edit);
                    imageLoadTask.execute();

                    System.out.println("img url : " + Uri.parse(server_info_url+"/Data/img_file/"+user_img_path));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }
}
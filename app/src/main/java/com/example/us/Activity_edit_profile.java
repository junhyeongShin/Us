package com.example.us;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// 1. 이미지 변경버튼 클릭시 이미지 선택 창 startActivityForResult로 열어서 이미지 선택 (비트맵 형식)
// 1-1. 이미지 선택시 imageView_edit_profile에 미리보기
// 2. 프로필 닉네임, 자기소개 수정
// 2-1. 닉네임 및 자기소개 Edittext에 수정
// 3. 수정완료 버튼 클릭시 일괄적으로 서버에 update 요청
//

public class Activity_edit_profile extends AppCompatActivity {

    ImageView imageView_edit_profile;
    Boolean img_update = false;
    String img_path;
    int img_index;

    server_info server_info = new server_info();

    String server_info_url = server_info.getURL();

    EditText  Edittext_edit_profile_name ;
    EditText  Edittext_edit_profile_intro ;
    String tmp_img = "5";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Edittext_edit_profile_name = findViewById(R.id.Edittext_edit_profile_name);
        Edittext_edit_profile_intro = findViewById(R.id.Edittext_edit_profile_intro);

        imageView_edit_profile = findViewById(R.id.imageView_edit_profile);

        final user_info user_info = com.example.us.user_info.getInstance();

        TextView TextView_edit_profile_id = findViewById(R.id.TextView_edit_profile_id);
        TextView_edit_profile_id.setText("ID : "+user_info.user_ID);

        TextView TextView_edit_profile_name = findViewById(R.id.TextView_edit_profile_name);
        TextView_edit_profile_name.setText("닉네임 : "+user_info.user_name);
        Edittext_edit_profile_name.setText(user_info.user_name);

        TextView TextView_edit_profile_detail = findViewById(R.id.TextView_edit_profile_detail);
        TextView_edit_profile_detail.setText("소개 : "+user_info.user_intro_profile);
        Edittext_edit_profile_intro.setText(user_info.user_intro_profile);


        Button  btn_edit_profile_img = findViewById(R.id.btn_edit_profile_img);
        btn_edit_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });


        Button btn_edit_profile_end = findViewById(R.id.btn_edit_profile_end);
        btn_edit_profile_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(img_update){
//                    imageUpload(img_path);
//                }

                        //리스너 생성
                        Response.Listener<String> responseListener = new Response.Listener<String>() {  //통신이 완료된후 다음 작업

                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    String message = jObj.getString("message");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    // JSON error
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        };

                        String img_index_to_string = String.valueOf(img_index);

                        db_data_update db_data_update = new db_data_update("/Data/user_data_edit.php","column","content",responseListener);  //request 클래스 가져오기. 보내는 데이터 .
                        RequestQueue queue = Volley.newRequestQueue(Activity_edit_profile.this);
                        queue.add(db_data_update); // 요청 보내기.
                        System.out.println("queue.add(db_data_update)");

//                finish();

            }
        });

        //상단 툴바 <- 버튼 클릭시
        ImageButton ibtn_edit_profile_back = findViewById(R.id.ibtn_edit_profile_back);
        ibtn_edit_profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView_edit_profile.setImageBitmap(img);
                    img_update = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        int index = 0;
        String[] proj = {MediaStore.Images.Media.DATA}; // 이미지 경로로 해당 이미지에 대한 정보를 가지고 있는 cursor 호출
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null); // 데이터가 있으면(가장 처음에 위치한 레코드를 가리킴)
        if (cursor.moveToFirst()) { // 해당 필드의 인덱스를 반환하고, 존재하지 않을 경우 예외를 발생시킨다.
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        Log.d("getRealPathFromURI", "getRealPathFromURI: " + cursor.getString(index));
        return cursor.getString(index);
    }

    private void imageUpload(final String imagePath) {
        System.out.println("imageUpload start");
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, server_info_url+"/Data/img_file/img_upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        smr.addFile("image", imagePath);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smr);


        System.out.println("imageUpload end");
    }
}
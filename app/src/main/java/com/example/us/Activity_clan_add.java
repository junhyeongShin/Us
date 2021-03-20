package com.example.us;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.InputStream;
import java.util.HashMap;

public class Activity_clan_add extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = Class.class.getName()+" : ";
    ImageView imageView_clan_add;
    Boolean img_update = false;
    String img_path;

    server_info server_info = new server_info();

    String server_info_url = server_info.getURL();

    EditText Edittext_clan_add_name;
    EditText Edittext_clan_add_intro;

    String category_selected ;



    @Override
    protected void onResume() {
        super.onResume();


//        imageView_edit_profile.setBackground(new ShapeDrawable(new OvalShape()));
//        imageView_edit_profile.setClipToOutline(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_add);

        imageView_clan_add = findViewById(R.id.imageView_clan_add);

        Edittext_clan_add_name = findViewById(R.id.Edittext_clan_add_name);
        Edittext_clan_add_intro = findViewById(R.id.Edittext_clan_add_intro);

        //최상단 카테고리 리스너
        //어뎁터를 이용해서 value - string - spinner_category 지정해놓은 값 불러옴.
        Spinner spinner_board_add_category = findViewById(R.id.spinner_board_add_category);
        ArrayAdapter<CharSequence> adapter_spinner = ArrayAdapter.createFromResource(this,
                R.array.spinner_category, R.layout.spinner_item_category);
        adapter_spinner.setDropDownViewResource(R.layout.spinner_dropdown_category);
        spinner_board_add_category.setAdapter(adapter_spinner);
        spinner_board_add_category.setOnItemSelectedListener(this);

        Button btn_clan_add_img = findViewById(R.id.btn_clan_add_img);
        btn_clan_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);
            }
        });


        Button btn_clan_add_input = findViewById(R.id.btn_clan_add_input);
        btn_clan_add_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("클랜 생성 버튼 클릭");

                //프로필 수정 완료 버튼을 누르면
                // 1. 이미지 업데이트 요청
                // 2. 닉네임, 소개 업데이트 요청

                Thread thread_url_imageUpload = new Thread(){

                    public void run(){
                        try{
                            imageUpload(img_path);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                thread_url_imageUpload.start();

                try {
                    thread_url_imageUpload.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        //상단 툴바 <- 버튼 클릭시
        ImageButton ibtn_clan_add_back = findViewById(R.id.ibtn_clan_add_back);
        ibtn_clan_add_back.setOnClickListener(new View.OnClickListener() {
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
                    imageView_clan_add.setImageBitmap(img);

                    // 이미지 업로드 준비
                    System.out.println("data.getData() : "+data.getData());
                    img_update = true;
                    img_path = getRealPathFromURI(data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //업로드 하기위한 img_path로 변경
    public String getRealPathFromURI(Uri uri) {
        int index = 0;
        System.out.println("getRealPathFromURI uri : "+uri);
        String[] proj = {MediaStore.Images.Media.DATA}; // 이미지 경로로 해당 이미지에 대한 정보를 가지고 있는 cursor 호출
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null); // 데이터가 있으면(가장 처음에 위치한 레코드를 가리킴)
        if (cursor.moveToFirst()) { // 해당 필드의 인덱스를 반환하고, 존재하지 않을 경우 예외를 발생시킨다.
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        Log.d("getRealPathFromURI", "getRealPathFromURI: " + cursor.getString(index));
        return cursor.getString(index);
    }

    private void imageUpload(final String imagePath) {
        //이미지 업로드용 멀티파트 요청
        //서버에서 받은 요청중 이미지 index값을 user 데이터에 업데이트

        System.out.println("imageUpload start");
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, server_info_url+"/Data/img_file/img_upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONObject jObj_response = new JSONObject(response);
                            String message = jObj_response.getString("result_index");

                            JSONObject jObj_max = new JSONObject(message);
                            final String max_img_id = jObj_max.getString("MAX(id)");
                            System.out.println("max_img_id"+max_img_id);

                            if(Edittext_clan_add_name.getText().toString().equals("")){
                                System.out.println("클랜명을 입력하세요.");
                                Toast.makeText(getApplicationContext(), "클랜명을 입력하세요.",Toast.LENGTH_SHORT).show();
                            }else {

                                //카테고리 (스피너에서 받아옴)
                                final String category = category_selected;


                                System.out.println("클랜추가 레트로핏 시작.");

                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(com.example.us.server_info.getInstance().getURL())
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .build();

                                Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

                                HashMap<String, Object> input = new HashMap<>();
                                input.put("user_id", user_info.getInstance().getUser_index_number());
                                input.put("clan_name", Edittext_clan_add_name.getText().toString());
                                input.put("clan_intro", Edittext_clan_add_intro.getText().toString());
                                input.put("clan_category", category);
                                input.put("clan_img", max_img_id);
                                System.out.println("input : " + input);

                                retrofit_api.postData_clan_add(input).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                        if (response.isSuccessful()) {
                                            String data = response.body();
                                            System.out.println("성공");
                                            System.out.println(data);

                                            finish();
                                        }
                                        System.out.println("성공 response : " + response);
                                        System.out.println("성공 call : " + call);
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        System.out.println("실패 : call : " + call);
                                        System.out.println("실패 : Throwable : " + t);
                                    }
                                });


                            }




                        } catch (JSONException e) {
                            // JSON error
                            System.out.println("이미지 업로드 실패 e : "+e.toString());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // 0 : 파오캐
        // 1 : 카오스
        // 2 : 원랜디
        // 3 : 뿔레전쟁
        // 4 : 기타

        if (id == 0) {
            category_selected = "파오캐";
        } else if (id == 1) {
            category_selected = "카오스";
        } else if (id == 2) {
            category_selected = "원랜디";
        } else if (id == 3) {
            category_selected = "뿔레";
        } else if (id == 4){
            category_selected = "기타";
        }

        System.out.println(TAG + " : onItemSelected category_selected : " + category_selected);
//        System.out.println(TAG+" : onItemSelected position : "+position);
        System.out.println(TAG + " : onItemSelected id : " + id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
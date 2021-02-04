package com.example.us;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Activity_board_view extends AppCompatActivity {

    private static final String TAG = "Activity_board_view - ";

    //게시판 정보
    int id;
    String title;
    String content;

    //유저 정보
    int user_id;
    String user_name;
    String user_email;
    String user_intro;
    String user_img;

    //
    CircleImageView imageview_board_view_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view);

        Intent mIntent =getIntent();
        id = mIntent.getIntExtra("id",0);
        title = mIntent.getStringExtra("title");
        content = mIntent.getStringExtra("content");

        user_id = mIntent.getIntExtra("user_id",0);
        user_name = mIntent.getStringExtra("user_name");
        user_email = mIntent.getStringExtra("user_email");
        user_intro = mIntent.getStringExtra("user_intro");
        user_img = mIntent.getStringExtra("user_img");

        ImageButton ibtn_board_view_back = findViewById(R.id.ibtn_board_view_back);
        ibtn_board_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView textview_board_view_id = findViewById(R.id.textview_board_view_id);
        TextView textview_board_view_title = findViewById(R.id.textview_board_view_title);
        TextView textview_board_view_name = findViewById(R.id.textview_board_view_name);

        user_email = "ID : "+user_email;
        textview_board_view_id.setText(user_email);
        textview_board_view_title.setText(title);
        user_name = "닉네임 : " + user_name;
        textview_board_view_name.setText(user_name);


        WebView webView = findViewById(R.id.webview);

        String base_html =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                        "<html><head>"+
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                        "<head><body>";

        content = base_html + content;
        content += "</body></html>";

        webView.loadData(content, "text/html; charset=utf-8", "UTF-8");


        imageview_board_view_profile = findViewById(R.id.imageview_board_view);


        try {
            retrofit_api(user_img);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void retrofit_api(String user_img) throws UnsupportedEncodingException {

        final String user_img_try = user_img;

        System.out.println(TAG+" - retrofit_api : start");
        System.out.println(TAG+" - retrofit_api_url : "+server_info.getInstance().getURL());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getData_img(Integer.parseInt(user_img)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    String img_path = response.body();
                    System.out.println("img_path : "+img_path);
                    System.out.println("성공");

                    // 선택한 이미지
                    ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL()+"/Data/img_file/"+img_path,imageview_board_view_profile);
                    imageLoadTask.execute();


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }
}
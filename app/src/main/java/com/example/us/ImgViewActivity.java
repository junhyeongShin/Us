package com.example.us;
import android.content.Intent;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ImgViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_view);

        Intent mIntent = getIntent();
        String img_name = mIntent.getStringExtra("img_view");

        ImageView imageView_img_view = findViewById(R.id.imageView_img_view);

        ImageButton ibtn_img_view = findViewById(R.id.ibtn_img_view);
        ibtn_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });

        //이미지 적용 클래스 사용
        //유저리스트의 이미지 (String) 으로 적용시킴
        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() + "/Data/img_file/"+
                img_name,
                imageView_img_view
        );
        imageLoadTask.execute();
    }
}
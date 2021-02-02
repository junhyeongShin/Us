package com.example.us;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    ImageView imageView_test_img;
    String img_path;

    private RichEditor mEditor;
    private TextView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);
        retrofit_api.getData("1").enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> data = response.body();
                    System.out.println("성공");
                    System.out.println(data.get(0).getTitle());
                }


            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                System.out.println("실패");
            }
        });

        HashMap<String, Object> input = new HashMap<>();
        input.put("userId",1);
        input.put("title","title title");
        input.put("body","body body 당근 당근");
        retrofit_api.postData(input).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()){
                    Post data = response.body();
                    System.out.println("성공");
                    System.out.println(data.getClass());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }

}
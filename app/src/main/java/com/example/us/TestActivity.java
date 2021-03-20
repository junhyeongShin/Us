package com.example.us;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jp.wasabeef.richeditor.RichEditor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    ImageView imageView_test_img;
    String img_path;

    private RichEditor mEditor;
    private TextView mPreview;

    private ArrayList<comment> mData = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view);



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getData_comment_list(11).enqueue(new Callback<List<comment>>() {
            @Override
            public void onResponse(Call<List<comment>> call, Response<List<comment>> response) {
                System.out.println("onResponse : call" + call);
                System.out.println("onResponse : response" + response);
                if (response.isSuccessful()) {
                    List<comment> board_list = response.body();
                    System.out.println("성공");

                    for (int i = 0; i < board_list.size(); i++) {
                        System.out.println("ID : " + board_list.get(i).getId());
                    }
                    mData = (ArrayList<comment>) board_list;

                    RecyclerView recyclerView = findViewById(R.id.recyclerview_board_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplication().getBaseContext()));
                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(mData);
                    recyclerView.setAdapter(recyclerAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<comment>> call, Throwable t) {
                System.out.println("getData_comment_list 실패 Throwable : " + t.toString());
                System.out.println("getData_comment_list 실패 call : " + call.toString());
            }
        });



    }

}


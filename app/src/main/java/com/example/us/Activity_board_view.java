package com.example.us;
import android.app.ProgressDialog;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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

    TextView textview_board_view_id ;
    TextView textview_board_view_title ;
    TextView textview_board_view_name ;
    Board_view_item Board_view_item;
    ImageButton menu_btn_board_view;

    public ProgressDialog loading_dialog;
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;


    boolean is_find ;
    int position ;
    int is_recomment ;
    boolean is_loading_comment = false;
    boolean is_loading_data = false;

    //게시판 정보
    int id;
    String title;
    String content;
    String category;

    //유저 정보
    int user_id;
    String user_name;
    String user_email;
    String user_intro;
    String user_img;

    //
    CircleImageView imageview_board_view_profile;
    Context context;

    private ArrayList<comment> mData = null ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view);

        context = Activity_board_view.this.getBaseContext();

        initDialog();
        show_dialog();

        Intent mIntent =getIntent();
        id = mIntent.getIntExtra("id",0);
        position = mIntent.getIntExtra("point",0);
        is_recomment = mIntent.getIntExtra("is_recomment",0);

        System.out.println(TAG+"point : "+position);
        System.out.println(TAG+"is_recomment : "+is_recomment);

        if(position!=0){
           is_find = true;
        }


//        title = mIntent.getStringExtra("title");
//        content = mIntent.getStringExtra("content");
//
//        user_id = mIntent.getIntExtra("user_id",0);
//        user_name = mIntent.getStringExtra("user_name");
//        user_email = mIntent.getStringExtra("user_email");
//        user_intro = mIntent.getStringExtra("user_intro");
//        user_img = mIntent.getStringExtra("user_img");

        ImageButton ibtn_board_view_back = findViewById(R.id.ibtn_board_view_back);
        ibtn_board_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textview_board_view_id      = findViewById(R.id.textview_board_view_id);
        textview_board_view_title = findViewById(R.id.textview_board_view_title);
        textview_board_view_name    = findViewById(R.id.textview_board_view_name);
        imageview_board_view_profile = findViewById(R.id.imageview_board_view);



        //댓글추가버튼
        Button btn_board_view_add_comment = findViewById(R.id.btn_board_view_add_comment);
        btn_board_view_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), Activity_comment_add.class);

                mIntent.putExtra("board_id",id);
                mIntent.putExtra("title",title);

                startActivity(mIntent);

            }
        });

        //        // 클릭이벤트 추가
        menu_btn_board_view = findViewById(R.id.menu_btn_board_view);

        menu_btn_board_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("댓글 메뉴 버튼 클릭");

                    final PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_board_writer, popupMenu.getMenu());
                    System.out.println("작성자 와 접속자 동일");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.board_edit){
                                System.out.println(TAG+"ibtn_comment_edit");

                                int index = id;

                                int user_id = user_info.getInstance().getUser_index_number();

                                Intent mIntent = new Intent(v.getContext(), Activity_board_edit.class);
                                mIntent.putExtra("id", index);
                                System.out.println("id : "+index);
                                mIntent.putExtra("title", title);
                                System.out.println("title : "+title);
                                mIntent.putExtra("content", content);
                                System.out.println("content : "+content);
                                mIntent.putExtra("category", category);
                                System.out.println("category : "+category);

                                mIntent.putExtra("user_id", user_id);
                                System.out.println("user_id : "+user_id);
                                mIntent.putExtra("user_name", user_name);
                                System.out.println("user_name : "+user_name);
                                mIntent.putExtra("user_email", user_email);
                                System.out.println("user_email : "+user_email);
                                mIntent.putExtra("user_intro", user_intro);
                                System.out.println("user_intro : "+user_intro);
                                mIntent.putExtra("user_img", user_img);
                                System.out.println("user_img : "+user_img);

                                v.getContext().startActivity(mIntent);

                                return true;
                            }else if (item.getItemId() == R.id.board_del){
                                delete_item(v);
                                System.out.println(TAG+"ibtn_comment_delete");

                                return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();



            }
        });

        recyclerView = findViewById(R.id.recyclerview_board_view);


    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            retrofit_api();
            retrofit_comment();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void retrofit_api() throws UnsupportedEncodingException {

        System.out.println(TAG+" - retrofit_api : start");
        System.out.println(TAG+" - retrofit_api_url : "+server_info.getInstance().getURL());
        System.out.println(TAG+" - retrofit_api_id : "+id);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getData_board_view(id).enqueue(new Callback<List<Board_view_item>>() {
            @Override
            public void onResponse(Call<List<Board_view_item>> call, Response<List<Board_view_item>> response) {
                System.out.println("onResponse : call"+call);
                System.out.println("onResponse : response"+response);
                if(response.isSuccessful()){
                    List<Board_view_item> list = response.body();
                    Board_view_item = list.get(0);

                    //게시판 정보
                    title = Board_view_item.getTitle();
                    content = Board_view_item.getContent();
                    category = Board_view_item.getCategory();

                    //유저 정보
                    user_id = Board_view_item.getUser_id();
                    user_name = Board_view_item.getUser_name();
                    user_email = Board_view_item.getUser_email();
                    user_img = Board_view_item.getUser_img();

                    init();
                }
            }

            @Override
            public void onFailure(Call<List<Board_view_item>> call, Throwable t) {
                System.out.println("실패 Throwable : "+t.toString());
                System.out.println("실패 call : "+call.toString());
            }
        });

    }

    private void retrofit_comment(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_info.getInstance().getURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        retrofit_api.getData_comment_list(id).enqueue(new Callback<List<comment>>() {
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

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerAdapter = new RecyclerAdapter(mData);
                    recyclerView.setAdapter(recyclerAdapter);

                    is_loading_comment = true;
                    find_item();

                }
            }

            @Override
            public void onFailure(Call<List<comment>> call, Throwable t) {
                System.out.println("getData_comment_list 실패 Throwable : " + t.toString());
                System.out.println("getData_comment_list 실패 call : " + call.toString());
            }
        });
    }

    public void init(){

//        user_email,user_name,title,content,

        user_email = "ID : "+user_email;
        textview_board_view_id.setText(user_email);
        textview_board_view_title.setText(title);
        user_name = "닉네임 : " + user_name;
        textview_board_view_name.setText(user_name);


        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("데이터 로드 완료");
                hide_dialog();
                is_loading_data = true;
                find_item();
            }

        });

        String base_html =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                        "<html><head>"+
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                        "<head><body>";

        content = base_html + content;
        content += "</body></html>";

        webView.loadData(content, "text/html; charset=utf-8", "UTF-8");

        // 선택한 이미지
//        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL()+"/Data/img_file/"+Board_view_item.getUser_img(),imageview_board_view_profile);
//        imageLoadTask.execute();
        Glide.with(imageview_board_view_profile.getContext()).load(server_info.getInstance().getURL_IMG()+Board_view_item.getUser_img()).into(imageview_board_view_profile);
        System.out.println("imageview_board_view : "+Board_view_item.getUser_img());

        if(user_info.getInstance().getUser_index_number()==user_id){
            menu_btn_board_view.setVisibility(View.VISIBLE);
        } else {
            menu_btn_board_view.setVisibility(View.GONE);
        }




//        final Handler handler = new Handler();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //TODO 코드작성해서 사용
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        if(is_find){
//                            hide_dialog();
//                            find_item();
//                        }else {
//                            hide_dialog();
//                        }
//                        System.out.println("init 쓰레드 end");
//
//                    }
//                });
//            }
//        }).start();

        System.out.println("init end");


    }

    public void delete_item(View v){


        //데이터 담아서 팝업(액티비티) 호출
        AlertDialog.Builder ad = new AlertDialog.Builder(Activity_board_view.this);

        ad.setTitle("게시물 삭제");

        ad.setMessage("삭제된 게시물은 복구할 수 없습니다.");// 제목 설정

        // 확인 버튼 설정
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "Yes Btn Click");

                dialog.dismiss();     //닫기
                // Event

                System.out.println(getClass().getName()+"-delete_item- ");
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(server_info.getInstance().getURL())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

                retrofit_api.getData_del_board(id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            String data = response.body();
                            System.out.println("성공");
                            System.out.println(data);
                            Toast.makeText(getApplicationContext(), "게시물이 삭제 되었습니다",Toast.LENGTH_SHORT).show();

                            onBackPressed();

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


    //다이얼로그 생성
    protected void initDialog() {
        loading_dialog = new ProgressDialog(Activity_board_view.this);
        loading_dialog.setMessage("loading");
        loading_dialog.setCancelable(true);
    }

    //다이얼로그 보여주는거
    protected void show_dialog() {

        if (!loading_dialog.isShowing()) {
            loading_dialog.show();
            System.out.println("show_dialog");
        }

    }

    //다이얼로그 삭제
    protected void hide_dialog() {

        if (loading_dialog.isShowing()) {
            loading_dialog.dismiss();
            System.out.println("hide_dialog");
        }
    }

//    내가쓴 댓글을 찾아주는 메소드
//    # 댓글과 게시판 모두 로드가 된 상태에서 동작해야한다.

//    동작 설명
//    oncreate 에서 받아준 intent 값 position, is_recomment(is_re) 를 통해서 찾아준다
//
//    댓글 찾기
//    1. is_re로 댓글인지 대댓글인지 구분을 해준다.
//    2. 댓글인경우 댓글 어뎁터에 position값을 넣어줘서 바로 찾아준다.
//
//    대댓글 찾기
//    1. is_re로 구분
//    2. 대댓글의 경우에는 대댓글의 어뎁터들을 리스트에 저장해서 가지고 있어야한다.
//    3. 어뎁터 리스트중에서 해당 포지션을 가지고 있는 어뎁터를 추출
//    4. 어뎁터의 포지션으로 이동해준다.
//

    void find_item(){
//        position
//        is_recomment 를 확인해서 댓글인지 대댓글인지 구분.

        if (loading_dialog.isShowing()){
            return;
        }

        System.out.println("find item");
        System.out.println("find item position : "+position);
        System.out.println("find item is_recomment : "+is_recomment);

            if(is_find){
                System.out.println("is_find : true");
                if(is_recomment==0){
                    System.out.println("is_recomment : 0");
                    for(int i=0; i<mData.size(); i++) {
                        if(mData.get(i).getId()==position){

                            System.out.println("댓글 위치 찾음 : "+i);
                            recyclerView.scrollToPosition(i);

                            break;
                        }
                    }
                }else {
                    System.out.println("is_recomment : "+is_recomment);
                    recyclerAdapter.re_position = position;
                }
            }

    }
}
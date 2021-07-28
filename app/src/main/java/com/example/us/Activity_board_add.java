package com.example.us;
import android.widget.Toast;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import jp.wasabeef.richeditor.RichEditor;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Activity_board_add extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Activity_board_add : ";
    String server_info_url = server_info.getInstance().getURL();
    RichEditor mEditor;
    String img_path;
    String video_path;
    EditText edit_text_board_add_title;
    String category_selected ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_add);



        //최상단 카테고리 리스너
        //어뎁터를 이용해서 value - string - spinner_category 지정해놓은 값 불러옴.
        Spinner spinner_board_add_category = findViewById(R.id.spinner_board_add_category);
        ArrayAdapter<CharSequence> adapter_spinner = ArrayAdapter.createFromResource(this,
                R.array.spinner_category_add, R.layout.spinner_item_category);
        adapter_spinner.setDropDownViewResource(R.layout.spinner_dropdown_category);
        spinner_board_add_category.setAdapter(adapter_spinner);
        spinner_board_add_category.setOnItemSelectedListener(this);


        //툴바 뒤로가기 버튼
        ImageButton ibtn_board_add_back = findViewById(R.id.ibtn_board_add_back);
        ibtn_board_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //게시물 추가 버튼
        //1. 제목 작성했는지 확인
        //2. 서버로 제목, 카테고리, 내용 DB insert 요청 POST
        //3. 결과값이 OK일경우 액티비티 종료 및 토스트창
        //3-1. 실패인경우 토스트창만 띄움

        final Button btn_board_add = findViewById(R.id.btn_board_add);
        btn_board_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(TAG+"btn_board_add Onclick");

                // 제목 카테고리 내용 받아오기
                // 제목
                edit_text_board_add_title = findViewById(R.id.edit_text_board_add_title);
                final String title = edit_text_board_add_title.getText().toString();

                //카테고리 (스피너에서 받아옴)
                final String category = category_selected;

                // 내용
                mEditor.focusEditor();
                final String content = mEditor.getHtml();


                if(title.equals("")){
                   Toast.makeText(getApplicationContext(), "제목을 입력하세요.",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "게시물 등록 중.",Toast.LENGTH_SHORT).show();

//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl(server_info_url)
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//
//                    Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);
//
//
//                    HashMap<String, Object> input = new HashMap<>();
//                    input.put("user_id",user_info.getInstance().user_index_number);
//                    input.put("title",title);
//                    input.put("content",content);
//                    input.put("category",category);
//
//                    System.out.println(input);
//
//                    retrofit_api.postData_board(input).enqueue(new Callback<Post>() {
//                        @Override
//                        public void onResponse(Call<Post> call, retrofit2.Response<Post> response) {
//                            if(response.isSuccessful()){
//                                Post data = response.body();
//                                System.out.println("성공");
//                                System.out.println(response.body());
//                                System.out.println(response.message());
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Post> call, Throwable t) {
//                            System.out.println("실패");
//                            System.out.println("Throwable : "+t);
//                        }
//                    });



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Post_to_server post_to_server = new Post_to_server();
                System.out.println(TAG+"post_board_add url : "+server_info_url+"/Data/board_add.php");
                String post_result = post_to_server.post_board_add("/Data/board_add.php",user_info.getInstance().getUser_index_number(),title,category,content);
                System.out.println(TAG+"post_result : "+post_result);
                try {
                    JSONObject jsonObject_board_add = new JSONObject(post_result);
                    System.out.println(TAG+"jsonObject_board_add : "+jsonObject_board_add);
                    String post_result_decode = jsonObject_board_add.getString("result_check");
                    if(post_result_decode.equals("OK")){
                        System.out.println("게시물 등록 성공");
//                        Toast.makeText(getApplicationContext(), "게시물 등록 성공.",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else {
//                        Toast.makeText(getApplicationContext(), "게시물 등록 실패",Toast.LENGTH_SHORT).show();
                        System.out.println("게시물 등록 실패");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finish();

                }
            }
        });

        mEditor = (RichEditor) findViewById(R.id.editor);
//        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
//        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("내용을 작성하세요.");
//        mEditor.setInputEnabled(false);

//        mPreview = (TextView) findViewById(R.id.preview);
//        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
//            @Override
//            public void onTextChange(String text) {
//                mPreview.setText(text);
//            }
//        });
//
//        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.undo();
//            }
//        });
//
//        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.redo();
//            }
//        });
//
        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });
//
//        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setSubscript();
//            }
//        });
//
//        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setSuperscript();
//            }
//        });
//
//        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setStrikeThrough();
//            }
//        });
//
        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });
//
        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });
//
//        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setHeading(2);
//            }
//        });
//
//        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setHeading(3);
//            }
//        });
//
//        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setHeading(4);
//            }
//        });
//
//        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setHeading(5);
//            }
//        });
//
        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });
//
        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.BLUE);
                isChanged = !isChanged;
            }
        });
//
//        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
//            private boolean isChanged;
////
//            @Override
//            public void onClick(View v) {
//                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
//                isChanged = !isChanged;
//            }
//        });
//
//        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setIndent();
//            }
//        });
//
//        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setOutdent();
//            }
//        });
//
        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });
//
//        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setBlockquote();
//            }
//        });
//
//        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setBullets();
//            }
//        });
//
//        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.setNumbers();
//            }
//        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);

            }
        });

//        findViewById(R.id.action_insert_youtube).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //데이터 담아서 팝업(액티비티) 호출
//                AlertDialog.Builder ad = new AlertDialog.Builder(Activity_board_add.this);
//
//                ad.setTitle("유튜브 링크 삽입하기");       // 제목 설정
//
//                // EditText 삽입하기
//                final EditText et_link = new EditText(Activity_board_add.this);
//                ad.setView(et_link);
//
//                // 취소 버튼 설정
//                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.v(TAG, "No Btn Click");
//                        dialog.dismiss();     //닫기
//                        // Event
//                    }
//                });
//
//                // 확인 버튼 설정
//                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.v(TAG, "Yes Btn Click");
//
//                        // Text 값 받아서 로그 남기기
//                        String value = et_link.getText().toString();
//                        Log.v(TAG, value);
//
//                        dialog.dismiss();     //닫기
//                        // Event
//                        mEditor.focusEditor();
//                        mEditor.insertYoutubeVideo(value,320);
//                    }
//                });
//
//
//                // 창 띄우기
//                ad.show();
//            }
//        });


        findViewById(R.id.action_insert_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 3);

            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //데이터 담아서 팝업(액티비티) 호출
                AlertDialog.Builder ad = new AlertDialog.Builder(Activity_board_add.this);

                ad.setTitle("링크 삽입하기");       // 제목 설정

                // EditText 삽입하기
                final EditText et_link = new EditText(Activity_board_add.this);
                ad.setView(et_link);

                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "Yes Btn Click");

                        // Text 값 받아서 로그 남기기
                        String value = et_link.getText().toString();
                        Log.v(TAG, value);

                        dialog.dismiss();     //닫기
                        // Event
                        mEditor.focusEditor();
                        mEditor.insertLink(value,value);
                    }
                });


                // 창 띄우기
                ad.show();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String img_url;

        //이미지 삽입 버튼 클릭시
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
//여기선 bitmap 안쓰는 액티비티
//                    // 선택한 이미지에서 비트맵 생성
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//
                    // 이미지 uri 받아오기
//                    System.out.println("data.getData() : " + data.getData());
                    img_url = getRealPathFromURI(data.getData());

                    //이미지 업로드
                    imageUpload(img_url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 링크삽입 버튼 클릭시
        else if (requestCode == 2) {
            Intent intent = getIntent();
            String get_link = intent.getStringExtra("link");
            mEditor.focusEditor();
            mEditor.insertLink(get_link, get_link);
        }

        //동영상 삽입 버튼 클릭시
        if (requestCode == 3) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 동영상 uri 받아오기
//                    System.out.println("data.getData() : " + data.getData());
                    img_url = getRealPathFromURI(data.getData());

                    //동영상 업로드
                    videoUpload(img_url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void imageUpload(final String imagePath) {
        //이미지 업로드용 멀티파트 요청
        //서버에서 받은 요청중 이미지 index값을 user 데이터에 업데이트

        System.out.println("imageUpload start");
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, server_info_url + "/Data/img_file/img_upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONObject jObj_response = new JSONObject(response);
                            String message = jObj_response.getString("img_name");
                            img_path = message;

                            // 이미지 표시
                            System.out.println("mEditor img_url : " + server_info_url + "/Data/img_file/" + message);

                            mEditor.focusEditor();
                            mEditor.insertImage(server_info_url + "/Data/img_file/" + img_path, img_path, 320, 320);
                            System.out.println("mEditor.insertImage");

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

    private void videoUpload(final String videoPath) {
        //이미지 업로드용 멀티파트 요청
        //서버에서 받은 요청중 이미지 index값을 user 데이터에 업데이트

        System.out.println("videoUpload start");
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, server_info_url + "/Data/video/video_upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONObject jObj_response = new JSONObject(response);
                            String message = jObj_response.getString("video_name");
                            video_path = message;

                            // 동영상 표시
                            System.out.println("mEditor video_url : " + server_info_url + "/Data/video/" + message);

                            mEditor.focusEditor();
                            mEditor.insertVideo(server_info_url + "/Data/video/" + video_path, 320, 320);
                            System.out.println("mEditor.insertVideo");

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
        smr.addFile("video", videoPath);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(smr);

        System.out.println("videoUpload end");
    }

    //업로드 하기위한 경로로 변경
    public String getRealPathFromURI(Uri uri) {
        int index = 0;

        String[] proj = {MediaStore.Images.Media.DATA}; // 이미지 경로로 해당 이미지에 대한 정보를 가지고 있는 cursor 호출
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null); // 데이터가 있으면(가장 처음에 위치한 레코드를 가리킴)
        if (cursor.moveToFirst()) { // 해당 필드의 인덱스를 반환하고, 존재하지 않을 경우 예외를 발생시킨다.
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        Log.d("getRealPathFromURI", "getRealPathFromURI: " + cursor.getString(index));
        System.out.println("getRealPathFromURI uri : " + cursor.getString(index));
        return cursor.getString(index);
    }

    //스피너에서 선택된 값을
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
        System.out.println(TAG + "NothingSelected");
    }


}


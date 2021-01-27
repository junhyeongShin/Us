package com.example.us;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

public class TestActivity extends AppCompatActivity {

    ImageView imageView_test_img;
    String img_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imageView_test_img = findViewById(R.id.imageView_test_img);

        Button btn_test_img = findViewById(R.id.btn_test_img);
        btn_test_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);
            }
        });

        Button btn_test_imgupload = findViewById(R.id.btn_test_imgupload);
        btn_test_imgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageUpload(img_path);

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

                    System.out.println("in.close()");

                    // 이미지 표시
                    imageView_test_img.setImageBitmap(img);

                    Uri selectedImageUri = data.getData();

                    System.out.println("selectedImageUri : " + selectedImageUri);
//
//                    final String path = String.valueOf(selectedImageUri);
//
//                    System.out.println("path : " + path);
//
//                    File f = new File(path);
//                    selectedImageUri = Uri.fromFile(f);
                    img_path = String.valueOf(getRealPathFromURI(selectedImageUri));

                    System.out.println("img_path : " + img_path);

//                    System.out.println("getPathFromUri(selectedImageUri) : " + getPathFromUri(data.getData()));


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
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, "http://192.168.56.1/Data/img_file/img_upload.php",
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
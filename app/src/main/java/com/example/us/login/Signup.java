package com.example.us.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.us.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;
public class Signup extends AppCompatActivity {
    private static final String TAG = "Sign";
    Button btn_sign;
//    Button btn_set_email;
    Button btn_agr_location;
    Button btn_agr_service;
    Button btn_agr_privacy;
    Button btn_id_check;
    EditText edit_text_name_signup;
    EditText edit_text_id_signup;
//    EditText edit_text_email;
    EditText edit_text_pw_signup;
    EditText edit_text_pwcheck_signup;
//    EditText email_check;
//    TextView text_sign;
//    TextView time_email_check;
//    String check = "1234";
//    String email;
    int number_email_check = 0;
    int i = 0;
//    TimerHandler timerHandler;
    Boolean id_check = false;

    private static boolean check = false;
    private static boolean sign_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Login login = new Login();

        final Activity activity = this;

        setContentView(R.layout.activity_signup);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        //전체동의
        final CheckBox checkBox_all=findViewById(R.id.checkBox_all);
        //필수 서비스이용약관
        final CheckBox checkBox_service=findViewById(R.id.checkBox_service);
        //선택 위치정보
        final CheckBox checkBox_location=findViewById(R.id.checkBox_location);
        //필수 개인정보
        final CheckBox checkBox_privacy=findViewById(R.id.checkBox_privacy);

        //체크박스 클릭시
        //전체가 클릭될경우 전체선택 체크 true
        //하나라도 체크가 안 되어 있을경우 전체선택

        //전체동의 클릭시
        checkBox_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_all.isChecked()){
                    checkBox_service.setChecked(true);
                    checkBox_location.setChecked(true);
                    checkBox_privacy.setChecked(true);
                }else {
                    checkBox_service.setChecked(false);
                    checkBox_location.setChecked(false);
                    checkBox_privacy.setChecked(false);
                }
            }
        });

        checkBox_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_all.isChecked()){
                    checkBox_all.setChecked(false);
                }else if(checkBox_service.isChecked()&&checkBox_location.isChecked()&&checkBox_privacy.isChecked()){
                    checkBox_all.setChecked(true);
                }
            }
        });

        checkBox_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_all.isChecked()){
                    checkBox_all.setChecked(false);
                }else if(checkBox_service.isChecked()&&checkBox_location.isChecked()&&checkBox_privacy.isChecked()){
                    checkBox_all.setChecked(true);
                }
            }
        });

        checkBox_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_all.isChecked()){
                    checkBox_all.setChecked(false);
                }else if(checkBox_service.isChecked()&&checkBox_location.isChecked()&&checkBox_privacy.isChecked()){
                    checkBox_all.setChecked(true);
                }
            }
        });


        btn_sign = findViewById(R.id.btn_sign);
        btn_id_check = findViewById(R.id.btn_id_check);
        edit_text_name_signup = findViewById(R.id.edit_text_name_signup);
        edit_text_id_signup = findViewById(R.id.edit_text_id_signup);
        edit_text_pw_signup = findViewById(R.id.edit_text_pw_signup);
        edit_text_pwcheck_signup = findViewById(R.id.edit_text_pwcheck_signup);

        //이용약관 버튼 - 서비스
        btn_agr_service = findViewById(R.id.btn_agr_service);
        btn_agr_service.setText(R.string.underlined_text);
        btn_agr_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                //다이얼로그 창의 제목 입력
                builder.setTitle("서비스 이용약관 ");
                //다이얼로그 창의 내용 입력
                builder.setMessage(R.string.app_service);
                //다이얼로그창에 취소 버튼 추가
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(TAG + "이용약관 닫기");
                            }
                        });
                //다이얼로그 보여주기
                builder.show();
            }
        });

        //이용약관 버튼2 - 위치정보
        btn_agr_location = findViewById(R.id.btn_agr_location);
        btn_agr_location.setText(R.string.underlined_text);
        btn_agr_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                //다이얼로그 창의 제목 입력
                builder.setTitle("위치 정보 이용 약관 ");
                //다이얼로그 창의 내용 입력
                builder.setMessage(R.string.app_location);
                //다이얼로그창에 취소 버튼 추가
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(TAG + "이용약관 닫기");
                            }
                        });
                //다이얼로그 보여주기
                builder.show();
            }
        });

        //이용약관 버튼3 - 개인정보
        btn_agr_privacy = findViewById(R.id.btn_agr_privacy);
        btn_agr_privacy.setText(R.string.underlined_text);
        btn_agr_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                //다이얼로그 창의 제목 입력
                builder.setTitle("개인정보처리방침 ");
                //다이얼로그 창의 내용 입력
                builder.setMessage(R.string.app_privacy);
                //다이얼로그창에 취소 버튼 추가
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(TAG + "이용약관 닫기");
                            }
                        });
                //다이얼로그 보여주기
                builder.show();
            }
        });

        // EditText ID
        edit_text_id_signup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                id_check = false;
                if (s.toString().getBytes().length >= 24) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("ID는 최대 24자리 입니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });

//        // Email 인증 확인 버튼
//        Button btn_email_check = findViewById(R.id.btn_email_check);
//        btn_email_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String text_email_check = email_check.getText().toString();
//
//                if (Objects.equals(text_email_check, check) && i > 0) {
//                    number_email_check = 1;
//                    Toast.makeText(getApplicationContext(), "이메일 인증완료", Toast.LENGTH_SHORT).show();
//                    //스레드 멈춤
//                    timerHandler.sendEmptyMessage(1);
//                    time_email_check.setText("인증완료");
//                    btn_set_email.setClickable(false);
//                    btn_set_email.setText("인증완료");
//                    btn_email_check.setText("인증완료");
//                    i=0;//인증 종료.
//                } else if (!Objects.equals(text_email_check, check)) {
//                    Toast.makeText(getApplicationContext(), "인증번호가 틀립니다.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "인증 시간이 초과되었습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        // Email 인증 보내기 버튼
//        btn_set_email = findViewById(R.id.btn_set_email);
//        btn_set_email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences pref = getSharedPreferences("ID", MODE_PRIVATE);
//
//                if(i==1){
//                    timerHandler.removeMessages(0);
//                }
//
//                timerHandler = new TimerHandler();
//
//                //인증번호 만들기 (랜덤)
//                Random r = new Random();
//                int x = 0;
//                while (x < 100000) {
//                    x = r.nextInt(999999);
//                }
//                check = String.valueOf(x);
//                System.out.println(TAG + " : check : " + check);
//
//                if(edit_text_email.getText().toString().equals("")){
//                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
//                }else if(Objects.equals(pref.getString(edit_text_email.getText().toString() + "email", ""), edit_text_email.getText().toString())){
//                    Toast.makeText(getApplicationContext(), "이미 인증된 이메일 입니다. 다른 이메일을 입력해 주세요", Toast.LENGTH_SHORT).show();
//                } else {
//                    //이메일 보내기
//                    try {
//                        Toast.makeText(getApplicationContext(), "이메일을 전송합니다. 잠시 기다려주세요.", Toast.LENGTH_SHORT).show();
//
//                        GMailSender gMailSender = new GMailSender("dnfzmfk14@gmail.com", "wnsgud167**");
//                        //GMailSender.sendMail(제목, 본문내용, 받는사람);
//                        gMailSender.sendMail("회원가입 인증 메일입니다.", check, edit_text_email.getText().toString());
//                        //쓰레드 시작.
//                        timerHandler.sendEmptyMessage(0);
//                        email = edit_text_email.getText().toString();
//                        Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
//                    } catch (SendFailedException e) {
//                        Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
//                    } catch (MessagingException e) {
//                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        // 회원가입 버튼
        //
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String sign_name = "";
//                String sign_id = "";
//                String sign_pw = "";
//                String sign_pw_check = "";

                String sign_name = edit_text_name_signup.getText().toString();
                String sign_id = edit_text_id_signup.getText().toString();
                String sign_pw = edit_text_pw_signup.getText().toString();
                String sign_pw_check = edit_text_pwcheck_signup.getText().toString();

                //이름이 비어있을때
                if (sign_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    System.out.println(TAG+" : sign_name : "+sign_name);
                    System.out.println("이름을 입력하세요");
                    return;
                }   // ID가 비어있을때
                else if (sign_id.equals("")) {
                    Toast.makeText(getApplicationContext(), "ID를 입력하세요", Toast.LENGTH_SHORT).show();
                    System.out.println("ID를 입력하세요");
                    return;
                }   //비밀번호가 비어있을때
                else if (sign_pw.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    System.out.println("비밀번호를 입력하세요");
                    return;
                }
                //중복확인 안했을때
                else if(!id_check){
                    Toast.makeText(getApplicationContext(), "ID 중복 확인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("ID 중복 확인이 필요합니다.");
                    return;
                }
                // 비밀번호확인 미 일치
                else if (!Objects.equals(sign_pw, sign_pw_check)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("비밀번호가 일치하지 않습니다.");
                    return;
                }
                //체크박스 확인
                else if(!ischecked(checkBox_all,checkBox_service,checkBox_location,checkBox_privacy)){
                    Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    System.out.println("이용약관에 동의해주세요.");
                    return;
                }
                else
                // 위 조건들 만족하고 회원가입
                {
                    System.out.println("회원가입 시도");

                    //저장소에 아이디 생성
                    final String final_sign_id = sign_id;
                    final String final_sign_pw = sign_pw;
                    final String final_sign_name = sign_name;
                    Thread thread_url = new Thread(){


                        public void run(){

                            try{
                                Post_signup(login.url+"/Login/signup.php", final_sign_id, final_sign_pw,final_sign_name);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };

                    //http 연결 및 json 데이터 수신
                    System.out.println(TAG+" : thread_url.start()");
                    thread_url.start();

                    //스레드 종료될때까지 대기
                    try {
                        thread_url.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(TAG+" : thread_url.join()");

                    System.out.println("sign_check : "+sign_check);

                    if(sign_check){
                    //회원가입 토스트 메시지
                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        System.out.println("회원가입이 완료되었습니다.");


                    //회원가입 창 닫기
                    Signup.this.finish();
                    }
                    //회원가입 실패 (서버에서 실패값 받아왔을때)
                    else {
                        System.out.println("회원가입에 실패하였습니다.");
                    }
                }

            }
        });


        // ID 중복 확인 버튼
        btn_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread_url = new Thread(){
                    String user_id = edit_text_id_signup.getText().toString();

                    public void run(){
                        try{
                            Post_id_check(login.url+"/Login/signup_id_check.php",user_id);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                //http 연결 및 json 데이터 수신
                thread_url.start();

                //스레드 종료될때까지 대기
                try {
                    thread_url.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!check){
                    Toast.makeText(getApplicationContext(), "ID가 중복됩니다. 다른 ID를 사용해주세요.", Toast.LENGTH_SHORT).show();
                    System.out.println("ID가 중복됩니다. 다른 ID를 사용해주세요.");
                }else {
                    id_check=true;
                    Toast.makeText(getApplicationContext(), "사용가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("사용가능한 ID 입니다.");
                }

            }
        });
    }

    public String Post_id_check(String url, String user_id){

        InputStream Post_is = null;
        String result = "";

        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", user_id);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("json : "+json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();

            // receive response as inputStream
            // http 연결 후 입력받은 json 데이터 판별
            try {
                Post_is = httpCon.getInputStream();
                // convert inputstream to string

                System.out.println(TAG+" Post_is :"+Post_is);

                if(Post_is != null){
                    result = convertInputStreamToString(Post_is);
                    System.out.println(TAG+" result : "+result);

                    JSONObject jsonObject_result = new JSONObject(result);
                    //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
                    //배열로된 자료를 가져올때
                    String json_chek_login = jsonObject_result.getString("result_check");//배열의 이름

                    if(json_chek_login.equals("OK")){
                        check = true;
                        System.out.println("중복확인 성공");
                    }else {
                        check = false;
                        System.out.println("중복확인 실패");
                        System.out.println(result);
                    }
                }
                //입력이 비었을경우
                else
                    result = "Did not work!";

            }

            //json 관련 오류
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //http 연결 제거
            finally {
                httpCon.disconnect();
            }

        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    public String Post_signup(String url, String user_id, String user_pw,String user_name){

        System.out.println(TAG+" : Post_signup : start");

        InputStream Post_is = null;
        String result = "";

        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();
            String json = "";


            // Json 오브젝트 생성
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", user_id);
            jsonObject.accumulate("user_pw", user_pw);
            jsonObject.accumulate("user_name", user_name);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("json : "+json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();

            // receive response as inputStream
            // http 연결 후 입력받은 json 데이터 판별
            try {
                Post_is = httpCon.getInputStream();
                // convert inputstream to string

                if(Post_is != null){
                    result = convertInputStreamToString(Post_is);
                    System.out.println(TAG+" result : "+result);

                    JSONObject jsonObject_result = new JSONObject(result);
                    //더 많은 정보를 확인하고 싶다면,  https://stickode.com/으로 접속하세요.
                    //배열로된 자료를 가져올때
                    String json_chek_login = jsonObject_result.getString("result_check");//배열의 이름
                    System.out.println(TAG+" : json_chek_login : "+json_chek_login);

                    if(json_chek_login.equals("OK")){
                        sign_check = true;
                    }else {
                        Toast.makeText(getApplicationContext(), "회원가입에 실패 하였습니다.", Toast.LENGTH_LONG).show();
                        System.out.println(result);
                    }
                }
                //입력이 비었을경우
                else
                    result = "Did not work!";

            }

            //json 관련 오류
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //http 연결 제거
            finally {
                httpCon.disconnect();
            }

        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream is) {

        final char[] buffer = new char[8192];
        final StringBuilder result = new StringBuilder();

        // InputStream -> Reader
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            int charsRead;
            while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
                result.append(buffer, 0, charsRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(TAG+" : convertInputStreamToString e : "+e);
        }

        System.out.println(TAG+" : result.toString() : "+result.toString());
        return result.toString();

    }
    @Override
    protected void onStop() {
        super.onStop();
//        if (timerHandler != null) {
//            timerHandler.sendEmptyMessage(1);
//        }

    }

    private boolean ischecked(CheckBox checkBox,CheckBox checkBox2,CheckBox checkBox3,CheckBox checkBox4){

        if(checkBox.isChecked()){
            return true;
        }else if(checkBox2.isChecked()&&checkBox4.isChecked()){
            return true;
        }
        return false;
    }

//    class TimerHandler extends Handler {
//        int min = 5, sec = 0;
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0: // 시작 하기
//                    if (min == 0 && sec == 0) {
//                        Toast.makeText(getApplicationContext(), "인증 가능 시간이 지났습니다.", Toast.LENGTH_SHORT).show();
//                        i = 0;
//                        btn_set_email.setText("다시 받기");
//                        time_email_check.setText("인증 시간 초과");
//                        timerHandler.sendEmptyMessage(1);
//                        break;
//                    }
//                    // Main Thread 가 쉴때 Main Thread 가 실행하기에
//                    // 오래걸리는 작업은 하면 안된다. (무한 루프 문 etc.. 은 쓰레드로)
//                    if (sec == 0) {
//                        min--;
//                        sec = 60;
//                    }
//                    sec--;
//
//                    String str = min + " : " + sec;
//                    if (sec < 10) {
//                        str = min + " : 0" + sec;
//                    }
//                    i = 1;
//                    time_email_check.setText(str);
//                    sendEmptyMessageDelayed(0, 1000);
//                    System.out.println(TAG + " : " + str);
//                    break;
//
//                case 1: //정지
//                    if (min > 0 || sec > 0) {
//                        i = 0;
//                        number_email_check = 1;
//                    }
//                    removeMessages(0); // 타이머 메시지 삭제
//                    System.out.println(TAG + " : " + "removeMessages");
//                    break;
//            }
//
//        }
//    }

}
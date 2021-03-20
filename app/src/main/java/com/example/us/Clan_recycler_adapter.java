package com.example.us;
import android.graphics.Color;
import android.util.Log;
import android.widget.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.HashMap;
import java.util.List;

public class Clan_recycler_adapter  extends RecyclerView.Adapter<Clan_recycler_adapter.ViewHolder>{

    private List<Clan_item> Items;

    AlertDialog dialog;

    Point size;

    Context context;
    Context context_toast;

    public Clan_recycler_adapter(List<Clan_item> Items,Context context) {
        this.Items = Items;
        this.context = context_toast;
    }


    @NonNull
    @NotNull
    @Override
    public Clan_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clan_list_item, parent, false);    //리사이클러뷰나 뷰페이저에 등록할 아이템뷰

        Clan_recycler_adapter.ViewHolder vh = new Clan_recycler_adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Clan_recycler_adapter.ViewHolder holder, final int position) {

        holder.id = Items.get(position).getId();

        holder.textview_clan_list_title.setText(Items.get(position).getTitle());
        holder.textview_clan_list_member.setText(Items.get(position).getCategory());

        String member_info = "멤버수 : "+Items.get(position).getMember_count()+" / 100";
        holder.textview_clan_list_intro.setText(member_info);


        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+Items.get(position).getClan_img(),holder.imageview_clan_list_item);
        imageLoadTask.execute();



        holder.ibtn_clan_list_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ibtn_clan_list_info 클릭 포지션 : "+position);
                System.out.println("ibtn_clan_list_info 클릭 ID : "+Items.get(position));

                //정보보기 팝업창
                popupImgXml(position);

            }
        });

        holder.ibtn_clan_list_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ibtn_clan_list_info 클릭 포지션 : "+position);
                System.out.println("ibtn_clan_list_info 클릭 ID : "+Items.get(position));

                //클랜가입 요청 to 서버
                Clan_join(position);

                Toast.makeText(context, "가입 신청",Toast.LENGTH_SHORT).show();




            }
        });

    }

    // XML 불러오기
    // 클랜정보 팝업창
    // 이미지 + 타이틀 + 카테고리 + 멤버 수 + 클랜소개 + 가입버튼
    public void popupImgXml(final int position) {
        //일단 res에 popupimg.xml 만든다
        //그 다음 화면을 inflate 시켜 setView 한다

        //팝업창에 xml 붙이기///////////////
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_clan_item, null);

        ImageView imageView_clan_info = view.findViewById(R.id.imageView_clan_info);
        ImageLoadTask imageLoadTask = new ImageLoadTask(server_info.getInstance().getURL() +"/Data/img_file/"+Items.get(position).getClan_img(),imageView_clan_info);
        imageLoadTask.execute();


        TextView textview_title_clan_info = view.findViewById(R.id.textview_title_clan_info);
        textview_title_clan_info.setText(Items.get(position).getTitle());
        TextView textview_category_clan_info = view.findViewById(R.id.textview_category_clan_info);
        textview_category_clan_info.setText(Items.get(position).getCategory());
        TextView textview_category_clan_member_count = view.findViewById(R.id.textview_category_clan_member_count);
        String tmp_member= Items.get(position).getMember_count()+" / 100";
        textview_category_clan_member_count.setText(tmp_member);

        TextView textview_clan_info_intro = view.findViewById(R.id.textview_clan_info_intro);
        textview_clan_info_intro.setText(Items.get(position).getClan_introduce());

        Button btn_clan_info_join = view.findViewById(R.id.btn_clan_info_join);
        btn_clan_info_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클랜 가입 신청 버튼 클릭");

                Toast.makeText(context, "클랜 가입",Toast.LENGTH_SHORT).show();

                if(Items.get(position).getMember_count()==100){



                    //데이터 담아서 팝업(액티비티) 호출
                    AlertDialog.Builder ad = new AlertDialog.Builder(context);
                    ad.setTitle("멤버수 초과");       // 제목 설정
                    // EditText 삽입하기
                    final TextView textView = new TextView(context);
//                    textView.setPadding(0,20,0,10);
//                    textView.setText("해당 클랜은 멤버가 가득차서 \n 가입신청이 불가능합니다.");
                    textView.setTextSize(14);
//                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextColor(Color.BLACK);
                    ad.setView(textView);
                    // 취소 버튼 설정
                    ad.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    // 창 띄우기
                    ad.show();


                }else {
                    System.out.println("클랜 가입 신청 시작");

                    Clan_join(position);


                }
            }
        });




        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("클 랜 정 보")
                .setView(view);

        // 취소 버튼 설정
        builder.setNegativeButton("닫 기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });


        dialog = builder.create();
        dialog.show();


        //디바이스 사이즈를 받아 팝업 크기창을 조절한다.
        int sizeX = size.x;
        int sizeY = size.y;

        //AlertDialog에서 위치 크기 수정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.x = (int) Math.round(sizeX * 0.005); // X위치
        params.y = (int) Math.round(sizeY * 0.01); // Y위치
        params.width = (int) Math.round(sizeX * 0.9);
        params.height = (int) Math.round(sizeY * 0.8);
        dialog.getWindow().setAttributes(params);
    }

    public void add_item(Clan_item items){
//        System.out.println("리사이클러뷰 아이템 추가");
        Items.add(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int id;

        TextView textview_clan_list_title;
        TextView textview_clan_list_member;
        TextView textview_clan_list_intro;
        ImageView imageview_clan_list_item;

        Button ibtn_clan_list_info;
        Button ibtn_clan_list_add;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textview_clan_list_title = itemView.findViewById(R.id.textview_clan_list_title);
            textview_clan_list_member = itemView.findViewById(R.id.textview_clan_list_member);
            textview_clan_list_intro = itemView.findViewById(R.id.textview_clan_list_intro);
            imageview_clan_list_item = itemView.findViewById(R.id.imageview_clan_list_item);

            ibtn_clan_list_info = itemView.findViewById(R.id.ibtn_clan_list_info);
            ibtn_clan_list_add = itemView.findViewById(R.id.ibtn_clan_list_add);

        }

    }

    public void Clan_join(int position){
        int user_id = user_info.getInstance().getUser_index_number();
        int clan_id = Items.get(position).getId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(com.example.us.server_info.getInstance().getURL())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

        HashMap<String, Object> input = new HashMap<>();
        input.put("user_id", user_id);
        input.put("clan_id", clan_id);
        System.out.println("input : " + input);

        retrofit_api.postData_clan_join(input).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    String data = response.body();
                    System.out.println("성공");
                    System.out.println(data);

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



}

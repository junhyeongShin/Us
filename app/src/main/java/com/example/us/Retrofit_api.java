package com.example.us;

import com.example.us.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Comment;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.List;

public interface Retrofit_api {

    //GET 기본형
    @GET()
    Call<List<Post>> getData(@Query("userId") String id);

    @GET("/Data/board/board_list.php")
    Call<List<Board_list>> getData_board(@Query("user_id") int id, @Query("category") String category, @Query("request_list") String list);

    @GET("/Data/board/board.php")
    Call<Board_view_item> getData_board_view(@Query("user_id") int user_id,@Query("id") int id);

    @GET("/Data/img_file/get_imgfile.php")
    Call<String> getData_img(@Query("img_porfile") int img_porfile);

    @GET("/Data/board/board_delete.php")
    Call<String> getData_del_board(@Query("id") int id);

    @GET("/Data/board/board_edit.php")
    Call<String> getData_edit_board(@Query("id") int id,@Query("category") String category,@Query("content") String content,@Query("title") String title);

    @GET("/Data/board/comment_delete.php")
    Call<String> getData_del_comment(@Query("id") int id);

    @GET("/Data/board/re_comment_delete.php")
    Call<String> getData_del_re_comment(@Query("id") int id);

    @GET("/Data/board/comment_list.php")
    Call<List<comment>> getData_comment_list(@Query("board_id") int board_id);

    @GET("/Data/board/re_comment_list.php")
    Call<List<re_comment>> getData_re_comment_list(@Query("comment_id") int comment_id);

    @GET("/Data/user_list.php")
    Call<List<User_list_item>> getUser_list();

    @GET("/Data/friend_find_list.php")
    Call<List<User_list_item>> getFind_list(@Query("id") int id);

    @GET("/Data/friend_list.php")
    Call<List<User_list_item>> getFriend_list(@Query("id") int id);

    @GET("/Data/friend_add.php")
    Call<String> Add_Friend(@Query("id") int id,@Query("friend_id") int friend_id);

    @GET("/Data/Clan/clan_join_ok.php")
    Call<String> Add_member(@Query("user_id") int user_id,@Query("clan_id") int clan_id);

    @GET("/Data/friend_del.php")
    Call<String> Del_Friend(@Query("id") int id,@Query("friend_id") int friend_id);

    @GET("/Data/Clan/clan_list.php")
    Call<List<Clan_item>> getClan_list();


    @GET("/Data/Clan/clan_list_search.php")
    Call<List<Clan_item>> getClan_list_search(@Query("search") String search);



    @GET("/Data/Clan/clan_list_user.php")
    Call<List<Clan_item>> getClan_list_user(@Query("id") int id);

    @GET("/Data/Clan/clan_join_list.php")
    Call<List<User_list_item>> getClan_join_list(@Query("id") int id);

    @GET("/Data/Clan/clan_check.php")
    Call<String> getClan_check(@Query("id") int id);

    @GET("/Data/Clan/clan_del.php")
    Call<String> getClan_del(@Query("clan_id") int clan_id);

    @GET("/Data/Clan/clan_member_out.php")
    Call<String> member_out(@Query("user_id") int user_id,@Query("clan_id") int clan_id);

    @GET("/Data/Clan/clan_user_out.php")
    Call<String> user_out(@Query("user_id") int user_id,@Query("clan_id") int clan_id);





    //포스트 기본형
    @FormUrlEncoded
    @POST()
    Call<Post> postData(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/board_add.php")
    Call<Post> postData_board(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/board/comment_add.php")
    Call<String> postData_comment_add(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/board/comment_edit.php")
    Call<String> postData_comment_edit(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/board/re_comment_add.php")
    Call<String> postData_re_comment_add(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/board/re_comment_edit.php")
    Call<String> postData_re_comment_edit(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/Clan/clan_add.php")
    Call<String> postData_clan_add(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/Clan/clan_join.php")
    Call<String> postData_clan_join(@FieldMap HashMap<String, Object> param);

}

///////////////////////초기세팅
//    Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();
//
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(server_info.getInstance().getURL())
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build();
//
//    Retrofit_api retrofit_api = retrofit.create(Retrofit_api.class);

//        retrofit_api.getData("1").enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                if(response.isSuccessful()){
//                    List<Post> data = response.body();
//                    System.out.println("성공");
//                    System.out.println(data.get(0).getTitle());
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                System.out.println("실패");
//            }
//        });

//        HashMap<String, Object> input = new HashMap<>();
//        input.put("user_id",1);
//        input.put("board_id",2);
//        input.put("content","body body 당근 당근");
//
//        System.out.println("input : "+input);
//
//        retrofit_api.postData_comment_add(input).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.isSuccessful()){
//                    String data = response.body();
//                    System.out.println("성공");
//                    System.out.println(data.getClass());
//                }
//                System.out.println("response : "+response);
//                System.out.println("call : "+call);
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                System.out.println("call : "+call);
//                System.out.println("Throwable : "+t);
//            }
//        });


//        HashMap<String, Object> input = new HashMap<>();
//        input.put("id",2);
//        input.put("content","body body 당근 당근 body body 당근 당근");
//
//        System.out.println("input : "+input);
//
//        retrofit_api.postData_comment_edit(input).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.isSuccessful()){
//                    String data = response.body();
//                    System.out.println("성공");
//                    System.out.println(data.getClass());
//                }
//                System.out.println("response : "+response);
//                System.out.println("call : "+call);
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                System.out.println("call : "+call);
//                System.out.println("Throwable : "+t);
//            }
//        });

//        retrofit_api.getData_del_comment(2).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                System.out.println("call : "+call);
//                System.out.println("response : "+response);
//
//                if (response.isSuccessful()) {
//                    String data = response.body();
//                    System.out.println("성공");
//                    System.out.println("data : "+data);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                System.out.println("실패");
//                System.out.println("call : "+call);
//                System.out.println("Throwable : "+t);
//            }
//        });

//        retrofit  _api.getData_comment_list(2).enqueue(new Callback<List<comment>>() {
//            @Override
//            public void onResponse(Call<List<comment>> call, Response<List<comment>> response) {
//                System.out.println("onResponse : call"+call);
//                System.out.println("onResponse : response"+response);
//                if(response.isSuccessful()){
//                    List<comment> board_list = response.body();
//                    System.out.println("성공");
//
//                    for (int i = 0; i < board_list.size(); i++){
//                        System.out.println("ID : "+board_list.get(i).getId());
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<comment>> call, Throwable t) {
//                System.out.println("실패 Throwable : "+t.toString());
//                System.out.println("실패 call : "+call.toString());
//            }
//        });

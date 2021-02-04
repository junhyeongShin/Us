package com.example.us;

import com.example.us.Post;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.List;

public interface Retrofit_api {

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

    @FormUrlEncoded
    @POST()
    Call<Post> postData(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/board_add.php")
    Call<Post> postData_board(@FieldMap HashMap<String, Object> param);

}

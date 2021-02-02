package com.example.us;

import com.example.us.Post;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.List;

public interface Retrofit_api {

    @GET()
    Call<List<Post>> getData(@Query("userId") String id);

    @GET("/Data/board_list.php")
    Call<List<Post>> getData_board(@Query("user_id") int id, @Query("category") String category, @Query("request_list") String list);

    @FormUrlEncoded
    @POST()
    Call<Post> postData(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Data/board_add.php")
    Call<Post> postData_board(@FieldMap HashMap<String, Object> param);

}

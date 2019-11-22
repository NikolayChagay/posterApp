package android.chagay.poster.network;

import android.chagay.poster.model.Answer;
import android.chagay.poster.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JSONApi {

    String API_ENDPOINT = "https://jsonplaceholder.typicode.com";

    @GET("/posts")
    Call<List<Post>> getAllPosts();

    @POST("/posts")
    Call<Answer> addNewPost(@Body Post data);
}
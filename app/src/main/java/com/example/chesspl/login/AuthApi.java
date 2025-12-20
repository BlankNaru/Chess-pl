package com.example.chesspl.login;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/auth/register")
    Call<Map<String, String>> register(@Body RegisterRequest req);

    @GET("/api/users/by-username/{username}")
    Call<Map<String, Object>> getUserByUsername(@Path("username") String username);

    @GET("/api/users/by-username")
    Call<Map<String, Object>> isUserFriend(@Query("username1") String username1, @Query("username2") String username2);

    @POST("/api/users/add-friend")
    Call<Map<String, Object>> addFriend(@Query("username1") String username1, @Query("username2") String username2);

    @GET("/api/users/get-players")
    Call<List<Map<String, Object>>> getPlayers(@Query("key") String key);

    @GET("/api/users/get-friends")
    Call<List<Map<String, Object>>> getFriendsOf(@Query("player") String player);
}

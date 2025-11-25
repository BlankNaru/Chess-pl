package com.example.chesspl.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt", null);

        Request request;

        if (token != null) {
            request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
        } else {
            request = chain.request();
        }

        return chain.proceed(request);
    }
}

package com.example.chesspl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chesspl.R;
import com.example.chesspl.login.ApiClient;
import com.example.chesspl.login.AuthApi;
import com.example.chesspl.login.AuthResponse;
import com.example.chesspl.login.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = findViewById(R.id.firstEditText);
        EditText password = findViewById(R.id.secondEditText);

        findViewById(R.id.signUpTextButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.submitButton).setOnClickListener(v -> {

            LoginRequest req = new LoginRequest(username.getText().toString(), password.getText().toString());
            AuthApi api = ApiClient.getAuthApi(this);

            api.login(req).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String jwtToken = response.body().getToken();
                        String username = response.body().getUsername();

                        getSharedPreferences("prefs", MODE_PRIVATE)
                                .edit()
                                .putString("jwt", jwtToken)
                                .apply();

                        getSharedPreferences("prefs", MODE_PRIVATE)
                                .edit()
                                .putString("username", username)
                                .apply();

                        getSharedPreferences("prefs", MODE_PRIVATE)
                                .edit()
                                .putString("elo", response.body().getElo())
                                .apply();

                        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
        });

//        GridLayout chessBoard = findViewById(R.id.chessBoard);
//        Chessboard chessboard = new Chessboard(false, chessBoard, this);

//        Button button = findViewById(R.id.offlineGame);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, LocalGameActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Button button1 = findViewById(R.id.onlineGame);
//        button1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MainActivity.this, OnlineGameActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
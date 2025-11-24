package com.example.chesspl.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chesspl.R;
import com.example.chesspl.login.ApiClient;
import com.example.chesspl.login.AuthApi;
import com.example.chesspl.login.RegisterRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.emailEditTetxt);
        EditText password = findViewById(R.id.setPassword);
        EditText passwordRepeated = findViewById(R.id.repeatPassword);

        findViewById(R.id.submitButton).setOnClickListener(v -> {

            if (!password.getText().toString().equals(passwordRepeated.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Passwords are different", Toast.LENGTH_SHORT).show();
                return;
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!email.getText().toString().matches(emailRegex)) {
                Toast.makeText(RegisterActivity.this, "Not proper email", Toast.LENGTH_SHORT).show();
                return;
            }


            RegisterRequest req = new RegisterRequest(username.getText().toString(), password.getText().toString(), email.getText().toString());
            AuthApi api = ApiClient.getAuthApi(this);

            api.register(req).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String message = response.body().get("message");
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error: " + response.body().get("message"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}

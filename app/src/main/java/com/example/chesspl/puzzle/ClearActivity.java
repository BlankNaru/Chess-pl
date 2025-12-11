package com.example.chesspl.puzzle;
import com.example.chesspl.R;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClearActivity extends AppCompatActivity {

    private ChessApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:9000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ChessApi.class);

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> clearDatabase());
    }

    private void clearDatabase() {
        api.clearPuzzles().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClearActivity.this, "Baza puzzli wyczyszczona!", Toast.LENGTH_SHORT).show();
                    Log.d("ClearActivity", "Czyszczenie zakończone sukcesem.");
                } else {
                    Toast.makeText(ClearActivity.this, "Błąd czyszczenia: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("ClearActivity", "Błąd czyszczenia: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClearActivity.this, "Błąd połączenia z API", Toast.LENGTH_SHORT).show();
                Log.e("ClearActivity", "Błąd Retrofit: ", t);
            }
        });
    }
}

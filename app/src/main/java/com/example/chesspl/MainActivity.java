package com.example.chesspl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

//        GridLayout chessBoard = findViewById(R.id.chessBoard);
//        Chessboard chessboard = new Chessboard(false, chessBoard, this);

        Button button = findViewById(R.id.localGame);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, LocalGameActivity.class);
                startActivity(intent);
            }
        });

    }
}
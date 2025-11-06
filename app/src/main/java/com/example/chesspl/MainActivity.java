package com.example.chesspl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chesspl.chessClasses.Chessboard;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        GridLayout chessBoard = findViewById(R.id.chessBoard);
        Chessboard chessboard = new Chessboard(false, chessBoard, this);

        Intent intent = new Intent(MainActivity.this, Puzzle.class);
        startActivity(intent);


    }
}
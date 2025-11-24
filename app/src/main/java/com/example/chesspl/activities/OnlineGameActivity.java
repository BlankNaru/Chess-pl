package com.example.chesspl.activities;

import android.os.Bundle;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chesspl.R;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.GameType;

public class OnlineGameActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        GridLayout chessBoard = findViewById(R.id.chessBoard);
        Chessboard chessboard = new Chessboard(false, chessBoard, this, GameType.ONLINE);


    }
}

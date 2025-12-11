package com.example.chesspl.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chesspl.R;

public class PuzzleMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_menu);

        Button addPuzzleBtn = findViewById(R.id.addPuzzleButton);
        Button listPuzzleBtn = findViewById(R.id.listPuzzleButton);

        addPuzzleBtn.setOnClickListener(v ->
                startActivity(new Intent(this, PuzzleActivity.class))
        );

        listPuzzleBtn.setOnClickListener(v ->
                startActivity(new Intent(this, SolvePuzzleActivity.class))
        );
    }
}

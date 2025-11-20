package com.example.chesspl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PuzzleMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_menu);

        Button addPuzzleBtn = findViewById(R.id.addPuzzleButton);
        Button listPuzzleBtn = findViewById(R.id.listPuzzleButton);

        addPuzzleBtn.setOnClickListener(v ->
                startActivity(new Intent(this, Puzzle.class))
        );

        listPuzzleBtn.setOnClickListener(v ->
                startActivity(new Intent(this, PuzzleList.class))
        );
    }
}

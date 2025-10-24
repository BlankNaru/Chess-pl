package com.example.chesspl;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        GridLayout chessBoard = findViewById(R.id.chessBoard);
        int size = 8;

        chessBoard.post(() -> {
            int cellSize = chessBoard.getWidth() / size;

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {

                    View square = new View(this);

                    if ((row + col) % 2 == 0) {
                        square.setBackgroundColor(Color.parseColor("#EEEED2")); // light
                    } else {
                        square.setBackgroundColor(Color.parseColor("#769656")); // dark
                    }

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = cellSize;
                    params.height = cellSize;
                    params.rowSpec = GridLayout.spec(row);
                    params.columnSpec = GridLayout.spec(col);

                    chessBoard.addView(square, params);
                }
            }
        });
    }
}
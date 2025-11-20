package com.example.chesspl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.chesspl.chessClasses.Chessboard;

import java.util.List;

public class PuzzleList extends Activity {

    private GridLayout chessboardLayout;
    private Chessboard chessboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_list);

        SinglePuzzle puzzle = PuzzleStorage.getInstance().getLastPuzzle();
        chessboardLayout = findViewById(R.id.puzzleBoard);
        chessboard = new Chessboard(true, chessboardLayout, this);
        chessboard.setFields(puzzle.getFields());


        PuzzleHelper.generateFields2(chessboardLayout, this, chessboard);


        final String correctMoveHistory = puzzle.getMoveHistory();


//        chessboard.setOnMoveListener(() -> {
//
//            if(chessboard.getMoveHistory().equals(correctMoveHistory))
//            {
//                Toast.makeText(this, "Correct! Congratulations!", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(this, PuzzleMenu.class);
//                startActivity(intent);
//            }
////            else
////            {
////                Toast.makeText(this, "Wrong!.", Toast.LENGTH_SHORT).show();
////            }
//        });

        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            if(chessboard.getMoveHistory().equals(correctMoveHistory)) {
                Toast.makeText(this, "Correct! Congratulations!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, PuzzleMenu.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        });



        if(chessboard.getMoveHistory().equals(puzzle.getMoveHistory()))
        {
            Toast.makeText(this, "Correct! Congratulations!", Toast.LENGTH_LONG).show();
        }

    }
}

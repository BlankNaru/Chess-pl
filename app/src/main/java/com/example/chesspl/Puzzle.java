package com.example.chesspl;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chesspl.chessClasses.Chessboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle extends AppCompatActivity
{

    private GridLayout chessboardLayout;
    private Chessboard chessboard;
    private RecyclerView pieceRecyclerView;
    private PieceAdapter pieceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        chessboardLayout = findViewById(R.id.chessboardLayout);
        pieceRecyclerView = findViewById(R.id.pieceRecyclerView);


        pieceRecyclerView.setVisibility(View.GONE);


        List<Integer> pieces = new ArrayList<>(Arrays.asList(
                R.drawable.king,
                R.drawable.queen,
                R.drawable.rook,
                R.drawable.bishop,
                R.drawable.knight,
                R.drawable.pawn
        ));


        pieceAdapter = new PieceAdapter(pieces, drawableId ->
        {
            if(chessboard.getSelectedField() != null)
            {
                chessboard.getSelectedField().getPieceView().setImageResource(drawableId);
                chessboard.getSelectedField().getPieceView().setVisibility(View.VISIBLE);
                pieceRecyclerView.setVisibility(View.GONE);
            }
        });

        pieceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        pieceRecyclerView.setAdapter(pieceAdapter);


        chessboard = new Chessboard(true, chessboardLayout, this);
        ChessHelper.generateFields(chessboardLayout, this, chessboard, () ->
        {
            ChessHelper.enableFieldClickListeners(chessboard, pieceRecyclerView);
        });
    }
}

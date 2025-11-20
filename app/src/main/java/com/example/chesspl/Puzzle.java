package com.example.chesspl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.PieceColor;
import com.example.chesspl.chessClasses.figureClasses.Piece;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle extends Activity {

    private GridLayout chessboardLayout;
    private Chessboard chessboard;
    private RecyclerView pieceRecyclerView;
    private PieceAdapter pieceAdapter;
    private List<List<ChessField>> boardState = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        pieceRecyclerView = findViewById(R.id.pieceRecyclerView);
        pieceRecyclerView.setVisibility(View.GONE);

        List<Integer> pieces = new ArrayList<>(Arrays.asList(
                R.drawable.king,
                R.drawable.queen,
                R.drawable.rook,
                R.drawable.bishop,
                R.drawable.knight,
                R.drawable.pawn,
                R.drawable.king,
                R.drawable.queen,
                R.drawable.rook,
                R.drawable.bishop,
                R.drawable.knight,
                R.drawable.pawn
        ));


        fieldsInit();

        pieceAdapter = new PieceAdapter(pieces, (drawableId, color) -> {
            ChessField field = chessboard.getSelectedField();
            if (field != null)
            {

                Piece piece = PuzzleHelper.createPieceFromDrawable(
                        drawableId,
                        (color == Color.BLACK) ? PieceColor.BLACK : PieceColor.WHITE
                );

                field.setPiece(piece);

                int rowIndex = 7 - field.getRow();
                int colIndex = field.getCol();
                ChessField boardField = boardState.get(rowIndex).get(colIndex);
                boardField.setSimulatedPiece(piece);

                pieceRecyclerView.setVisibility(View.GONE);
            }
        });

        pieceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        pieceRecyclerView.setAdapter(pieceAdapter);



        chessboardLayout = findViewById(R.id.puzzleBoard);
        chessboard = new Chessboard(true, chessboardLayout, this);


        PuzzleHelper.generateFields(chessboardLayout, this, chessboard, pieceRecyclerView);


        Button saveButton = findViewById(R.id.saveButton);
        SinglePuzzle puzzle = new SinglePuzzle();

        saveButton.setOnClickListener(v -> {

            puzzle.setFields(boardState);

            PuzzleHelper.generateFields2(chessboardLayout, this, chessboard);
            Toast.makeText(this, "Pieces set, record your moves", Toast.LENGTH_LONG).show();
        });

        Button saveMoves = findViewById(R.id.saveMoves);
        saveMoves.setOnClickListener(v ->
        {

            puzzle.setMoveHistory(chessboard.getMoveHistory());
            puzzle.setPlayerToMove(PieceColor.WHITE);
            puzzle.setCategory(PuzzleCategory.MAT_W_1);

            PuzzleStorage.getInstance().setLastPuzzle(puzzle);

            Toast.makeText(this, "Added a puzzle", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, PuzzleMenu.class);
            startActivity(intent);
        });


    }

    private void fieldsInit()
    {
        for(int i = 8; i >= 1; i--)
        {
            List<ChessField> row = new ArrayList<>();
            for(char j = 'A'; j <= 'H'; j++)
            {
                row.add(new ChessField(Character.toString(j), Integer.toString(i)));
            }
            boardState.add(row);
        }
    }

}

package com.example.chesspl.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chesspl.R;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.PieceColor;
import com.example.chesspl.chessClasses.figureClasses.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SolvePuzzleActivity extends AppCompatActivity {

    private ChessApi api;
    private GridLayout chessboardLayout;
    private Chessboard chessboard;
    private SinglePuzzle puzzle;
    private TextView puzzleCategoryText;
    private TextView playerToMoveText;
    private long currentPuzzleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        chessboardLayout = findViewById(R.id.puzzleBoard);
        puzzleCategoryText = findViewById(R.id.puzzleCategoryText);
        playerToMoveText = findViewById(R.id.playerToMoveText);
        chessboard = new Chessboard(true, chessboardLayout, this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:9000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ChessApi.class);


        loadPuzzleCount();

    }


    private void loadPuzzleCount() {
        api.getAllPuzzles().enqueue(new Callback<List<PuzzleDTO>>() {
            @Override
            public void onResponse(Call<List<PuzzleDTO>> call, Response<List<PuzzleDTO>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(SolvePuzzleActivity.this, "Error getting puzzles!", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<PuzzleDTO> puzzles = response.body();
                int count = puzzles.size();

                if (count == 0) {
                    Toast.makeText(SolvePuzzleActivity.this, "Lack of puzzles in database!", Toast.LENGTH_SHORT).show();
                    return;
                }


                currentPuzzleId = new Random().nextInt(count) + 1;


                loadPuzzle(currentPuzzleId);
            }

            @Override
            public void onFailure(Call<List<PuzzleDTO>> call, Throwable t) {
                Toast.makeText(SolvePuzzleActivity.this, "API connection error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadPuzzle(long puzzleId) {
        api.getPuzzleById(puzzleId).enqueue(new Callback<PuzzleDTO>() {
            @Override
            public void onResponse(Call<PuzzleDTO> call, Response<PuzzleDTO> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(SolvePuzzleActivity.this, "Error getting puzzle!" + puzzleId, Toast.LENGTH_SHORT).show();
                    return;
                }

                chessboard.clearBoard();
                PuzzleDTO dto = response.body();

                List<List<ChessField>> board = new ArrayList<>();
                for (List<ChessFieldDTO> rowDTO : dto.getFields()) {
                    List<ChessField> row = new ArrayList<>();
                    for (ChessFieldDTO fDTO : rowDTO) {

                        ChessField field = new ChessField(fDTO.getLetter(), fDTO.getNumber());

                        if (fDTO.getPiece() != null) {
                            Piece piece = PuzzleHelper.createPieceFromDTO(fDTO.getPiece());
                            chessboard.getLocation(fDTO.getLetter() + fDTO.getNumber()).setPiece(piece);
                        }

                        row.add(field);
                    }
                    board.add(row);
                }

                puzzle = new SinglePuzzle(
                        board,
                        PieceColor.valueOf(dto.getPlayerToMove()),
                        PuzzleCategory.valueOf(dto.getCategory())
                );
                puzzle.setMoveHistory(dto.getMoveHistory());

                chessboard.playerToMove = PieceColor.valueOf(dto.getPlayerToMove());



                setupFinishButton();
                displayPuzzleInfo();
            }

            @Override
            public void onFailure(Call<PuzzleDTO> call, Throwable t) {
                Toast.makeText(SolvePuzzleActivity.this, "API connection error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupFinishButton() {
        Button finishButton = findViewById(R.id.finishButton);

        finishButton.setOnClickListener(v -> {

            String userHistory = chessboard.getMoveHistory().trim();
            String correctSolution = puzzle.getMoveHistory().trim();

            if (userHistory.equals(correctSolution)) {
                Toast.makeText(this, "Correct! Congratulations!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, PuzzleMenu.class));
            } else {
                Toast.makeText(this, "Wrong! The board will be reset!", Toast.LENGTH_SHORT).show();

                loadPuzzle(currentPuzzleId);
            }
        });
    }


    private void displayPuzzleInfo()
    {
        if (puzzle == null) return;

        String category = puzzle.getCategory().toString().replace("_", " ");

        String player = puzzle.getPlayerToMove() == PieceColor.WHITE ? "White" : "Black";

        puzzleCategoryText.setText("Category: " + category);
        playerToMoveText.setText("Player to move: " + player);

    }
}

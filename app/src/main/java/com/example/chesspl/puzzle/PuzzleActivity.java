package com.example.chesspl.puzzle;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chesspl.PieceAdapter;
import com.example.chesspl.R;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.PieceColor;
import com.example.chesspl.chessClasses.figureClasses.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PuzzleActivity extends AppCompatActivity {

    private SinglePuzzle puzzle;
    private GridLayout chessboardLayout;
    private Chessboard chessboard;
    private RecyclerView pieceRecyclerView;
    private PieceAdapter pieceAdapter;
    private List<List<ChessField>> boardState = new ArrayList<>();
    private Spinner puzzleCategorySpinner;
    private Spinner startColorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        puzzle = new SinglePuzzle();
        puzzle.setCategory(PuzzleCategory.CHECKMATE_IN_1);
        puzzle.setPlayerToMove(PieceColor.WHITE);
        puzzle.setMoveHistory("");

        fieldsInit();
        puzzle.setFields(boardState);

        pieceRecyclerView = findViewById(R.id.pieceRecyclerView);
        pieceRecyclerView.setVisibility(View.GONE);

        puzzleCategorySpinner = findViewById(R.id.puzzleCategorySpinner);
        startColorSpinner = findViewById(R.id.startColorSpinner);


        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.puzzle_categories,
                R.layout.spinner_dropdown_item
        );
        categoryAdapter.setDropDownViewResource( R.layout.spinner_dropdown_item);
        puzzleCategorySpinner.setAdapter(categoryAdapter);


        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.start_colors,
                R.layout.spinner_dropdown_item
        );
        colorAdapter.setDropDownViewResource( R.layout.spinner_dropdown_item);
        startColorSpinner.setAdapter(colorAdapter);


        chessboardLayout = findViewById(R.id.puzzleBoard);
        chessboard = new Chessboard(true, chessboardLayout, this);

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

        pieceAdapter = new PieceAdapter(pieces, (drawableId, color) -> {
            ChessField field = chessboard.getSelectedField();
            if (field != null) {
                Piece piece = PuzzleHelper.createPieceFromDrawable(
                        drawableId,
                        (color == Color.BLACK) ? PieceColor.BLACK : PieceColor.WHITE
                );

                field.setPiece(piece);

                int rowIndex = 7 - field.getRow();
                int colIndex = field.getCol();
                ChessField boardField = boardState.get(rowIndex).get(colIndex);
                boardField.setSimulatedPiece(piece);

                pieceAdapter.notifyDataSetChanged();

                pieceRecyclerView.setVisibility(View.GONE);
            }
        }, chessboard);

        pieceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        pieceRecyclerView.setAdapter(pieceAdapter);

        PuzzleHelper.generateFields(chessboardLayout, this, chessboard, pieceRecyclerView, pieceAdapter);


        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {


                    int index = row;


                    ChessField activeField = chessboard.getFields().get(index).get(col);


                    ChessField stateField = boardState.get(index).get(col);

                    stateField.setSimulatedPiece(activeField.getPiece());
                }
            }


            puzzle.setFields(boardState);
            chessboard.playerToMove = getSelectedPlayerToMove();


            PuzzleHelper.generateFields2(chessboardLayout, this, chessboard);

            Toast.makeText(this, "Pieces set, record your moves", Toast.LENGTH_LONG).show();
        });


        Button saveMoves = findViewById(R.id.saveMoves);
        saveMoves.setOnClickListener(v -> {
            puzzle.setMoveHistory(chessboard.getMoveHistory());
            PieceColor colorToMove = getSelectedPlayerToMove();
            puzzle.setPlayerToMove(colorToMove);


            PuzzleCategory category = getSelectedPuzzleCategory();
            puzzle.setCategory(category);



            if (category == PuzzleCategory.CHECKMATE_IN_1) {
                String moveHistory = puzzle.getMoveHistory().trim();
                String[] moves = moveHistory.split(" ");


                if (moves.length != 1 || moveHistory.isEmpty()) {
                    Toast.makeText(this, "Validation error: For Checkmate in 1, exactly one move is required!", Toast.LENGTH_LONG).show();
                    return;
                }


                PieceColor matedColor = (colorToMove == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

                if (!chessboard.checkIfMated(matedColor)) {
                    Toast.makeText(this, "Validation error: Move does not lead to Checkmate!" + matedColor.toString() + ".", Toast.LENGTH_LONG).show();
                    resetToSetupMode();
                    return;
                }

                Toast.makeText(this, "Validation: Correct!", Toast.LENGTH_SHORT).show();
            }
            else if(category == PuzzleCategory.FORK)
            {
                if (!chessboard.checkIfForkOccurred()) {
                    Toast.makeText(this, "Validation error: The move did not create a fork on at least two of the opponent's valuable pieces!", Toast.LENGTH_LONG).show();
                    resetToSetupMode();
                    return;
                }

                Toast.makeText(this, "Validation: Correct!", Toast.LENGTH_SHORT).show();
            }


            else if(category == PuzzleCategory.TOWERS_ENDGAME)
            {

                if (!chessboard.isRookEndgameComposition()) {
                    Toast.makeText(this, "Validation error: The board must contain only Kings, Rooks, and Pawns!", Toast.LENGTH_LONG).show();
                    resetToSetupMode();
                    return;
                }

                PieceColor matedColor = chessboard.playerToMove;


                if (!chessboard.checkIfMated(matedColor)) {
                    Toast.makeText(this, "Validation error: Move does not lead to Checkmate!" + matedColor.toString() + ".", Toast.LENGTH_LONG).show();
                    resetToSetupMode();
                    return;
                }

                Toast.makeText(this, "Validation: Correct!", Toast.LENGTH_SHORT).show();
            }


            PuzzleDTO dto = new PuzzleDTO();
            dto.setCategory(puzzle.getCategory().toString());
            dto.setPlayerToMove(puzzle.getPlayerToMove().toString());
            dto.setMoveHistory(puzzle.getMoveHistory());


            dto.setFields(convertBoardToDTO(puzzle.getFields()));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:9000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ChessApi api = retrofit.create(ChessApi.class);

            api.addPuzzle(dto).enqueue(new Callback<PuzzleDTO>() {
                @Override
                public void onResponse(Call<PuzzleDTO> call, Response<PuzzleDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PuzzleActivity.this, "Puzzle saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PuzzleActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PuzzleDTO> call, Throwable t) {
                    Toast.makeText(PuzzleActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        });
    }



    private void resetToSetupMode() {

        chessboard.clearBoard();


        for (List<ChessField> row : boardState) {
            for (ChessField fieldInState : row) {


                Piece pieceToPlace = fieldInState.getPiece();

                if (pieceToPlace != null) {

                    Piece newPiece = PuzzleHelper.createPieceFromTypeAndColor(
                            pieceToPlace.getClass().getSimpleName(),
                            pieceToPlace.getPieceColor()
                    );


                    ChessField targetField = chessboard.getLocation(fieldInState.getCoordinates());

                    targetField.setPiece(newPiece);
                }
            }
        }


        chessboard.playerToMove = getSelectedPlayerToMove();


        chessboard.clearMoveHistory();


        Toast.makeText(this, "Edit or re-record moves!", Toast.LENGTH_LONG).show();
    }



    private PieceDTO convertPiece(Piece p) {
        PieceDTO dto = new PieceDTO();


        dto.setType(p.getClass().getSimpleName().toUpperCase());


        dto.setPieceColor(p.getPieceColor() == PieceColor.WHITE ? "WHITE" : "BLACK");


        dto.setFirstMove(!p.hasMoved());

        return dto;
    }


    private List<List<ChessFieldDTO>> convertBoardToDTO(List<List<ChessField>> board) {
        List<List<ChessFieldDTO>> dtoBoard = new ArrayList<>();

        for (List<ChessField> row : board) {
            List<ChessFieldDTO> dtoRow = new ArrayList<>();

            for (ChessField f : row) {
                ChessFieldDTO dto = new ChessFieldDTO();
                dto.setLetter(f.getCoordinates().substring(0, 1));
                dto.setNumber(f.getCoordinates().substring(1));
                dto.setClicked(f.isClicked());

                if (f.getPiece() != null) {
                    dto.setPiece(convertPiece(f.getPiece()));
                } else {
                    dto.setPiece(null);
                }

                dtoRow.add(dto);
            }

            dtoBoard.add(dtoRow);
        }

        return dtoBoard;
    }


    private void fieldsInit() {
        for (int i = 8; i >= 1; i--) {
            List<ChessField> row = new ArrayList<>();
            for (char j = 'A'; j <= 'H'; j++) {
                row.add(new ChessField(Character.toString(j), Integer.toString(i)));
            }
            boardState.add(row);
        }
    }

    private PieceColor getSelectedPlayerToMove() {
        String selectedColor = startColorSpinner.getSelectedItem().toString();


        if ("White".equals(selectedColor)) {
            return PieceColor.WHITE;
        } else if ("Black".equals(selectedColor)) {
            return PieceColor.BLACK;
        }

        return PieceColor.WHITE;
    }

    private PuzzleCategory getSelectedPuzzleCategory() {
        String selectedCategoryString = puzzleCategorySpinner.getSelectedItem().toString();


        switch (selectedCategoryString) {
            case "Checkmate in 1":
                return PuzzleCategory.CHECKMATE_IN_1;
            case "Fork":
                return PuzzleCategory.FORK;
            case "Towers Endgame":
                return PuzzleCategory.TOWERS_ENDGAME;
            default:
                return PuzzleCategory.CHECKMATE_IN_1;
        }
    }
}

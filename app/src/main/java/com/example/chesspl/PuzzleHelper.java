package com.example.chesspl;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.PieceColor;
import com.example.chesspl.chessClasses.figureClasses.Bishop;
import com.example.chesspl.chessClasses.figureClasses.King;
import com.example.chesspl.chessClasses.figureClasses.Knight;
import com.example.chesspl.chessClasses.figureClasses.Pawn;
import com.example.chesspl.chessClasses.figureClasses.Piece;
import com.example.chesspl.chessClasses.figureClasses.Queen;
import com.example.chesspl.chessClasses.figureClasses.Rook;

import java.util.List;

public class PuzzleHelper {

    public static void generateFields(GridLayout chessboardLayout, Activity activity, Chessboard chessboard, RecyclerView pieceRecyclerView) {
        int size = 8;

        chessboardLayout.post(() -> {
            int cellSize = chessboardLayout.getWidth() / size;

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    FrameLayout square = new FrameLayout(activity);

                    setView(activity, chessboard, row, col, square);
                    setHighlightFilterView(activity, chessboard, square, row, col);
                    setMoveView(activity, chessboard, square, row, col);
                    setPieceView(activity, chessboard, square, row, col);

                    setOnClickAction(square, chessboard, pieceRecyclerView, row, col);

                    addViewToLayout(chessboardLayout, cellSize, row, col, square);
                }
            }
        });
    }


    public static void generateFields2(GridLayout chessboardLayout, Activity activity, Chessboard chessboard) {
        int size = 8;

        chessboardLayout.removeAllViews();

        chessboardLayout.post(() -> {
            int cellSize = chessboardLayout.getWidth() / size;

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    FrameLayout square = new FrameLayout(activity);


                    ChessField field = chessboard.getFields().get(row).get(col);


                    setView(activity, chessboard, row, col, square);
                    setHighlightFilterView(activity, chessboard, square, row, col);
                    setMoveView(activity, chessboard, square, row, col);


                    ImageView pieceView = new ImageView(activity);
                    pieceView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    pieceView.setLayoutParams(new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    ));


                    if (field.getPiece() != null) {
                        int drawableId = PuzzleHelper.getDrawableIdFromPiece(field.getPiece());
                        pieceView.setImageResource(drawableId);
                        pieceView.setVisibility(View.VISIBLE);
                        pieceView.setTag(drawableId);


                        if (field.getPiece().getPieceColor() == PieceColor.BLACK) {
                            pieceView.setColorFilter(Color.BLACK);
                        } else {
                            pieceView.setColorFilter(Color.WHITE);
                        }
                    } else {
                        pieceView.setVisibility(View.GONE);
                    }

                    square.addView(pieceView);
                    field.setGraphic(pieceView);

                    setOnClickAction2(square, chessboard);
                    addViewToLayout(chessboardLayout, cellSize, row, col, square);
                }
            }
        });
    }


    static int getDrawableIdFromPiece(Piece piece) {
        if (piece instanceof Pawn) return R.drawable.pawn;
        if (piece instanceof Rook) return R.drawable.rook;
        if (piece instanceof Knight) return R.drawable.knight;
        if (piece instanceof Bishop) return R.drawable.bishop;
        if (piece instanceof Queen) return R.drawable.queen;
        if (piece instanceof King) return R.drawable.king;
        return 0;
    }


    private static void setView(Activity activity, Chessboard chessboard, int row, int col, FrameLayout square) {
        View tileView = new View(activity);
        tileView.setBackgroundColor(((row + col) % 2 == 0) ? Color.parseColor("#a6a67b") : Color.parseColor("#769656"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(tileView, params);
        chessboard.getFields().get(row).get(col).setView(square);
    }

    private static void setHighlightFilterView(Activity activity, Chessboard chessboard, FrameLayout square, int row, int col) {
        ImageView highlightFilter = new ImageView(activity);
        highlightFilter.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        highlightFilter.setImageResource(0);
        highlightFilter.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(highlightFilter, params);
        chessboard.getFields().get(row).get(col).setHighlightFilterView(highlightFilter);
    }

    private static void setMoveView(Activity activity, Chessboard chessboard, FrameLayout square, int row, int col) {
        ImageView moveView = new ImageView(activity);
        moveView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        moveView.setImageResource(0);
        moveView.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(moveView, params);
        chessboard.getFields().get(row).get(col).setMoveView(moveView);
    }

    private static void setPieceView(Activity activity, Chessboard chessboard, FrameLayout square, int row, int col) {
        ImageView pieceView = new ImageView(activity);
        pieceView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        pieceView.setImageResource(0);
        pieceView.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(pieceView, params);
        chessboard.getFields().get(row).get(col).setGraphic(pieceView);
    }

    private static void setOnClickAction(FrameLayout square, Chessboard chessboard, RecyclerView pieceRecyclerView, int row, int col) {
        square.setOnClickListener(v -> {
            ChessField field = chessboard.getFields().get(row).get(col);
            chessboard.setSelectedField(field);
            pieceRecyclerView.setVisibility(View.VISIBLE);
        });
    }

    private static void setOnClickAction2(FrameLayout view, Chessboard chessboard) {
        view.setOnClickListener(chessboard::onClick);
    }


    private static void addViewToLayout(GridLayout chessboardLayout, int cellSize, int row, int col, FrameLayout square) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = cellSize;
        params.height = cellSize;
        params.rowSpec = GridLayout.spec(row);
        params.columnSpec = GridLayout.spec(col);

        chessboardLayout.addView(square, params);
    }

    public static Piece createPieceFromDrawable(int drawableId, PieceColor color)
    {
        if (drawableId == R.drawable.pawn) return new Pawn(color);
        if (drawableId == R.drawable.rook) return new Rook(color);
        if (drawableId == R.drawable.knight) return new Knight(color);
        if (drawableId == R.drawable.bishop) return new Bishop(color);
        if (drawableId == R.drawable.queen) return new Queen(color);
        if (drawableId == R.drawable.king) return new King(color);
        return null;
    }


}

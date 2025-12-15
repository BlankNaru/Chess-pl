package com.example.chesspl;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.chesspl.chessClasses.Chessboard;

public class ChessHelper {


    public static void generateFields(GridLayout chessboardLayout, Activity activity, Chessboard chessboard)
    {

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

                    setOnClickAction(square, chessboard);
                    addViewToLayout(chessboardLayout, cellSize, row, col, square);
                    

                }
            }
            chessboard.piecesInit();
        });
    }

    public static void generateFields(GridLayout chessboardLayout, Activity activity, Chessboard chessboard, boolean isWhite)
    {

        if(isWhite)
        {
            generateFields(chessboardLayout, activity, chessboard);
            return;
        }

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

                    setOnClickAction(square, chessboard);
                    addViewToLayout(chessboardLayout, cellSize, 7 - row, 7 - col, square);


                }
            }
            chessboard.piecesInit();
        });
    }

    private static void setOnClickAction(FrameLayout view, Chessboard chessboard) {
        view.setOnClickListener(chessboard::onClick);
    }

    private static void setHighlightFilterView(Activity activity, Chessboard chessboard, FrameLayout square, int row, int col) {
        ImageView highlightFilter = new ImageView(activity);
        highlightFilter.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        highlightFilter.setImageResource(0);
        highlightFilter.setVisibility(View.GONE);
        FrameLayout.LayoutParams pieceParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(highlightFilter, pieceParams);
        chessboard.getFields().get(row).get(col).setHighlightFilterView(highlightFilter);
    }

    private static void setMoveView(Activity activity, Chessboard chessboard, FrameLayout square, int row, int col) {
        ImageView moveView = new ImageView(activity);
        moveView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        moveView.setImageResource(0);
        moveView.setVisibility(View.GONE);
        FrameLayout.LayoutParams pieceParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(moveView, pieceParams);
        chessboard.getFields().get(row).get(col).setMoveView(moveView);
    }

    private static void addViewToLayout(GridLayout chessboardLayout, int cellSize, int row, int col, FrameLayout square) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = cellSize;
        params.height = cellSize;
        params.rowSpec = GridLayout.spec(row);
        params.columnSpec = GridLayout.spec(col);

        chessboardLayout.addView(square, params);
    }

    private static void setPieceView(Activity activity, Chessboard chessboard, FrameLayout square, int row, int col) {
        ImageView pieceView = new ImageView(activity);
        pieceView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        pieceView.setImageResource(0);
        pieceView.setVisibility(View.GONE);
        FrameLayout.LayoutParams pieceParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(pieceView, pieceParams);
        chessboard.getFields().get(row).get(col).setGraphic(pieceView);
    }

    private static void setView(Activity activity, Chessboard chessboard, int row, int col, FrameLayout square) {
        View tileView = new View(activity);
        tileView.setBackgroundColor(
                ((row + col) % 2 == 0) ? Color.parseColor("#a6a67b") : Color.parseColor("#769656")
        );
        FrameLayout.LayoutParams tileParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        square.addView(tileView, tileParams);
        chessboard.getFields().get(row).get(col).setView(square);
    }
}

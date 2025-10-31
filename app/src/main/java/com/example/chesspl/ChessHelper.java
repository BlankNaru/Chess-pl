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

                    View tileView = new View(activity);
                    tileView.setBackgroundColor(
                            ((row + col) % 2 == 0) ? Color.parseColor("#EEEED2") : Color.parseColor("#769656")
                    );
                    FrameLayout.LayoutParams tileParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    );
                    square.addView(tileView, tileParams);

                    ImageView pieceView = new ImageView(activity);
                    pieceView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    pieceView.setImageResource(R.drawable.king);
                    pieceView.setVisibility(View.GONE);
                    FrameLayout.LayoutParams pieceParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    );
                    square.addView(pieceView, pieceParams);

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = cellSize;
                    params.height = cellSize;
                    params.rowSpec = GridLayout.spec(row);
                    params.columnSpec = GridLayout.spec(col);

                    chessboardLayout.addView(square, params);
                    chessboard.getFields().get(row).get(col).setGraphic(square, pieceView);
                }
            }
            chessboard.piecesInit();
        });
    }
}

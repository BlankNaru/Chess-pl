package com.example.chesspl;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.ChessField;

public class ChessHelper
{

    public static void generateFields(android.widget.GridLayout chessboardLayout, Activity activity,
                                      Chessboard chessboard, Runnable onReady)
    {
        int size = 8;

        chessboardLayout.post(() -> {
            int cellSize = chessboardLayout.getWidth() / size;

            for(int row = 0; row < size; row++)
            {
                for(int col = 0; col < size; col++)
                {

                    FrameLayout square = new FrameLayout(activity);

                    View tileView = new View(activity);
                    tileView.setBackgroundColor(
                            ((row + col) % 2 == 0)
                                    ? android.graphics.Color.parseColor("#EEEED2")
                                    : android.graphics.Color.parseColor("#769656")
                    );

                    FrameLayout.LayoutParams tileParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    );
                    square.addView(tileView, tileParams);

                    android.widget.ImageView pieceView = new android.widget.ImageView(activity);
                    pieceView.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
                    pieceView.setVisibility(View.GONE);

                    FrameLayout.LayoutParams pieceParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                    );
                    square.addView(pieceView, pieceParams);

                    android.widget.GridLayout.LayoutParams params = new android.widget.GridLayout.LayoutParams();
                    params.width = cellSize;
                    params.height = cellSize;
                    params.rowSpec = android.widget.GridLayout.spec(row);
                    params.columnSpec = android.widget.GridLayout.spec(col);

                    chessboardLayout.addView(square, params);
                    chessboard.getFields().get(row).get(col).setGraphic(square, pieceView);
                }
            }

            if(onReady != null)
            {
                onReady.run();
            }
        });
    }

    public static void enableFieldClickListeners(Chessboard chessboard, RecyclerView pieceRecyclerView)
    {
        int size = 8;

        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {

                FrameLayout square = chessboard.getFields().get(row).get(col).getView();
                ChessField field = chessboard.getFields().get(row).get(col);

                square.setOnClickListener(v ->
                {
                    chessboard.setSelectedField(field);
                    pieceRecyclerView.setVisibility(View.VISIBLE);
                });
            }
        }
    }
}

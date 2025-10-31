package com.example.chesspl.chessClasses;

import com.example.chesspl.R;

public class Queen implements Piece {
    private Color color;

    public Queen(Color color)
    {
        this.color = color;
    }

    @Override
    public int getDrawable() {
        return R.drawable.queen;
    }
}

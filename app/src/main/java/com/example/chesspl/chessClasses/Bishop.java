package com.example.chesspl.chessClasses;

import com.example.chesspl.R;

public class Bishop implements Piece {
    private Color color;

    public Bishop(Color color)
    {
        this.color = color;
    }

    @Override
    public int getDrawable() {
        return R.drawable.bishop;
    }
}

package com.example.chesspl.chessClasses;

import com.example.chesspl.R;

public class Rook implements Piece {
    private Color color;

    public Rook(Color color)
    {
        this.color = color;
    }

    @Override
    public int getDrawable() {
        return R.drawable.rook;
    }
}

package com.example.chesspl.chessClasses;

import com.example.chesspl.R;

public class Pawn implements Piece{
    private Color color;

    public Pawn(Color color)
    {
        this.color = color;
    }

    @Override
    public int getDrawable() {
        return R.drawable.pawn;
    }
}

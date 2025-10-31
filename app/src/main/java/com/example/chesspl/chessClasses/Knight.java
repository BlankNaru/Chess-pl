package com.example.chesspl.chessClasses;

import com.example.chesspl.R;

public class Knight implements Piece{

    private Color color;

    public Knight(Color color)
    {
        this.color = color;
    }

    @Override
    public int getDrawable() {
        return R.drawable.knight;
    }
}

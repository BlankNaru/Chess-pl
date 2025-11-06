package com.example.chesspl.chessClasses;

import android.graphics.drawable.Drawable;

import com.example.chesspl.R;

public class King implements Piece{
    private Color color;

    public King(Color color)
    {
        this.color = color;
    }


    @Override
    public int getDrawable() {
        return R.drawable.king;
    }
}

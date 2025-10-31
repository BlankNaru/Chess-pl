package com.example.chesspl.chessClasses;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.chesspl.R;

public class ChessField {
    private String letter;
    private String number;
    private Piece piece = null;
    private FrameLayout view;
    private ImageView pieceView;

    public ChessField(String field)
    {
        letter = field.substring(0,1);
        number = field.substring(1);
    }

    public ChessField(String letter, String number)
    {
        this.letter = letter;
        this.number = number;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        pieceView.setImageResource(piece.getDrawable());
        pieceView.setVisibility(View.VISIBLE);
    }
    public void clearPiece()
    {
        pieceView.setVisibility(View.GONE);
        this.piece = null;
    }


    public String getCoordinates()
    {
        return letter + number;
    }

    public void setGraphic(FrameLayout view, ImageView pieceView) {
        this.view = view;
        this.pieceView = pieceView;
    }
}

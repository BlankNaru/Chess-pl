package com.example.chesspl.chessClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Knight implements Piece{

    private PieceColor pieceColor;

    public Knight(PieceColor pieceColor)
    {
        this.pieceColor = pieceColor;
    }

    @Override
    public int getDrawable() {
        return R.drawable.knight;
    }

    @Override
    public void setPiece(ImageView pieceView) {
        pieceView.setImageResource(getDrawable());
        pieceView.setColorFilter(getColor());
        pieceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoves(Chessboard chessboard) {
        List<ChessField> moves = chessboard.getLShaped(chessboard.getLocation(this));
        moves = moves.stream().filter(f -> f.isEmpty() || !f.getPiece().getPieceColor().equals(pieceColor)).collect(Collectors.toList());

        chessboard.setPossibleMoves(moves);
    }

    @Override
    public void setAsMoved() {

    }

    private int getColor()
    {
        if(pieceColor == PieceColor.BLACK)
            return Color.BLACK;
        return Color.WHITE;
    }

    @Override
    public PieceColor getPieceColor() {
        return pieceColor;
    }
}

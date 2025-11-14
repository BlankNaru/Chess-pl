package com.example.chesspl.chessClasses.figureClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.PieceColor;

import java.util.List;
import java.util.stream.Collectors;

public class Knight implements Piece {

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
    public List<ChessField> getMoves(Chessboard chessboard, boolean skipEnemyKing, boolean includeProtected, boolean includeDiscoveredCheck) {
        List<ChessField> moves = chessboard.getLShaped(chessboard.getLocation(this));
        moves = moves.stream().filter(f -> f.isEmpty() || !f.getPiece().getPieceColor().equals(pieceColor) || includeProtected).collect(Collectors.toList());

        return moves;
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

    @Override
    public boolean hasMoved() {
        return false;
    }
}

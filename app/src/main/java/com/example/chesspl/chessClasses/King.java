package com.example.chesspl.chessClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;

import java.util.ArrayList;
import java.util.List;

public class King implements Piece {
    private PieceColor pieceColor;
    private boolean isFirstMove = true;

    public King(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public int getDrawable() {
        return R.drawable.king;
    }


    @Override
    public void setPiece(ImageView pieceView) {
        pieceView.setImageResource(getDrawable());
        pieceView.setColorFilter(getColor());
        pieceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoves(Chessboard chessboard) {
        List<ChessField> moves = new ArrayList<>();
        ChessField currentPosition = chessboard.getLocation(this);
        ChessField upperPosition = chessboard.getUpperField(currentPosition);
        ChessField lowerPosition = chessboard.getLowerField(currentPosition);
        ChessField rightPosition = chessboard.getRightField(currentPosition);
        ChessField leftPosition = chessboard.getLeftField(currentPosition);
        ChessField upperRightPosition = chessboard.getRightField(upperPosition);
        ChessField upperLeftPosition = chessboard.getLeftField(upperPosition);
        ChessField lowerRightPosition = chessboard.getRightField(lowerPosition);
        ChessField lowerLeftPosition = chessboard.getLeftField(lowerPosition);
        if (upperPosition != null && (upperPosition.isEmpty() || !upperPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(upperPosition);
        if (lowerPosition != null && (lowerPosition.isEmpty() || !lowerPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(lowerPosition);
        if (rightPosition != null && (rightPosition.isEmpty() || !rightPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(rightPosition);
        if (leftPosition != null && (leftPosition.isEmpty() || !leftPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(leftPosition);
        if (upperRightPosition != null && (upperRightPosition.isEmpty() || !upperRightPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(upperRightPosition);
        if (upperLeftPosition != null && (upperLeftPosition.isEmpty() || !upperLeftPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(upperLeftPosition);
        if (lowerRightPosition != null && (lowerRightPosition.isEmpty() || !lowerRightPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(lowerRightPosition);
        if (lowerLeftPosition != null && (lowerLeftPosition.isEmpty() || !lowerLeftPosition.getPiece().getPieceColor().equals(pieceColor)))
            moves.add(lowerLeftPosition);
        chessboard.setPossibleMoves(moves);
    }

    @Override
    public void setAsMoved() {
        isFirstMove = false;
    }

    private int getColor() {
        if (pieceColor == PieceColor.BLACK)
            return Color.BLACK;
        return Color.WHITE;
    }
    @Override
    public PieceColor getPieceColor() {
        return pieceColor;
    }
}

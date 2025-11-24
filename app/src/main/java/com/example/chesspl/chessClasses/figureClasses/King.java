package com.example.chesspl.chessClasses.figureClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.GameType;
import com.example.chesspl.chessClasses.PieceColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void setPiece(ImageView pieceView, GameType gameType) {
        if(gameType == GameType.LOCAL && getPieceColor() == PieceColor.BLACK)
            pieceView.setRotation(180f);
        pieceView.setImageResource(getDrawable());
        pieceView.setColorFilter(getColor());
        pieceView.setVisibility(View.VISIBLE);
    }

    @Override
    public List<ChessField> getMoves(Chessboard chessboard, boolean skipEnemyKing, boolean includeProtected, boolean includeDiscoveredCheck) {
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
        if ((upperPosition != null && includeProtected) || (upperPosition != null && (upperPosition.isEmpty() || !upperPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(upperPosition);
        if ((upperPosition != null && includeProtected) || (lowerPosition != null && (lowerPosition.isEmpty() || !lowerPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(lowerPosition);
        if ((upperPosition != null && includeProtected) || (rightPosition != null && (rightPosition.isEmpty() || !rightPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(rightPosition);
        if ((upperPosition != null && includeProtected) || (leftPosition != null && (leftPosition.isEmpty() || !leftPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(leftPosition);
        if ((upperPosition != null && includeProtected) || (upperRightPosition != null && (upperRightPosition.isEmpty() || !upperRightPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(upperRightPosition);
        if ((upperPosition != null && includeProtected) || (upperLeftPosition != null && (upperLeftPosition.isEmpty() || !upperLeftPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(upperLeftPosition);
        if ((upperPosition != null && includeProtected) || (lowerRightPosition != null && (lowerRightPosition.isEmpty() || !lowerRightPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(lowerRightPosition);
        if ((upperPosition != null && includeProtected) || (lowerLeftPosition != null && (lowerLeftPosition.isEmpty() || !lowerLeftPosition.getPiece().getPieceColor().equals(pieceColor))))
            moves.add(lowerLeftPosition);
        if(includeDiscoveredCheck)
        {
            List<ChessField> notAvailableFields = chessboard.getAllMovesForColor(getEnemyColor());
            moves = moves.stream().filter(move -> !notAvailableFields.contains(move)).collect(Collectors.toList());
        }
        return moves;
    }

    @Override
    public void setAsMoved(int distance) {
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

    @Override
    public boolean hasMoved() {
        return !isFirstMove;
    }

    public PieceColor getEnemyColor()
    {
        if(pieceColor == PieceColor.WHITE)
            return PieceColor.BLACK;
        return PieceColor.WHITE;
    }
}

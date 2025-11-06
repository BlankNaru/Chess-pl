package com.example.chesspl.chessClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;

import java.util.ArrayList;
import java.util.List;

public class Pawn implements Piece{
    private boolean isFirstMove = true;
    private PieceColor pieceColor;

    public Pawn(PieceColor pieceColor)
    {
        this.pieceColor = pieceColor;
    }

    @Override
    public int getDrawable() {
        return R.drawable.pawn;
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
        ChessField currentField = chessboard.getLocation(this);
        if(pieceColor.equals(PieceColor.WHITE))
        {
            ChessField fieldForward = chessboard.getUpperField(currentField);
            if(fieldForward != null && fieldForward.isEmpty())
            {
                moves.add(fieldForward);
                ChessField fieldDoubleForward = chessboard.getUpperField(fieldForward);
                if(isFirstMove && fieldDoubleForward != null && fieldDoubleForward.isEmpty())
                    moves.add(fieldDoubleForward);
//                ChessField rightAttack = chessboard.getUpperRightField(currentField);
//                ChessField leftAttack = chessboard.getUpperLeftField(currentField);
//                if(rightAttack != null && rightAttack.isEmpty())
//                    moves.add(rightAttack);
//                if(leftAttack != null && leftAttack.isEmpty())
//                    moves.add(leftAttack);
            }
        }
        else
        {
            ChessField fieldForward = chessboard.getLowerField(currentField);
            if(fieldForward != null && fieldForward.isEmpty())
            {
                moves.add(fieldForward);
                ChessField fieldDoubleForward = chessboard.getLowerField(fieldForward);
                if(isFirstMove && fieldDoubleForward != null && fieldDoubleForward.isEmpty())
                    moves.add(fieldDoubleForward);
//                ChessField rightAttack = chessboard.getLowerRightField(currentField);
//                ChessField leftAttack = chessboard.getLowerLeftField(currentField);
//                if(rightAttack != null && rightAttack.isEmpty())
//                    moves.add(rightAttack);
//                if(leftAttack != null && leftAttack.isEmpty())
//                    moves.add(leftAttack);
            }
        }
        chessboard.setPossibleMoves(moves);
    }

    private int getColor()
    {
        if(pieceColor == PieceColor.BLACK)
            return Color.BLACK;
        return Color.WHITE;
    }
    @Override
    public void setAsMoved()
    {
        isFirstMove = false;
    }

    @Override
    public PieceColor getPieceColor() {
        return pieceColor;
    }
}

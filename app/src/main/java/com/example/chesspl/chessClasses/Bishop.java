package com.example.chesspl.chessClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;

import java.util.ArrayList;
import java.util.List;

public class Bishop implements Piece {
    private PieceColor pieceColor;

    public Bishop(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public int getDrawable() {
        return R.drawable.bishop;
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

        ChessField upperRightField = chessboard.getUpperRightField(currentField);
        while(upperRightField!=null)
        {
            if(upperRightField.isEmpty())
            {
                moves.add(upperRightField);
                upperRightField = chessboard.getUpperRightField(upperRightField);
                continue;
            }
            if(upperRightField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(upperRightField);
            break;
        }

        ChessField upperLeftField = chessboard.getUpperLeftField(currentField);
        while(upperLeftField!=null)
        {
            if(upperLeftField.isEmpty())
            {
                moves.add(upperLeftField);
                upperLeftField = chessboard.getUpperLeftField(upperLeftField);
                continue;
            }
            if(upperLeftField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(upperLeftField);
            break;
        }

        ChessField lowerLeftField = chessboard.getLowerLeftField(currentField);
        while(lowerLeftField!=null)
        {
            if(lowerLeftField.isEmpty())
            {
                moves.add(lowerLeftField);
                lowerLeftField = chessboard.getLowerLeftField(lowerLeftField);
                continue;
            }
            if(lowerLeftField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(lowerLeftField);
            break;
        }

        ChessField lowerRightField = chessboard.getLowerRightField(currentField);
        while(lowerRightField!=null)
        {
            if(lowerRightField.isEmpty())
            {
                moves.add(lowerRightField);
                lowerRightField = chessboard.getLowerRightField(lowerRightField);
                continue;
            }
            if(lowerRightField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(lowerRightField);
            break;
        }

        chessboard.setPossibleMoves(moves);
    }

    @Override
    public void setAsMoved() {

    }

    @Override
    public PieceColor getPieceColor() {
        return pieceColor;
    }

    private int getColor() {
        if (pieceColor == PieceColor.BLACK)
            return Color.BLACK;
        return Color.WHITE;
    }
}

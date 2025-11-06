package com.example.chesspl.chessClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;

import java.util.ArrayList;
import java.util.List;

public class Rook implements Piece {
    private PieceColor pieceColor;
    private boolean isFirstMove = true;

    public Rook(PieceColor pieceColor)
    {
        this.pieceColor = pieceColor;
    }

    @Override
    public int getDrawable() {
        return R.drawable.rook;
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

        ChessField upperField = chessboard.getUpperField(currentField);
        while(upperField!=null)
        {
            if(upperField.isEmpty())
            {
                moves.add(upperField);
                upperField = chessboard.getUpperField(upperField);
                continue;
            }
            if(upperField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(upperField);
            break;
        }

        ChessField leftField = chessboard.getLeftField(currentField);
        while(leftField!=null)
        {
            if(leftField.isEmpty())
            {
                moves.add(leftField);
                leftField = chessboard.getLeftField(leftField);
                continue;
            }
            if(leftField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(leftField);
            break;
        }

        ChessField lowerField = chessboard.getLowerField(currentField);
        while(lowerField!=null)
        {
            if(lowerField.isEmpty())
            {
                moves.add(lowerField);
                lowerField = chessboard.getLowerField(lowerField);
                continue;
            }
            if(lowerField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(lowerField);
            break;
        }

        ChessField rightField = chessboard.getRightField(currentField);
        while(rightField!=null)
        {
            if(rightField.isEmpty())
            {
                moves.add(rightField);
                rightField = chessboard.getRightField(rightField);
                continue;
            }
            if(rightField.getPiece().getPieceColor().equals(pieceColor))
                break;
            else
                moves.add(rightField);
            break;
        }

        chessboard.setPossibleMoves(moves);
    }

    @Override
    public void setAsMoved() {
        isFirstMove = false;
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

package com.example.chesspl.chessClasses.figureClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.GameType;
import com.example.chesspl.chessClasses.PieceColor;
import com.example.chesspl.chessClasses.Simulation;

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
        ChessField currentField = chessboard.getLocation(this);

        ChessField upperRightField = chessboard.getUpperRightField(currentField);
        while(upperRightField!=null)
        {
            if(upperRightField.isEmpty() || (skipEnemyKing && upperRightField.getPiece() instanceof King))
            {
                moves.add(upperRightField);
                upperRightField = chessboard.getUpperRightField(upperRightField);
                continue;
            }
            if(upperRightField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(upperRightField);
                break;
            }
            else
                moves.add(upperRightField);
            break;
        }

        ChessField upperLeftField = chessboard.getUpperLeftField(currentField);
        while(upperLeftField!=null)
        {
            if(upperLeftField.isEmpty() || (skipEnemyKing && upperLeftField.getPiece() instanceof King))
            {
                moves.add(upperLeftField);
                upperLeftField = chessboard.getUpperLeftField(upperLeftField);
                continue;
            }
            if(upperLeftField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(upperLeftField);
                break;
            }
            else
                moves.add(upperLeftField);
            break;
        }

        ChessField lowerLeftField = chessboard.getLowerLeftField(currentField);
        while(lowerLeftField!=null)
        {
            if(lowerLeftField.isEmpty() || (skipEnemyKing && lowerLeftField.getPiece() instanceof King))
            {
                moves.add(lowerLeftField);
                lowerLeftField = chessboard.getLowerLeftField(lowerLeftField);
                continue;
            }
            if(lowerLeftField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(lowerLeftField);
                break;
            }
            else
                moves.add(lowerLeftField);
            break;
        }

        ChessField lowerRightField = chessboard.getLowerRightField(currentField);
        while(lowerRightField!=null)
        {
            if(lowerRightField.isEmpty() || (skipEnemyKing && lowerRightField.getPiece() instanceof King))
            {
                moves.add(lowerRightField);
                lowerRightField = chessboard.getLowerRightField(lowerRightField);
                continue;
            }
            if(lowerRightField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(lowerRightField);
                break;
            }
            else
                moves.add(lowerRightField);
            break;
        }

        if(includeDiscoveredCheck)
        {
            List<ChessField> resultInCheckFields = new ArrayList<>();
            for(ChessField move : moves)
            {
                Simulation simulation = new Simulation();
                simulation.startSimulation(chessboard.getLocation(this), move);
                if(chessboard.checkIfChecked(getPieceColor()))
                    resultInCheckFields.add(move);
                simulation.stopSimulation();
            }
            moves.removeAll(resultInCheckFields);
        }

        return moves;
    }

    @Override
    public void setAsMoved() {

    }

    @Override
    public PieceColor getPieceColor() {
        return pieceColor;
    }

    @Override
    public boolean hasMoved() {
        return false;
    }

    private int getColor() {
        if (pieceColor == PieceColor.BLACK)
            return Color.BLACK;
        return Color.WHITE;
    }
}

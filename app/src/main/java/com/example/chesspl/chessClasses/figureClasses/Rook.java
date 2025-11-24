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

        ChessField upperField = chessboard.getUpperField(currentField);
        while(upperField!=null)
        {
            if(upperField.isEmpty() || (skipEnemyKing && upperField.getPiece() instanceof King))
            {
                moves.add(upperField);
                upperField = chessboard.getUpperField(upperField);
                continue;
            }
            if(upperField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(upperField);
                break;
            }
            else
                moves.add(upperField);
            break;
        }

        ChessField leftField = chessboard.getLeftField(currentField);
        while(leftField!=null)
        {
            if(leftField.isEmpty() || (skipEnemyKing && leftField.getPiece() instanceof King))
            {
                moves.add(leftField);
                leftField = chessboard.getLeftField(leftField);
                continue;
            }
            if(leftField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(leftField);
                break;
            }
            else
                moves.add(leftField);
            break;
        }

        ChessField lowerField = chessboard.getLowerField(currentField);
        while(lowerField!=null)
        {
            if(lowerField.isEmpty() || (skipEnemyKing && lowerField.getPiece() instanceof King))
            {
                moves.add(lowerField);
                lowerField = chessboard.getLowerField(lowerField);
                continue;
            }
            if(lowerField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(lowerField);
                break;
            }
            else
                moves.add(lowerField);
            break;
        }

        ChessField rightField = chessboard.getRightField(currentField);
        while(rightField!=null)
        {
            if(rightField.isEmpty() || (skipEnemyKing && rightField.getPiece() instanceof King))
            {
                moves.add(rightField);
                rightField = chessboard.getRightField(rightField);
                continue;
            }
            if(rightField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(rightField);
                break;
            }
            else
                moves.add(rightField);
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
    public void setAsMoved(int distance) {
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

    @Override
    public boolean hasMoved() {
        return !isFirstMove;
    }
}

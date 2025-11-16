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

public class Pawn implements Piece {
    private boolean isFirstMove = true;
    private boolean isEnPassantPossible = false;
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
        if(pieceColor.equals(PieceColor.WHITE))
        {
            if(!includeProtected)
            {
                ChessField fieldForward = chessboard.getUpperField(currentField);
                if(fieldForward != null && fieldForward.isEmpty())
                {
                    moves.add(fieldForward);
                    if(isFirstMove)
                    {
                        ChessField fieldDoubleForward = chessboard.getUpperField(fieldForward);
                        if(isFirstMove && fieldDoubleForward != null && fieldDoubleForward.isEmpty())
                            moves.add(fieldDoubleForward);
                    }
                }
            }

            ChessField rightAttack = chessboard.getUpperRightField(currentField);
            ChessField leftAttack = chessboard.getUpperLeftField(currentField);
            if(rightAttack != null && rightAttack.getPiece() != null)
                if(rightAttack.getPiece().getPieceColor().equals(PieceColor.BLACK) || (rightAttack.getPiece().getPieceColor().equals(PieceColor.WHITE) && includeProtected))
                    moves.add(rightAttack);
            if(leftAttack != null && leftAttack.getPiece() != null)
                if(leftAttack.getPiece().getPieceColor().equals(PieceColor.BLACK) || (leftAttack.getPiece().getPieceColor().equals(PieceColor.WHITE) && includeProtected))
                    moves.add(leftAttack);
        }
        else
        {
            if(!includeProtected) {
                ChessField fieldForward = chessboard.getLowerField(currentField);
                if (fieldForward != null && fieldForward.isEmpty()) {
                    moves.add(fieldForward);
                    if (isFirstMove) {
                        ChessField fieldDoubleForward = chessboard.getLowerField(fieldForward);
                        if (isFirstMove && fieldDoubleForward != null && fieldDoubleForward.isEmpty())
                            moves.add(fieldDoubleForward);
                    }
                }
            }
            ChessField rightAttack = chessboard.getLowerRightField(currentField);
            ChessField leftAttack = chessboard.getLowerLeftField(currentField);
            if(rightAttack != null && rightAttack.getPiece() != null)
                if(rightAttack.getPiece().getPieceColor().equals(PieceColor.WHITE) || (rightAttack.getPiece().getPieceColor().equals(PieceColor.BLACK) && includeProtected))
                    moves.add(rightAttack);
            if(leftAttack != null && leftAttack.getPiece() != null)
                if(leftAttack.getPiece().getPieceColor().equals(PieceColor.WHITE) || (leftAttack.getPiece().getPieceColor().equals(PieceColor.BLACK) && includeProtected))
                    moves.add(leftAttack);
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

    @Override
    public boolean hasMoved() {
        return !isFirstMove;
    }

}

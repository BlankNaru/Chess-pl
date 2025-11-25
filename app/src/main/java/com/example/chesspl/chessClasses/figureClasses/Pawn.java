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
import java.util.stream.Collectors;

public class Pawn implements Piece {
    private boolean isFirstMove = true;
    private boolean isEnPassantPossible = false;
    private PieceColor pieceColor;

    public Pawn(PieceColor pieceColor) {
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
        if (gameType == GameType.LOCAL && getPieceColor() == PieceColor.BLACK)
            pieceView.setRotation(180f);
        pieceView.setImageResource(getDrawable());
        pieceView.setColorFilter(getColor());
        pieceView.setVisibility(View.VISIBLE);
    }
    public boolean isEnPassantPossible()
    {
        return isEnPassantPossible;
    }
    public boolean enPassantOnRight(Chessboard chessboard, ChessField currentField)
    {
        if(chessboard.getRightField(currentField) == null || chessboard.getRightField(currentField).getPiece() == null)
            return false;
        return chessboard.getRightField(currentField).getPiece() instanceof Pawn && ((Pawn) chessboard.getRightField(currentField).getPiece()).isEnPassantPossible();
    }

    public boolean enPassantOnLeft(Chessboard chessboard, ChessField currentField)
    {
        if(chessboard.getLeftField(currentField) == null || chessboard.getLeftField(currentField).getPiece() == null)
            return false;
        return chessboard.getLeftField(currentField).getPiece() instanceof Pawn && ((Pawn) chessboard.getLeftField(currentField).getPiece()).isEnPassantPossible();
    }

    @Override
    public List<ChessField> getMoves(Chessboard chessboard, boolean skipEnemyKing, boolean includeProtected, boolean includeDiscoveredCheck) {
        List<ChessField> moves = new ArrayList<>();
        ChessField currentField = chessboard.getLocation(this);
        if (pieceColor.equals(PieceColor.WHITE)) {

            ChessField fieldForward = chessboard.getUpperField(currentField);
            if (!includeProtected && fieldForward != null && fieldForward.isEmpty()) {
                moves.add(fieldForward);
                if (isFirstMove) {
                    ChessField fieldDoubleForward = chessboard.getUpperField(fieldForward);
                    if (isFirstMove && fieldDoubleForward != null && fieldDoubleForward.isEmpty())
                        moves.add(fieldDoubleForward);
                }
            }


            ChessField rightAttack = chessboard.getUpperRightField(currentField);
            ChessField leftAttack = chessboard.getUpperLeftField(currentField);
            if (rightAttack != null && rightAttack.getPiece() != null)
                if (rightAttack.getPiece().getPieceColor().equals(PieceColor.BLACK))
                    moves.add(rightAttack);
            if (rightAttack != null && rightAttack.getPiece() == null)
                if (chessboard.getRightField(currentField).getPiece() instanceof Pawn && ((Pawn) chessboard.getRightField(currentField).getPiece()).isEnPassantPossible() )
                    moves.add(rightAttack);
            if (rightAttack != null && includeProtected)
                moves.add(rightAttack);
            if (leftAttack != null && leftAttack.getPiece() != null)
                if (leftAttack.getPiece().getPieceColor().equals(PieceColor.BLACK))
                    moves.add(leftAttack);
            if (leftAttack != null && leftAttack.getPiece() == null)
                if (chessboard.getLeftField(currentField).getPiece() instanceof Pawn && ((Pawn) chessboard.getLeftField(currentField).getPiece()).isEnPassantPossible() )
                    moves.add(leftAttack);
            if (leftAttack != null && includeProtected)
                moves.add(leftAttack);
        } else {

            ChessField fieldForward = chessboard.getLowerField(currentField);
            if (!includeProtected && fieldForward != null && fieldForward.isEmpty()) {
                moves.add(fieldForward);
                if (isFirstMove) {
                    ChessField fieldDoubleForward = chessboard.getLowerField(fieldForward);
                    if (isFirstMove && fieldDoubleForward != null && fieldDoubleForward.isEmpty())
                        moves.add(fieldDoubleForward);
                }
            }
            ChessField rightAttack = chessboard.getLowerRightField(currentField);
            ChessField leftAttack = chessboard.getLowerLeftField(currentField);
            if (rightAttack != null && rightAttack.getPiece() != null)
                if (rightAttack.getPiece().getPieceColor().equals(PieceColor.WHITE))
                    moves.add(rightAttack);
            if (rightAttack != null && rightAttack.getPiece() == null)
                if (chessboard.getRightField(currentField).getPiece() instanceof Pawn && ((Pawn) chessboard.getRightField(currentField).getPiece()).isEnPassantPossible() )
                    moves.add(rightAttack);
            if (rightAttack != null && includeProtected)
                moves.add(rightAttack);
            if (leftAttack != null && leftAttack.getPiece() != null)
                if (leftAttack.getPiece().getPieceColor().equals(PieceColor.WHITE))
                    moves.add(leftAttack);
            if (leftAttack != null && leftAttack.getPiece() == null)
                if (chessboard.getLeftField(currentField).getPiece() instanceof Pawn && ((Pawn) chessboard.getLeftField(currentField).getPiece()).isEnPassantPossible() )
                    moves.add(leftAttack);
            if (leftAttack != null && includeProtected)
                moves.add(leftAttack);
        }
        moves = moves.stream().distinct().collect(Collectors.toList());

        if (includeDiscoveredCheck) {
            List<ChessField> resultInCheckFields = new ArrayList<>();
            for (ChessField move : moves) {
                Simulation simulation = new Simulation();
                simulation.startSimulation(chessboard.getLocation(this), move);
                if (chessboard.checkIfChecked(getPieceColor()))
                    resultInCheckFields.add(move);
                simulation.stopSimulation();
            }
            moves.removeAll(resultInCheckFields);
        }

        return moves;
    }

    private int getColor() {
        if (pieceColor == PieceColor.BLACK)
            return Color.BLACK;
        return Color.WHITE;
    }

    @Override
    public void setAsMoved(int distance) {
        if (distance == 0)
        {
            isEnPassantPossible = false;
            return;
        }
        if (distance == 2)
            isEnPassantPossible = true;
        if(!isFirstMove)
            isEnPassantPossible = false;
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

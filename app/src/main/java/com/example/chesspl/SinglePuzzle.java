package com.example.chesspl;

import static com.example.chesspl.chessClasses.PieceColor.WHITE;

import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.PieceColor;

import java.util.List;

public class SinglePuzzle
{

    private List<List<ChessField>> fields;
    private PieceColor playerToMove = WHITE;
    private PuzzleCategory category;

    public String getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(String moveHistory) {
        this.moveHistory = moveHistory;
    }

    private String moveHistory = "";


    public SinglePuzzle() {}


    public SinglePuzzle(List<List<ChessField>> fields, PieceColor playerToMove, PuzzleCategory category) {
        this.fields = fields;
        this.playerToMove = playerToMove;
        this.category = category;
    }

    public List<List<ChessField>> getFields() {
        return fields;
    }

    public void setFields(List<List<ChessField>> fields) {
        this.fields = fields;
    }



    public PuzzleCategory getCategory() {
        return category;
    }

    public void setCategory(PuzzleCategory category) {
        this.category = category;
    }

    public PieceColor getPlayerToMove() {
        return playerToMove;
    }

    public void setPlayerToMove(PieceColor playerToMove) {
        this.playerToMove = playerToMove;
    }
}

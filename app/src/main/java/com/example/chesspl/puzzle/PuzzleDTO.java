package com.example.chesspl.puzzle;


import java.util.List;

public class PuzzleDTO {
    private String category;
    private String playerToMove;
    private String moveHistory;
    private List<List<ChessFieldDTO>> fields;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPlayerToMove() { return playerToMove; }
    public void setPlayerToMove(String playerToMove) { this.playerToMove = playerToMove; }

    public String getMoveHistory() { return moveHistory; }
    public void setMoveHistory(String moveHistory) { this.moveHistory = moveHistory; }

    public List<List<ChessFieldDTO>> getFields() { return fields; }
    public void setFields(List<List<ChessFieldDTO>> fields) { this.fields = fields; }
}

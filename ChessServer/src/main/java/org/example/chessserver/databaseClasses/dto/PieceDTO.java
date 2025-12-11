package org.example.chessserver.databaseClasses.dto;

public class PieceDTO {
    private String type;
    private String pieceColor;
    private boolean firstMove;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPieceColor() { return pieceColor; }
    public void setPieceColor(String pieceColor) { this.pieceColor = pieceColor; }

    public boolean isFirstMove() { return firstMove; }
    public void setFirstMove(boolean firstMove) { this.firstMove = firstMove; }
}

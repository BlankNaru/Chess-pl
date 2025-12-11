package org.example.chessserver.databaseClasses.dto;

public class ChessFieldDTO {
    private String letter;
    private String number;
    private boolean isClicked;
    private PieceDTO piece;

    public String getLetter() { return letter; }
    public void setLetter(String letter) { this.letter = letter; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public boolean isClicked() { return isClicked; }
    public void setClicked(boolean clicked) { isClicked = clicked; }

    public PieceDTO getPiece() { return piece; }
    public void setPiece(PieceDTO piece) { this.piece = piece; }
}

package com.example.chesspl.puzzle;

public class ChessFieldDTO {
    private String letter;
    private String number;
    private PieceDTO piece;
    private boolean clicked;

    public String getLetter() { return letter; }
    public void setLetter(String letter) { this.letter = letter; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public PieceDTO getPiece() { return piece; }
    public void setPiece(PieceDTO piece) { this.piece = piece; }

    public boolean isClicked() { return clicked; }
    public void setClicked(boolean clicked) { this.clicked = clicked; }
}

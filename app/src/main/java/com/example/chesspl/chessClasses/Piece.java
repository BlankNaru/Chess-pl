package com.example.chesspl.chessClasses;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public interface Piece {
    int getDrawable();
    void setPiece(ImageView pieceView);
    void showMoves(Chessboard chessboard);
    void setAsMoved();
    PieceColor getPieceColor();
}

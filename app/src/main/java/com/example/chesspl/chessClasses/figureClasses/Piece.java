package com.example.chesspl.chessClasses.figureClasses;

import android.widget.ImageView;

import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.GameType;
import com.example.chesspl.chessClasses.PieceColor;

import java.util.List;

public interface Piece {
    int getDrawable();
    void setPiece(ImageView pieceView);
    void setPiece(ImageView pieceView, GameType gameType);
    List<ChessField> getMoves(Chessboard chessboard, boolean skipEnemyKing, boolean includeProtected, boolean includeDiscoveredCheck);
    void setAsMoved();
    PieceColor getPieceColor();
    boolean hasMoved();
}

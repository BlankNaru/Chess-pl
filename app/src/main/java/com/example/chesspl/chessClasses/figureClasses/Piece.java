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
    // Lista dostępnych ruchów
    // skipEnemyKing - by sprawdzić, czy król się nie znajdzie na polu bicia figury po swoim ruchu
    // includeProtected - żeby sprawdzić czy jak król zbije to czy nie znajdzie się pod szachem, bo pole jest bronione
    // includeDiscoveredCheck - żeby sprawdzić czy po ruchu król się nie znajdzie pod biciem
    List<ChessField> getMoves(Chessboard chessboard, boolean skipEnemyKing, boolean includeProtected, boolean includeDiscoveredCheck);
    void setAsMoved(int distance);
    PieceColor getPieceColor();
    boolean hasMoved();
}

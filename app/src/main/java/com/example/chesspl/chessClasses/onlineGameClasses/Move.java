package com.example.chesspl.chessClasses.onlineGameClasses;

import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.figureClasses.Piece;

public class Move
{
    public final ChessField from;
    public final ChessField to;
    public final Piece piece;

    public Move(ChessField from, ChessField to, Piece piece)
    {
        this.from = from;
        this.to = to;
        this.piece = piece;
    }
}

package com.example.chesspl.chessClasses;

import com.example.chesspl.chessClasses.figureClasses.Piece;

public class Simulation {

    private Piece movedPiece = null;
    private Piece takenPiece = null;
    private ChessField sourceField;
    private ChessField destinationField;
    public void startSimulation(ChessField sourceField, ChessField destinationField)
    {
        this.sourceField = sourceField;
        this.destinationField = destinationField;

        movedPiece = this.sourceField.getPiece();
        takenPiece = this.destinationField.getPiece();

        sourceField.setSimulatedPiece(null);
        destinationField.setSimulatedPiece(movedPiece);
    }


    public void stopSimulation()
    {
        sourceField.setSimulatedPiece(movedPiece);
        destinationField.setSimulatedPiece(takenPiece);
    }
}

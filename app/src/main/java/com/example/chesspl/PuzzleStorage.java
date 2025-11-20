package com.example.chesspl;

public class PuzzleStorage
{
    private static PuzzleStorage instance;
    private SinglePuzzle lastPuzzle;

    private PuzzleStorage() {}

    public static PuzzleStorage getInstance() {
        if (instance == null) instance = new PuzzleStorage();
        return instance;
    }

    public void setLastPuzzle(SinglePuzzle puzzle) {
        lastPuzzle = puzzle;
    }

    public SinglePuzzle getLastPuzzle() {
        return lastPuzzle;
    }
}

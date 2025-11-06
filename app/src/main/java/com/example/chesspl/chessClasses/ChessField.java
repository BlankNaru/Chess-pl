package com.example.chesspl.chessClasses;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.chesspl.R;

public class ChessField {
    private String letter;
    private String number;
    private boolean isClicked = false;
    private Piece piece = null;
    private View view;
    private ImageView pieceView;
    private ImageView moveView;
    private ImageView highlightFilterView;

    public ChessField(String field)
    {
        letter = field.substring(0,1);
        number = field.substring(1);
    }

    public ChessField(String letter, String number)
    {
        this.letter = letter;
        this.number = number;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        if(piece == null)
        {
            pieceView.setImageResource(0);
            this.piece = null;
            return;
        }
        this.piece = piece;
        piece.setPiece(pieceView);
    }
    public void clearPiece()
    {
        pieceView.setVisibility(View.GONE);
        this.piece = null;
    }


    public String getCoordinates()
    {
        return letter + number;
    }

    public void setGraphic(ImageView pieceView) {
        this.pieceView = pieceView;
    }

    public void setMovementHighlight()
    {

    }
    public int getCol()
    {
        char c = letter.charAt(0);
        return c - 'A';
    }
    public int getRow()
    {
        return Integer.parseInt(number) - 1;
    }

    public ImageView getMoveView() {
        return moveView;
    }

    public void setMoveView(ImageView moveView) {
        this.moveView = moveView;
    }

    public void setHighlightFilterView(ImageView highlightFilterView) {
        this.highlightFilterView = highlightFilterView;
    }

    public View getView() {
        return view;
    }
    public void setView(View view)
    {
        this.view = view;
    }

    public void onCLick(Chessboard chessboard) {
        if(piece == null)
            return;
        isClicked = true;
        piece.showMoves(chessboard);
    }

    public boolean isClicked() {
        return isClicked;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public void markAsPossibleToMove()
    {
        moveView.setImageResource(R.drawable.small_circle);
        moveView.setVisibility(View.VISIBLE);
    }

    public void clearMovement()
    {
        moveView.setImageResource(R.drawable.small_circle);
        moveView.setVisibility(View.GONE);
    }

    public void clearClick()
    {
        isClicked = false;
    }
}

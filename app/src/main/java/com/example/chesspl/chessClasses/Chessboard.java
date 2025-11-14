package com.example.chesspl.chessClasses;

import static com.example.chesspl.chessClasses.PieceColor.WHITE;
import static com.example.chesspl.chessClasses.PieceColor.BLACK;

import android.app.Activity;
import android.view.View;
import android.widget.GridLayout;

import com.example.chesspl.ChessHelper;
import com.example.chesspl.PuzzleHelper;
import com.example.chesspl.chessClasses.figureClasses.Bishop;
import com.example.chesspl.chessClasses.figureClasses.King;
import com.example.chesspl.chessClasses.figureClasses.Knight;
import com.example.chesspl.chessClasses.figureClasses.Pawn;
import com.example.chesspl.chessClasses.figureClasses.Piece;
import com.example.chesspl.chessClasses.figureClasses.Queen;
import com.example.chesspl.chessClasses.figureClasses.Rook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chessboard {

    private List<List<ChessField>> fields;
    private List<ChessField> possibleMoves = null;
    private boolean empty;
    private ChessField selectedField;
    private PieceColor playerToMove = WHITE;
    private String moveHistory = "";

    public Chessboard(boolean empty, GridLayout chessboardLayout, Activity activity)
    {
        this.empty = empty;
        fieldsInit();
        ChessHelper.generateFields(chessboardLayout, activity, this);
    }

    public void fieldsInit() {
        fields = new ArrayList<>();
        for (int i = 8; i >= 1; i--) {
            List<ChessField> row = new ArrayList<>();
            for (int j = 'A'; j <= 'H'; j++) {
                String letterAsString = Character.toString((char) j);
                String numberAsString = Integer.toString(i);
                row.add(new ChessField(letterAsString, numberAsString));
            }
            fields.add(row);
        }
    }

    public void piecesInit() {
        if (empty)
            return;

        // WHITE
        for (int i = 0; i < 8; i++)
            fields.get(6).get(i).setPiece(new Pawn(WHITE));
        fields.get(7).get(0).setPiece(new Rook(WHITE));
        fields.get(7).get(1).setPiece(new Knight(WHITE));
        fields.get(7).get(2).setPiece(new Bishop(WHITE));
        fields.get(7).get(3).setPiece(new Queen(WHITE));
        fields.get(7).get(4).setPiece(new King(WHITE));
        fields.get(7).get(5).setPiece(new Bishop(WHITE));
        fields.get(7).get(6).setPiece(new Knight(WHITE));
        fields.get(7).get(7).setPiece(new Rook(WHITE));

        // BLACK
        for (int i = 0; i < 8; i++)
            fields.get(1).get(i).setPiece(new Pawn(PieceColor.BLACK));
        fields.get(0).get(0).setPiece(new Rook(PieceColor.BLACK));
        fields.get(0).get(1).setPiece(new Knight(PieceColor.BLACK));
        fields.get(0).get(2).setPiece(new Bishop(PieceColor.BLACK));
        fields.get(0).get(4).setPiece(new Queen(PieceColor.BLACK));
        fields.get(0).get(3).setPiece(new King(PieceColor.BLACK));
        fields.get(0).get(5).setPiece(new Bishop(PieceColor.BLACK));
        fields.get(0).get(6).setPiece(new Knight(PieceColor.BLACK));
        fields.get(0).get(7).setPiece(new Rook(PieceColor.BLACK));

//        fields.get(0).get(7).setPiece(new King(BLACK));
//        fields.get(0).get(1).setPiece(new Rook(BLACK));
//        fields.get(5).get(1).setPiece(new Rook(BLACK));
//        fields.get(6).get(1).setPiece(new Rook(WHITE));
//        fields.get(7).get(1).setPiece(new King(WHITE));
//        fields.get(6).get(2).setPiece(new Pawn(WHITE));
    }

    public void setNewPiece(int row, int col, Piece piece)
    {
        fields.get(row).get(col).setPiece(piece);
    }

    public void setPiece(String coordinates, Piece piece)
    {
        for(List<ChessField> row : fields)
            for(ChessField field : row)
                if(field.getCoordinates().equals(coordinates))
                {
                    takePiece(field);
                    ChessField oldField = getLocation(piece);
                    field.setPiece(piece);
                    oldField.setPiece(null);
                    moveHistory += oldField.getCoordinates() + "->" + coordinates + " ";
                    break;
                }
    }

    public void takePiece(ChessField field)
    {
        field.setPiece(null);
    }

    public ChessField getLocation(Piece piece) {
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getPiece() == piece)
                    return field;
        return null;
    }

    public ChessField getLocation(View view) {
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getView() == view)
                    return field;
        return null;
    }

    public List<List<ChessField>> getFields() {
        return fields;
    }

    public ChessField getSelectedField()
    {
        return selectedField;
    }

    public void setSelectedField(ChessField field)
    {
        this.selectedField = field;
    }



    public List<List<ChessField>> getWhiteView() {
        return fields;
    }

    public List<List<ChessField>> getBlackView() {
        List<List<ChessField>> flipped = new ArrayList<>();
        for (int i = fields.size() - 1; i >= 0; i--) {
            flipped.add(new ArrayList<>(fields.get(i)));
        }
        return flipped;
    }

    public ChessField getUpperField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (i > 0)
                        return fields.get(i - 1).get(j);
                    else
                        return null;
                }

        return null;
    }

    public ChessField getLowerField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (i < 7)
                        return fields.get(i + 1).get(j);
                    else
                        return null;
                }

        return null;
    }

    public ChessField getRightField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (j < 7)
                        return fields.get(i).get(j + 1);
                    else
                        return null;
                }

        return null;
    }

    public ChessField getLeftField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (j > 0)
                        return fields.get(i).get(j - 1);
                    else
                        return null;
                }

        return null;
    }

    public ChessField getUpperLeftField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (j > 0 && i > 0)
                        return fields.get(i - 1).get(j - 1);
                    else
                        return null;
                }

        return null;
    }

    public ChessField getUpperRightField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (j < 7 && i > 0)
                        return fields.get(i - 1).get(j + 1);
                    else
                        return null;
                }

        return null;
    }

    public ChessField getLowerLeftField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (j > 0 && i < 7)
                        return fields.get(i + 1).get(j - 1);
                    else
                        return null;
                }

        return null;
    }

    public ChessField getLowerRightField(ChessField startingField) {
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (j < 7 && i < 7)
                        return fields.get(i + 1).get(j + 1);
                    else
                        return null;
                }

        return null;
    }

    public List<ChessField> getLShaped(ChessField startingField) {
        List<ChessField> moves = new ArrayList<>();
        if (startingField == null)
            return null;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (fields.get(i).get(j) == startingField) {
                    if (i > 1 && j < 7)
                        moves.add(fields.get(i - 2).get(j + 1));
                    if (i > 0 && j < 6)
                        moves.add(fields.get(i - 1).get(j + 2));
                    if (i < 7 && j < 6)
                        moves.add(fields.get(i + 1).get(j + 2));
                    if (i < 6 && j < 7)
                        moves.add(fields.get(i + 2).get(j + 1));
                    if (i < 6 && j > 0)
                        moves.add(fields.get(i + 2).get(j - 1));
                    if (i < 7 && j > 1)
                        moves.add(fields.get(i + 1).get(j - 2));
                    if (i > 0 && j > 1)
                        moves.add(fields.get(i - 1).get(j - 2));
                    if (i > 1 && j > 0)
                        moves.add(fields.get(i - 2).get(j - 1));
                    return moves;
                }

        return null;
    }

    public ChessField findClickedField() {
        for (List<ChessField> list : fields)
            for (ChessField field : list)
                if (field.isClicked())
                    return field;
        return null;
    }

    public void onClick(View view) {
        ChessField clickedTile = getLocation(view);
        if((clickedTile.getPiece() == null || !clickedTile.getPiece().getPieceColor().equals(playerToMove)) && !isSomethingClicked())
            return;
        if (possibleMoves == null)
            clickedTile.onCLick(this);
        else if (possibleMoves.contains(clickedTile)) {
            findClickedField().getPiece().setAsMoved();
            setPiece(clickedTile.getCoordinates(), findClickedField().getPiece());
            deClick();
            setNextPlayer();
//            if(checkIfMated(playerToMove))
//                fields.get(7).get(7).setPiece(new Pawn(WHITE));
        } else
            deClick();

    }

    public void deClick() {
        for (ChessField field : possibleMoves)
            field.clearMovement();
        for (List<ChessField> list : fields) {
            for (ChessField field : list)
                field.clearClick();
        }
        possibleMoves = null;
    }

    public boolean isSomethingClicked() {
        for (List<ChessField> chessFieldList : fields)
            for (ChessField field : chessFieldList)
                if (field.isClicked())
                    return true;
        return false;
    }


    public void setPossibleMoves(List<ChessField> possibleFields) {
        possibleMoves = possibleFields;
        for (ChessField field : possibleMoves)
            field.markAsPossibleToMove();
    }

    public void setNextPlayer()
    {
        if(playerToMove.equals(WHITE))
            playerToMove = BLACK;
        else
            playerToMove = WHITE;
    }

    public List<ChessField> getAllMovesForColor(PieceColor color)
    {
        List<ChessField> moves = new ArrayList<>();
        for(List<ChessField> row : fields)
            for(ChessField field : row)
                if(!field.isEmpty() && field.getPiece().getPieceColor().equals(color))
                    moves.addAll(field.getPiece().getMoves(this, true, true, false));

        moves = moves.stream().distinct().collect(Collectors.toList());
        return moves;
    }

    public boolean checkIfChecked(PieceColor color)
    {
        Piece checkedKing = null;
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getPiece() instanceof King && field.getPiece().getPieceColor().equals(color))
                {
                    checkedKing = field.getPiece();
                    break;
                }
        List<ChessField> checkedFields = getAllMovesForColor(checkedKing.getPieceColor()==WHITE? BLACK : WHITE);
        return checkedFields.contains(getLocation(checkedKing));
    }

    public boolean checkIfDraw(PieceColor color)
    {
return false;
    }

    public boolean checkIfMated(PieceColor color)
    {
        if(!checkIfChecked(color))
            return false;
        List<ChessField> possibleMoves = getAllMovesForColor(color);
        return !possibleMoves.isEmpty();
    }

}

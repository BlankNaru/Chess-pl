package com.example.chesspl.chessClasses;

import android.app.Activity;
import android.widget.GridLayout;

import com.example.chesspl.ChessHelper;

import java.util.ArrayList;
import java.util.List;

public class Chessboard {

    private List<List<ChessField>> fields;
    private boolean empty;
    private ChessField selectedField;

    public Chessboard(boolean empty, GridLayout chessboardLayout, Activity activity)
    {
        this.empty = empty;
        fieldsInit();
        ChessHelper.generateFields(chessboardLayout, activity, this,null);
    }

    public void fieldsInit()
    {
        fields = new ArrayList<>();
        for(int i=1; i<=8; i++)
        {
            List<ChessField> row = new ArrayList<>();
            for(int j='A'; j<='H'; j++)
            {
                String letterAsString = Character.toString(Character.forDigit(j, 10));
                String numberAsString = Integer.toString(i);
                row.add(new ChessField(letterAsString, numberAsString));
            }
            fields.add(row);
        }
    }

    public void piecesInit()
    {
        if(empty)
            return;
        fields.get(0).get(0).setPiece(new King(Color.WHITE));
        fields.get(1).get(1).setPiece(new Queen(Color.WHITE));
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
                }
    }

    public void takePiece(ChessField field)
    {
        field.setPiece(null);
    }
    public ChessField getLocation(Piece piece)
    {
        for(List<ChessField> row : fields)
            for(ChessField field : row)
                if(field.getPiece().equals(piece))
                    return field;
        return null;
    }

    public List<List<ChessField>> getFields()
    {
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


}

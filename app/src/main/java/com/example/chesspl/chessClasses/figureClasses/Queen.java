package com.example.chesspl.chessClasses.figureClasses;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.example.chesspl.R;
import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.GameType;
import com.example.chesspl.chessClasses.PieceColor;
import com.example.chesspl.chessClasses.Simulation;

import java.util.ArrayList;
import java.util.List;

public class Queen implements Piece {
    private PieceColor pieceColor;

    public Queen(PieceColor pieceColor)
    {
        this.pieceColor = pieceColor;
    }

    @Override
    public int getDrawable() {
        return R.drawable.queen;
    }

    @Override
    public void setPiece(ImageView pieceView) {
        pieceView.setImageResource(getDrawable());
        pieceView.setColorFilter(getColor());
        pieceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPiece(ImageView pieceView, GameType gameType) {
        if(gameType == GameType.LOCAL && getPieceColor() == PieceColor.BLACK)
            pieceView.setRotation(180f);
        pieceView.setImageResource(getDrawable());
        pieceView.setColorFilter(getColor());
        pieceView.setVisibility(View.VISIBLE);
    }

    @Override
    public List<ChessField> getMoves(Chessboard chessboard, boolean skipEnemyKing, boolean includeProtected, boolean includeDiscoveredCheck) {
        List<ChessField> moves = new ArrayList<>();

        moves.addAll(getBishopLikeFields(chessboard, skipEnemyKing, includeProtected));
        moves.addAll(getRookLikeFields(chessboard, skipEnemyKing, includeProtected));

        if(includeDiscoveredCheck)
        {
            List<ChessField> resultInCheckFields = new ArrayList<>();
            for(ChessField move : moves)
            {
                Simulation simulation = new Simulation();
                simulation.startSimulation(chessboard.getLocation(this), move);
                if(chessboard.checkIfChecked(getPieceColor()))
                    resultInCheckFields.add(move);
                simulation.stopSimulation();
            }
            moves.removeAll(resultInCheckFields);
        }

        return moves;
    }

    @Override
    public void setAsMoved(int distance) {
    }

    private int getColor()
    {
        if(pieceColor == PieceColor.BLACK)
            return Color.BLACK;
        return Color.WHITE;
    }

    @Override
    public PieceColor getPieceColor() {
        return pieceColor;
    }

    @Override
    public boolean hasMoved() {
        return false;
    }

    private List<ChessField> getBishopLikeFields(Chessboard chessboard, boolean skipKing, boolean includeProtected)
    {
        List<ChessField> moves = new ArrayList<>();
        ChessField currentField = chessboard.getLocation(this);

        ChessField upperRightField = chessboard.getUpperRightField(currentField);
        while(upperRightField!=null)
        {
            if(upperRightField.isEmpty()  || (skipKing && upperRightField.getPiece() instanceof King))
            {
                moves.add(upperRightField);
                upperRightField = chessboard.getUpperRightField(upperRightField);
                continue;
            }
            if(upperRightField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(upperRightField);
                break;
            }
            else
                moves.add(upperRightField);
            break;
        }

        ChessField upperLeftField = chessboard.getUpperLeftField(currentField);
        while(upperLeftField!=null)
        {
            if(upperLeftField.isEmpty()  || (skipKing && upperLeftField.getPiece() instanceof King))
            {
                moves.add(upperLeftField);
                upperLeftField = chessboard.getUpperLeftField(upperLeftField);
                continue;
            }
            if(upperLeftField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(upperLeftField);
                break;
            }
            else
                moves.add(upperLeftField);
            break;
        }

        ChessField lowerLeftField = chessboard.getLowerLeftField(currentField);
        while(lowerLeftField!=null)
        {
            if(lowerLeftField.isEmpty()  || (skipKing && lowerLeftField.getPiece() instanceof King))
            {
                moves.add(lowerLeftField);
                lowerLeftField = chessboard.getLowerLeftField(lowerLeftField);
                continue;
            }
            if(lowerLeftField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(lowerLeftField);
                break;
            }
            else
                moves.add(lowerLeftField);
            break;
        }

        ChessField lowerRightField = chessboard.getLowerRightField(currentField);
        while(lowerRightField!=null)
        {
            if(lowerRightField.isEmpty()  || (skipKing && lowerRightField.getPiece() instanceof King))
            {
                moves.add(lowerRightField);
                lowerRightField = chessboard.getLowerRightField(lowerRightField);
                continue;
            }
            if(lowerRightField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(lowerRightField);
                break;
            }
            else
                moves.add(lowerRightField);
            break;
        }

        return moves;
    }

    private List<ChessField> getRookLikeFields(Chessboard chessboard, boolean skipKing, boolean includeProtected)
    {
        List<ChessField> moves = new ArrayList<>();
        ChessField currentField = chessboard.getLocation(this);

        ChessField upperField = chessboard.getUpperField(currentField);
        while(upperField!=null)
        {
            if(upperField.isEmpty()  || (skipKing && upperField.getPiece() instanceof King))
            {
                moves.add(upperField);
                upperField = chessboard.getUpperField(upperField);
                continue;
            }
            if(upperField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(upperField);
                break;
            }
            else
                moves.add(upperField);
            break;
        }

        ChessField leftField = chessboard.getLeftField(currentField);
        while(leftField!=null)
        {
            if(leftField.isEmpty()  || (skipKing && leftField.getPiece() instanceof King))
            {
                moves.add(leftField);
                leftField = chessboard.getLeftField(leftField);
                continue;
            }
            if(leftField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(leftField);
                break;
            }
            else
                moves.add(leftField);
            break;
        }

        ChessField lowerField = chessboard.getLowerField(currentField);
        while(lowerField!=null)
        {
            if(lowerField.isEmpty()  || (skipKing && lowerField.getPiece() instanceof King))
            {
                moves.add(lowerField);
                lowerField = chessboard.getLowerField(lowerField);
                continue;
            }
            if(lowerField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(lowerField);
                break;
            }
            else
                moves.add(lowerField);
            break;
        }

        ChessField rightField = chessboard.getRightField(currentField);
        while(rightField!=null)
        {
            if(rightField.isEmpty()  || (skipKing && rightField.getPiece() instanceof King))
            {
                moves.add(rightField);
                rightField = chessboard.getRightField(rightField);
                continue;
            }
            if(rightField.getPiece().getPieceColor().equals(pieceColor))
            {
                if(includeProtected)
                    moves.add(rightField);
                break;
            }
            else
                moves.add(rightField);
            break;
        }
        return moves;
    }

}

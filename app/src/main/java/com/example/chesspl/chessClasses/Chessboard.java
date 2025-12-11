package com.example.chesspl.chessClasses;

import static com.example.chesspl.chessClasses.PieceColor.BLACK;
import static com.example.chesspl.chessClasses.PieceColor.WHITE;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.chesspl.ChessHelper;
import com.example.chesspl.R;
import com.example.chesspl.chessClasses.figureClasses.Bishop;
import com.example.chesspl.chessClasses.figureClasses.King;
import com.example.chesspl.chessClasses.figureClasses.Knight;
import com.example.chesspl.chessClasses.figureClasses.Pawn;
import com.example.chesspl.chessClasses.figureClasses.Piece;
import com.example.chesspl.chessClasses.figureClasses.Queen;
import com.example.chesspl.chessClasses.figureClasses.Rook;
import com.example.chesspl.chessClasses.onlineGameClasses.Move;
import com.example.chesspl.chessClasses.onlineGameClasses.OnlineGameHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chessboard {

    private PieceColor playerColorInOnlineGame = null;
    private OnlineGameHelper onlineGameHelper;
    private List<List<ChessField>> fields;
    private List<ChessField> possibleMoves = null;
    private boolean empty;
    private ChessField selectedField;
    public PieceColor playerToMove = WHITE;
    private String moveHistory = "";
    private String lastMove = "";
    private GameType gameType = GameType.NONE;
    private Activity activity;
    private final List<Move> moveHistory2 = new ArrayList<>();
    public Chessboard(boolean empty, GridLayout chessboardLayout, Activity activity) {
        this.empty = empty;
        this.activity = activity;
        fieldsInit();
        ChessHelper.generateFields(chessboardLayout, activity, this);
    }

    public Chessboard(boolean empty, GridLayout chessboardLayout, Activity activity, GameType gameType) {
        this.empty = empty;
        this.activity = activity;
        fieldsInit();
        ChessHelper.generateFields(chessboardLayout, activity, this);
        this.gameType = gameType;
        if(gameType == GameType.ONLINE)
        {
            onlineGameHelper = new OnlineGameHelper();
            onlineGameHelper.connect();
            onlineGameHelper.setOnColorDefinedListener(color -> {
                playerColorInOnlineGame = color.equalsIgnoreCase("white")? WHITE:BLACK;
            });
            onlineGameHelper.setOnMoveReceivedListener((receivedMove) -> {
                if(moveHistory.contains(receivedMove))
                    return;
                String from = receivedMove.substring(0, 2);
                String to = receivedMove.substring(4);
                setPiece(to, getLocation(from).getPiece());
                setNextPlayer();
            });
        }
    }

    //MARIUSZA
//    public void onClick(View view) {
//        if(gameType == GameType.ONLINE && playerColorInOnlineGame == null)
//            return;
//        if(gameType == GameType.ONLINE && playerToMove != playerColorInOnlineGame)
//            return;
//        ChessField clickedTile = getLocation(view);
//        if((clickedTile.getPiece() == null || !clickedTile.getPiece().getPieceColor().equals(playerToMove)) && !isSomethingClicked())
//            return;
//        if (possibleMoves == null)
//            clickedTile.onCLick(this);
//        else if (possibleMoves.contains(clickedTile)) {
//            setPiece(clickedTile.getCoordinates(), findClickedField().getPiece());
//            deClick();
//            if(pawnOnLastRow() != null)
//                showPromotionDialog(pawnOnLastRow());
//            setNextPlayer();
//            clearEnPassants(playerToMove);
//            if(gameType == GameType.ONLINE)
//                onlineGameHelper.sendMove(lastMove);
//            if(checkIfDraw(playerToMove))
//                fields.get(7).get(7).setPiece(new Pawn(WHITE));
//            if(checkIfMated(playerToMove))
//                fields.get(7).get(7).setPiece(new Pawn(WHITE));
//        } else
//            deClick();
//
//    }

    //MOJE
    public void onClick(View view) {
        if(gameType == GameType.ONLINE && playerColorInOnlineGame == null)
            return;
        if(gameType == GameType.ONLINE && playerToMove != playerColorInOnlineGame)
            return;

        ChessField clickedTile = getLocation(view);

        if((clickedTile.getPiece() == null || !clickedTile.getPiece().getPieceColor().equals(playerToMove)) && !isSomethingClicked())
            return;

        if (possibleMoves == null) {
            clickedTile.onCLick(this);
        } else if (possibleMoves.contains(clickedTile)) {
            ChessField from = findClickedField();
            ChessField to = clickedTile;
            Piece piece = from.getPiece();


            moveHistory2.add(new Move(from, to, piece));


            setPiece(to.getCoordinates(), piece);

            deClick();

            if(pawnOnLastRow() != null)
                showPromotionDialog(pawnOnLastRow());

            setNextPlayer();
            clearEnPassants(playerToMove);

            if(gameType == GameType.ONLINE)
                onlineGameHelper.sendMove(lastMove);

//            if(checkIfDraw(playerToMove))
//                fields.get(7).get(7).setPiece(new Pawn(WHITE));
//            if(checkIfMated(playerToMove))
//                fields.get(7).get(7).setPiece(new Pawn(WHITE));
        } else {
            deClick();
        }
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
            fields.get(6).get(i).setPiece(new Pawn(WHITE), gameType);
        fields.get(7).get(0).setPiece(new Rook(WHITE), gameType);
        fields.get(7).get(1).setPiece(new Knight(WHITE), gameType);
        fields.get(7).get(2).setPiece(new Bishop(WHITE), gameType);
        fields.get(7).get(3).setPiece(new Queen(WHITE), gameType);
        fields.get(7).get(4).setPiece(new King(WHITE), gameType);
        fields.get(7).get(5).setPiece(new Bishop(WHITE), gameType);
        fields.get(7).get(6).setPiece(new Knight(WHITE), gameType);
        fields.get(7).get(7).setPiece(new Rook(WHITE), gameType);

        // BLACK
        for (int i = 0; i < 8; i++)
            fields.get(1).get(i).setPiece(new Pawn(PieceColor.BLACK), gameType);
        fields.get(0).get(0).setPiece(new Rook(PieceColor.BLACK), gameType);
        fields.get(0).get(1).setPiece(new Knight(PieceColor.BLACK), gameType);
        fields.get(0).get(2).setPiece(new Bishop(PieceColor.BLACK), gameType);
        fields.get(0).get(4).setPiece(new Queen(PieceColor.BLACK), gameType);
        fields.get(0).get(3).setPiece(new King(PieceColor.BLACK), gameType);
        fields.get(0).get(5).setPiece(new Bishop(PieceColor.BLACK), gameType);
        fields.get(0).get(6).setPiece(new Knight(PieceColor.BLACK), gameType);
        fields.get(0).get(7).setPiece(new Rook(PieceColor.BLACK), gameType);

//        fields.get(0).get(7).setPiece(new King(BLACK));
//        fields.get(0).get(1).setPiece(new Rook(BLACK));
//        fields.get(5).get(1).setPiece(new Rook(BLACK));
//        fields.get(6).get(1).setPiece(new Rook(WHITE));
//        fields.get(7).get(1).setPiece(new King(WHITE));
//        fields.get(6).get(2).setPiece(new Pawn(WHITE));
    }

    public void setNewPiece(int row, int col, Piece piece) {
        fields.get(row).get(col).setPiece(piece);
    }

    // coordinates - miejsce docelowe
    // piece - figura dążąca do miejsca docelowego
    public void setPiece(String coordinates, Piece piece) {
        piece.setAsMoved(getDistance(coordinates, piece));
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getCoordinates().equals(coordinates)) {
                    checkIfEnPassant(piece);
                    takePiece(field);
                    ChessField oldField = getLocation(piece);
                    field.setPiece(piece, gameType);
                    oldField.setPiece(null);
                    lastMove = oldField.getCoordinates() + "->" + coordinates;
                    moveHistory += lastMove + " ";
                    break;
                }
    }

    public void clearEnPassants(PieceColor color)
    {
        for (List<ChessField> row : fields)
            for (ChessField field : row)
            {
                if(field.isEmpty() || field.getPiece().getPieceColor() != color)
                    continue;
                if(field.getPiece() instanceof Pawn)
                    field.getPiece().setAsMoved(0);
            }
    }

    public void checkIfEnPassant(Piece piece)
    {
        if(! (piece instanceof Pawn))
            return;
        if(((Pawn) piece).enPassantOnRight(this, getLocation(piece)))
            takePiece(getRightField(getLocation(piece)));
        if(((Pawn) piece).enPassantOnLeft(this, getLocation(piece)))
            takePiece(getLeftField(getLocation(piece)));
    }

    public int getDistance(String coordinates, Piece piece) {
        ChessField from = getLocation(piece);
        ChessField to = null;
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getCoordinates().equals(coordinates))
                    to = field;
        return Math.abs(to.getRow() - from.getRow());

    }

    public void setPromotedPiece(String coordinates, Piece piece)
    {
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getCoordinates().equals(coordinates)) {
                    takePiece(field);
                    field.setPiece(piece, gameType);
                    break;
                }
    }

    public void takePiece(ChessField field) {
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

    public ChessField getLocation(String coordinates) {
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getCoordinates().equalsIgnoreCase(coordinates))
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


    private ChessField pawnOnLastRow() {
        for(int i=0; i<8; i++)
        {
            if(fields.get(0).get(i).getPiece() != null && fields.get(0).get(i).getPiece() instanceof Pawn)
                return fields.get(0).get(i);
            if(fields.get(7).get(i).getPiece() != null && fields.get(7).get(i).getPiece() instanceof Pawn)
                return fields.get(7).get(i);
        }
        return null;
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

    public boolean isEnoughMaterialForPlayer(PieceColor color)
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

    private void showPromotionDialog(ChessField field) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(16, 16, 16, 16);
        layout.setGravity(Gravity.CENTER);

        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60, // 60dp
                activity.getResources().getDisplayMetrics()
        );

        int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8, // 8dp
                activity.getResources().getDisplayMetrics()
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();

        List<ImageView> drawables = getDrawablesForPromotion();
        for(int i = 0; i < 4; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(margin, margin, margin, margin);
            drawables.get(i).setLayoutParams(params);

            int index = i;
            PieceColor color = playerToMove;
            drawables.get(i).setOnClickListener(v -> {
                if(index == 0)
                    setPromotedPiece(field.getCoordinates(), new Queen(color));
                if(index == 1)
                    setPromotedPiece(field.getCoordinates(), new Rook(color));
                if(index == 2)
                    setPromotedPiece(field.getCoordinates(), new Bishop(color));
                if(index == 3)
                    setPromotedPiece(field.getCoordinates(), new Knight(color));
                dialog.dismiss();
            });

            layout.addView(drawables.get(i));
        }

        dialog.show();
    }

    private List<ImageView> getDrawablesForPromotion()
    {
        List<ImageView> figures = new ArrayList<>();

        // QUEEN
        ImageView promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.queen);
        promotionView.setVisibility(View.VISIBLE);
        if(playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else
        {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        // ROOK
        promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.rook);
        promotionView.setVisibility(View.VISIBLE);
        if(playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else
        {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        // BISHOP
        promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.bishop);
        promotionView.setVisibility(View.VISIBLE);
        if(playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else
        {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        // KNIGHT
        promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.knight);
        promotionView.setVisibility(View.VISIBLE);
        if(playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else
        {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        return figures;
    }

    public void setFields(List<List<ChessField>> fields)
    {
        this.fields = fields;
    }
    public interface OnMoveListener
    {
        void onMoveExecuted();
    }

    private OnMoveListener moveListener;

    public void setOnMoveListener(OnMoveListener listener)
    {
        this.moveListener = listener;
    }

    public String getMoveHistory()
    {
        return moveHistory;

    }

    public boolean isRookEndgameComposition() {
        for (List<ChessField> row : fields) {
            for (ChessField field : row) {
                Piece piece = field.getPiece();

                if (piece != null) {

                    if (!(piece instanceof King) &&
                            !(piece instanceof Rook) &&
                            !(piece instanceof Pawn)) {

                        return false;
                    }
                }
            }
        }

        return true;
    }
    public boolean checkIfForkOccurred() {
        if (moveHistory2.isEmpty()) {
            return false;
        }


        Move lastMoveObj = moveHistory2.get(moveHistory2.size() - 1);
        ChessField toField = lastMoveObj.to;
        Piece movedPiece = lastMoveObj.piece;
        PieceColor opponentColor = movedPiece.getPieceColor() == WHITE ? BLACK : WHITE;

        List<ChessField> attackedFields;
        try {

            attackedFields = movedPiece.getMoves(this, false, false, false);
        } catch (Exception e) {

            return false;
        }

        int valuableTargetsHit = 0;


        for (ChessField field : attackedFields) {
            Piece target = field.getPiece();

            if (target != null && target.getPieceColor() == opponentColor) {

                boolean isValuableTarget = !(target instanceof Pawn);

                if (isValuableTarget) {
                    valuableTargetsHit++;
                }
            }
        }


        return valuableTargetsHit >= 2;
    }

    public void clearMoveHistory() {

        this.moveHistory = "";
    }


    public void clearBoard() {

        for (List<ChessField> row : fields) {
            for (ChessField field : row) {
                field.setPiece(null);
            }
        }


        moveHistory = "";
        moveHistory2.clear();

        selectedField = null;
    }

    public void undoLastMove() {
        if (moveHistory2.isEmpty()) return;


        Move lastMoveObj = moveHistory2.remove(moveHistory2.size() - 1);


        lastMoveObj.from.setPiece(lastMoveObj.piece);
        lastMoveObj.to.setPiece(null);


        selectedField = lastMoveObj.from;


        if(playerToMove.equals(WHITE))
            playerToMove = BLACK;
        else
            playerToMove = WHITE;

        String lastMoveString = lastMoveObj.from.getCoordinates() + "->" + lastMoveObj.to.getCoordinates();
        String fullMoveEntry = lastMoveString + " ";

        if (moveHistory.endsWith(fullMoveEntry)) {
            moveHistory = moveHistory.substring(0, moveHistory.length() - fullMoveEntry.length());
        }


        if (!moveHistory2.isEmpty()) {

            Move newLastMoveObj = moveHistory2.get(moveHistory2.size() - 1);
            this.lastMove = newLastMoveObj.from.getCoordinates() + "->" + newLastMoveObj.to.getCoordinates();
        } else {
            this.lastMove = "";
        }
    }


}

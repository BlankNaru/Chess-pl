package com.example.chesspl.chessClasses;

import static com.example.chesspl.chessClasses.PieceColor.BLACK;
import static com.example.chesspl.chessClasses.PieceColor.WHITE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.chesspl.ChessHelper;
import com.example.chesspl.R;
import com.example.chesspl.activities.ChessTimer;
import com.example.chesspl.activities.WebSocketManager;
import com.example.chesspl.chessClasses.figureClasses.Bishop;
import com.example.chesspl.chessClasses.figureClasses.King;
import com.example.chesspl.chessClasses.figureClasses.Knight;
import com.example.chesspl.chessClasses.figureClasses.Pawn;
import com.example.chesspl.chessClasses.figureClasses.Piece;
import com.example.chesspl.chessClasses.figureClasses.Queen;
import com.example.chesspl.chessClasses.figureClasses.Rook;
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
    private PieceColor playerToMove = WHITE;
    private String moveHistory = "";
    private String lastMove = "";
    private GameType gameType = GameType.NONE;
    private Activity activity;
    private boolean end = false;
    private TableLayout table;
    private int moveNo = 1;
    private ChessTimer timerWhite, timerBlack;
    private PieceColor winner = null;
    private ImageView white, black;


    public void setWinnerImageView(ImageView white, ImageView black)
    {
        if(playerColorInOnlineGame == WHITE)
        {
            this.white = white;
            this.black = black;
        }
        else
        {
            this.white = black;
            this.black = white;
        }
    }
    public void updateWinner()
    {
        timerWhite.pause();
        timerBlack.pause();
        if(winner == null)
        {
            white.setVisibility(View.VISIBLE);
            black.setVisibility(View.VISIBLE);
            white.setImageResource(R.drawable.half_crown);
            black.setImageResource(R.drawable.half_crown);
        }
        else if(winner == WHITE)
        {
            white.setVisibility(View.VISIBLE);
        }
        else
        {
            black.setVisibility(View.VISIBLE);
        }
    }
    public void setTimers(String time, TextView whiteTextView, TextView blackTextView)
    {
        timerWhite = new ChessTimer(whiteTextView, Integer.parseInt(time));
        timerWhite.setOnTimeOutListener(() -> {
            end = true;
            winner = BLACK;
            updateWinner();
            onlineGameHelper.sendTimeOut("white");
        });
        timerBlack = new ChessTimer(blackTextView, Integer.parseInt(time));
        timerBlack.setOnTimeOutListener(() -> {
            end = true;
            winner = WHITE;
            updateWinner();
            onlineGameHelper.sendTimeOut("black");
        });
        timerWhite.start();
    }

    public void setMoveTable(TableLayout table)
    {
        this.table = table;
    }
    @SuppressLint("SetTextI18n")
    public void updateTable()
    {
        if(playerToMove == BLACK)
        {
            TableRow row = new TableRow(activity);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView cell1 = new TextView(activity);
            cell1.setText(moveNo + ".");
            cell1.setGravity(Gravity.CENTER);
            cell1.setTextColor(ContextCompat.getColor(activity, R.color.orange));
            cell1.setTextSize(20);
            row.addView(cell1);

            TextView cell2 = new TextView(activity);
            cell2.setText(lastMove.trim());
            cell2.setGravity(Gravity.CENTER);
            cell2.setTextColor(ContextCompat.getColor(activity, R.color.white));
            cell2.setTextSize(20);
            row.addView(cell2);

            TextView cell3 = new TextView(activity);
            cell3.setText("");
            cell3.setGravity(Gravity.CENTER);
            cell3.setTextColor(ContextCompat.getColor(activity, R.color.white));
            cell3.setTextSize(20);
            row.addView(cell3);

            table.addView(row);
            moveNo++;
        }
        else
        {
            int rowCount = table.getChildCount();
            TableRow lastRow = (TableRow) table.getChildAt(rowCount - 1);
            TextView blackMove  = (TextView) lastRow.getChildAt(2);
            blackMove.setText(lastMove.trim());
        }
    }
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
        this.gameType = gameType;
        if (gameType == GameType.ONLINE) {
            onlineGameHelper = WebSocketManager.getInstance().getHelper();

            playerColorInOnlineGame = onlineGameHelper.myColor.equalsIgnoreCase("white") ? WHITE : BLACK;
            ChessHelper.generateFields(chessboardLayout, activity, this, playerColorInOnlineGame == WHITE);

            onlineGameHelper.setOnMoveReceivedListener(receivedMove -> {
                String[] words = moveHistory.trim().split("\\s+");
                if (words[words.length - 1].trim().contains(receivedMove.trim()))
                    return;
                String from = receivedMove.substring(0, 2);
                String to = receivedMove.substring(4);
                setPiece(to, getLocation(from).getPiece());
                setNextPlayer();
                if (checkIfDraw(playerToMove))
                {
                    onlineGameHelper.sendMoveDraw(lastMove);
                    end = true;
                }
                if (checkIfMated(playerToMove))
                {
                    onlineGameHelper.sendMoveMate(lastMove);
                    end = true;
                }
                updateTable();
            });

            onlineGameHelper.setOnWinnerDefinedListener(winner -> {
                if(winner.equals("draw"))
                {
                    end = true;
                    updateWinner();
                    onlineGameHelper.disconnect();
                }else if(winner.equals("white"))
                {
                    end = true;
                    this.winner = WHITE;
                    updateWinner();
                    onlineGameHelper.disconnect();
                }
                else {
                    end = true;
                    this.winner = BLACK;
                    updateWinner();
                    onlineGameHelper.disconnect();
                }
            });
        }
    }

    public void onClick(View view) {
        if(end)
            return;
        if (gameType == GameType.ONLINE && playerColorInOnlineGame == null)
            return;
        if (gameType == GameType.ONLINE && playerToMove != playerColorInOnlineGame)
            return;
        ChessField clickedTile = getLocation(view);
        if ((clickedTile.getPiece() == null || !clickedTile.getPiece().getPieceColor().equals(playerToMove)) && !isSomethingClicked())
            return;
        if (possibleMoves == null)
            clickedTile.onCLick(this);
        else if (possibleMoves.contains(clickedTile)) {
            setPiece(clickedTile.getCoordinates(), findClickedField().getPiece());
            deClick();
            if (pawnOnLastRow() != null)
                showPromotionDialog(pawnOnLastRow());
            setNextPlayer();
            clearEnPassants(playerToMove);
            if (gameType == GameType.ONLINE)
                onlineGameHelper.sendMove(lastMove);
            if (checkIfDraw(playerToMove))
            {
                onlineGameHelper.sendMoveDraw(lastMove);
                onlineGameHelper.disconnect();
                end = true;
                updateWinner();
            }
            if (checkIfMated(playerToMove))
            {
                onlineGameHelper.sendMoveMate(lastMove);
                onlineGameHelper.disconnect();
                end = true;
                winner = playerToMove == WHITE? BLACK : WHITE;
                updateWinner();
            }
            updateTable();
        } else
            deClick();

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
        fields.get(0).get(3).setPiece(new Queen(PieceColor.BLACK), gameType);
        fields.get(0).get(4).setPiece(new King(PieceColor.BLACK), gameType);
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

    public void clearEnPassants(PieceColor color) {
        for (List<ChessField> row : fields)
            for (ChessField field : row) {
                if (field.isEmpty() || field.getPiece().getPieceColor() != color)
                    continue;
                if (field.getPiece() instanceof Pawn)
                    field.getPiece().setAsMoved(0);
            }
    }

    public void checkIfEnPassant(Piece piece) {
        if (!(piece instanceof Pawn))
            return;
        if (((Pawn) piece).enPassantOnRight(this, getLocation(piece)))
            takePiece(getRightField(getLocation(piece)));
        if (((Pawn) piece).enPassantOnLeft(this, getLocation(piece)))
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

    public void setPromotedPiece(String coordinates, Piece piece) {
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
        for (int i = 0; i < 8; i++) {
            if (fields.get(0).get(i).getPiece() != null && fields.get(0).get(i).getPiece() instanceof Pawn)
                return fields.get(0).get(i);
            if (fields.get(7).get(i).getPiece() != null && fields.get(7).get(i).getPiece() instanceof Pawn)
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

    public void setNextPlayer() {
        if (playerToMove.equals(WHITE))
        {
            playerToMove = BLACK;
            timerBlack.start();
            timerWhite.pause();
        }
        else
        {
            playerToMove = WHITE;
            timerBlack.pause();
            timerWhite.start();
        }
    }

    public List<ChessField> getAllMovesForColor(PieceColor color) {
        List<ChessField> moves = new ArrayList<>();
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (!field.isEmpty() && field.getPiece().getPieceColor().equals(color))
                    moves.addAll(field.getPiece().getMoves(this, true, true, false));

        moves = moves.stream().distinct().collect(Collectors.toList());
        return moves;
    }

    public boolean checkIfChecked(PieceColor color) {
        Piece checkedKing = null;
        for (List<ChessField> row : fields)
            for (ChessField field : row)
                if (field.getPiece() instanceof King && field.getPiece().getPieceColor().equals(color)) {
                    checkedKing = field.getPiece();
                    break;
                }
        List<ChessField> checkedFields = getAllMovesForColor(checkedKing.getPieceColor() == WHITE ? BLACK : WHITE);
        return checkedFields.contains(getLocation(checkedKing));
    }

    public boolean checkIfDraw(PieceColor color) {
        if(!isEnoughMaterialForPlayer(WHITE) && !isEnoughMaterialForPlayer(BLACK))
            return true;
        return getAllMovesForColor(color).isEmpty() && !checkIfChecked(color);
    }


    public boolean isEnoughMaterialForPlayer(PieceColor color) {
        List<Piece> pieces = new ArrayList<>();
        for(List<ChessField> row : fields)
            for(ChessField f : row)
                if(f.getPiece() != null && !(f.getPiece() instanceof King && f.getPiece().getPieceColor() == color))
                    pieces.add(f.getPiece());
        if(pieces.isEmpty())
            return true;
        return pieces.size() != 1 || (!(pieces.get(0) instanceof Knight) && !(pieces.get(0) instanceof Bishop));
    }

    public boolean checkIfMated(PieceColor color) {
        if (!checkIfChecked(color))
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
        for (int i = 0; i < 4; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(margin, margin, margin, margin);
            drawables.get(i).setLayoutParams(params);

            int index = i;
            PieceColor color = playerToMove;
            drawables.get(i).setOnClickListener(v -> {
                if (index == 0)
                    setPromotedPiece(field.getCoordinates(), new Queen(color));
                if (index == 1)
                    setPromotedPiece(field.getCoordinates(), new Rook(color));
                if (index == 2)
                    setPromotedPiece(field.getCoordinates(), new Bishop(color));
                if (index == 3)
                    setPromotedPiece(field.getCoordinates(), new Knight(color));
                dialog.dismiss();
            });

            layout.addView(drawables.get(i));
        }

        dialog.show();
    }

    private List<ImageView> getDrawablesForPromotion() {
        List<ImageView> figures = new ArrayList<>();

        // QUEEN
        ImageView promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.queen);
        promotionView.setVisibility(View.VISIBLE);
        if (playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        // ROOK
        promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.rook);
        promotionView.setVisibility(View.VISIBLE);
        if (playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        // BISHOP
        promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.bishop);
        promotionView.setVisibility(View.VISIBLE);
        if (playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        // KNIGHT
        promotionView = new ImageView(activity);
        promotionView.setImageResource(R.drawable.knight);
        promotionView.setVisibility(View.VISIBLE);
        if (playerToMove == WHITE)
            promotionView.setColorFilter(Color.WHITE);
        else {
            promotionView.setColorFilter(Color.BLACK);
            promotionView.setRotation(180f);
        }
        figures.add(promotionView);

        return figures;
    }

}

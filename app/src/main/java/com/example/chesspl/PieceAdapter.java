package com.example.chesspl;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chesspl.chessClasses.ChessField;
import com.example.chesspl.chessClasses.Chessboard;
import com.example.chesspl.chessClasses.figureClasses.Piece;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder> {

    public interface OnPieceClickListener {
        void onPieceClick(int drawableId, int color);
    }

    private List<Integer> pieces;
    private OnPieceClickListener listener;

    private final Chessboard chessboard;
    private final Map<String, Integer> initialLimits;


    public PieceAdapter(List<Integer> pieces, OnPieceClickListener listener, Chessboard chessboard)
    {
        this.pieces = pieces;
        this.listener = listener;
        this.chessboard = chessboard;

        initialLimits = new HashMap<>();
        initialLimits.put("king_white", 1);
        initialLimits.put("king_black", 1);

        initialLimits.put("queen_white", 1);
        initialLimits.put("queen_black", 1);

        initialLimits.put("rook_white", 2);
        initialLimits.put("rook_black", 2);

        initialLimits.put("bishop_white", 2);
        initialLimits.put("bishop_black", 2);

        initialLimits.put("knight_white", 2);
        initialLimits.put("knight_black", 2);

        initialLimits.put("pawn_white", 8);
        initialLimits.put("pawn_black", 8);
    }

    private String getPieceType(int drawableId) {
        if(drawableId == R.drawable.king) return "king";
        if(drawableId == R.drawable.queen) return "queen";
        if(drawableId == R.drawable.rook) return "rook";
        if(drawableId == R.drawable.bishop) return "bishop";
        if(drawableId == R.drawable.knight) return "knight";
        if(drawableId == R.drawable.pawn) return "pawn";
        return "piece";
    }

    private int countPieces(String type, String colorString)
    {
        int count = 0;
        if (chessboard == null || chessboard.getFields() == null) return 0;

        for (List<ChessField> row : chessboard.getFields()) {
            for (ChessField field : row) {
                Piece piece = field.getPiece();
                if (piece != null) {
                    String pieceType = piece.getClass().getSimpleName().toLowerCase();
                    String pieceColor = piece.getPieceColor().toString().toLowerCase();

                    if (pieceType.equals(type) && pieceColor.equals(colorString)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public PieceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_piece, parent, false);
        return new PieceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PieceViewHolder holder, int position) {

        int drawableId = pieces.get(position);
        holder.pieceImage.setImageResource(drawableId);

        boolean isBlack = position >= 6;
        String colorString = isBlack ? "black" : "white";
        int color = isBlack ? Color.BLACK : Color.WHITE;

        holder.pieceImage.setColorFilter(color);

        String type = getPieceType(drawableId);
        String key = type + "_" + colorString;

        int currentCount = countPieces(type, colorString);
        int maxLimit = initialLimits.getOrDefault(key, 0);
        int remaining = maxLimit - currentCount;

        if (remaining <= 0) {
            holder.pieceImage.setAlpha(0.3f);
            holder.itemView.setOnClickListener(null);
        } else {
            holder.pieceImage.setAlpha(1.0f);

            holder.itemView.setOnClickListener(v -> {
                listener.onPieceClick(drawableId, color);

                notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        return pieces.size();
    }

    public static class PieceViewHolder extends RecyclerView.ViewHolder
    {
        ImageView pieceImage;
        public PieceViewHolder(View itemView)
        {
            super(itemView);
            pieceImage = itemView.findViewById(R.id.pieceImage);
        }
    }
}
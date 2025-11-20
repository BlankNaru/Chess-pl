//package com.example.chesspl;
//
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder>
//{
//
//    public interface OnPieceClickListener
//    {
//        void onPieceClick(int drawableId, int color);
//    }
//
//    private List<Integer> pieces;
//    private OnPieceClickListener listener;
//
//    public PieceAdapter(List<Integer> pieces, OnPieceClickListener listener)
//    {
//        this.pieces = pieces;
//        this.listener = listener;
//    }
//
//
//    @Override
//    public PieceViewHolder onCreateViewHolder( ViewGroup parent, int viewType)
//    {
//
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.item_piece, parent, false);
//        return new PieceViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(PieceViewHolder holder, int position)
//    {
//        int drawableId = pieces.get(position);
//        holder.pieceImage.setImageResource(drawableId);
//
//        int color;
//        if(position >= 6)
//        {
//            color = Color.BLACK;
//        }
//        else
//        {
//            color = Color.WHITE;
//        }
//        holder.pieceImage.setColorFilter(color);
//
//        holder.itemView.setOnClickListener(v ->
//        {
//            listener.onPieceClick(drawableId, color);
//            pieces.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, pieces.size());
//        });
//    }
//
//    @Override
//    public int getItemCount()
//    {
//        return pieces.size();
//    }
//
//    public static class PieceViewHolder extends RecyclerView.ViewHolder
//    {
//        ImageView pieceImage;
//        public PieceViewHolder( View itemView)
//        {
//            super(itemView);
//            pieceImage = itemView.findViewById(R.id.pieceImage);
//        }
//    }
//}

package com.example.chesspl;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder> {

    public interface OnPieceClickListener {
        void onPieceClick(int drawableId, int color);
    }

    private List<Integer> pieces;
    private OnPieceClickListener listener;

    // Limity figur
    private Map<String, Integer> limits = new HashMap<>();

    public PieceAdapter(List<Integer> pieces, OnPieceClickListener listener)
    {
        this.pieces = pieces;
        this.listener = listener;

        limits.put("king_white", 1);
        limits.put("king_black", 1);

        limits.put("queen_white", 1);
        limits.put("queen_black", 1);

        limits.put("rook_white", 2);
        limits.put("rook_black", 2);

        limits.put("bishop_white", 2);
        limits.put("bishop_black", 2);

        limits.put("knight_white", 2);
        limits.put("knight_black", 2);

        limits.put("pawn_white", 8);
        limits.put("pawn_black", 8);
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
        int color = isBlack ? Color.BLACK : Color.WHITE;

        holder.pieceImage.setColorFilter(color);

        String type = getPieceType(drawableId);
        String key = type + "_" + (isBlack ? "black" : "white");

        holder.itemView.setOnClickListener(v -> {

            int remaining = limits.get(key);

            if (remaining <= 0)
            {
                return;
            }

            listener.onPieceClick(drawableId, color);

            limits.put(key, remaining - 1);

            if (limits.get(key) == 0)
            {
                holder.pieceImage.setAlpha(70);
            }
        });
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



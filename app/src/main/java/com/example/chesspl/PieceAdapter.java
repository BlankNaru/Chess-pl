package com.example.chesspl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.PieceViewHolder>
{

    public interface OnPieceClickListener
    {
        void onPieceClick(int drawableId);
    }

    private List<Integer> pieces;
    private OnPieceClickListener listener;

    public PieceAdapter(List<Integer> pieces, OnPieceClickListener listener)
    {
        this.pieces = pieces;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PieceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_piece, parent, false);
        return new PieceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PieceViewHolder holder, int position)
    {

        int drawableId = pieces.get(position);
        holder.pieceImage.setImageResource(drawableId);

        holder.itemView.setOnClickListener(v ->
        {
            listener.onPieceClick(drawableId);
            pieces.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pieces.size());
        });
    }

    @Override
    public int getItemCount()
    {
        return pieces.size();
    }

    public static class PieceViewHolder extends RecyclerView.ViewHolder
    {
        ImageView pieceImage;
        public PieceViewHolder(@NonNull View itemView)
        {
            super(itemView);
            pieceImage = itemView.findViewById(R.id.pieceImage);
        }
    }
}

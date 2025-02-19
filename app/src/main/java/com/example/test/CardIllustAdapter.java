package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardIllustAdapter extends RecyclerView.Adapter<CardIllustAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Integer> imageIds;
    private OnItemClickListener listener;

    // インターフェース定義（クリックイベント用）
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CardIllustAdapter(Context context, ArrayList<Integer> imageIds, OnItemClickListener listener) {
        this.context = context;
        this.imageIds = imageIds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(imageIds.get(position));
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount() {
        return imageIds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gridImage);
        }

        public void bind(int position, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}

package com.example.playertest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
        private ArrayList<Track> mDataset;
        private OnItemClickInterface onItemClickInterface;


    public RecyclerAdapter(ArrayList<Track> mDataset, OnItemClickInterface onItemClickInterface) {
        this.mDataset = mDataset;
        this.onItemClickInterface = onItemClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            holder.title.setText(String.format("%s - %s", mDataset.get(position).getArtist(), mDataset.get(position).getTitle()));
            holder.albumcover.setImageBitmap(mDataset.get(position).getAlbumCover());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickInterface.onItemClick(holder.getAdapterPosition());
                }
            });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView albumcover;
        ConstraintLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTracktitle_tv);
            albumcover = itemView.findViewById(R.id.itemAlbumCover_iv);
            container = itemView.findViewById(R.id.card);
        }
    }

public interface OnItemClickInterface {
      void onItemClick(int position);
}
}


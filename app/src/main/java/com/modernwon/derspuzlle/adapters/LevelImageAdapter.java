package com.modernwon.derspuzlle.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modernwon.derspuzlle.PuzzleActivity;
import com.modernwon.derspuzlle.R;
import java.util.List;

public class LevelImageAdapter extends RecyclerView.Adapter<LevelImageAdapter.ViewHolder> {
    private List<Integer> mImageList;
    private Context mContext;
    public LevelImageAdapter(List<Integer> imageList) {
        mImageList = imageList;
    }
    @NonNull
    @Override
    public LevelImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.level_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelImageAdapter.ViewHolder holder, int position) {
        int imageResource1 = mImageList.get(position * 2);
        int imageResource2 = 0;
        if ((position * 2 + 1) < mImageList.size()) {
            imageResource2 = mImageList.get(position * 2 + 1);
        }
        holder.imageView1.setImageResource(imageResource1);
        holder.imageView2.setImageResource(imageResource2);
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) mImageList.size() / 2);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1, imageView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.image_view1);
            imageView2 = itemView.findViewById(R.id.image_view2);

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // Get the position of the item clicked
                    if (position != RecyclerView.NO_POSITION) { // Check if the position is valid
                        startPuzzleActivity(position * 2); // Start PuzzleActivity with position * 2
                    }
                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // Get the position of the item clicked
                    if (position != RecyclerView.NO_POSITION) { // Check if the position is valid
                        startPuzzleActivity(position * 2 + 1); // Start PuzzleActivity with position * 2 + 1
                    }
                }
            });
        }
        private void startPuzzleActivity(int pos) {
            Log.d("Position", "pos1 = " + pos);
            Intent intent = new Intent(itemView.getContext(), PuzzleActivity.class);
            intent.putExtra("image_pos", pos);
            itemView.getContext().startActivity(intent);
        }
    }
}


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
    private int mCurrentLevel;

    private Context mContext;
    public LevelImageAdapter(List<Integer> imageList, int currentLevel) {
        mImageList = imageList;
        mCurrentLevel = currentLevel;
        Log.d("LevelImageAdapter", "mImageList = " + mImageList );
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
        int index1 = position * 2;
        int index2 = position * 2 + 1;

        // Check if the first image index is within the current level
        if (index1 < mCurrentLevel) {
            holder.imageView1.setImageResource(mImageList.get(index1));
        } else {
            holder.imageView1.setImageResource(R.drawable.locked_level);  // Default image for beyond the current level
        }

        // Check if the second image index is within the list and the current level
        if (index2 < mImageList.size()) {
            if (index2 < mCurrentLevel) {
                holder.imageView2.setImageResource(mImageList.get(index2));
            } else {
                holder.imageView2.setImageResource(R.drawable.locked_level);  // Default image for beyond the current level
            }
            holder.imageView2.setVisibility(View.VISIBLE);
        } else {
            holder.imageView2.setVisibility(View.INVISIBLE);
        }
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


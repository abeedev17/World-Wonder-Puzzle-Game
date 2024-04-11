package com.modernwon.derspuzlle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        }
    }
}


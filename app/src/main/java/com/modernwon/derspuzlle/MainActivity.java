package com.modernwon.derspuzlle;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modernwon.derspuzlle.adapters.LevelImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private RecyclerView mLevelRecyclerView;
    private List<Integer> mLevelImageDataList = new ArrayList<Integer>() {{
        add(R.drawable.level_icon_1);
        add(R.drawable.level_icon_2);
        add(R.drawable.level_icon_3);
        add(R.drawable.level_icon_4);
        add(R.drawable.level_icon_5);
        add(R.drawable.level_icon_6);
        add(R.drawable.level_icon_7);
        add(R.drawable.level_icon_8);
        add(R.drawable.level_icon_9);
        add(R.drawable.level_icon_10);
    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text_title);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.plus_jakarta_sans);
        textView.setTypeface(typeface, Typeface.BOLD);

        mLevelRecyclerView = findViewById(R.id.level_recycler_view);
        mLevelRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        SharedPreferences sharedPreferences = getSharedPreferences("MyGamePrefs", Context.MODE_PRIVATE);
        int currentLevel = sharedPreferences.getInt("currentLevel", 1);

        LevelImageAdapter mLevelImageAdapter = new LevelImageAdapter(mLevelImageDataList, currentLevel);
        mLevelImageAdapter.setImageList(mLevelImageDataList);
        mLevelRecyclerView.setAdapter(mLevelImageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLevels();
    }

    private void updateLevels() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyGamePrefs", Context.MODE_PRIVATE);
        int currentLevel = sharedPreferences.getInt("currentLevel", 1);
        LevelImageAdapter mLevelImageAdapter = new LevelImageAdapter(mLevelImageDataList, currentLevel);
        mLevelImageAdapter.setImageList(mLevelImageDataList);
        mLevelRecyclerView.setAdapter(mLevelImageAdapter);
    }

}
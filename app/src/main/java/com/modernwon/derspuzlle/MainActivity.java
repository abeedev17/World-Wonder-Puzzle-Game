package com.modernwon.derspuzlle;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
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
    private List<Integer> mLeveImageDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text_title);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.plus_jakarta_sans);
        textView.setTypeface(typeface, Typeface.BOLD);

        mLevelRecyclerView = (RecyclerView) findViewById(R.id.level_recycler_view);
        mLevelRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        String[] imageNames = getResources().getStringArray(R.array.level_icons);

        // Loop through the image names and get their resource IDs
        for (String imageName : imageNames) {
            Log.d("MainActivity", "ImageName = " + imageName);
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            Log.d("MainActivity", "resourceId = " + resourceId);
            if (resourceId != 0) {
                Log.d("MainActivity", "resourceId inside = " + resourceId);
                mLeveImageDataList.add(resourceId);
            }
        }

        // Retrieve currentLevel from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyGamePrefs", Context.MODE_PRIVATE);
        int currentLevel = sharedPreferences.getInt("currentLevel", 1); // Default to 1 if not found

        // Pass currentLevel and image list to adapter
        LevelImageAdapter mLevelImageAdapter = new LevelImageAdapter(mLeveImageDataList, currentLevel);
        mLevelRecyclerView.setAdapter(mLevelImageAdapter);
    }
}
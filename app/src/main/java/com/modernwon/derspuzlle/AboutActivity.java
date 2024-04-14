package com.modernwon.derspuzlle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageButton backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mJigsawPuzzle = findViewById(R.id.text_select_attraction);

        int currentLevel = getIntent().getIntExtra("image_pos", 0) + 1;
        String wonderStringId = "level_string_";

        int wonderTextResource = getResources().getIdentifier(wonderStringId + currentLevel, "string", getPackageName());
        String wonderTitle = getResources().getString(wonderTextResource);

        mJigsawPuzzle.setText(wonderTitle.toUpperCase());



        String layoutName = "level_about_" + currentLevel;
        int layoutResId = getResources().getIdentifier(layoutName, "layout", getPackageName());

        // Find the container LinearLayout
        LinearLayout container = findViewById(R.id.about_container);

        if (layoutResId != 0) {
            // Inflate the specific level layout into the found container
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(layoutResId, container, false);

            // Add the inflated layout to the container
            container.addView(layout);
        } else {
            // Optionally handle the error, e.g., show a default text or layout
            TextView errorText = new TextView(this);
            errorText.setText("Details for this level are not available.");
            container.addView(errorText);
        }
    }
}
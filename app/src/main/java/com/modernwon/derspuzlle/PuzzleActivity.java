package com.modernwon.derspuzlle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuzzleActivity extends AppCompatActivity {

    private GridLayout puzzleGrid;
    private List<Bitmap> originalPieces;
    private List<ImageView> views = new ArrayList<>();
    private TextView mJigsawPuzzle;
    List<Bitmap> originalOrder = new ArrayList<>();
    List<Bitmap> reArrangeOrder = new ArrayList<>();
    List<Bitmap> currentOrder = new ArrayList<>();

    RelativeLayout shuffleButton, nextButton, aboutButton;
    ImageButton menuButton;
    TextView nextBtnText, aboutBtnText;
    private ImageView selectedPiece = null;
    private int pieceWidth = 0, pieceHeight = 0;
    private int mLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        puzzleGrid = findViewById(R.id.puzzleGrid);
        shuffleButton = (RelativeLayout) findViewById(R.id.shuffle_btn);
        nextButton = (RelativeLayout) findViewById(R.id.next_btn);
        aboutButton = (RelativeLayout) findViewById(R.id.about_btn);
        nextBtnText = (TextView) findViewById(R.id.next_btn_text);
        aboutBtnText = (TextView) findViewById(R.id.about_btn_text);
        menuButton = (ImageButton) findViewById(R.id.menu_btn);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyGamePrefs", Context.MODE_PRIVATE);

        int defaultLevel = 1;
        mLevel = sharedPreferences.getInt("currentLevel", defaultLevel);


        mJigsawPuzzle = findViewById(R.id.text_select_attraction);

        Intent intent = getIntent();
        int imagePos = intent.getIntExtra("image_pos", 0) + 1;
        String imageName = "level_img_";
        String wonderStringId = "level_string_";

        int imageResource = getResources().getIdentifier(imageName + imagePos, "drawable", getPackageName());
        int wonderTextResource = getResources().getIdentifier(wonderStringId + imagePos, "string", getPackageName());
        String wonderTitle = getResources().getString(wonderTextResource);

        mJigsawPuzzle.setText(wonderTitle.toUpperCase());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int availableWidth = screenWidth - convertDpToPx(24 * 2);
        int availableHeight = screenHeight - convertDpToPx(390);

        int gap = convertDpToPx(4);
        int totalGapWidth = (4 * gap); // 4 gaps between 5 columns
        int totalGapHeight = (6 * gap); // 6 gaps between 7 rows

        pieceWidth = (availableWidth - totalGapWidth) / 5;
        pieceHeight = (availableHeight - totalGapHeight) / 7;

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageResource);
        originalPieces = splitImage(originalBitmap, 5, 7);


        for (int i = 0; i < originalPieces.size(); i++) {
            originalOrder.add(originalPieces.get(i));
        }

        int cnt = 0;
        for (int j = 0; j < 5; j ++) {
            for (int i = 0; i < originalOrder.size(); i++) {
                if (i % 5 == j) {
                    reArrangeOrder.add(cnt++, originalOrder.get(i));
                }
            }
        }

        List<Bitmap> shuffledPieces = new ArrayList<>(originalPieces);
        Collections.shuffle(shuffledPieces);
        initializeGrid(shuffledPieces, pieceWidth, pieceHeight);
        shuffleButton.setOnClickListener(v -> {
            // Shuffle and re-initialize the puzzle
            Collections.shuffle(shuffledPieces);
            initializeGrid(shuffledPieces, pieceWidth, pieceHeight);
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nextPuzzleIndex = getIntent().getIntExtra("image_pos", 0) + 1;
                if (nextPuzzleIndex < mLevel) { // Ensure the next puzzle is within the unlocked levels
                    Intent intent = new Intent(PuzzleActivity.this, PuzzleActivity.class);
                    intent.putExtra("image_pos", nextPuzzleIndex);
                    startActivity(intent);
                    finish(); // Finish the current activity to avoid back stack build-up
                } else {
                    Toast.makeText(PuzzleActivity.this, "No more puzzles unlocked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "about", Toast.LENGTH_SHORT).show();

            }
        });

        int currentPuzzleIndex = getIntent().getIntExtra("image_pos", 0);
        if (currentPuzzleIndex + 1 < mLevel) {
            enableControls();
        } else {
            disableControls();
        }
    }

    private void enableControls() {
        nextButton.setEnabled(true);
        aboutButton.setEnabled(true);
        nextBtnText.setTextColor(ContextCompat.getColor(this, R.color.active_color));
        aboutBtnText.setTextColor(ContextCompat.getColor(this, R.color.active_color));
    }

    private void disableControls() {
        nextButton.setEnabled(false);
        aboutButton.setEnabled(false);
        nextBtnText.setTextColor(ContextCompat.getColor(this, R.color.inactive_color));
        aboutBtnText.setTextColor(ContextCompat.getColor(this, R.color.inactive_color));
    }

    public int convertDpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
    private List<Bitmap> splitImage(Bitmap image, int cols, int rows) {
        int pieceWidth = image.getWidth() / cols;
        int pieceHeight = image.getHeight() / rows;
        List<Bitmap> pieces = new ArrayList<>();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                pieces.add(Bitmap.createBitmap(image, x * pieceWidth, y * pieceHeight, pieceWidth, pieceHeight));
            }
        }
        return pieces;
    }

    private void initializeGrid(List<Bitmap> pieces, int pieceWidth, int pieceHeight) {
        puzzleGrid.removeAllViews();
        int gap = convertDpToPx(4);
        for (Bitmap piece : pieces) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(piece);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = pieceWidth;
            params.height = pieceHeight;
            params.setMargins(gap/2, gap/2, gap/2, gap/2);  // Assuming a 2dp margin
            imageView.setLayoutParams(params);
            puzzleGrid.addView(imageView);
            views.add(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(this::swapPieces);
        }
    }


    private void swapPieces(View view) {
        if (selectedPiece == null) {
            selectedPiece = (ImageView) view;
            selectedPiece.setAlpha(0.5f);
        } else {
            int firstPosition = views.indexOf(selectedPiece);
            int secondPosition = views.indexOf(view);
            Log.d("SwapPieces", "First position: " + firstPosition + ", Second position: " + secondPosition);
            Drawable firstDrawable = selectedPiece.getDrawable();
            Drawable secondDrawable = ((ImageView) view).getDrawable();

            // Convert drawables to bitmaps
            Bitmap firstPiece = drawableToBitmap(firstDrawable);
            Bitmap secondPiece = drawableToBitmap(secondDrawable);

            // Set bitmaps to the image views
            selectedPiece.setImageBitmap(secondPiece);
            ((ImageView) view).setImageBitmap(firstPiece);

            selectedPiece.setAlpha(1.0f);
            selectedPiece = null;

            currentOrder = new ArrayList<>();
            for (ImageView imageView : views) {
                currentOrder.add(drawableToBitmap(imageView.getDrawable()));
            }

            // Now you have the current order of shuffled pieces
            Log.d("SwapPieces", "rearang pieces: " + reArrangeOrder);
            Log.d("SwapPieces", "current pieces: " + currentOrder);

            checkCompletion();
        }
    }

    private void checkCompletion() {
        boolean completed = compareOrders(); // Simplified the completion check to directly use the result of compareOrders.
        if (completed) {
            Toast.makeText(this, "Puzzle Completed!", Toast.LENGTH_SHORT).show();

            int completedPuzzleIndex = getIntent().getIntExtra("image_pos", 0);
            if (completedPuzzleIndex + 1 == mLevel) {
                mLevel++;
                SharedPreferences sharedPreferences = getSharedPreferences("MyGamePrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentLevel", mLevel);
                editor.apply();
            }
            enableControls();
        }
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    private boolean compareOrders() {
        if (reArrangeOrder.size() != currentOrder.size()) {
            return false;
        }
        for (int i = 0; i < reArrangeOrder.size(); i++) {
            if (reArrangeOrder.get(i).hashCode() != currentOrder.get(i).hashCode()) {
                return false;
            }
        }
        return true;
    }
}
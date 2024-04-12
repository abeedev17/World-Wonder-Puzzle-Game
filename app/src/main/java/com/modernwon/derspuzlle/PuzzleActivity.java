package com.modernwon.derspuzlle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuzzleActivity extends AppCompatActivity {

    private GridLayout puzzleGrid;
    private List<Bitmap> originalPieces;
    private List<ImageView> views = new ArrayList<>();
    List<Bitmap> originalOrder = new ArrayList<>();
    List<Bitmap> reArrangeOrder = new ArrayList<>();
    List<Bitmap> currentOrder = new ArrayList<>();
    private ImageView selectedPiece = null;
    private int pieceWidth = 0, pieceHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        puzzleGrid = findViewById(R.id.puzzleGrid);
        Button shuffleButton = findViewById(R.id.shuffleButton);

        Intent intent = getIntent();
        int imagePos = intent.getIntExtra("image_pos", 0) + 1;
        String imageName = "level_img_";

        Log.d("PuzzleActivity", "PuzzleImg imagePos = " + imagePos);

        int imageResource = getResources().getIdentifier(imageName + imagePos, "drawable", getPackageName());
        Log.d("PuzzleActivity", "PuzzleImg imageResource = " + imageResource);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int availableWidth = screenWidth - convertDpToPx(16 * 2);
        int availableHeight = screenHeight - convertDpToPx(300 + 16 * 2);

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
        boolean completed = true;
        for (int i = 0; i < views.size(); i++) {
            ImageView view = views.get(i);
            Drawable drawable = view.getDrawable();
            Bitmap currentPiece = drawableToBitmap(drawable);
            if (compareOrders()) {
                Log.d("PuzzleActivity", "RightPlace");
            }
            if (!compareOrders()) {
                Log.d("PuzzleActivity", "Not RightPlace");
                completed = false;
                break;
            }
        }
        if (completed) {
            // Handle completion, e.g., show a message or close the activity
            finish();
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

    private boolean bitmapEquals(Bitmap bitmap1, Bitmap bitmap2) {
        if (bitmap1.getWidth() != bitmap2.getWidth() || bitmap1.getHeight() != bitmap2.getHeight()) {
            Log.d("PuzzleActivity", "PieceSize wrong");
            return false;
        }
        Log.d("PuzzleActivity", "PieceSize Right: (" + bitmap1.getWidth() + "," + bitmap1.getHeight() + ")" + ", (" + bitmap2.getWidth() +", " + bitmap2.getHeight() + ")");

        for (int y = 0; y < bitmap1.getHeight(); y++) {
            for (int x = 0; x < bitmap1.getWidth(); x++) {
                if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
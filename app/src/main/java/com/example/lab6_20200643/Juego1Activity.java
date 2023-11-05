package com.example.lab6_20200643;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lab6_20200643.databinding.ActivityJuego1Binding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Juego1Activity extends AppCompatActivity {
    ActivityJuego1Binding binding;
    GridLayout gridLayout;
    Button resetButton;
    ImageButton backBtn;
    private ImageView[][] imageViews;
    private int emptyX, emptyY;
    private int gridSize = 3;
    Bitmap[][] imagePieces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJuego1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gridLayout = findViewById(R.id.gridLayout1);
        resetButton = findViewById(R.id.resetButton1);
        backBtn = binding.backBtn;
        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });


        // Initialize the imageViews grid
        imageViews = new ImageView[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                imageViews[i][j] = new ImageView(this);
                imageViews[i][j].setLayoutParams(new GridLayout.LayoutParams());
                imageViews[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViews[i][j].setPadding(2, 2, 2, 2);
                imageViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTileClick((ImageView) view);
                    }
                });
                gridLayout.addView(imageViews[i][j]);
            }
        }

        // Load the image and initialize the game
        loadAndInitializeImage();
    }


    private void loadAndInitializeImage() {
        // Load your image and initialize the game here
        // You can split the image into pieces, shuffle them, and display them in the grid

        // Replace this with your image loading logic
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rompecabezas);

        // Split the original image into pieces
        imagePieces = splitImage(originalBitmap, gridSize);

        // Shuffle the pieces
        shuffleImagePieces(imagePieces);

        // Display the shuffled pieces in the grid
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                imageViews[i][j].setImageBitmap(imagePieces[i][j]);
            }
        }
    }

    private void onTileClick(ImageView tile) {
        // Implement logic for tile clicks
        // Check if the clicked tile can be moved and swap it with the empty tile
        // Check if the puzzle is solved
        BitmapDrawable drawable = (BitmapDrawable) tile.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Get the position of the clicked tile
        int clickedX = -1;
        int clickedY = -1;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (imageViews[i][j] == tile) {
                    clickedX = i;
                    clickedY = j;
                    break;
                }
            }
        }

        if (canMoveTile(clickedX, clickedY)) {
            // Swap the clicked tile with the empty tile
            Bitmap emptyTileBitmap = ((BitmapDrawable) imageViews[emptyX][emptyY].getDrawable()).getBitmap();
            imageViews[emptyX][emptyY].setImageBitmap(bitmap);
            imageViews[clickedX][clickedY].setImageBitmap(emptyTileBitmap);

            // Update the position of the empty tile
            emptyX = clickedX;
            emptyY = clickedY;

            // Check if the puzzle is solved
            if (isPuzzleSolved()) {
                // Implement logic for puzzle solved
            }
        }
    }

    public void onResetButtonClick(View view) {
        // Implement logic to reset the puzzle
        // Shuffle the tiles and display them in a random order
        shuffleImagePieces(imagePieces);

        // Redraw the shuffled pieces in the grid
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                imageViews[i][j].setImageBitmap(imagePieces[i][j]);
            }
        }
    }


    private Bitmap[][] splitImage(Bitmap originalImage, int pieces) {
        int pieceWidth = originalImage.getWidth() / pieces;
        int pieceHeight = originalImage.getHeight() / pieces;
        Bitmap[][] imagePieces = new Bitmap[pieces][pieces];

        for (int i = 0; i < pieces; i++) {
            for (int j = 0; j < pieces; j++) {
                imagePieces[i][j] = Bitmap.createBitmap(originalImage, i * pieceWidth, j * pieceHeight, pieceWidth, pieceHeight);
            }
        }

        return imagePieces;
    }

    private void shuffleImagePieces(Bitmap[][] imagePieces) {
        List<Bitmap> pieceList = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                pieceList.add(imagePieces[i][j]);
            }
        }
        Collections.shuffle(pieceList);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                imagePieces[i][j] = pieceList.get(i * gridSize + j);
            }
        }
    }

    private boolean canMoveTile(int clickedX, int clickedY) {
        if (Math.abs(clickedX - emptyX) + Math.abs(clickedY - emptyY) == 1) {
            return true;
        }
        return false;
    }

    private boolean isPuzzleSolved() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                BitmapDrawable drawable = (BitmapDrawable) imageViews[i][j].getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Bitmap originalPiece = imagePieces[i][j];
                if (!bitmap.sameAs(originalPiece)) {
                    return false;
                }
            }
        }
        return true;
    }
}
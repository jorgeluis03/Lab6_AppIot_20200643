package com.example.lab6_20200643;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab6_20200643.databinding.ActivityJuego2Binding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Juego2Activity extends AppCompatActivity {
    ActivityJuego2Binding binding;
    ImageButton backBtn;
    GridLayout gridLayout;
    Button addImagesButton;
    Button shuffleButton;
    Button helpButton;
    TextView numImagesTextView;
    int numImagesAdded = 0;
    int numPairs = 0;
    int gridSize = 4; // Tamaño del tablero (4x4)
    static final int MAX_IMAGES = 15;
    private List<Integer> imageResources = new ArrayList<>();
    private ImageView firstCard = null;
    private ImageView secondCard = null;
    private int helpCount = 0;
    private static final int MAX_HELP_COUNT = 2; // Máximo de veces que se puede usar la ayuda

    List<Integer> imagePairs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJuego2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backBtn = binding.backBtn;

        gridLayout = findViewById(R.id.gridLayout);
        addImagesButton = findViewById(R.id.addImagesButton);
        shuffleButton = findViewById(R.id.shuffleButton);
        helpButton = findViewById(R.id.helpButton);
        numImagesTextView = findViewById(R.id.numImagesTextView);

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });


        addImagesButton.setOnClickListener(v -> {
            if (numImagesAdded < MAX_IMAGES) {
                // Agregar una imagen al carrusel
                ImageView newImageView = new ImageView(getApplicationContext());
                newImageView.setImageResource(R.drawable.rompecabezas); // Reemplaza con la imagen deseada
                newImageView.setLayoutParams(new GridLayout.LayoutParams());
                gridLayout.addView(newImageView);
                numImagesAdded++;

                // Actualizar el contador de imágenes
                numImagesTextView.setText("Imágenes agregadas: " + numImagesAdded);
            } else {
                // Se ha alcanzado el límite de imágenes
                Toast.makeText(this,"Se alcanzó el limite",Toast.LENGTH_SHORT).show();
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numImagesAdded >= 2) {
                    // Si hay al menos 2 imágenes agregadas
                    numPairs = numImagesAdded; // Cada imagen debe tener su pareja
                    createGameBoard();
                } else {
                    // No hay suficientes imágenes para crear el tablero
                    Toast.makeText(Juego2Activity.this,"Se alcanzó el limite",Toast.LENGTH_SHORT).show();
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helpCount < MAX_HELP_COUNT) {
                    // Implementar la lógica para la función de ayuda
                    if (firstCard != null) {
                        revealCard(firstCard, imageResources.get(imagePairs.indexOf(firstCard)));
                    }
                    helpCount++;
                    if (helpCount >= MAX_HELP_COUNT) {
                        helpButton.setEnabled(false); // Desactivar el botón de ayuda cuando se agota
                    }
                }
            }
        });


    }
    private void createGameBoard() {
        // Crear el tablero de juego
        gridLayout.removeAllViews(); // Eliminar las vistas anteriores

        int rows, cols;
        if (numPairs % 2 == 0) {
            // Si hay un número par de imágenes, crea un tablero cuadrado
            rows = cols = (int) Math.sqrt(numPairs * 2);
        } else {
            // Si hay un número impar de imágenes, crea un tablero rectangular con un número impar de columnas
            rows = (int) Math.sqrt(numPairs * 2);
            cols = rows - 1;
        }

        gridLayout.setRowCount(rows);
        gridLayout.setColumnCount(cols);

        // Duplicar los recursos de imágenes para tener pares
        imagePairs = new ArrayList<>(imageResources);
        imagePairs.addAll(imageResources);

        // Aleatorizar las imágenes en el tablero
        Collections.shuffle(imagePairs);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ImageView card = new ImageView(this);
                card.setImageResource(R.drawable.memoriajuego); // Imagen de dorso de tarjeta
                card.setLayoutParams(new GridLayout.LayoutParams());

                final int position = i * cols + j;
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Implementa la lógica para revelar la tarjeta
                        revealCard((ImageView) view, imagePairs.get(position));
                    }
                });

                gridLayout.addView(card);
            }
        }
    }

    private void revealCard(ImageView card, int imageResource) {
        if (firstCard == null) {
            firstCard = card;
            firstCard.setImageResource(imageResource);
        } else if (secondCard == null && !card.equals(firstCard)) {
            secondCard = card;
            secondCard.setImageResource(imageResource);

            // Comprobar si las dos cartas reveladas coinciden
            if (imageResource == imageResources.get(imagePairs.indexOf(firstCard)) &&
                    imagePairs.indexOf(firstCard) != imagePairs.indexOf(secondCard)) {
                // Las cartas coinciden, mantenerlas visibles
                firstCard = null;
                secondCard = null;

                // Comprobar si se completó el juego
                if (isGameCompleted()) {
                    // Implementar la lógica para el juego completado
                }
            } else {
                // Las cartas no coinciden, ocultarlas después de un breve retraso
                gridLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        firstCard.setImageResource(R.drawable.memoriajuego);
                        secondCard.setImageResource(R.drawable.memoriajuego);
                        firstCard = null;
                        secondCard = null;
                    }
                }, 1000); // 1000 ms (1 segundo) de retraso
            }
        }
    }
    private boolean isGameCompleted() {
        // Comprobar si todas las cartas están visibles
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof ImageView) {
                ImageView card = (ImageView) child;
                if (card.getDrawable().getConstantState().equals(
                        getResources().getDrawable(R.drawable.memoriajuego).getConstantState())) {
                    // Al menos una carta aún está oculta, el juego no está completo
                    return false;
                }
            }
        }

        // Si no se encontraron cartas ocultas, el juego está completo
        // Puedes mostrar un mensaje o realizar una acción específica aquí
        // Por ejemplo, mostrar un mensaje de felicitación
        // showToast("¡Has completado el juego!");

        return true;
    }

}
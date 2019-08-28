package com.proyecto.recipemaster.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.proyecto.recipemaster.R;

public class RecetasActivity extends AppCompatActivity {

    private ImageView imgToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgToolbar = findViewById(R.id.imgToolbar);
    }
}

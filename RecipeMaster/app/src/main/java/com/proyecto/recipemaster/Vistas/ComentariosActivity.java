package com.proyecto.recipemaster.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.proyecto.recipemaster.Clases.Comentario;
import com.proyecto.recipemaster.R;

import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private RecyclerView comentariosRecycler;
    private EditText comentarioEdt;
    private Button comentarButton;
    private LinearLayoutManager linearLayoutManager;
    private List<Comentario> comentariosList;
    private RecyclerView.Adapter comentarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Comentarios");

        comentariosRecycler = findViewById(R.id.comentario);
        comentarioEdt = findViewById(R.id.mensaje);
        comentarButton = findViewById(R.id.publicar);
    
        String id = getIntent().getStringExtra("idReceta");



    }
}

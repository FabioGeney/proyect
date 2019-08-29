package com.proyecto.recipemaster.Admin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proyecto.recipemaster.Adapter.IngredientesRecetaAdapter;
import com.proyecto.recipemaster.Adapter.PasosAdapterReceta;
import com.proyecto.recipemaster.Clases.Ingredientes;
import com.proyecto.recipemaster.Clases.Pasos;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;
import com.proyecto.recipemaster.Vistas.ComentariosActivity;
import com.proyecto.recipemaster.Vistas.PerfilAvtivity;
import com.proyecto.recipemaster.Vistas.RecetasActivity;
import com.squareup.picasso.Picasso;

public class RecetasAdmin extends AppCompatActivity {

    private ImageView imgToolbar;
    private CollapsingToolbarLayout ctlLayout;
    private TextView nombre;
    private TextView de;
    private TextView descripcion;
    private RecyclerView ingredientesRecycler;
    private RecyclerView pasosRecycler;
    private RecyclerView.LayoutManager layoutManagerPaso;
    private RecyclerView.LayoutManager layoutManagerIngre;
    private RecyclerView.Adapter pasosAdapter;
    private RecyclerView.Adapter ingreAdapter;
    private FloatingActionButton fab;
    private Receta receta;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SingletonReceta singletonReceta = SingletonReceta.getInstance();

        receta = singletonReceta.getReceta();

        imgToolbar = findViewById(R.id.imgToolbar);
        ctlLayout = findViewById(R.id.ctlLayout);
        nombre = findViewById(R.id.nombre);
        de = findViewById(R.id.de);
        descripcion = findViewById(R.id.descripcion);
        ingredientesRecycler  = findViewById(R.id.ingredientes);
        pasosRecycler = findViewById(R.id.pasos);
        fab = findViewById(R.id.enviarMensaje);



        layoutManagerPaso = new LinearLayoutManager(this);
        layoutManagerIngre = new LinearLayoutManager(this);

        nombre.setText(receta.getNombre());
        de.setText( "De: " + receta.getNombreRecetario());
        descripcion.setText(receta.getDescripcion());

        pasosAdapter = new PasosAdapterReceta(receta.getPasos(), R.layout.list_pasos_receta, new PasosAdapterReceta.OnItemClickListener() {
            @Override
            public void OnItemClick(Pasos pasos, int posicion) {

            }
        });

        ingreAdapter = new IngredientesRecetaAdapter(receta.getIngredientes(), R.layout.list_ingredientes_recet, new IngredientesRecetaAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Ingredientes ingredientes, int posicion) {

            }
        });

        de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecetasAdmin.this, PerfilAvtivity.class);
                intent.putExtra("idRecetero", receta.getIdUsuario());
                startActivity(intent);
            }
        });

        ingredientesRecycler.setLayoutManager(layoutManagerIngre);
        ingredientesRecycler.setAdapter(ingreAdapter);

        pasosRecycler.setLayoutManager(layoutManagerPaso);
        pasosRecycler.setAdapter(pasosAdapter);
        // ingredientesRecycler.setNestedScrollingEnabled(false);
        //pasosRecycler.setNestedScrollingEnabled(false);



        ctlLayout.setTitle(" ");
        Picasso.with(this).load(receta.getImagen()).fit().into(imgToolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecetasAdmin.this, ComentariosAdmin.class);
                intent.putExtra("idReceta", receta.getIdDocument());
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

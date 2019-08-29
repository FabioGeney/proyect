package com.proyecto.recipemaster.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.recipemaster.Adapter.CategoriasAdapter;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    private RecyclerView recetas;
    private CollapsingToolbarLayout ctlLayout;
    private GridLayoutManager gridLayoutManager;
    private List<Receta> recetasList;
    private RecyclerView.Adapter recetasAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        ctlLayout = findViewById(R.id.ctlLayout);
        recetas = findViewById(R.id.listaRecetas);
        imageView = findViewById(R.id.imgToolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Categoria categoria = (Categoria) getIntent().getSerializableExtra("categoria");

        ctlLayout.setTitle(categoria.getNombre());
        Picasso.with(this).load(categoria.getImagen()).fit().into(imageView);


        gridLayoutManager =  gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recetasList = getRecetas(categoria.getNombre());


    }

    private ArrayList<Receta> getRecetas(String cat){
        final ArrayList<Receta> temp = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Recetas").whereEqualTo("tipo", cat)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Receta receta = document.toObject(Receta.class);
                                receta.setIdDocument(document.getId());
                                temp.add(receta);

                            }
                            recetasAdapter = new RecetasAdapter(recetasList, CategoriaActivity.this,  R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(CategoriaActivity.this, RecetasActivity.class);
                                    SingletonReceta singletonReceta = SingletonReceta.getInstance();
                                    singletonReceta.setReceta(receta);
                                    startActivity(intent);
                                }
                            });

                            //Relacionando la lista con el adaptador
                            recetas.setLayoutManager(gridLayoutManager);
                            recetas.setAdapter(recetasAdapter);


                        } else {

                        }
                    }
                });
        return temp;
    }
}

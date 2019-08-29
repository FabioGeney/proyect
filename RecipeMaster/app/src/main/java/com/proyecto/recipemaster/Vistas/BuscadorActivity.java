package com.proyecto.recipemaster.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;

import java.util.ArrayList;
import java.util.List;

public class BuscadorActivity extends AppCompatActivity {

    private RecyclerView requestRecycler;
    private RecyclerView.Adapter requessAdapter;
    private List<Receta> requesriaList;
    private GridLayoutManager gridLayoutManager;
    private String request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        request = getIntent().getStringExtra("request");
        this.setTitle(request);

        requestRecycler = findViewById(R.id.request);

        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

        requesriaList = getRequest();


    }

    private ArrayList<Receta> getRequest(){
        final String temp = request.toLowerCase();
        final ArrayList<Receta> recetasRequest = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Receta receta = document.toObject(Receta.class);
                                receta.setIdDocument(document.getId());

                                String nombre = receta.getNombre().toLowerCase();
                                if( receta.getIngredienteLowerCase()!= null){
                                    String ingredientes = receta.getIngredienteLowerCase().toLowerCase();
                                    if(ingredientes.contains(temp) || temp.contains(ingredientes)){
                                        recetasRequest.add(receta);
                                    }
                                }


                                if(nombre.contains(temp) || temp.contains(nombre)){
                                    recetasRequest.add(receta);
                                }


                            }
                            // Inicializar el adaptador con la fuente de datos.
                            requessAdapter = new RecetasAdapter(requesriaList, BuscadorActivity.this, R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(BuscadorActivity.this, RecetasActivity.class);
                                    SingletonReceta singletonReceta = SingletonReceta.getInstance();
                                    singletonReceta.setReceta(receta);
                                    startActivity(intent);

                                }
                            });
                            //Relacionando la lista con el adaptador
                            requestRecycler.setLayoutManager(gridLayoutManager);
                            requestRecycler.setAdapter(requessAdapter);



                        } else {

                        }
                    }
                });

        return recetasRequest;


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

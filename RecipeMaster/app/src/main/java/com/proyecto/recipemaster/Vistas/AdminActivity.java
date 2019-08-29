package com.proyecto.recipemaster.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.recipemaster.Adapter.AdapterAdmin;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Admin.RecetasAdmin;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView.Adapter recetasAdapter;
    private List<Receta> recetaList;
    private GridLayoutManager gridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recetas);

        gridLayoutManager =  new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recetaList = getRecetas();

    }

    private ArrayList<Receta> getRecetas(){
        final ArrayList<Receta> temp = new ArrayList<>();

        db.collection("Recetas")
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
                            recetasAdapter = new AdapterAdmin(recetaList, AdminActivity.this,  R.layout.list_recetas, new AdapterAdmin.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(AdminActivity.this, RecetasAdmin.class);
                                    SingletonReceta singletonReceta = SingletonReceta.getInstance();
                                    singletonReceta.setReceta(receta);
                                    startActivity(intent);
                                }
                            });


                            //Relacionando la lista con el adaptador
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(recetasAdapter);


                        } else {

                        }
                    }
                });
        return temp;
    }
}

package com.proyecto.recipemaster.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAvtivity extends AppCompatActivity {

    private TextView nombre;
    private CircleImageView circleImageView;
    private RecyclerView misRecetas;
    private GridLayoutManager gridLayoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Receta> recetas;
    private RecyclerView.Adapter recAdapter;
    private String idRecetero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_avtivity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setTitle(" ");

        idRecetero = getIntent().getStringExtra("idRecetero");

        nombre = findViewById(R.id.nombre);
        misRecetas = findViewById(R.id.recyclerView);
        circleImageView = findViewById(R.id.image);

        getNombre();
        recetas = getRecetas();

        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
    }

    private void getNombre(){
        db.collection("Usuarios").document(idRecetero).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String temp = task.getResult().getData().get("nombre").toString();
                    Object image = task.getResult().getData().get("image");
                    nombre.setText(temp);
                    if(image!=null){
                        Picasso.with(PerfilAvtivity.this).load(image.toString()).fit().into(circleImageView);
                    }

                }
            }
        });
    }

    private ArrayList<Receta> getRecetas(){
        final ArrayList<Receta> temp = new ArrayList<>();
        db.collection("Recetas").whereEqualTo("idUsuario", idRecetero)
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
                            recAdapter = new RecetasAdapter(recetas, PerfilAvtivity.this,  R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(PerfilAvtivity.this, RecetasActivity.class);
                                    SingletonReceta singletonReceta = SingletonReceta.getInstance();
                                    singletonReceta.setReceta(receta);
                                    startActivity(intent);
                                }
                            });

                            //Relacionando la lista con el adaptador
                            misRecetas.setLayoutManager(gridLayoutManager);
                            misRecetas.setAdapter(recAdapter);


                        } else {

                        }
                    }
                });
        return temp;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

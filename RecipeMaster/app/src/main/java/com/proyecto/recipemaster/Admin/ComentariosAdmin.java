package com.proyecto.recipemaster.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.proyecto.recipemaster.Adapter.ComentariosAdapterAdmin;
import com.proyecto.recipemaster.Adapter.MensajesAdapter;
import com.proyecto.recipemaster.Clases.Comentario;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.R;

import java.util.ArrayList;
import java.util.List;

public class ComentariosAdmin extends AppCompatActivity {

    private RecyclerView comentariosRecycler;

    private LinearLayoutManager linearLayoutManager;
    private List<Comentario> comentariosList;
    private ComentariosAdapterAdmin comentarioAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Comentarios");

        comentariosRecycler = findViewById(R.id.comentario);
        linearLayoutManager = new LinearLayoutManager(this);

        final String id = getIntent().getStringExtra("idReceta");

        comentariosList = getComentarios(id);


    }

    private void addComentario(Comentario comentario, String id){
        comentarioAdapter.addComentario(comentario);
        db.collection("Recetas").document(id).collection("Comentarios").add(comentario);

    }

    private ArrayList<Comentario> getComentarios(final String id){
        final ArrayList<Comentario> tempRequest = new ArrayList<>();

        db.collection("Recetas").document(id).collection("Comentarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Comentario comentario = document.toObject(Comentario.class);
                        comentario.setIdDocument(document.getId());
                        tempRequest.add(comentario);

                    }

                    comentarioAdapter = new ComentariosAdapterAdmin(comentariosList, id, R.layout.list_comentario, new ComentariosAdapterAdmin.OnItemClickListener() {
                        @Override
                        public void OnItemClick(Comentario comentario, int posicion) {


                        }
                    });

                    comentariosRecycler.setLayoutManager(linearLayoutManager);
                    comentariosRecycler.setAdapter(comentarioAdapter);



                } else {

                }
            }
        });
        return tempRequest;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

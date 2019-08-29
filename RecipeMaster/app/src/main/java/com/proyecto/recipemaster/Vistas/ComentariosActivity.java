package com.proyecto.recipemaster.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.proyecto.recipemaster.Adapter.MensajesAdapter;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Clases.Comentario;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;

import java.util.ArrayList;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private RecyclerView comentariosRecycler;
    private EditText comentarioEdt;
    private Button comentarButton;
    private LinearLayoutManager linearLayoutManager;
    private List<Comentario> comentariosList;
    private MensajesAdapter comentarioAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        linearLayoutManager = new LinearLayoutManager(this);

        final String id = getIntent().getStringExtra("idReceta");

        comentariosList = getComentarios(id);

        SessionManager sessionManager = new SessionManager(this);
        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        final Recetero recetero = gson.fromJson(userGson, Recetero.class);

        comentarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comentarioEdt!=null){
                    Comentario comentario = new Comentario(recetero.getNombre(), recetero.getId(), comentarioEdt.getText().toString());
                    addComentario(comentario, id);
                    comentarioEdt.setText(null);
                }


            }
        });



    }

    private void addComentario(Comentario comentario, String id){
        comentarioAdapter.addComentario(comentario);
        db.collection("Recetas").document(id).collection("Comentarios").add(comentario);

    }

    private ArrayList<Comentario> getComentarios(String id){
        final ArrayList<Comentario> tempRequest = new ArrayList<>();

        db.collection("Recetas").document(id).collection("Comentarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Comentario comentario = document.toObject(Comentario.class);

                        tempRequest.add(comentario);

                    }

                    comentarioAdapter = new MensajesAdapter(comentariosList, R.layout.list_comentario, new MensajesAdapter.OnItemClickListener() {
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

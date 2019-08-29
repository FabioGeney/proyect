package com.proyecto.recipemaster.Vistas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.proyecto.recipemaster.Adapter.IngredientesRecetaAdapter;
import com.proyecto.recipemaster.Adapter.PasosAdapterReceta;
import com.proyecto.recipemaster.Clases.Ingredientes;
import com.proyecto.recipemaster.Clases.Pasos;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecetasActivity extends AppCompatActivity {

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
    private ScrollView scrollView;
    private Menu menu;
    private boolean bool;
    private SessionManager sessionManager;
    private Receta receta;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas);

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
        sessionManager = new SessionManager(this);


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
                Intent intent = new Intent(RecetasActivity.this, PerfilAvtivity.class);
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
                Intent intent = new Intent(RecetasActivity.this, ComentariosActivity.class);
                intent.putExtra("idReceta", receta.getIdDocument());
                startActivity(intent);
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getFav()){
            getMenuInflater().inflate(R.menu.fav1, menu);
            bool = false;
        }else {
            getMenuInflater().inflate(R.menu.favorito, menu);
            bool = true;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fav) {
            if(bool){
                item.setIcon(getResources().getDrawable(R.drawable.fav));
                bool = false;
            }else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                bool = true;
            }
            setFavoritos();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavoritos(){

        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        Recetero recetero = gson.fromJson(userGson, Recetero.class);
        if(recetero.isState(receta.getIdDocument())){
            recetero.removeFav(receta);
        }else {
            recetero.addFavoritos( receta );
        }
        sessionManager.createSession(recetero);
        //updateDatabase(recetero.getFavoritos(), recetero.getId());

    }

    private boolean getFav(){
        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        Recetero recetero = gson.fromJson(userGson, Recetero.class);

        return recetero.isState(receta.getIdDocument());
    }

    private void updateDatabase(List<Receta> temp, String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> token = new HashMap<>();
        token.put("favoritos", temp);
        db.collection("Usuarios").document(id).update(token);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

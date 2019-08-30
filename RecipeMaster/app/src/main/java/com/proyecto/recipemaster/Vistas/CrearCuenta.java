
package com.proyecto.recipemaster.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.recipemaster.Adapter.CategoriasAdapterInicio;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearCuenta extends AppCompatActivity {

    private static final String TAG = "CrearCuenta";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText nombres;
    private EditText correo;
    private EditText contraseña1;
    private EditText contraseña2;
    private Button guardar;
    private GridLayoutManager gridLayoutManager;
    private CategoriasAdapterInicio categoriasAdapterInicio;
    private RecyclerView recyclerView;
    private List<Categoria> categoriaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Crear Cuenta");

        nombres = findViewById(R.id.nombre);
        correo = findViewById(R.id.correo);
        contraseña1 = findViewById(R.id.contraseña);
        contraseña2 = findViewById(R.id.contraseña2);
        recyclerView = findViewById(R.id.recycler);
        guardar = findViewById(R.id.guardar);

        initialize();

        gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuentanueva();
            }
        });



        categoriaList = getCategoriaList();

        categoriasAdapterInicio = new CategoriasAdapterInicio(categoriaList, R.layout.list_categorias, new CategoriasAdapterInicio.OnItemClickListener() {
            @Override
            public void OnItemClick(Categoria categoria, int posicion) {
                categoriasAdapterInicio.notifyItemChanged(posicion);
            }
        });

        recyclerView.setAdapter(categoriasAdapterInicio);
        recyclerView.setLayoutManager(gridLayoutManager);


    }

    private void initialize(){
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Log.w(TAG, "onAuthStateChanged - inició sesión" + firebaseUser.getUid());
                    Log.w(TAG, "onAuthStateChanged - inició sesión" + firebaseUser.getEmail());
                } else {
                    Log.w(TAG, "onAuthStateChanged - cerró sesión");
                }
            }
        };
    }

    private ArrayList<Categoria> getCategoriaList(){
        ArrayList<Categoria> temp = new ArrayList<>();
        temp.add(new Categoria("Típica", 1));
        temp.add(new Categoria("Fast", 1));
        temp.add(new Categoria("Fit", 1));
        temp.add(new Categoria("Europea", 1));
        temp.add(new Categoria("Asiatica", 1));
        temp.add(new Categoria("Otro", 1));

        return temp;

    }

    private void cuentanueva(){

        final String nombre = nombres.getText().toString();
        final String email = correo.getText().toString();
        String password = contraseña1.getText().toString();
        String password2 = contraseña2.getText().toString();


        if(nombre != null && email != null && password != null && password2 != null){
            if(password.equals(password2)){

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String id = firebaseUser.getUid();
                            final Map<String, Object> user = new HashMap<>();
                            user.put("nombre",nombre);
                            user.put("email",email);
                            user.put("id",id);
                            user.put("categorias",categoriasAdapterInicio.getItem());
                            db.collection("Usuarios").document(id).set(user);
                            Toast.makeText(CrearCuenta.this, "Cuenta creada exitoasamente", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(CrearCuenta.this, IniciarSesion.class);
                            startActivity(i);
                        } else{
                            Toast.makeText(CrearCuenta.this, "Error creando cuenta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(CrearCuenta.this, "Las contraseñas son diferentes, por favor vuelve a intentarlo", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(CrearCuenta.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

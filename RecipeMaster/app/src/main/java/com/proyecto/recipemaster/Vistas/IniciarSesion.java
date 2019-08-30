package com.proyecto.recipemaster.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.MainActivity;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonUsuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IniciarSesion extends AppCompatActivity {

    private EditText correo;
    private EditText contraseña;
    private TextView crearCuenta;
    private Button iniciar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "IniciarSesion";
    private Recetero recetero;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        setContentView(R.layout.activity_iniciar_sesion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Iniciar sesión");

        correo = findViewById(R.id.correo);
        contraseña = findViewById(R.id.contraseña);
        crearCuenta = findViewById(R.id.crearCuenta);
        iniciar = findViewById(R.id.button);


        initialize();

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( IniciarSesion.this, CrearCuenta.class);
                startActivity(intent);
            }
        });

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correo.getText() != null && contraseña.getText() != null){
                    inicio(correo.getText().toString(), contraseña.getText().toString());
                }


            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void tokenID(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String token_id = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> token = new HashMap<>();
        token.put("token_id", token_id);
        db.collection("Usuarios").document(firebaseAuth.getUid()).update(token);
        firebaseDatabase.getReference(firebaseAuth.getUid()).child("TokenId").updateChildren(token);


    }

    private void inicio(final String email, String password){
        if(email.equals("admin") && password.equals("admin")){
            Intent intent = new Intent(IniciarSesion.this, AdminActivity.class);
            startActivity(intent);
        }else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        getUsuario(email);

                    } else{
                        Toast.makeText(IniciarSesion.this, "Verifique datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void getUsuario(String email){

        db.collection("Usuarios").whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                /*
                                String nombres = document.getData().get("nombre").toString();
                                String email = document.getData().get("email").toString();
                                List<String> categorias = (List<String>) document.getData().get("categorias");
                                String id = document.getId();

                                recetero = new Recetero(nombres, email, id, categorias);

                                */
                                recetero = document.toObject(Recetero.class);
                            }

                        }else{
                            Toast.makeText(IniciarSesion.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        if(recetero!=null ){
                            sessionManager.createSession(recetero);
                            Toast.makeText(IniciarSesion.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(IniciarSesion.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
        });

    }
}

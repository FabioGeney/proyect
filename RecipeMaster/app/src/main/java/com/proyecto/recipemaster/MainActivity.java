package com.proyecto.recipemaster;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.Fragments.BuscarFragment;
import com.proyecto.recipemaster.Fragments.InicioFragment;
import com.proyecto.recipemaster.Fragments.PerfilFragment;
import com.proyecto.recipemaster.Singletons.SingletonUsuario;
import com.proyecto.recipemaster.Vistas.CrearProducto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private FloatingActionButton fab;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new InicioFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new BuscarFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new PerfilFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SessionManager sessionManager = new SessionManager(this);
        SingletonUsuario singletonUsuario = SingletonUsuario.getInstance();
        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        Recetero recetero = gson.fromJson(userGson, Recetero.class);
        singletonUsuario.setReceta(recetero);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CrearProducto.class);
                startActivity(intent);
            }
        });

        loadFragment(new InicioFragment());
    }

    private boolean loadFragment (Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();

            return true;
        }
        return false;

    }

}

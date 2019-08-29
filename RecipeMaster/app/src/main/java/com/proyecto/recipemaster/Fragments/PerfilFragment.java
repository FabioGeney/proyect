package com.proyecto.recipemaster.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;
import com.proyecto.recipemaster.Vistas.RecetasActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilFragment extends Fragment {

    private TextView nombre;
    private CircleImageView circleImageView;
    private RecyclerView favoritos;
    private RecyclerView misRecetas;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager2;
    private List<Receta> recetasFavoritas;
    private List<Receta> recetas;
    private Button cerrar;
    private SessionManager sessionManager;
    private RecyclerView.Adapter favAdapter;
    private RecyclerView.Adapter recAdapter;
    private Recetero recetero;
    private TextView textRecetas;
    private TextView textFavoritos;

    public PerfilFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        nombre = root.findViewById(R.id.nombre);
        favoritos = root.findViewById(R.id.favoritos);
        misRecetas = root.findViewById(R.id.recyclerView);



        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        sessionManager = new SessionManager(getContext());
        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        recetero = gson.fromJson(userGson,Recetero.class);
        nombre.setText(recetero.getNombre());

        recetasFavoritas = getFavoritos();
        recetas = getRecetas();

        return root;
    }

    private ArrayList<Receta> getFavoritos(){

        final ArrayList<Receta> temp = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Usuarios").document(recetero.getId()).collection("Favoritos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Receta receta = document.toObject(Receta.class);
                                receta.setIdDocument(document.getId());
                                temp.add(receta);

                            }

                            favAdapter = new RecetasAdapter(recetasFavoritas, getContext(), R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(getContext(), RecetasActivity.class);
                                    SingletonReceta singletonReceta = SingletonReceta.getInstance();
                                    singletonReceta.setReceta(receta);
                                    startActivity(intent);

                                }
                            });

                            favoritos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            favoritos.setAdapter(favAdapter);



                        } else {
                            Toast.makeText(getContext(), "nothing", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return temp;


    }

    private ArrayList<Receta> getRecetas(){
        final ArrayList<Receta> temp = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Recetas").whereEqualTo("idUsuario", recetero.getId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Receta receta = document.toObject(Receta.class);
                                receta.setIdDocument(document.getId());
                                temp.add(receta);

                            }

                            recAdapter = new RecetasAdapter(recetas, getContext(), R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(getContext(), RecetasActivity.class);
                                    SingletonReceta singletonReceta = SingletonReceta.getInstance();
                                    singletonReceta.setReceta(receta);
                                    startActivity(intent);

                                }
                            });

                            misRecetas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            misRecetas.setAdapter(recAdapter);



                        } else {
                            Toast.makeText(getContext(), "nothing", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        return temp;


    }


}

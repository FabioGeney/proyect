package com.proyecto.recipemaster.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.recipemaster.Adapter.CategoriasAdapter;
import com.proyecto.recipemaster.Adapter.CategoriasAdapterInicio;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Vistas.RecetasActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InicioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment {
    private RecyclerView recyclerRecom;
    private RecyclerView recyclerPopulares;
    private RecyclerView recyclerCat;
    private RecyclerView.LayoutManager layoutManagerRecom;
    private RecyclerView.LayoutManager layoutManagerFav;
    private RecyclerView.LayoutManager layoutManagerCat;
    private RecyclerView.Adapter remendadosAdapter;
    private RecyclerView.Adapter popAdapter;
    private RecyclerView.Adapter catAdapter;
    private List<Receta> recomendadas;
    private List<Receta> populares;
    private List<Categoria> categorias;


    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        recyclerRecom = root.findViewById(R.id.recomendadas);
        recyclerPopulares = root.findViewById(R.id.populares);
        recyclerCat = root.findViewById(R.id.categorias);


        layoutManagerFav = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerRecom = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerCat = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recomendadas = getRecomendados();

        populares = getPopulares();

        categorias = getCategorias();

        catAdapter = new CategoriasAdapter(categorias, R.layout.list_cat_inicio, new CategoriasAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Categoria categoria, int posicion) {

            }
        });

        recyclerCat.setAdapter(catAdapter);
        recyclerCat.setLayoutManager(layoutManagerCat);


        return root;
    }


    private ArrayList<Receta> getRecomendados(){

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
                                recetasRequest.add(receta);

                            }
                            // Inicializar el adaptador con la fuente de datos.
                            remendadosAdapter = new RecetasAdapter(recomendadas, getContext(), R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {


                                }
                            });
                            //Relacionando la lista con el adaptador
                            recyclerRecom.setLayoutManager(layoutManagerRecom);
                            recyclerRecom.setAdapter(remendadosAdapter);



                        } else {

                        }
                    }
                });

        return recetasRequest;


    }

    private ArrayList<Receta> getPopulares(){

        final ArrayList<Receta> recetasRequest = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Recetas").orderBy("likes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Receta receta = document.toObject(Receta.class);
                                recetasRequest.add(receta);

                            }
                            // Inicializar el adaptador con la fuente de datos.
                            popAdapter = new RecetasAdapter(recomendadas, getContext(), R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(getContext(), RecetasActivity.class);
                                    intent.putExtra("receta",  receta);
                                    startActivity(intent);

                                }
                            });
                            //Relacionando la lista con el adaptador
                            recyclerPopulares.setLayoutManager(layoutManagerFav);
                            recyclerPopulares.setAdapter(popAdapter);



                        } else {

                        }
                    }
                });

        return recetasRequest;


    }

    private List<Categoria> getCategorias(){
        ArrayList<Categoria> temp = new ArrayList<>();
        temp.add(new Categoria("Típica", 1));
        temp.add(new Categoria("Fast", 1));
        temp.add(new Categoria("Fit", 1));
        temp.add(new Categoria("Europea", 1));
        temp.add(new Categoria("Asiatica", 1));
        temp.add(new Categoria("Otro", 1));

        return temp;
    }
}

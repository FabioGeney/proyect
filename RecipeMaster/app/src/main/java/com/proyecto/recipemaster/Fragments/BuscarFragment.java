package com.proyecto.recipemaster.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.proyecto.recipemaster.Adapter.CategoriasAdapter;
import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Vistas.BuscadorActivity;
import com.proyecto.recipemaster.Vistas.CategoriaActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuscarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarFragment extends Fragment {

    private RecyclerView categorias;
    private EditText buscador;
    private RecyclerView.Adapter categoriasAdapter;
    private List<Categoria> categoriaList;
    private GridLayoutManager gridLayoutManager;


    public BuscarFragment() {
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buscar, container, false);

        categorias = root.findViewById(R.id.categorias);
        buscador = root.findViewById(R.id.buscador);
        buscador.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);

        categoriaList = getCategorias();

        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

        categoriasAdapter = new CategoriasAdapter(categoriaList, R.layout.list_cat_inicio, new CategoriasAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Categoria categoria, int posicion) {

                Intent intent = new Intent(getContext(), CategoriaActivity.class);
                intent.putExtra("categoria", categoria);
                startActivity(intent);
            }
        });

        categorias.setAdapter(categoriasAdapter);
        categorias.setLayoutManager(gridLayoutManager);

        buscador.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(getContext(), BuscadorActivity.class);
                    intent.putExtra("request", buscador.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    private List<Categoria> getCategorias(){
        ArrayList<Categoria> temp = new ArrayList<>();
        temp.add(new Categoria("Típica", R.drawable.tipica));
        temp.add(new Categoria("Fast", R.drawable.fast));
        temp.add(new Categoria("Fit", R.drawable.fit));
        temp.add(new Categoria("Europea", R.drawable.europea));
        temp.add(new Categoria("Oriental", R.drawable.asiatica));
        temp.add(new Categoria("Otro", R.drawable.otros));

        return temp;
    }




}

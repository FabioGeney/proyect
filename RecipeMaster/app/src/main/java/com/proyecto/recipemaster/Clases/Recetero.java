package com.proyecto.recipemaster.Clases;

import java.util.ArrayList;
import java.util.List;

public class Recetero extends Usuario {
    private String nombres;
    private String id;
    private String correo;
    private List<String> categorias;
    private boolean inab;

    public Recetero(String nombres, String email, String id, List<String>categorias) {
        super(nombres, email, id);
        this.inab = false;
        this.categorias = categorias;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public boolean isInab() {
        return inab;
    }

    public void setInab(boolean inab) {
        this.inab = inab;
    }
}

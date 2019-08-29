package com.proyecto.recipemaster.Clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recetero {
    private String nombre;
    private String id;
    private String correo;
    private List<String> categorias;
    private List<Receta> favoritos = new ArrayList<>();
    private boolean inab;

    public Recetero() {
    }

    public Recetero(String nombre, String correo, String id, List<String>categorias) {
        this.nombre = nombre;
        this.correo = correo;
        this.id = id;
        this.categorias = categorias;

    }

    public Recetero(String nombre, String id, String correo, List<String> categorias,  List<Receta> favoritos) {
        this.nombre = nombre;
        this.id = id;
        this.correo = correo;
        this.categorias = categorias;
        this.favoritos = favoritos;
    }

    public boolean isState(String key) {

        boolean bool = false;
        for (Receta receta : favoritos){
            if(receta.getIdDocument().equals(key));
            {
                bool = true;
            }
        }
        return bool;
    }

    public  List<Receta> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos( List<Receta> favoritos) {
        this.favoritos = favoritos;
    }

    public void removeFav(Receta key){
        int i = 0;
        for(Receta receta: favoritos){
            if(receta.getIdDocument().equals(key));
            {
                favoritos.remove(i);
            }
            i++;
        }

    }

    public void addFavoritos(Receta value) {
       favoritos.add( value);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

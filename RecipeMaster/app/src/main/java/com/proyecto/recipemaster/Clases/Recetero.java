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
    private Map<String, Receta> favoritos = new HashMap<>();
    private boolean inab;
    private String image;

    public Recetero() {
    }

    public Recetero(String nombre, String correo, String id, List<String>categorias) {
        this.nombre = nombre;
        this.correo = correo;
        this.id = id;
        this.categorias = categorias;

    }
    public Recetero(String nombre, String correo, String id, List<String>categorias, String image) {
        this.nombre = nombre;
        this.correo = correo;
        this.id = id;
        this.categorias = categorias;
        this.image = image;

    }

    public Recetero(String nombre, String id, String correo, List<String> categorias,  HashMap<String, Receta> favoritos) {
        this.nombre = nombre;
        this.id = id;
        this.correo = correo;
        this.categorias = categorias;
        this.favoritos = favoritos;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isState(String key) {

        boolean bool = false;

        if(favoritos.get(key)!=null)
        {
            bool = true;
        }

        return bool;
    }

    public  Map<String, Receta> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos( HashMap<String, Receta> favoritos) {
        this.favoritos = favoritos;
    }

    public void removeFav(String key){

       favoritos.remove(key);
    }

    public void addFavoritos(String key, Receta value) {
       favoritos.put( key, value);
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

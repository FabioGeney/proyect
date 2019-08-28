package com.proyecto.recipemaster.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receta implements Serializable {
    private String idUsuario;
    private String idDocument;
    private int likes = 0;
    private String nombre;
    private String nombreRecetario;
    private String descripcion;
    private String tipo;
    private String imagen;
    private List<Pasos> pasos = new ArrayList<>();
    private List<Ingredientes> ingredientes = new ArrayList<>();

    public Receta() {
    }

    public Receta(String idUsuario, String nombre, String nombreRecetario,  String descripcion, String tipo, String imagen , List<Pasos> pasos, List<Ingredientes> ingredientes) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.nombreRecetario = nombreRecetario;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.imagen = imagen;
        this.pasos = pasos;
        this.ingredientes = ingredientes;
    }

    public String getNombreRecetario() {
        return nombreRecetario;
    }

    public void setNombreRecetario(String nombreRecetario) {
        this.nombreRecetario = nombreRecetario;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Pasos> getPasos() {
        return pasos;
    }

    public void setPasos(List<Pasos> pasos) {
        this.pasos = pasos;
    }

    public List<Ingredientes> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingredientes> ingredientes) {
        this.ingredientes = ingredientes;
    }
}

package com.proyecto.recipemaster.Models;

public class Pasos {
    private int index;
    private String descripcion;
    private String imagen;

    public Pasos(int index, String descripcion, String imagen) {
        this.index = index;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

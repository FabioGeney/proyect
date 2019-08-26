package com.proyecto.recipemaster.Clases;

public class Ingredientes {
    private String nombreCantidad;

    public Ingredientes() {
    }

    public Ingredientes(String nombreCantidad) {
        this.nombreCantidad = nombreCantidad;
    }

    public String getNombreCantidad() {
        return nombreCantidad;
    }

    public void setNombreCantidad(String nombreCantidad) {
        this.nombreCantidad = nombreCantidad;
    }
}

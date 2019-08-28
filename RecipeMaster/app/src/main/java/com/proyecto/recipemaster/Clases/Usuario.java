package com.proyecto.recipemaster.Clases;

public class Usuario {

    private String nombres;
    private String email;
    private String id;

    public Usuario() {

    }

    public Usuario(String nombres, String email, String id) {
        this.nombres = nombres;
        this.email = email;
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

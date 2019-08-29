package com.proyecto.recipemaster.Clases;

public class Comentario {
    private String nombreRemintente;
    private String idRemitente;
    private String mensaje;
    private String idDocument;

    public Comentario() {
    }

    public Comentario(String nombreRemintente, String idRemitente, String mensaje) {
        this.nombreRemintente = nombreRemintente;
        this.idRemitente = idRemitente;
        this.mensaje = mensaje;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getNombreRemintente() {
        return nombreRemintente;
    }

    public void setNombreRemintente(String nombreRemintente) {
        this.nombreRemintente = nombreRemintente;
    }

    public String getIdRemitente() {
        return idRemitente;
    }

    public void setIdRemitente(String idRemitente) {
        this.idRemitente = idRemitente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

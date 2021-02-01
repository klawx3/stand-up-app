package com.example.stand_up.pojos;

public class Users {
    private String id,nombre,mail,foto;

    public Users() {
    }

    public Users(String id, String nombre, String mail, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}

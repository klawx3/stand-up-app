package pojos;

public class Users {

    private String id;
    private String nombre;
    private String mail;
    private String foto;

    public Users() {
    }

    public Users(String id, String nomre, String mail, String foto) {
        this.id = id;
        this.nombre = nomre;
        this.mail = mail;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomre() {
        return nombre;
    }

    public void setNomre(String nomre) {
        this.nombre = nomre;
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

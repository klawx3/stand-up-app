package pojos;

public class Users {

    private String id;
    private String nombre;
    private String mail;


    public Users() {
    }

    public Users(String id, String nombre, String mail) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;

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


}

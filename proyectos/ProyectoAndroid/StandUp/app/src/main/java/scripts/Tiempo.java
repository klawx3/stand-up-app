package scripts;

public class Tiempo {
    private String tiempo;
    private String dia;


    public Tiempo() {
    }

    public Tiempo(String tiempo, String dia) {
        this.tiempo = tiempo;
        this.dia = dia;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}

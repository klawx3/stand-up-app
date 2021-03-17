package pojos;

public class Avisos {
    private String titulo;
    private String hora;
    private String mensaje;
    private String horafin;
    private String mensajefin;



    public Avisos() {

    }

    public Avisos(String titulo ,String hora, String mensaje, String horafin, String mensajefin) {
        this.mensaje = mensaje;
        this.mensajefin = mensajefin;
        this.hora = hora;
        this.horafin = horafin;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajefin() {
        return mensajefin;
    }

    public void setMensajefin(String mensajefin) {
        this.mensajefin = mensajefin;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHorafin() {
        return horafin;
    }


}

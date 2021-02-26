package pojos;

public class Avisos {
    private String tipo;
    private String mensaje;
    private String hora;
    private String estado;

    public Avisos() {
    }

    public Avisos(String tipo, String mensaje, String hora, String estado) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.hora = hora;
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

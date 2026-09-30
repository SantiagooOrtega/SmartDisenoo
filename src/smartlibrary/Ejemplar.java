package smartlibrary;

public class Ejemplar {

    public enum Estado {
        DISPONIBLE,
        PRESTADO,
        EN_REPARACION,
        BAJA
    }

    private final String codigoEjemplar;
    private final Libro libro;
    private Estado estado;

    public Ejemplar(String codigoEjemplar, Libro libro) {
        this.codigoEjemplar = codigoEjemplar;
        this.libro = libro;
        this.estado = Estado.DISPONIBLE;
    }

    public String getCodigoEjemplar() {
        return codigoEjemplar;
    }

    public Libro getLibro() {
        return libro;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean estaDisponible() {
        return estado == Estado.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "Ejemplar [codigo=" + codigoEjemplar +
               ", libro=" + libro.getTitulo() +
               ", estado=" + estado + "]";
    }
}

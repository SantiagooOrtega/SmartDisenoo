package smartlibrary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reserva {

    public enum Estado {
        PENDIENTE,
        ATENDIDA,
        CANCELADA
    }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String idReserva;
    private final Estudiante estudiante;
    private final Libro libro;
    private final LocalDate fechaReserva;
    private Estado estado;

    public Reserva(String idReserva, Estudiante estudiante, Libro libro, LocalDate fechaReserva) {
        this.idReserva   = idReserva;
        this.estudiante  = estudiante;
        this.libro       = libro;
        this.fechaReserva = fechaReserva;
        this.estado      = Estado.PENDIENTE;
    }

    public void atender() {
        if (estado != Estado.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden atender reservas en estado PENDIENTE.");
        }
        estado = Estado.ATENDIDA;
    }

    public void cancelar() {
        if (estado == Estado.ATENDIDA) {
            throw new IllegalStateException("No se puede cancelar una reserva que ya fue atendida.");
        }
        estado = Estado.CANCELADA;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public Estado getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "Reserva [id=" + idReserva +
               ", estudiante=" + estudiante.getNombre() +
               ", libro=" + libro.getTitulo() +
               ", fecha=" + fechaReserva.format(FMT) +
               ", estado=" + estado + "]";
    }
}

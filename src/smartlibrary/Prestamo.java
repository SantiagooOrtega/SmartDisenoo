package smartlibrary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Prestamo {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String idPrestamo;
    private final Estudiante estudiante;
    private final Ejemplar ejemplar;
    private final LocalDate fechaInicio;
    private LocalDate fechaPrevistaDevolucion;
    private boolean activo;

    private final List<Renovacion> renovaciones = new ArrayList<>();

    public Prestamo(String idPrestamo, Estudiante estudiante, Ejemplar ejemplar,
                    LocalDate fechaInicio, LocalDate fechaPrevistaDevolucion) {
        this.idPrestamo = idPrestamo;
        this.estudiante = estudiante;
        this.ejemplar   = ejemplar;
        this.fechaInicio = fechaInicio;
        this.fechaPrevistaDevolucion = fechaPrevistaDevolucion;
        this.activo = true;
        ejemplar.setEstado(Ejemplar.Estado.PRESTADO);
    }

    public void renovar(LocalDate nuevaFecha) {
        if (!activo) {
            throw new IllegalStateException("No se puede renovar un préstamo que ya fue cerrado.");
        }
        if (!nuevaFecha.isAfter(fechaPrevistaDevolucion)) {
            throw new IllegalArgumentException(
                "La nueva fecha de devolución (" + nuevaFecha.format(FMT) +
                ") debe ser posterior a la fecha vigente (" +
                fechaPrevistaDevolucion.format(FMT) + ").");
        }

        LocalDate fechaAnterior = this.fechaPrevistaDevolucion;
        renovaciones.add(new Renovacion(LocalDate.now(), fechaAnterior, nuevaFecha));
        this.fechaPrevistaDevolucion = nuevaFecha;
    }

    public void cerrar() {
        if (!activo) {
            throw new IllegalStateException("El préstamo ya está cerrado.");
        }
        activo = false;
        ejemplar.setEstado(Ejemplar.Estado.DISPONIBLE);
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaPrevistaDevolucion() {
        return fechaPrevistaDevolucion;
    }

    public boolean isActivo() {
        return activo;
    }

    public List<Renovacion> getRenovaciones() {
        return Collections.unmodifiableList(renovaciones);
    }

    public int getCantidadRenovaciones() {
        return renovaciones.size();
    }

    @Override
    public String toString() {
        return "Prestamo [id=" + idPrestamo +
               ", estudiante=" + estudiante.getNombre() +
               ", ejemplar=" + ejemplar.getCodigoEjemplar() +
               ", devolucion=" + fechaPrevistaDevolucion.format(FMT) +
               ", renovaciones=" + renovaciones.size() +
               ", activo=" + activo + "]";
    }
}

package smartlibrary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Renovacion {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final LocalDate fechaRenovacion;
    private final LocalDate fechaAnterior;
    private final LocalDate nuevaFecha;

    public Renovacion(LocalDate fechaRenovacion, LocalDate fechaAnterior, LocalDate nuevaFecha) {
        this.fechaRenovacion = fechaRenovacion;
        this.fechaAnterior   = fechaAnterior;
        this.nuevaFecha      = nuevaFecha;
    }

    public LocalDate getFechaRenovacion() {
        return fechaRenovacion;
    }

    public LocalDate getFechaAnterior() {
        return fechaAnterior;
    }

    public LocalDate getNuevaFecha() {
        return nuevaFecha;
    }

    @Override
    public String toString() {
        return "Renovacion [realizada=" + fechaRenovacion.format(FMT) +
               ", anterior=" + fechaAnterior.format(FMT) +
               ", nueva="    + nuevaFecha.format(FMT) + "]";
    }
}

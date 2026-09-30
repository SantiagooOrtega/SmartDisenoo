package smartlibrary;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        separador("CONFIGURACIÓN INICIAL DEL SISTEMA");

        Libro libro = new Libro("978-0-13-468599-1", "Clean Code", "Robert C. Martin", 2008);
        Ejemplar ejemplar = new Ejemplar("CC-001", libro);
        System.out.println("Libro creado    : " + libro);
        System.out.println("Ejemplar creado : " + ejemplar);

        Estudiante estudiante = new Estudiante(
            "1001234567", "Laura Gómez", "laura.gomez@uni.edu.co",
            "EST-2021-089", "Ingeniería de Sistemas"
        );

        Bibliotecario bibliotecario = new Bibliotecario(
            "7654321001", "Carlos Ríos", "carlos.rios@biblioteca.edu.co",
            "BIB-045", "MAÑANA"
        );

        System.out.println("\nEstudiante      : " + estudiante);
        System.out.println("Bibliotecario   : " + bibliotecario);

        separador("PRUEBA DE CONTRATO Notificable");
        estudiante.notificar("Bienvenido al sistema SmartLibrary.");
        bibliotecario.notificar("Nuevo ejemplar registrado en catálogo.");

        separador("CREACIÓN DE PRÉSTAMO");

        LocalDate hoy         = LocalDate.of(2026, 9, 30);
        LocalDate devolucion1 = LocalDate.of(2026, 10, 10);

        Prestamo prestamo = new Prestamo("P-2026-001", estudiante, ejemplar, hoy, devolucion1);
        System.out.println("Préstamo creado : " + prestamo);
        System.out.println("Estado ejemplar : " + ejemplar.getEstado());

        separador("PRUEBA 1 – RENOVACIÓN VÁLIDA");

        LocalDate nuevaFechaValida = LocalDate.of(2026, 10, 17);
        System.out.println("Intentando renovar al " + nuevaFechaValida + " ...");
        prestamo.renovar(nuevaFechaValida);
        System.out.println("✔ Renovación exitosa.");
        System.out.println("  Nueva fecha de devolución : " + prestamo.getFechaPrevistaDevolucion());
        System.out.println("  Total renovaciones        : " + prestamo.getCantidadRenovaciones());
        System.out.println("  Detalle                   : " + prestamo.getRenovaciones().get(0));
        estudiante.notificar("Su préstamo fue renovado hasta el " + prestamo.getFechaPrevistaDevolucion() + ".");

        separador("PRUEBA 2 – RENOVACIÓN INVÁLIDA (fecha igual a la vigente)");

        LocalDate fechaIgual = LocalDate.of(2026, 10, 17);
        System.out.println("Intentando renovar al " + fechaIgual + " (igual a la vigente) ...");
        try {
            prestamo.renovar(fechaIgual);
            System.out.println("✘ ERROR: el sistema debió haber rechazado esta renovación.");
        } catch (IllegalArgumentException e) {
            System.out.println("✔ Excepción capturada correctamente:");
            System.out.println("  → " + e.getMessage());
        }

        separador("PRUEBA 3 – RENOVACIÓN INVÁLIDA (fecha anterior a la vigente)");

        LocalDate fechaAnterior = LocalDate.of(2026, 10, 5);
        System.out.println("Intentando renovar al " + fechaAnterior + " (anterior a la vigente) ...");
        try {
            prestamo.renovar(fechaAnterior);
            System.out.println("✘ ERROR: el sistema debió haber rechazado esta renovación.");
        } catch (IllegalArgumentException e) {
            System.out.println("✔ Excepción capturada correctamente:");
            System.out.println("  → " + e.getMessage());
        }

        separador("ESTADO FINAL DEL PRÉSTAMO");
        System.out.println(prestamo);

        separador("PRUEBA DE RESERVA");

        Libro libro2 = new Libro("978-0-201-63361-0", "Design Patterns", "Gang of Four", 1994);
        Reserva reserva = new Reserva("R-2026-001", estudiante, libro2, hoy);
        System.out.println("Reserva creada  : " + reserva);
        reserva.atender();
        System.out.println("Reserva atendida: " + reserva);
        estudiante.notificar("Su reserva del libro '" + libro2.getTitulo() + "' fue atendida.");

        separador("FIN DE PRUEBAS");
    }

    private static void separador(String titulo) {
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("  " + titulo);
        System.out.println("══════════════════════════════════════════════════");
    }
}

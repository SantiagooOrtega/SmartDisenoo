# SmartLibrary - Bloque 5

## Integrantes

- Santiago Ortega Bolaños
- Joan Mosquera

---

## Diagramas

### Diagrama de Clases
![Diagrama de Clases](Docs/diagrama%20clases.jpg)

### Diagrama de Componentes
![Diagrama de Componentes](Docs/diagrama%20comopnentes.jpg)

---

## Estructura del proyecto

```
SmartDiseno/
├── src/
│   └── smartlibrary/
│       ├── Notificable.java
│       ├── Usuario.java
│       ├── Estudiante.java
│       ├── Bibliotecario.java
│       ├── Libro.java
│       ├── Ejemplar.java
│       ├── Renovacion.java
│       ├── Prestamo.java
│       ├── Reserva.java
│       └── Main.java
├── Docs/
│   ├── diagrama clases.jpg
│   └── diagrama comopnentes.jpg
└── README.md
```

### Compilar y ejecutar

```bash
javac -d out src/smartlibrary/*.java
java -cp out smartlibrary.Main
```

Requiere Java 11 o superior.

---

## Actividad 1 - Diagnostico de relaciones

| Elementos | Relacion en el problema | Existencia independiente | Decision |
|---|---|---|---|
| Estudiante - Prestamo | Un estudiante genera prestamos; un prestamo registra quien recibio el ejemplar | Si, el estudiante existe antes y despues del prestamo | Asociacion |
| Libro - Ejemplar | Un libro tiene copias fisicas; cada copia pertenece a un libro | El ejemplar pierde sentido operativo sin libro | Agregacion |
| Prestamo - Renovacion | Una renovacion es parte del historial interno de un prestamo | No, sin prestamo la renovacion no tiene significado | Composicion |
| Usuario - Estudiante | Un estudiante es un usuario del sistema con datos adicionales | El estudiante es-un usuario | Herencia |
| Usuario - Bibliotecario | Un bibliotecario es un usuario con rol de gestion | El bibliotecario es-un usuario | Herencia |

---

## Actividad 2 - Justificacion de las cuatro relaciones

### 1. Estudiante - Prestamo: Asociacion

El Prestamo conoce al Estudiante que lo origino, pero ninguno contiene al otro ni depende del ciclo de vida del otro. Un Estudiante puede existir sin prestamos activos; un Prestamo historico puede consultarse aunque el estudiante haya egresado.

Multiplicidad: un Estudiante puede tener 0..* prestamos; cada Prestamo pertenece a exactamente 1 Estudiante.

### 2. Prestamo - Ejemplar: Asociacion

El Prestamo referencia al Ejemplar para gestionar su estado, pero el Ejemplar existe independientemente: puede ser devuelto, reparado y vuelto a prestar en multiples prestamos. La existencia del Ejemplar no esta condicionada por la del Prestamo.

Multiplicidad: cada Prestamo involucra exactamente 1 Ejemplar; un Ejemplar puede aparecer en 0..* prestamos a lo largo del tiempo.

### 3. Libro - Ejemplar: Agregacion

Existe una relacion todo-parte: un Ejemplar es una copia fisica de un Libro. Se elige agregacion y no composicion porque el ciclo de vida del Ejemplar no esta necesariamente ligado al del Libro. En un proceso de inventario o baja administrativa, el Ejemplar puede seguir existiendo como registro historico aunque el Libro sea retirado del catalogo.

Supuesto: si el dominio exigiera que al eliminar el Libro se destruyan automaticamente todos sus Ejemplares, la decision correcta seria composicion. Dado que SmartLibrary requiere trazabilidad de inventario, se mantiene agregacion.

Multiplicidad: 1 Libro tiene 1..* Ejemplares; cada Ejemplar pertenece a exactamente 1 Libro.

### 4. Prestamo - Renovacion: Composicion

Una Renovacion es parte del historial interno de un Prestamo. No tiene identidad propia fuera de ese contexto: no existe una renovacion sin el prestamo al que pertenece y su eliminacion es consecuencia directa de la eliminacion del prestamo.

Multiplicidad: un Prestamo puede tener 0..* Renovaciones; cada Renovacion pertenece a exactamente 1 Prestamo.

---

## Actividad 3 - Herencia

### Un Estudiante es un tipo de Usuario?

Si. En SmartLibrary, un Estudiante es un Usuario del sistema: se autentica, aparece en reportes de prestamos y reservas, y el sistema lo trata en cualquier operacion que afecte a usuarios. La relacion no se basa solo en atributos compartidos, sino en que Estudiante satisface completamente el contrato de Usuario y agrega comportamiento propio.

### Un Bibliotecario es un tipo de Usuario?

Si, con el mismo razonamiento. Un Bibliotecario se identifica en el sistema, puede recibir notificaciones y es un participante del dominio con las mismas propiedades base mas atributos propios de su rol.

### Los atributos repetidos son suficiente para crear una superclase?

No. La repeticion de atributos es un sintoma, no una justificacion. Dos clases que comparten campos por coincidencia pero representan conceptos distintos no deben unificarse en una jerarquia. Por ejemplo, Vehiculo y Edificio podrian tener ambos direccion y propietario, pero crear una superclase CosaConPropietario produciria un diseno debil porque no existe una abstraccion real del dominio que los unifique.

Otro caso: Libro y Revista comparten titulo y anioPublicacion, pero Revista tiene volumen y numero y una semantica distinta de prestamo. Crear una superclase solo para evitar duplicar campos forzaria al codigo a contemplar casos irrelevantes para cada subclase.

---

## Actividad 4 - Interfaz Notificable

### Contrato

```java
public interface Notificable {
    void notificar(String mensaje);
}
```

### Clases que implementan Notificable

| Clase | Implementa | Justificacion |
|---|---|---|
| Estudiante | Si | Principal receptor de alertas: vencimientos, renovaciones, reservas |
| Bibliotecario | Si | Receptor de alertas internas: solicitudes pendientes, incidencias |

### Que garantiza y que no especifica

| Aspecto | Detalle |
|---|---|
| Que promete | Que cualquier objeto que implemente Notificable expone el metodo notificar(String) |
| Que no especifica | Como se entrega el mensaje (consola, correo, push). Cada clase decide el mecanismo |
| Define el canal? | No. La interfaz es agnostica al canal de comunicacion |

### Una clase puede heredar de Usuario e implementar Notificable al mismo tiempo?

Si. Java permite herencia simple pero implementacion multiple de interfaces. `Estudiante extends Usuario implements Notificable` hereda la estructura de Usuario y cumple el contrato de Notificable de forma independiente.

La diferencia es que la herencia comparte estructura de una superclase, mientras que la interfaz establece un contrato de comportamiento sin imponer estructura interna. Si Usuario declarara notificar() como metodo concreto, obligaria a que todo usuario fuera notificable aunque no corresponda.

---

## Actividad 6 - Vista funcional por componentes

| Componente | Clases | Dependencias |
|---|---|---|
| Gestion de Usuarios | Usuario, Estudiante, Bibliotecario, Notificable | Provee informacion de usuarios a Prestamos y Reservas |
| Gestion de Catalogo | Libro, Ejemplar | Provee estado de disponibilidad a Prestamos y Reservas |
| Gestion de Prestamos | Prestamo, Renovacion | Consulta Usuarios y Catalogo; notifica tras renovaciones |
| Gestion de Reservas | Reserva | Consulta Usuarios y Catalogo; puede originar un Prestamo |

---

## Evidencia de ejecucion

Salida al ejecutar `java -cp out smartlibrary.Main`:

```
CONFIGURACION INICIAL DEL SISTEMA
Libro creado    : Libro [isbn=978-0-13-468599-1, titulo=Clean Code, autor=Robert C. Martin, anio=2008]
Ejemplar creado : Ejemplar [codigo=CC-001, libro=Clean Code, estado=DISPONIBLE]
Estudiante      : Estudiante [id=1001234567, nombre=Laura Gomez, correo=laura.gomez@uni.edu.co] | codigo=EST-2021-089, programa=Ingenieria de Sistemas
Bibliotecario   : Bibliotecario [id=7654321001, nombre=Carlos Rios, correo=carlos.rios@biblioteca.edu.co] | empleado=BIB-045, turno=MANANA

PRUEBA 1 - RENOVACION VALIDA
Intentando renovar al 2026-10-17 ...
Renovacion exitosa.
  Nueva fecha de devolucion : 2026-10-17
  Total renovaciones        : 1

PRUEBA 2 - RENOVACION INVALIDA (fecha igual a la vigente)
Intentando renovar al 2026-10-17 ...
Excepcion capturada: La nueva fecha de devolucion (17/10/2026) debe ser posterior a la fecha vigente (17/10/2026).

PRUEBA 3 - RENOVACION INVALIDA (fecha anterior a la vigente)
Intentando renovar al 2026-10-05 ...
Excepcion capturada: La nueva fecha de devolucion (05/10/2026) debe ser posterior a la fecha vigente (17/10/2026).
```

| Intento | Fecha solicitada | Fecha vigente | Resultado |
|---|---|---|---|
| Renovacion valida | 17/10/2026 | 10/10/2026 | Exito, fecha actualizada |
| Renovacion invalida (igual) | 17/10/2026 | 17/10/2026 | IllegalArgumentException lanzada |
| Renovacion invalida (anterior) | 05/10/2026 | 17/10/2026 | IllegalArgumentException lanzada |

---

## Conclusion

Disenar relaciones entre clases obliga a preguntarse que significa cada vinculo en el dominio. Cuando se miran clases aisladas los atributos y metodos parecen suficientes, pero al modelar colaboraciones aparecen preguntas que cambian las decisiones de diseno: puede este objeto vivir sin el otro, necesitan conocerse o uno pertenece al otro, la semejanza refleja una abstraccion real o solo coincidencia estructural. Responder esas preguntas con base en el requisito es lo que diferencia un diseno justificado de uno arbitrario. Las interfaces agregan otra dimension: permiten comprometer comportamiento sin imponer herencia, separando el que del como.

---

*Taller Bloque 5 - SmartLibrary - Programacion Orientada a Objetos*

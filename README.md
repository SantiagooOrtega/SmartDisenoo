# SmartLibrary – Bloque 5: De clases aisladas a objetos que colaboran

**Taller práctico · Interfaces · Asociación · Agregación · Composición · Herencia · Componentes UML**

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
├── out/                  ← clases compiladas (generadas con javac)
└── README.md
```

### Compilar y ejecutar

```bash
# Desde la raíz del proyecto
javac -d out src/smartlibrary/*.java
java -cp out smartlibrary.Main
```

Requiere **Java 11 o superior** (usa `java.time.LocalDate`).

---

## Actividad 1 – Diagnóstico de relaciones

| Elementos | Relación en el problema | Existencia independiente | Decisión |
|---|---|---|---|
| Estudiante – Préstamo | Un estudiante genera préstamos; un préstamo registra quién recibió el ejemplar | Sí — el estudiante existe antes y después del préstamo | **Asociación** |
| Libro – Ejemplar | Un libro tiene copias físicas; cada copia pertenece a un libro | El ejemplar pierde sentido operativo sin libro (ver justificación) | **Agregación** |
| Préstamo – Renovación | Una renovación es parte del historial interno de un préstamo | No — sin préstamo la renovación carece de significado | **Composición** |
| Usuario – Estudiante | Un estudiante *es* un usuario del sistema con datos adicionales | El estudiante es-un usuario (no solo se *parece*) | **Herencia** |
| Usuario – Bibliotecario | Un bibliotecario *es* un usuario con rol de gestión | El bibliotecario es-un usuario (misma justificación) | **Herencia** |

---

## Actividad 2 – Justificación de las cuatro relaciones

### 1. Estudiante – Préstamo → **Asociación**

El Préstamo *conoce* al Estudiante que lo originó, pero ninguno contiene al otro ni uno depende del ciclo de vida del otro. Un Estudiante puede existir sin préstamos activos; un Préstamo histórico puede consultarse aunque el estudiante haya egresado. Solo necesitamos que los objetos *se conozcan y colaboren*, que es exactamente la semántica de la asociación.

> **Multiplicidad:** un Estudiante puede tener 0..* préstamos; cada Préstamo pertenece a exactamente 1 Estudiante.

### 2. Préstamo – Ejemplar → **Asociación**

El Préstamo referencia al Ejemplar para gestionar su estado (PRESTADO/DISPONIBLE), pero el Ejemplar existe independientemente: puede ser devuelto, reparado y vuelto a prestar en múltiples préstamos a lo largo del tiempo. La existencia del Ejemplar no está condicionada por la del Préstamo, por lo que no hay razón para una relación todo–parte. Una asociación expresa correctamente que el préstamo *utiliza* el ejemplar.

> **Multiplicidad:** cada Préstamo involucra exactamente 1 Ejemplar; un Ejemplar puede aparecer en 0..* préstamos a lo largo del tiempo (no simultáneamente).

### 3. Libro – Ejemplar → **Agregación**

Aquí existe una relación conceptual **todo–parte**: un Ejemplar es una copia física de un Libro y le pertenece conceptualmente. Sin embargo, elegimos **agregación** (y no composición) porque el ciclo de vida del Ejemplar no está *necesariamente* ligado al del Libro. En un proceso de inventario o baja administrativa, el Ejemplar puede seguir existiendo como registro histórico aunque el Libro sea retirado del catálogo. El Ejemplar mantiene su código, su historial de préstamos y su estado de forma independiente.

> **Supuesto documentado:** si el dominio exigiera que al eliminar el Libro se destruyan automáticamente todos sus Ejemplares sin posibilidad de consulta histórica, la decisión correcta sería composición. Dado que SmartLibrary requiere trazabilidad de inventario, mantenemos agregación.

> **Multiplicidad:** 1 Libro tiene 1..* Ejemplares; cada Ejemplar pertenece a exactamente 1 Libro.

### 4. Préstamo – Renovación → **Composición**

Una Renovación es exclusivamente parte del historial interno de un Préstamo (R11). No tiene identidad propia fuera de ese contexto: no existe "una renovación" sin el préstamo al que pertenece, no se consulta de forma independiente y su eliminación es consecuencia directa de la eliminación del préstamo. Esto satisface la condición de composición: la parte carece de sentido sin el todo y su ciclo de vida está completamente subordinado.

> **Multiplicidad:** un Préstamo puede tener 0..* Renovaciones; cada Renovación pertenece a exactamente 1 Préstamo.

---

## Actividad 3 – Herencia: ¿es-un o solo se parecen?

### ¿Estudiante es realmente un tipo de Usuario?

**Sí.** En el dominio de SmartLibrary, un Estudiante *es* un Usuario del sistema: se autentica, aparece en reportes de préstamos y reservas, y el sistema lo trata polimórficamente en cualquier operación que afecte a usuarios (por ejemplo, notificaciones). La relación no se basa solo en atributos compartidos, sino en que Estudiante satisface completamente el contrato de Usuario y agrega comportamiento y estado propios.

### ¿Bibliotecario es realmente un tipo de Usuario?

**Sí, con el mismo razonamiento.** Un Bibliotecario se identifica en el sistema, puede recibir notificaciones y es un participante legítimo del dominio con las mismas propiedades base de identificación, nombre y correo, más atributos propios de su rol.

### ¿Atributos repetidos son suficiente para crear una superclase?

**No.** La mera repetición de atributos es un síntoma, no una justificación. Dos clases que comparten campos por razones accidentales (coincidencia de implementación) pero que representan conceptos distintos no deben unificarse en una jerarquía. Ejemplo: `Vehiculo` y `Edificio` podrían tener ambos `direccion` y `propietario`, pero crear una superclase `CosaConPropietario` produciría un diseño conceptualmente débil porque no existe una abstracción real del dominio que los unifique.

### Situación donde la superclase solo por atributos repetidos produce diseño débil

Imagínese que `Libro` y `Revista` tienen ambos `titulo`, `issn/isbn` y `anioPublicacion`. Si creáramos `PublicacionConTitulo` solo para evitar duplicar esos campos, pero `Revista` tiene `volumen`, `numero` y una semántica completamente distinta de préstamo (no se presta, se consulta en sala), la jerarquía forzaría a que todo el código que trate `PublicacionConTitulo` también deba contemplar casos irrelevantes para cada subclase. La generalización debilita la cohesión sin aportar una abstracción real.

---

## Actividad 4 – Interfaz Notificable

### Diseño del contrato

```java
public interface Notificable {
    void notificar(String mensaje);
}
```

### ¿Qué clases implementan Notificable y por qué?

| Clase | Implementa Notificable | Justificación |
|---|---|---|
| `Estudiante` | ✅ Sí | Principal receptor de alertas: vencimientos, confirmaciones de renovación, disponibilidad de reservas |
| `Bibliotecario` | ✅ Sí | Receptor de alertas internas del sistema: incidencias, solicitudes pendientes, estadísticas de turno |

### ¿Qué garantiza el contrato Notificable y qué NO especifica?

| Aspecto | Garantía del contrato |
|---|---|
| **Qué promete** | Que cualquier objeto que implemente `Notificable` expone el método `notificar(String)` y puede ser invocado sin conocer su tipo concreto |
| **Qué NO especifica** | Cómo se entrega el mensaje (consola, correo, SMS, push). Cada implementador decide el mecanismo |
| **¿Define el canal?** | No. La interfaz es agnóstica al canal de comunicación |

### ¿Puede una clase heredar de Usuario e implementar Notificable simultáneamente?

**Sí, en Java es perfectamente válido.** Java permite herencia simple (solo una superclase) pero implementación múltiple de interfaces. `Estudiante extends Usuario implements Notificable` es la prueba concreta: hereda la estructura de datos de `Usuario` y cumple el contrato de comportamiento de `Notificable` de forma independiente. Esto ilustra la diferencia fundamental:

- **Herencia** comparte estructura y comportamiento de una superclase (relación *es-un*).
- **Interfaz** establece un contrato de comportamiento que la clase se compromete a implementar, sin imponer estructura interna.

Una superclase `Usuario` que declarara `notificar()` como método concreto asumiría *cómo* se notifica, violando la separación de responsabilidades y obligando a que *todo* usuario sea notificable aunque conceptualmente no corresponda. La interfaz permite seleccionar qué clases cumplen ese contrato de forma explícita.

---

## Actividad 6 – Vista funcional mediante componentes UML

| Componente | Clases relacionadas | Dependencias funcionales |
|---|---|---|
| **Gestión de Usuarios** | `Usuario`, `Estudiante`, `Bibliotecario`, `Notificable` | Provee información de usuarios a **Préstamos** y **Reservas** (quién solicita). Recibe eventos de **Préstamos** para notificar al estudiante |
| **Gestión de Catálogo** | `Libro`, `Ejemplar` | Provee a **Préstamos** el estado de disponibilidad de ejemplares. Provee a **Reservas** la información del libro solicitado |
| **Gestión de Préstamos** | `Prestamo`, `Renovacion` | Consulta a **Usuarios** para validar el estudiante. Consulta a **Catálogo** para verificar y actualizar el estado del ejemplar. Notifica a **Usuarios** tras renovaciones |
| **Gestión de Reservas** | `Reserva` | Consulta a **Usuarios** para identificar al solicitante. Consulta a **Catálogo** para conocer disponibilidad. Puede disparar la creación de un Préstamo en **Préstamos** cuando se atiende una reserva |

> El diagrama de componentes visual se encuentra en el diagrama UML entregado por el equipo de diseño (archivo Draw.io / Visual Paradigm adjunto al repositorio).

---

## Evidencia de ejecución

La siguiente salida se obtiene ejecutando `java -cp out smartlibrary.Main`:

```
══════════════════════════════════════════════════
  CONFIGURACIÓN INICIAL DEL SISTEMA
══════════════════════════════════════════════════
Libro creado    : Libro [isbn=978-0-13-468599-1, titulo=Clean Code, autor=Robert C. Martin, anio=2008]
Ejemplar creado : Ejemplar [codigo=CC-001, libro=Clean Code, estado=DISPONIBLE]
Estudiante      : Estudiante [id=1001234567, nombre=Laura Gómez, correo=laura.gomez@uni.edu.co] | código=EST-2021-089, programa=Ingeniería de Sistemas
Bibliotecario   : Bibliotecario [id=7654321001, nombre=Carlos Ríos, correo=carlos.rios@biblioteca.edu.co] | empleado=BIB-045, turno=MAÑANA

══════════════════════════════════════════════════
  PRUEBA DE CONTRATO Notificable
══════════════════════════════════════════════════
[NOTIFICACIÓN → Laura Gómez] Bienvenido al sistema SmartLibrary.
[ALERTA INTERNO → Carlos Ríos] Nuevo ejemplar registrado en catálogo.

══════════════════════════════════════════════════
  CREACIÓN DE PRÉSTAMO
══════════════════════════════════════════════════
Préstamo creado : Prestamo [id=P-2026-001, estudiante=Laura Gómez, ejemplar=CC-001, devolucion=10/10/2026, renovaciones=0, activo=true]
Estado ejemplar : PRESTADO

══════════════════════════════════════════════════
  PRUEBA 1 – RENOVACIÓN VÁLIDA
══════════════════════════════════════════════════
Intentando renovar al 2026-10-17 ...
✔ Renovación exitosa.
  Nueva fecha de devolución : 2026-10-17
  Total renovaciones        : 1
  Detalle                   : Renovacion [realizada=30/09/2026, anterior=10/10/2026, nueva=17/10/2026]
[NOTIFICACIÓN → Laura Gómez] Su préstamo fue renovado hasta el 2026-10-17.

══════════════════════════════════════════════════
  PRUEBA 2 – RENOVACIÓN INVÁLIDA (fecha igual a la vigente)
══════════════════════════════════════════════════
Intentando renovar al 2026-10-17 (igual a la vigente) ...
✔ Excepción capturada correctamente:
  → La nueva fecha de devolución (17/10/2026) debe ser posterior a la fecha vigente (17/10/2026).

══════════════════════════════════════════════════
  PRUEBA 3 – RENOVACIÓN INVÁLIDA (fecha anterior a la vigente)
══════════════════════════════════════════════════
Intentando renovar al 2026-10-05 (anterior a la vigente) ...
✔ Excepción capturada correctamente:
  → La nueva fecha de devolución (05/10/2026) debe ser posterior a la fecha vigente (17/10/2026).

══════════════════════════════════════════════════
  ESTADO FINAL DEL PRÉSTAMO
══════════════════════════════════════════════════
Prestamo [id=P-2026-001, estudiante=Laura Gómez, ejemplar=CC-001, devolucion=17/10/2026, renovaciones=1, activo=true]

══════════════════════════════════════════════════
  PRUEBA DE RESERVA
══════════════════════════════════════════════════
Reserva creada  : Reserva [id=R-2026-001, estudiante=Laura Gómez, libro=Design Patterns, fecha=30/09/2026, estado=PENDIENTE]
Reserva atendida: Reserva [id=R-2026-001, estudiante=Laura Gómez, libro=Design Patterns, fecha=30/09/2026, estado=ATENDIDA]
[NOTIFICACIÓN → Laura Gómez] Su reserva del libro 'Design Patterns' fue atendida.
```

### Comportamiento de las renovaciones inválidas

| Intento | Fecha solicitada | Fecha vigente | Resultado esperado |
|---|---|---|---|
| Renovación válida | 17/10/2026 | 10/10/2026 | ✅ Éxito – nueva fecha registrada |
| Renovación inválida (igual) | 17/10/2026 | 17/10/2026 | ✅ `IllegalArgumentException` lanzada |
| Renovación inválida (anterior) | 05/10/2026 | 17/10/2026 | ✅ `IllegalArgumentException` lanzada |

---

## Conclusión

Diseñar relaciones entre clases obliga a preguntarse qué significa realmente cada vínculo en el dominio. Mientras se observan clases aisladas, los atributos y métodos parecen suficientes. Al modelar colaboraciones aparecen preguntas críticas: ¿este objeto puede vivir sin el otro?, ¿necesitan conocerse o uno pertenece al otro?, ¿la semejanza refleja una abstracción real o solo coincidencia estructural? Responder esas preguntas con evidencia del requisito —y no por intuición o por el nombre de las clases— es lo que diferencia un diseño justificado de uno arbitrario. Las interfaces añaden una dimensión adicional: permiten comprometer comportamiento sin imponer herencia, separando el *qué* del *cómo*. El resultado es un modelo donde cada línea del diagrama tiene un argumento detrás.

---

*Taller Bloque 5 · SmartLibrary · Programación Orientada a Objetos*

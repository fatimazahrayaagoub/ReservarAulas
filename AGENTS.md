# Instrucciones para agentes de IA

## 1. Proyecto y objetivo

ReservarAulas es una aplicación educativa para consultar y gestionar reservas de aulas. Incluye una API REST para administrar aulas, profesores, franjas horarias y reservas, además de una interfaz web estática que consulta la API y permite registrar reservas.

Al trabajar en el proyecto, conserva el comportamiento existente y limita los cambios al alcance solicitado. La interfaz no es servida por Spring Boot: se encuentra en `frontend/` y consume la API por HTTP.

## 2. Tecnologías y versiones

- Java 21.
- Spring Boot 4.1.1, definido como parent en `pom.xml`.
- Spring Web MVC para los controladores REST.
- Spring Data JPA y Hibernate para persistencia.
- MySQL mediante MySQL Connector/J.
- Maven con Maven Wrapper; la configuración del wrapper especifica Maven 3.10.0.
- Lombok para generar métodos habituales en entidades.
- Frontend estático con HTML, CSS y JavaScript, sin framework de JavaScript.
- JUnit y dependencias de prueba de Spring Boot para pruebas.

Antes de cambiar versiones o dependencias, comprueba el `pom.xml` y mantén compatibles Java, Spring Boot y las bibliotecas existentes.

## 3. Arquitectura y estructura

El backend sigue una estructura sencilla por capas:

- `src/main/java/es/iesjuanbosco/reservarsaula/ReservarsAulaApplication.java`: punto de entrada de Spring Boot.
- `controller/`: controladores REST y rutas HTTP. Cada controlador usa repositorios para ejecutar operaciones.
- `entidad/`: entidades JPA (`Aula`, `Profesor`, `Reserva`, `FranjaHoraria`) y el record `ReservaRequest` para la entrada de creación de reservas.
- `repository/`: interfaces `JpaRepository` y consultas derivadas de Spring Data.
- `src/main/resources/application.properties`: configuración de Spring, MySQL y JPA.
- `src/test/java/es/iesjuanbosco/reservarsaula/`: pruebas. Actualmente incluye una prueba de carga del contexto.
- `frontend/index.html`: página de consulta y formulario de reserva.
- `frontend/app.js`: llamadas HTTP a la API, actualización de la interfaz, filtros de aulas y creación de reservas. La URL base configurada es `http://localhost:8080`.
- `frontend/style.css`: estilos adaptables de la interfaz.

Los recursos principales de la API son `/aulas`, `/profesores`, `/franjasHorarias` y `/reservas`. No presupongas que todos tienen exactamente las mismas operaciones; verifica su controlador antes de documentar o modificar endpoints. Actualmente los controladores acceden directamente a los repositorios; no hay una capa de servicios establecida.

## 4. Nomenclatura y estilo

- Mantén los nombres de paquetes en minúsculas y la organización existente bajo `es.iesjuanbosco.reservarsaula`.
- Conserva los nombres de dominio en español y en singular para entidades y tipos de recurso: `Aula`, `Profesor`, `Reserva`, `FranjaHoraria`.
- Usa `UpperCamelCase` para clases y records, `lowerCamelCase` para métodos y atributos, y `UPPER_SNAKE_CASE` para constantes.
- Los controladores terminan en `Controller`; los repositorios, en `Repository`.
- Sigue los tipos y convenciones de Spring existentes: anotaciones REST, `ResponseEntity`, entidades JPA y métodos derivados de `JpaRepository`.
- En el frontend, conserva el JavaScript y HTML actuales, los selectores por ID y las funciones de nombres descriptivos en español. Usa `textContent` para insertar texto de datos en la página, como hace el código existente.
- Respeta el formato del archivo que estés editando y evita reformatear archivos no relacionados.

## 5. Buenas prácticas

- Mantén una única fuente de verdad para reglas de negocio y aplícalas en el backend; la validación del navegador no sustituye la validación del servidor.
- Para reservas, conserva las reglas actuales: la fecha no puede ser anterior a hoy, las referencias a profesor/aula/franja deben existir y no se puede duplicar aula, fecha y franja.
- Usa las consultas derivadas disponibles en los repositorios en lugar de cargar y filtrar todos los registros si la consulta puede resolverse en base de datos.
- Respeta las relaciones JPA y los identificadores existentes al crear o actualizar entidades.
- No introduzcas dependencias, capas arquitectónicas ni abstracciones nuevas si el cambio puede resolverse de forma clara con los patrones actuales.
- Actualiza las pruebas y la documentación directamente relacionadas con el comportamiento que cambies.
- No incluyas credenciales, datos personales reales ni otros secretos en código, documentación o registros.

## 6. Creación y modificación de clases y métodos

- Antes de crear una clase, comprueba si el controlador, entidad, record o repositorio actual cubre ya el caso y sigue su ubicación y responsabilidad.
- Mantén los controladores enfocados en HTTP y la coordinación existente con repositorios. No muevas lógica a una nueva capa de servicios sin que el cambio lo requiera explícitamente.
- Usa DTOs o records para datos de entrada cuando corresponda; `ReservaRequest` define actualmente los campos `fecha`, `profesorId`, `aulaId` y `franjaId`.
- En endpoints nuevos o modificados, define claramente la ruta, método HTTP, cuerpo esperado y códigos de respuesta; conserva las respuestas coherentes con el resto de la API.
- Para entidades JPA, conserva las anotaciones, claves, nulabilidad y relaciones existentes. No cambies nombres de columnas o tablas sin considerar el esquema MySQL persistido.
- Evita añadir métodos o campos públicos que no sean necesarios. Aprovecha Lombok donde el proyecto ya lo usa y no dupliques métodos que este genere.
- No cambies contratos JSON utilizados por `frontend/app.js` sin actualizar también su consumidor.

## 7. Errores y excepciones

- Los controladores actuales comunican resultados HTTP con `ResponseEntity`: entre otros, `400 Bad Request` para datos inválidos, `404 Not Found` para recursos o referencias inexistentes, `409 Conflict` para una reserva duplicada y `201 Created` al crear.
- Conserva estos significados al extender la API. No conviertas errores en respuestas exitosas ni ocultes fallos con valores por defecto.
- Si introduces una excepción o una gestión global de errores, limita el cambio a errores que puedas traducir de forma explícita y consistente a una respuesta HTTP útil. El proyecto no tiene actualmente un manejador global establecido.
- En el frontend, presenta los fallos de las llamadas HTTP al usuario y no los trates como resultados vacíos exitosos. Mantén visibles los errores de red o de respuesta de la API.
- No registres credenciales ni información personal en mensajes de error.

## 8. Ejecución y pruebas

Se requiere JDK 21 y una instancia de MySQL accesible. `src/main/resources/application.properties` configura MySQL local en `localhost:3306`, la base de datos `reservas_aula`, el usuario `root` y `spring.jpa.hibernate.ddl-auto=update`; revisa y ajusta la configuración localmente según el entorno. No añadas credenciales reales al repositorio.

Desde la raíz, inicia el backend con Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

Spring Boot usa el puerto `8080` por defecto. El frontend debe servirse por separado; por ejemplo, con Python 3 desde la raíz:

```bash
python -m http.server 5500 --directory frontend
```

Después, abre `http://localhost:5500`. Si cambia la dirección del backend, actualiza `API_URL` en `frontend/app.js`.

Ejecuta las pruebas con:

```powershell
.\mvnw.cmd test
```

La suite actual comprueba que el contexto de Spring carga correctamente. Al añadir o cambiar lógica, añade pruebas enfocadas al comportamiento afectado; no des por probadas reglas de negocio solo porque cargue el contexto.

## 9. Restricciones y aspectos que deben evitarse

- No inventes funcionalidades, endpoints, configuración o resultados de pruebas: confirma cada afirmación en el código.
- No modifiques archivos fuera del alcance de la tarea ni reviertas cambios preexistentes del usuario.
- No guardes contraseñas o secretos en el repositorio. La configuración de MySQL debe tratarse como específica del entorno.
- No asumas que la interfaz está integrada en los recursos estáticos de Spring Boot; actualmente se sirve aparte.
- No cambies rutas, nombres de campos JSON, tipos de fecha ni formatos de respuesta sin actualizar los clientes y pruebas correspondientes.
- No elimines registros de forma masiva ni invoques los endpoints `DELETE` sin identificador salvo que la tarea lo solicite expresamente.
- No abras el acceso CORS más allá de la configuración ya existente (`origins = "*"`) ni lo presentes como una medida de seguridad; cualquier cambio debe estar justificado por el alcance del proyecto.
- No confíes exclusivamente en validaciones del frontend para proteger datos o preservar reglas de reserva.
- No añadas comentarios obvios, dependencias innecesarias ni cambios de formato masivos.

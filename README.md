# ReservarAulas

Aplicación web para consultar los recursos de un centro educativo y gestionar reservas de aulas. El proyecto combina una API REST desarrollada con Spring Boot, persistencia en MySQL y una interfaz web estática que consume la API.

## Funcionalidades

- Consulta de aulas, profesores, franjas horarias y reservas.
- Registro de reservas indicando fecha, profesor, aula y franja horaria.
- Validación de reservas en el servidor: no se aceptan fechas anteriores al día actual ni reservas duplicadas para la misma aula, fecha y franja.
- Consulta de aulas por identificador, disponibilidad de ordenadores o capacidad superior a un valor indicado.
- Operaciones REST de consulta, creación, modificación y eliminación para los recursos implementados en la API.
- Interfaz web con contadores, tablas de consulta, filtros de aulas y formulario de reserva.

## Tecnologías

- **Java 21**
- **Spring Boot 4.1.1**
- **Spring Web MVC** para la API REST
- **Spring Data JPA** e **Hibernate** para la persistencia
- **MySQL** y MySQL Connector/J
- **Maven** con Maven Wrapper
- **Lombok**
- **HTML, CSS y JavaScript** para el frontend, sin framework de JavaScript

## Requisitos

- JDK 21.
- MySQL en ejecución y credenciales con permisos para acceder a la base de datos.
- Un navegador web moderno.
- Python 3, solo si se desea servir el frontend con el comando de ejemplo de esta guía.

## Instalación y configuración

1. Clona el repositorio y entra en su directorio:

   ```bash
   git clone https://github.com/fatimazahrayaagoub/ReservarAulas.git
   cd ReservarAulas
   ```

2. Comprueba la conexión a MySQL en `src/main/resources/application.properties`. La configuración incluida apunta a `localhost:3306`, utiliza la base de datos `reservas_aula` y el usuario `root`. Ajusta la URL, el usuario y la contraseña a tu entorno. La URL configura la creación de la base de datos si no existe; Hibernate tiene `ddl-auto=update` para actualizar el esquema.

   No guardes credenciales reales en un repositorio compartido.

3. No hay un proceso de compilación independiente obligatorio: el Maven Wrapper descarga Maven si hace falta y resuelve las dependencias del proyecto al ejecutar la aplicación.

## Ejecución

Inicia la API desde la raíz del proyecto:

```bash
# Windows (PowerShell)
.\mvnw.cmd spring-boot:run

# Linux o macOS
./mvnw spring-boot:run
```

La aplicación Spring Boot usa el puerto `8080` por defecto. El frontend está en `frontend/` y no se configura como contenido estático servido por Spring Boot; sírvelo por separado. Por ejemplo, con Python 3, desde la raíz del proyecto:

```bash
python -m http.server 5500 --directory frontend
```

Abre `http://localhost:5500` en el navegador. El frontend apunta por defecto a `http://localhost:8080`; si cambias la dirección o el puerto de la API, actualiza `API_URL` al principio de `frontend/app.js`. Los controladores permiten peticiones CORS desde cualquier origen.

## API REST

La API está disponible en el mismo host y puerto que Spring Boot:

| Recurso | Rutas principales |
|---|---|
| Aulas | `/aulas`, `/aulas/{id}`, `/aulas/ordenadores?ordenadores={true\|false}`, `/aulas/capacidad?capacidad={n}` |
| Profesores | `/profesores`, `/profesores/{id}` |
| Franjas horarias | `/franjasHorarias`, `/franjasHorarias/{id}` |
| Reservas | `/reservas`, `/reservas/{id}` |

Los recursos admiten `GET` para consultar, `POST` para crear, `PUT /{id}` para modificar y `DELETE /{id}` para eliminar cuando esa operación está implementada para el recurso. `DELETE` sin identificador elimina todos los registros del recurso.

Para crear una reserva, envía un `POST` a `/reservas` con una fecha ISO (`AAAA-MM-DD`) e identificadores existentes:

```json
{
  "fecha": "2026-11-02",
  "profesorId": 1,
  "aulaId": 1,
  "franjaId": 1
}
```

El ejemplo requiere que existan el profesor, el aula y la franja indicados. La API responde con `201 Created` cuando la reserva se crea; devuelve `400 Bad Request` si faltan datos o la fecha es anterior al día actual, `404 Not Found` si alguna entidad referenciada no existe y `409 Conflict` si esa aula ya está reservada en esa fecha y franja.

## Estructura del proyecto

```text
ReservarAulas/
├── frontend/
│   ├── app.js                 # Lógica de interfaz e integración con la API
│   ├── index.html             # Página principal
│   └── style.css              # Estilos y diseño adaptable
├── src/
│   ├── main/
│   │   ├── java/es/iesjuanbosco/reservarsaula/
│   │   │   ├── controller/    # Controladores REST
│   │   │   ├── entidad/       # Entidades JPA y datos de solicitud
│   │   │   ├── repository/    # Repositorios Spring Data JPA
│   │   │   └── ReservarsAulaApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/              # Prueba de carga del contexto Spring
├── .mvn/wrapper/              # Configuración del Maven Wrapper
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## Pruebas

El proyecto contiene una prueba de contexto de Spring Boot. Para ejecutar la suite con el Maven Wrapper:

```bash
# Windows (PowerShell)
.\mvnw.cmd test

# Linux o macOS
./mvnw test
```

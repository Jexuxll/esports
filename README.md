# Stadion — Gestor de Esports

Aplicación web para la gestión de torneos, equipos, jugadores y partidos de esports. Desarrollada con Spring Boot y Thymeleaf.

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Backend | Java 21 · Spring Boot 4.0.4 |
| Web MVC | Spring Web MVC · Thymeleaf |
| Seguridad | Spring Security 6 |
| Base de datos | MySQL 8 · JDBC |
| Frontend | Bootstrap 5.3.3 · CSS custom |
| Build | Maven (mvnw) |

---

## Requisitos previos

- **Java 21** o superior
- **MySQL 8** en ejecución local
- Maven no es necesario; el proyecto incluye el wrapper `mvnw`

---

## Configuración de la base de datos

1. Ejecuta el script de inicialización (crea la base de datos `stadion` y todas sus tablas):

```sql
-- Desde MySQL Workbench, DBeaver o la CLI de MySQL:
source docs/init.sql
```

2. Si quieres cargar datos de ejemplo:

```sql
source docs/Data.sql
```

3. Edita `src/main/resources/application.properties` y añade tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stadion
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

---

## Ejecutar la aplicación

```bash
# En Windows
.\mvnw.cmd spring-boot:run

# En Linux / macOS
./mvnw spring-boot:run
```

La app arranca en **http://localhost:8080**

---

## Credenciales de administrador

| Campo | Valor |
|---|---|
| Usuario | `admin` |
| Contraseña | `admin1234` |

Accede desde el botón **👤** de la barra de navegación → `/admin/login`.

---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/proyecto/esports/
│   │   ├── config/          # SecurityConfig, WebConfig
│   │   ├── controller/      # Controladores MVC (Equipo, Jugador, Torneo, Partido, Juego…)
│   │   ├── model/           # Entidades (Equipo, Jugador, Torneo, Partido, Juego, EquipoTorneo)
│   │   ├── repository/      # Repositorios JDBC
│   │   └── service/         # Capa de servicio
│   └── resources/
│       ├── application.properties
│       ├── static/          # CSS, JS
│       └── templates/       # Vistas Thymeleaf (17 páginas)
docs/
├── init.sql                 # Script de creación de BD
├── Data.sql                 # Datos de ejemplo
└── diagramas-uml-er.md      # Diagramas UML y ER
```

---

## Funcionalidades

### Públicas
- Listado y detalle de **equipos**, **jugadores**, **torneos** y **partidos**
- Filtros en tiempo real (búsqueda por texto, rol, juego, equipo, estado)
- Inscripción de equipos a torneos

### Solo administrador
- CRUD completo de equipos, jugadores, torneos, partidos y juegos
- Subida de imágenes (logos de equipos, fotos de jugadores)
- Registro de marcadores en partidos

---

## Modelos de datos

```
Juego        ← Jugador (juego)
Equipo       ← Jugador (equipo)
Equipo       ↔ Torneo  (via EquipoTorneo)
Equipo       ← Partido (equipoLocal / equipoVisitante / ganador)
Torneo       ← Partido
```

---

## Capturas de pantalla

> Las imágenes de jugadores y logos de equipos se guardan y se sirven desde `c:/Imagenes/`.

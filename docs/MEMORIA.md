# Memoria del Proyecto — Stadion

> Aplicación web de gestión de competiciones de eSports

---

## Índice

1. [Introducción](#1-introducción)
2. [Tecnologías utilizadas](#2-tecnologías-utilizadas)
3. [Arquitectura del sistema](#3-arquitectura-del-sistema)
4. [Modelo de datos](#4-modelo-de-datos)
5. [Funcionalidades implementadas](#5-funcionalidades-implementadas)
6. [Seguridad y control de acceso](#6-seguridad-y-control-de-acceso)
7. [Capa de presentación](#7-capa-de-presentación)
8. [Estructura del proyecto](#8-estructura-del-proyecto)
9. [Guía de instalación y ejecución](#9-guía-de-instalación-y-ejecución)
10. [Diagramas](#10-diagramas)
11. [Conclusiones](#11-conclusiones)

---

## 1. Introducción

**Stadion** es una aplicación web para la gestión integral de competiciones de eSports. Permite administrar equipos, jugadores, juegos, torneos y partidos desde una interfaz centralizada, diferenciando entre un rol de administrador (con capacidad de creación, edición y eliminación) y acceso público (solo lectura).

El objetivo del proyecto es disponer de un panel único que agrupe toda la información competitiva: plantillas de equipos, brackets de torneos, resultados de partidos e inscripciones, accesible desde cualquier navegador sin necesidad de instalar software adicional.

---

## 2. Tecnologías utilizadas

| Capa | Tecnología | Versión |
|---|---|---|
| Lenguaje | Java | 21 |
| Framework backend | Spring Boot | 4.0.4 |
| Motor de plantillas | Thymeleaf | (incluido en Spring Boot) |
| Seguridad | Spring Security | (incluido en Spring Boot) |
| Acceso a datos | JDBC puro (sin ORM) | — |
| Base de datos | MySQL | 8+ |
| Construcción | Maven | (wrapper incluido) |
| Framework CSS | Bootstrap | 5.3.3 (vía CDN) |
| Frontend | HTML5 + CSS3 + JavaScript vanilla | — |
| Servidor embebido | Apache Tomcat | (incluido en Spring Boot) |

El frontend adopta un enfoque híbrido: **Bootstrap 5.3.3** (cargado vía CDN) se encarga de la estructura y utilidades comunes (rejilla responsive, navbar, formularios, tablas, botones, utilidades de espaciado), mientras que `styles.css` define el sistema de diseño de marca propio y todos los componentes visuales únicos que Bootstrap no cubre. El comportamiento dinámico del cliente está íntegramente implementado en `app.js`, sin librerías JavaScript externas.

---

## 3. Arquitectura del sistema

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** con cuatro capas bien diferenciadas:

```
Navegador (HTML/CSS/JS)
        ↕  HTTP
   Controller  ←→  Service  ←→  DAO (interface)  ←→  DAOJdbc  ←→  MySQL
                                                        (JDBC)
        ↕
     Thymeleaf (Templates HTML)
```

### Capas

- **Controller**: Recibe las peticiones HTTP, llama al servicio correspondiente y devuelve la vista. Gestiona también la carga de ficheros de imagen.
- **Service**: Contiene la lógica de negocio. Actúa de intermediario entre el controlador y el repositorio.
- **DAO (Data Access Object)**: Interfaz que define las operaciones CRUD para cada entidad.
- **DAOJdbc**: Implementación concreta de cada DAO usando `PreparedStatement` sobre JDBC. Se inyecta con `@Qualifier`.
- **Conexion**: Clase singleton que gestiona la conexión a la base de datos MySQL.

### Controladores disponibles

| Controlador | Ruta base | Responsabilidad |
|---|---|---|
| `HomeController` | `/` | Dashboard principal, estadísticas, calendario |
| `EquipoController` | `/equipos` | CRUD de equipos |
| `JugadorController` | `/jugadores` | CRUD de jugadores |
| `JuegoController` | `/juegos` | CRUD de juegos |
| `TorneoController` | `/torneos` | CRUD de torneos, bracket |
| `PartidoController` | `/partidos` | CRUD de partidos |
| `EquipoTorneoController` | `/equipos-torneos` | Inscripción de equipos en torneos |
| `AuthController` | `/admin` | Login de administrador |

---

## 4. Modelo de datos

### Entidades Java

#### Equipo
Representa un equipo competitivo. Campos: `id`, `nombre`, `pais`, `tag`, `foto`.  
Relaciones: tiene una lista de `Jugador` y una lista de `EquipoTorneo`.

#### Jugador
Representa un jugador profesional. Campos: `id`, `nickname` (único), `nombre`, `apellido`, `rol`, `juego`, `foto`, `edad`, `nacionalidad`.  
Relaciones: pertenece a un `Equipo`.

#### Juego
Catálogo de videojuegos disponibles. Campos: `id`, `nombre`.

#### Torneo
Competición organizada. Campos: `id`, `nombre`, `juego`, `fechaInicio`, `fechaFin`, `estado` (`PENDIENTE` / `ACTIVO` / `FINALIZADO`), `foto`, `tag`.  
Relaciones: tiene listas de `EquipoTorneo` y `Partido`.

#### Partido
Enfrentamiento entre dos equipos dentro de un torneo. Campos: `id`, `torneo`, `equipoLocal`, `equipoVisitante`, `fechaPartido`, `ronda`, `marcadorLocal`, `marcadorVisitante`, `ganador`.

#### EquipoTorneo
Tabla de unión entre equipos y torneos. Campos: `id`, `equipo`, `torneo`, `fechaInscripcion`. Implementa la restricción de unicidad por par (equipo, torneo).

### Esquema de base de datos

```
equipos (id_equipo PK, nombre, tag_equipos UK, pais, foto)
juegos (id_juego PK, nombre)
torneos (id_torneo PK, nombre, juego, fecha_inicio, fecha_fin, estado ENUM, foto, tag, id_juego FK)
jugadores (id_jugador PK, nickname UK, nombre, apellido, rol, foto, edad, nacionalidad, juego, id_equipo FK, id_juego FK)
partidos (id_partido PK, id_torneo FK, id_equipo_local FK, id_equipo_visitante FK, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo FK)
equipos_torneos (id_equipo_torneo PK, id_equipo FK, id_torneo FK, fecha_inscripcion, UNIQUE(id_equipo, id_torneo))
```

---

## 5. Funcionalidades implementadas

### Acceso público (cualquier usuario)

| Función | Descripción |
|---|---|
| Dashboard principal | Estadísticas generales (equipos, jugadores, torneos, partidos), carrusel de torneos destacados, calendario de partidos y acceso rápido a secciones |
| Listado de equipos | Tabla con todos los equipos registrados, búsqueda y paginación visual |
| Detalle de equipo | Plantilla de jugadores agrupada por juego, historial de partidos con carrusel por torneo y enlace directo al torneo |
| Listado de jugadores | Tabla con foto, nickname, rol, equipo y juego |
| Detalle de jugador | Ficha completa con todos los datos del jugador |
| Listado de torneos | Tarjetas de torneos con estado (badge), juego, fechas y equipos inscritos |
| Detalle de torneo | Bracket visual de partidos agrupado por ronda, equipos participantes con sus jugadores |
| Listado de partidos | Tabla de todos los partidos con equipos, marcador, torneo y ronda |

### Acceso exclusivo ADMIN

| Función | Descripción |
|---|---|
| Crear / editar / eliminar equipo | Formulario con subida de imagen (logo) |
| Crear / editar / eliminar jugador | Formulario con subida de foto, selector de rol dinámico según el juego |
| Crear / editar / eliminar juego | Formulario simple |
| Crear / editar / eliminar torneo | Formulario con subida de foto, selector de juego, fechas y estado |
| Inscribir equipo en torneo | Selector de equipo con validación de inscripción duplicada |
| Crear / editar / eliminar partido | Asignación de equipos (solo inscritos en el torneo), marcadores y ronda |

### Funcionalidades JavaScript destacadas

- **Carrusel de torneos destacados** (index): navegación con flechas, transición automática.
- **Carrusel de partidos por torneo** (detalle de equipo): agrupación y navegación por torneo; clic en el nombre del torneo navega a su detalle.
- **Bracket visual** (detalle de torneo): líneas SVG que conectan partidos por ronda.
- **Calendario de partidos** (index): vista mensual con indicadores de días con partido y listado del día seleccionado.
- **Selector de rol dinámico** (registro de jugador): el desplegable de rol se filtra automáticamente según el juego seleccionado.
- **Tabs de jugadores** (detalle de equipo/torneo): navegación por pestañas agrupando jugadores por juego.

---

## 6. Seguridad y control de acceso

La seguridad se gestiona mediante **Spring Security**. Se define un único usuario administrador con credenciales almacenadas en `application.properties` y cifradas con `BCryptPasswordEncoder`.

```
Usuario: admin
Contraseña: admin1234
```

### Rutas protegidas (requieren rol ADMIN)

Todas las rutas de creación, edición y eliminación están protegidas:

```
/equipos/nuevo, /equipos/guardar, /equipos/editar/**, /equipos/actualizar, /equipos/eliminar/**
/jugadores/nuevo, /jugadores/guardar, /jugadores/editar/**, /jugadores/actualizar, /jugadores/eliminar/**
/juegos/nuevo, /juegos/guardar, /juegos/editar/**, /juegos/actualizar, /juegos/eliminar/**
/torneos/nuevo, /torneos/guardar, /torneos/editar/**, /torneos/actualizar, /torneos/eliminar/**
/torneos/*/inscribir, /torneos/*/partidos/nuevo, /torneos/*/partidos/guardar
/partidos/nuevo, /partidos/guardar, /partidos/editar/**, /partidos/actualizar, /partidos/eliminar/**
```

El resto de rutas son públicas. El login se realiza en `/admin/login` con un formulario personalizado (`admin_login.html`).

---

## 7. Capa de presentación

### Plantillas Thymeleaf

Todas las vistas se encuentran en `src/main/resources/templates/`. Se usa el dialecto estándar de Thymeleaf (`th:each`, `th:if`, `th:text`, `th:attr`) y el dialecto de Spring Security (`sec:authorize`) para mostrar u ocultar elementos según el rol del usuario.

| Plantilla | Descripción |
|---|---|
| `index.html` | Página principal: hero, carrusel de torneos, acceso rápido, calendario |
| `indexEquipo.html` | Listado de equipos |
| `indexJugador.html` | Listado de jugadores |
| `indexJuego.html` | Listado de juegos |
| `indexTorneo.html` | Listado de torneos |
| `indexPartido.html` | Listado de partidos |
| `detalle_equipo.html` | Detalle de equipo con plantilla y partidos |
| `detalle_jugador.html` | Ficha de jugador |
| `detalle_torneo.html` | Detalle de torneo con bracket |
| `registro_equipo.html` | Formulario crear/editar equipo |
| `registro_jugador.html` | Formulario crear/editar jugador |
| `registro_juego.html` | Formulario crear/editar juego |
| `registro_torneo.html` | Formulario crear/editar torneo |
| `registro_partido.html` | Formulario crear partido |
| `editar_partido.html` | Formulario editar partido |
| `inscribir_equipo_torneo.html` | Formulario de inscripción |
| `admin_login.html` | Login de administrador |

### Estilos

La capa de presentación combina **Bootstrap 5.3.3** (CDN) con un fichero de estilos propio `styles.css`:

**Bootstrap se encarga de:**
- Estructura y rejilla responsive (`container-fluid`, `row`, `col-*`)
- Componentes de navegación (`navbar`, `navbar-expand-lg`, hamburguesa móvil)
- Formularios (`form-control`, `form-select`, `mb-3`)
- Tablas (`table`, `table-hover`, `table-responsive`)
- Botones base (`btn`, `btn-outline-*`)
- Utilidades de espaciado, flexbox y visibilidad
- Tema oscuro mediante `data-bs-theme="dark"` en `<html>`

**`styles.css` se encarga de:**
- Sistema de CSS custom properties de la marca:

```css
--accent          /* azul principal */
--accent-cyan     /* cian para acentos secundarios */
--bg-card         /* fondo de tarjetas */
--text-primary    /* texto principal */
--font-display    /* tipografía de títulos */
--font-mono       /* tipografía monoespaciada para stats */
```

- Sobreescritura del tema oscuro de Bootstrap con los colores de marca (`[data-bs-theme="dark"]`)
- Componentes visuales únicos: bracket SVG de torneos, carrusel de torneos destacados, carrusel de partidos por equipo, calendario mensual, hero section, fichas de jugador (ID card), banners de torneo, tabs de jugadores, nav-cards de acceso rápido, badges de estado
- Tipografías fluidas con `clamp()` y media queries para los componentes propios

---

## 8. Estructura del proyecto

```
esports/
├── pom.xml                          # Dependencias Maven (Spring Boot 4.0.4, Java 21)
├── mvnw / mvnw.cmd                  # Maven wrapper
├── docs/
│   ├── init.sql                     # DDL completo de la base de datos
│   ├── Data.sql                     # Datos de ejemplo
│   ├── diagramas-uml-er.md          # Diagramas Mermaid (clase, CU, ER, secuencia)
│   └── MEMORIA.md                   # Este documento
├── Imagenes/                        # Imágenes subidas por los usuarios (runtime)
└── src/
    └── main/
        ├── java/com/proyecto/esports/
        │   ├── EsportsApplication.java
        │   ├── config/
        │   │   ├── SecurityConfig.java
        │   │   └── WebConfig.java
        │   ├── controller/          # 8 controladores MVC
        │   ├── model/               # 6 clases de modelo (POJO)
        │   ├── repository/          # Interfaces DAO + implementaciones DAOJdbc
        │   └── service/             # 6 servicios
        └── resources/
            ├── application.properties
            ├── static/
            │   ├── app.js           # Todo el JavaScript cliente
            │   └── styles.css       # Todos los estilos
            └── templates/           # 17 plantillas Thymeleaf
```

---

## 9. Guía de instalación y ejecución

### Requisitos previos

- Java 21 (JDK)
- MySQL 8 en ejecución local
- Maven (o usar el wrapper incluido `mvnw`)

### Pasos

**1. Crear la base de datos**

Ejecutar el script DDL en MySQL:

```sql
SOURCE docs/init.sql;
```

Opcionalmente cargar datos de ejemplo:

```sql
SOURCE docs/Data.sql;
```

**2. Configurar la conexión**

Editar `src/main/resources/application.properties` si es necesario ajustar host, puerto, usuario o contraseña de MySQL. La clase `Conexion.java` gestiona la conexión; revisar sus parámetros si difieren del entorno local.

**3. Ejecutar la aplicación**

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

**4. Acceder en el navegador**

```
http://localhost:8080
```

Para acceder al panel de administración:

```
http://localhost:8080/admin/login
Usuario: admin
Contraseña: admin1234
```

---

## 10. Diagramas

Los diagramas completos en formato Mermaid se encuentran en [`docs/diagramas-uml-er.md`](diagramas-uml-er.md):

- **Diagrama de clases** — Todas las entidades con atributos, métodos y relaciones.
- **Diagrama de casos de uso** — Diferenciación entre actor Administrador y Visitante.
- **Diagrama ER** — Esquema de la base de datos con claves primarias, foráneas y restricciones.
- **Diagramas de secuencia** (×5):
  - Ver detalle de torneo
  - Crear torneo (con flujo de autenticación)
  - Inscribir equipo en torneo
  - Ver detalle de equipo
  - Registrar equipo

---

## 11. Conclusiones

El proyecto **Stadion** cubre de forma completa el ciclo CRUD de una plataforma de gestión de eSports, diferenciando claramente entre acceso público y administración. Las decisiones técnicas principales han sido:

- **JDBC puro sin ORM**: mayor control sobre las consultas SQL y mejor comprensión del acceso a datos a nivel académico.
- **Enfoque CSS híbrido (Bootstrap + CSS propio)**: Bootstrap 5.3.3 gestiona la estructura, rejilla y componentes estándar, mientras que `styles.css` implementa el sistema de diseño de marca y todos los componentes visuales únicos. Esto demuestra tanto el uso práctico de un framework profesional como el dominio de CSS moderno con custom properties, `clamp()` y animaciones. El comportamiento dinámico sigue siendo JavaScript vanilla puro (`app.js`), sin librerías externas.
- **Arquitectura en capas estricta**: separación clara entre controladores, servicios y repositorios, facilitando el mantenimiento y la extensibilidad.
- **Spring Security**: gestión de autenticación y autorización de forma declarativa, protegiendo todas las rutas de escritura.

Como posibles mejoras futuras se identifican: migración a JPA/Hibernate para reducir código repetitivo en los DAOs, paginación del servidor para grandes volúmenes de datos, y un sistema de notificaciones en tiempo real para resultados de partidos.

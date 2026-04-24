# Diagramas del Sistema eSports

Este archivo incluye cuatro diagramas basados en la estructura real del proyecto.

## 1. Diagrama de clases

```mermaid
classDiagram
    class Equipo {
        +int id
        +String nombre
        +String pais
        +String tag
        +String foto
        +getId() int
        +setId(id int)
        +getNombre() String
        +setNombre(nombre String)
        +getPais() String
        +setPais(pais String)
        +getTag() String
        +setTag(tag String)
        +getFoto() String
        +setFoto(foto String)
    }

    class Jugador {
        +int id
        +String nickname
        +String nombre
        +String apellido
        +String rol
        +String juego
        +String foto
        +int edad
        +String nacionalidad
        +getId() int
        +getNickname() String
        +setNickname(nickname String)
        +getNombre() String
        +setNombre(nombre String)
        +getApellido() String
        +setApellido(apellido String)
        +getRol() String
        +setRol(rol String)
        +getJuego() String
        +setJuego(juego String)
        +getFoto() String
        +setFoto(foto String)
        +getEdad() int
        +setEdad(edad int)
        +getNacionalidad() String
        +setNacionalidad(nacionalidad String)
        +getEquipo() Equipo
        +setEquipo(equipo Equipo)
    }

    class Juego {
        +int id
        +String nombre
        +String foto
        +getId() int
        +setId(id int)
        +getNombre() String
        +setNombre(nombre String)
        +getFoto() String
        +setFoto(foto String)
    }

    class Torneo {
        +int id
        +String nombre
        +String juego
        +LocalDate fechaInicio
        +LocalDate fechaFin
        +String estado
        +String foto
        +String tag
        +getId() int
        +setId(id int)
        +getNombre() String
        +setNombre(nombre String)
        +getJuego() String
        +setJuego(juego String)
        +getFechaInicio() LocalDate
        +setFechaInicio(fechaInicio LocalDate)
        +getFechaFin() LocalDate
        +setFechaFin(fechaFin LocalDate)
        +getEstado() String
        +setEstado(estado String)
        +getFoto() String
        +setFoto(foto String)
        +getTag() String
        +setTag(tag String)
    }

    class Partido {
        +int id
        +LocalDateTime fechaPartido
        +String ronda
        +Integer marcadorLocal
        +Integer marcadorVisitante
        +getId() int
        +setId(id int)
        +getTorneo() Torneo
        +setTorneo(torneo Torneo)
        +getEquipoLocal() Equipo
        +setEquipoLocal(equipoLocal Equipo)
        +getEquipoVisitante() Equipo
        +setEquipoVisitante(equipoVisitante Equipo)
        +getFechaPartido() LocalDateTime
        +setFechaPartido(fechaPartido LocalDateTime)
        +getRonda() String
        +setRonda(ronda String)
        +getMarcadorLocal() Integer
        +setMarcadorLocal(marcadorLocal Integer)
        +getMarcadorVisitante() Integer
        +setMarcadorVisitante(marcadorVisitante Integer)
        +getGanador() Equipo
        +setGanador(ganador Equipo)
    }

    class EquipoTorneo {
        +int id
        +LocalDate fechaInscripcion
        +getId() int
        +setId(id int)
        +getEquipo() Equipo
        +setEquipo(equipo Equipo)
        +getTorneo() Torneo
        +setTorneo(torneo Torneo)
        +getFechaInscripcion() LocalDate
        +setFechaInscripcion(fechaInscripcion LocalDate)
    }

    Equipo "1" --> "0..*" Jugador : tiene
    Juego "1" --> "0..*" Torneo : clasifica
    Juego "1" --> "0..*" Jugador : especialidad
    Equipo "1" --> "0..*" EquipoTorneo : se inscribe en
    Torneo "1" --> "0..*" EquipoTorneo : recibe
    Torneo "1" --> "0..*" Partido : organiza
    Partido "0..*" --> "1" Equipo : equipoLocal
    Partido "0..*" --> "1" Equipo : equipoVisitante
    Partido "0..*" --> "0..1" Equipo : ganador
```

## 2. Diagrama de casos de uso

```mermaid
flowchart LR
    actorA([Administrador])
    actorV([Visitante])

    subgraph Sistema eSports
        UC0((Iniciar sesion admin))
        UC1((Gestionar equipos))
        UC2((Gestionar jugadores))
        UC3((Gestionar juegos))
        UC4((Gestionar torneos))
        UC5((Inscribir equipo en torneo))
        UC6((Gestionar partidos))
        UC7((Ver dashboard principal))
        UC8((Consultar detalle de equipo))
        UC9((Consultar detalle de jugador))
        UC10((Consultar detalle de torneo))
        UC11((Listar entidades publicas))
    end

    actorA --> UC0
    actorA --> UC1
    actorA --> UC2
    actorA --> UC3
    actorA --> UC4
    actorA --> UC5
    actorA --> UC6

    actorV --> UC7
    actorV --> UC8
    actorV --> UC9
    actorV --> UC10
    actorV --> UC11

    actorA --> UC7
    actorA --> UC8
    actorA --> UC9
    actorA --> UC10
    actorA --> UC11

    UC4 -. incluye .-> UC5
    UC4 -. incluye .-> UC6
```

## 3. Diagramas de secuencia (por partes)

### 3.1 Parte usuario: ver detalle de torneo

```mermaid
sequenceDiagram
    autonumber
    actor Usuario
    participant Browser
    participant Sec as Security
    participant TC as TorneoController
    participant TS as TorneoService
    participant ETS as EquipoTorneoService
    participant PS as PartidoService
    participant UI as detalle_torneo

    Usuario->>Browser: Entra a /torneos/{id}
    Browser->>Sec: GET /torneos/{id}
    Sec-->>Browser: Permitido (ruta publica)
    Browser->>TC: GET /torneos/{id}
    TC->>TS: obtenerPorId(id)
    TS-->>TC: Torneo
    TC->>ETS: listarPorTorneo(id)
    ETS-->>TC: EquiposInscritos
    TC->>PS: listarPorTorneo(id)
    PS-->>TC: Partidos
    TC-->>UI: Render vista detalle_torneo
    UI-->>Usuario: Muestra torneo + equipos + partidos
```

### 3.2 Parte usuario admin: iniciar sesion

```mermaid
sequenceDiagram
    autonumber
    actor Usuario as UsuarioAdmin
    participant Browser
    participant Auth as AuthController
    participant Sec as Security

    Usuario->>Browser: Entra a /admin/login
    Browser->>Auth: GET /admin/login
    Auth-->>Browser: Vista admin_login
    Usuario->>Browser: Envia credenciales
    Browser->>Sec: POST /admin/login
    alt Credenciales validas
        Sec-->>Browser: Login OK + sesion
        Browser-->>Usuario: Redireccion a /
    else Credenciales invalidas
        Sec-->>Browser: Error autenticacion
        Browser-->>Usuario: Muestra login con error
    end
```

### 3.3 Parte admin: inscribir equipo en torneo

```mermaid
sequenceDiagram
    autonumber
    actor Usuario as Admin
    participant Browser
    participant Sec as Security
    participant TC as TorneoController
    participant ETS as EquipoTorneoService
    participant TS as TorneoService
    participant ETDAO as EquipoTorneoDAOJdbc

    Usuario->>Browser: Envia formulario inscripcion
    Browser->>Sec: POST /torneos/{id}/inscribir(equipoId)
    alt Sin rol ADMIN
        Sec-->>Browser: Redirect /admin/login
        Browser-->>Usuario: Solicita autenticacion
    else Con rol ADMIN
        Sec-->>Browser: Permitido
        Browser->>TC: procesarInscripcion(id, equipoId)
        TC->>ETS: existeInscripcion(id, equipoId)
        ETS->>ETDAO: existeInscripcion(id, equipoId)
        ETDAO-->>ETS: true/false
        ETS-->>TC: true/false

        alt No existe inscripcion
            TC->>TS: obtenerPorId(id)
            TS-->>TC: Torneo
            TC->>ETS: guardar(EquipoTorneo)
            ETS->>ETDAO: guardar(equipoTorneo)
            ETDAO-->>ETS: ok
            ETS-->>TC: ok
        else Ya existe
            Note over TC: No duplica inscripcion
        end

        TC-->>Browser: redirect /torneos/{id}
        Browser-->>Usuario: Torneo actualizado
    end
```

## 4. Diagrama ER (entidad-relacion)

```mermaid
erDiagram
    EQUIPOS {
      INT id_equipo PK
      VARCHAR nombre
      VARCHAR tag_equipos UK
      VARCHAR pais
      VARCHAR foto
    }

    JUEGOS {
      INT id_juego PK
      VARCHAR nombre
      VARCHAR foto
    }

    TORNEOS {
      INT id_torneo PK
      VARCHAR nombre
      VARCHAR juego
      DATE fecha_inicio
      DATE fecha_fin
      ENUM estado
      VARCHAR foto
      VARCHAR tag
      INT id_juego FK
    }

    JUGADORES {
      INT id_jugador PK
      VARCHAR nickname UK
      VARCHAR nombre
      VARCHAR apellido
      VARCHAR rol
      INT id_equipo FK
      VARCHAR foto
      INT edad
      VARCHAR nacionalidad
      VARCHAR juego
      INT id_juego FK
    }

    PARTIDOS {
      INT id_partido PK
      INT id_torneo FK
      INT id_equipo_local FK
      INT id_equipo_visitante FK
      DATETIME fecha_partido
      VARCHAR ronda
      INT marcador_local
      INT marcador_visitante
      INT ganador_id_equipo FK
    }

    EQUIPOS_TORNEOS {
      INT id_equipo_torneo PK
      INT id_equipo FK
      INT id_torneo FK
      DATETIME fecha_inscripcion
    }

    JUEGOS ||--o{ TORNEOS : clasifica
    JUEGOS ||--o{ JUGADORES : especialidad
    EQUIPOS ||--o{ JUGADORES : pertenece
    TORNEOS ||--o{ PARTIDOS : contiene

    EQUIPOS ||--o{ PARTIDOS : local
    EQUIPOS ||--o{ PARTIDOS : visitante
    EQUIPOS ||--o{ PARTIDOS : ganador

    EQUIPOS ||--o{ EQUIPOS_TORNEOS : participa
    TORNEOS ||--o{ EQUIPOS_TORNEOS : inscribe
```

## Nota

Si tu profesor pide notacion UML estricta para casos de uso (con ovalos UML), puedo pasarte una version en PlantUML de los 4 diagramas tambien.

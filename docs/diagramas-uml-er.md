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

## 3. Diagrama ER (entidad-relacion)

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

## 4. Diagramas de secuencia

> En esta seccion cada diagrama representa un unico caso de uso. Asi se evita mezclar flujos de usuario y de administrador dentro del mismo escenario.

### 4.1 Ver detalle de torneo

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant Browser
    participant Sec as Security
    participant TC as TorneoController
    participant TS as TorneoService
    participant ETS as EquipoTorneoService
    participant PS as PartidoService
    participant UI as Vista torneo

    U->>Browser: Accede al detalle de un torneo
    Browser->>Sec: Solicita la pagina del torneo
    Sec-->>Browser: Acceso permitido (ruta publica)
    Browser->>TC: Procesa la solicitud
    TC->>TS: Busca los datos del torneo
    TS-->>TC: Devuelve informacion del torneo
    TC->>ETS: Consulta los equipos inscritos
    ETS-->>TC: Devuelve equipos participantes
    TC->>PS: Consulta los partidos agrupados por ronda
    PS-->>TC: Devuelve partidos ordenados por ronda
    TC-->>UI: Prepara la vista con torneo, equipos y partidos
    UI-->>U: Muestra torneo completo con bracket
```

### 4.2 Crear torneo

```mermaid
sequenceDiagram
    autonumber
    actor A as Admin
    participant Browser
    participant Auth as AuthController
    participant Sec as Security
    participant TC as TorneoController
    participant TS as TorneoService

    A->>Browser: Accede al panel de administracion
    Browser->>Auth: Solicita el formulario de login
    Auth-->>Browser: Muestra la pagina de inicio de sesion
    A->>Browser: Introduce credenciales
    Browser->>Sec: Envia las credenciales para verificacion
    alt Credenciales invalidas
        Sec-->>Browser: Autenticacion fallida
        Browser-->>A: Muestra mensaje de error en el login
    else Credenciales validas
        Sec-->>Browser: Autenticacion exitosa, inicia sesion
        A->>Browser: Rellena el formulario de nuevo torneo
        Browser->>Sec: Solicita guardar el torneo
        Sec-->>Browser: Acceso autorizado (rol ADMIN)
        Browser->>TC: Procesa el registro del torneo
        TC->>TS: Guarda el nuevo torneo
        TS-->>TC: Torneo creado correctamente
        TC-->>Browser: Redirige al listado de torneos
        Browser-->>A: Muestra el torneo creado
    end
```

### 4.3 Inscribir equipo en torneo

```mermaid
sequenceDiagram
    autonumber
    actor A as Admin
    participant Browser
    participant Sec as Security
    participant TC as TorneoController
    participant ETS as EquipoTorneoService

    A->>Browser: Envia el formulario de inscripcion de equipo
    Browser->>Sec: Tramita la peticion de inscripcion
    Sec-->>Browser: Acceso autorizado (rol ADMIN)
    Browser->>TC: Procesa la inscripcion del equipo
    TC->>ETS: Comprueba si el equipo ya esta inscrito
    ETS-->>TC: Informa si existe o no inscripcion previa
    alt No existe inscripcion previa
        TC->>ETS: Registra la nueva inscripcion
        ETS-->>TC: Inscripcion completada
    else Ya existe inscripcion
        Note over TC: Se evita duplicar la inscripcion
    end
    TC-->>Browser: Redirige al detalle del torneo
    Browser-->>A: Muestra el torneo actualizado
```

### 4.4 Ver detalle de equipo

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant Browser
    participant Sec as Security
    participant EC as EquipoController
    participant ES as EquipoService
    participant JS as JugadorService
    participant PS as PartidoService
    participant UI as Vista equipo

    U->>Browser: Accede al detalle de un equipo
    Browser->>Sec: Solicita la pagina del equipo
    Sec-->>Browser: Acceso permitido (ruta publica)
    Browser->>EC: Procesa la solicitud
    EC->>ES: Busca los datos del equipo
    ES-->>EC: Devuelve informacion del equipo
    EC->>JS: Consulta los jugadores agrupados por juego
    JS-->>EC: Devuelve plantilla de jugadores
    EC->>PS: Consulta los partidos agrupados por torneo
    PS-->>EC: Devuelve historial de partidos
    EC-->>UI: Prepara la vista con equipo, plantilla e historial
    UI-->>U: Muestra equipo con jugadores y partidos
```

### 4.5 Registrar equipo

```mermaid
sequenceDiagram
    autonumber
    actor A as Admin
    participant Browser
    participant Sec as Security
    participant EC as EquipoController
    participant ES as EquipoService

    A->>Browser: Rellena el formulario de nuevo equipo
    Browser->>Sec: Solicita guardar el equipo
    Sec-->>Browser: Acceso autorizado (rol ADMIN)
    Browser->>EC: Procesa el registro del equipo
    EC->>ES: Guarda el nuevo equipo
    ES-->>EC: Equipo creado correctamente
    EC-->>Browser: Redirige al listado de equipos
    Browser-->>A: Muestra el equipo creado
```

### 4.6 Ver detalle de jugador

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant Browser
    participant Sec as Security
    participant JC as JugadorController
    participant JS as JugadorService
    participant UI as Vista jugador

    U->>Browser: Accede al detalle de un jugador
    Browser->>Sec: Solicita la pagina del jugador
    Sec-->>Browser: Acceso permitido (ruta publica)
    Browser->>JC: Procesa la solicitud
    JC->>JS: Busca los datos del jugador
    JS-->>JC: Devuelve informacion del jugador con su equipo
    JC-->>UI: Prepara la vista con los datos del jugador
    UI-->>U: Muestra perfil completo del jugador
```

### 4.7 Registrar jugador

```mermaid
sequenceDiagram
    autonumber
    actor A as Admin
    participant Browser
    participant Sec as Security
    participant JC as JugadorController
    participant JS as JugadorService

    A->>Browser: Rellena el formulario de nuevo jugador
    Browser->>Sec: Solicita guardar el jugador
    Sec-->>Browser: Acceso autorizado (rol ADMIN)
    Browser->>JC: Procesa el registro del jugador
    JC->>JS: Guarda el nuevo jugador
    JS-->>JC: Jugador creado correctamente
    JC-->>Browser: Redirige al listado de jugadores
    Browser-->>A: Muestra el jugador creado
```

## Nota

Si tu profesor pide notacion UML estricta para casos de uso (con ovalos UML), puedo pasarte una version en PlantUML de los 4 diagramas tambien.

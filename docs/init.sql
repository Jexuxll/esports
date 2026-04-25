-- ============================================
-- CREAR BASE DE DATOS
-- ============================================
DROP DATABASE IF EXISTS torneogg;
CREATE DATABASE torneogg CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE torneogg;

-- ============================================
-- TABLA: equipos
-- ============================================
CREATE TABLE `equipos` (
  `id_equipo` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `tag_equipos` varchar(10) NOT NULL,
  `pais` varchar(50) DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_equipo`),
  UNIQUE KEY `tag_equipos` (`tag_equipos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: juegos
-- ============================================
CREATE TABLE `juegos` (
  `id_juego` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id_juego`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: torneos
-- ============================================
CREATE TABLE `torneos` (
  `id_torneo` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `juego` varchar(100) NOT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` enum('PENDIENTE','ACTIVO','FINALIZADO') DEFAULT 'PENDIENTE',
  `foto` varchar(255) DEFAULT NULL,
  `tag` varchar(10) DEFAULT NULL,
  `id_juego` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_torneo`),
  KEY `id_juego` (`id_juego`),
  CONSTRAINT `torneos_ibfk_1` FOREIGN KEY (`id_juego`) REFERENCES `juegos` (`id_juego`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: equipos_torneos
-- ============================================
CREATE TABLE `equipos_torneos` (
  `id_equipo_torneo` int(11) NOT NULL AUTO_INCREMENT,
  `id_equipo` int(11) NOT NULL,
  `id_torneo` int(11) NOT NULL,
  `fecha_inscripcion` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_equipo_torneo`),
  UNIQUE KEY `uq_equipo_torneo` (`id_equipo`,`id_torneo`),
  KEY `fk_et_torneo` (`id_torneo`),
  CONSTRAINT `fk_et_equipo` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_et_torneo` FOREIGN KEY (`id_torneo`) REFERENCES `torneos` (`id_torneo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: jugadores
-- ============================================
CREATE TABLE `jugadores` (
  `id_jugador` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `rol` varchar(50) DEFAULT NULL,
  `id_equipo` int(11) NOT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `edad` int(11) DEFAULT NULL,
  `nacionalidad` varchar(255) DEFAULT NULL,
  `juego` varchar(255) DEFAULT NULL,
  `id_juego` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_jugador`),
  UNIQUE KEY `nickname` (`nickname`),
  KEY `fk_jugador_equipo` (`id_equipo`),
  KEY `id_juego` (`id_juego`),
  CONSTRAINT `fk_jugador_equipo` FOREIGN KEY (`id_equipo`) REFERENCES `equipos` (`id_equipo`) ON UPDATE CASCADE,
  CONSTRAINT `jugadores_ibfk_1` FOREIGN KEY (`id_juego`) REFERENCES `juegos` (`id_juego`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: partidos
-- ============================================
CREATE TABLE `partidos` (
  `id_partido` int(11) NOT NULL AUTO_INCREMENT,
  `id_torneo` int(11) NOT NULL,
  `id_equipo_local` int(11) DEFAULT NULL,
  `id_equipo_visitante` int(11) DEFAULT NULL,
  `fecha_partido` datetime DEFAULT NULL,
  `ronda` varchar(50) DEFAULT NULL,
  `marcador_local` int(11) DEFAULT NULL,
  `marcador_visitante` int(11) DEFAULT NULL,
  `ganador_id_equipo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_partido`),
  KEY `fk_partido_torneo` (`id_torneo`),
  KEY `fk_partido_equipo_local` (`id_equipo_local`),
  KEY `fk_partido_equipo_visitante` (`id_equipo_visitante`),
  KEY `fk_partido_ganador` (`ganador_id_equipo`),
  CONSTRAINT `fk_partido_equipo_local` FOREIGN KEY (`id_equipo_local`) REFERENCES `equipos` (`id_equipo`) ON UPDATE CASCADE,
  CONSTRAINT `fk_partido_equipo_visitante` FOREIGN KEY (`id_equipo_visitante`) REFERENCES `equipos` (`id_equipo`) ON UPDATE CASCADE,
  CONSTRAINT `fk_partido_ganador` FOREIGN KEY (`ganador_id_equipo`) REFERENCES `equipos` (`id_equipo`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_partido_torneo` FOREIGN KEY (`id_torneo`) REFERENCES `torneos` (`id_torneo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_equipos_distintos` CHECK (`id_equipo_local` <> `id_equipo_visitante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

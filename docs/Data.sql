USE stadion;


-- JUEGOS (10)
INSERT INTO juegos (nombre) VALUES
('League of Legends'),      -- 1
('Valorant'),               -- 2
('CS2'),                    -- 3
('Dota 2'),                 -- 4
('Rocket League'),          -- 5
('Fortnite'),               -- 6
('Apex Legends'),           -- 7
('Overwatch 2'),            -- 8
('Rainbow Six Siege'),      -- 9
('FIFA');                   -- 10

-- EQUIPOS (10)
INSERT INTO equipos (nombre, tag_equipos, pais, foto) VALUES
('Astral Nova',       'ANV', 'España',        'astralnova.png'), -- 1
('Iron Serpents',     'IRS', 'Alemania',      'ironserpents.png'), -- 2
('Crimson Horizon',   'CRH', 'Francia',       'crimsonhorizon.png'), -- 3
('Eclipse Vanguard',  'ECV', 'Suecia',        'eclipsevanguard.png'), -- 4
('Phantom Titans',    'PHT', 'Reino Unido',   'phantomtitans.png'), -- 5
('Royal Hydra',       'RHY', 'Italia',        'royalhydra.png'), -- 6
('Solaris Edge',      'SLE', 'Portugal',      'solarisedge.png'), -- 7
('Quantum Wolves',    'QWV', 'Polonia',       'quantumwolves.png'), -- 8
('Vanguard Esports',  'VGE', 'Países Bajos',  'vanguardesports.png'), -- 9
('Polaris Gaming',    'PLG', 'Dinamarca',     'polarisgaming.png'); -- 10


-- TORNEOS (6)
INSERT INTO torneos (nombre, juego, fecha_inicio, fecha_fin, estado, foto, tag, id_juego) VALUES
('LoL Astral Circuit 2026',      'League of Legends', '2026-05-10', '2026-05-25', 'ACTIVO',      'lol.jpg', 'LAC', 1), -- 1
('Valorant Eclipse Masters',     'Valorant',          '2026-06-01', '2026-06-18', 'PENDIENTE',   'valorant.jpg', 'VEM', 2), -- 2
('CS2 Iron Clash 2025',          'CS2',               '2025-09-05', '2025-09-20', 'FINALIZADO',  'cs2.jpg', 'CIC', 3), -- 3
('Dota 2 Ancients Cup',          'Dota 2',            '2026-03-10', '2026-03-22', 'FINALIZADO',  'dota2.jpg', 'DAC', 4), -- 4
('Rocket League Sky League',     'Rocket League',     '2026-04-01', '2026-04-10', 'ACTIVO',      'rocketleague.jpg', 'RSL', 5), -- 5
('R6 Global Siege 2026',         'Rainbow Six Siege', '2026-07-01', '2026-07-20', 'PENDIENTE',   'r6.jpg', 'RGS', 9); -- 6

-- INSCRIPCIONES EQUIPOS_TORNEOS
-- TORNEO 1 (LoL Astral Circuit 2026) — 8 equipos
INSERT INTO equipos_torneos (id_equipo, id_torneo, fecha_inscripcion) VALUES
(1, 1, '2026-04-15 12:00:00'),
(3, 1, '2026-04-15 12:30:00'),
(4, 1, '2026-04-16 13:00:00'),
(5, 1, '2026-04-16 13:30:00'),
(6, 1, '2026-04-17 13:45:00'),
(7, 1, '2026-04-17 14:00:00'),
(9, 1, '2026-04-17 14:30:00'),
(10,1, '2026-04-17 15:00:00');

-- Torneo 2 (Valorant Eclipse Masters) - 8 equipos
INSERT INTO equipos_torneos (id_equipo, id_torneo, fecha_inscripcion) VALUES
(1, 2, '2026-05-10 10:00:00'),
(3, 2, '2026-05-10 10:30:00'),
(4, 2, '2026-05-10 11:00:00'),
(5, 2, '2026-05-10 11:15:00'),
(6, 2, '2026-05-11 11:30:00'),
(7, 2, '2026-05-11 12:00:00'),
(9, 2, '2026-05-11 12:30:00'),
(10,2, '2026-05-11 13:00:00');

-- Torneo 3 (CS2 Iron Clash) - 4 equipos
INSERT INTO equipos_torneos (id_equipo, id_torneo, fecha_inscripcion) VALUES
(2, 3, '2025-08-10 09:00:00'),
(4, 3, '2025-08-10 09:30:00'),
(8, 3, '2025-08-10 10:00:00'),
(10,3, '2025-08-10 10:30:00');

-- Torneo 4 (Dota 2 Ancients Cup) - 4 equipos
INSERT INTO equipos_torneos (id_equipo, id_torneo, fecha_inscripcion) VALUES
(2, 4, '2026-02-20 15:00:00'),
(10, 4, '2026-02-20 15:30:00'),
(5, 4, '2026-02-21 16:00:00'),
(8, 4, '2026-02-21 16:30:00');

-- Torneo 5 (Rocket League Sky League) - 4 equipos
INSERT INTO equipos_torneos (id_equipo, id_torneo, fecha_inscripcion) VALUES
(1, 5, '2026-03-10 11:00:00'),
(4, 5, '2026-03-10 11:30:00'),
(6, 5, '2026-03-11 12:00:00'),
(9, 5, '2026-03-11 12:30:00');

-- Torneo 6 (R6 Global Siege) - 4 equipos
INSERT INTO equipos_torneos (id_equipo, id_torneo, fecha_inscripcion) VALUES
(2, 6, '2026-06-10 13:00:00'),
(4, 6, '2026-06-10 13:30:00'),
(8, 6, '2026-06-11 14:00:00'),
(10,6, '2026-06-11 14:30:00');


-- JUGADORES
-- Cada equipo: 4 juegos (subequipos) con tamaños realistas

-- EQUIPO 1: Astral Nova
-- Juegos: LoL(1), Valorant(2), Rocket League(5), FIFA(10)
-- LoL (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('NovaBlade',   'Javier',   'Serrano',   'Top',      1, 23, 'España',      'League of Legends', 1),
('Starfall',    'Lucía',    'Morales',   'Jungle',   1, 22, 'México',      'League of Legends', 1),
('Celest',      'Daniela',  'Navarro',   'Mid',      1, 24, 'Argentina',   'League of Legends', 1),
('Luminex',     'Nerea',    'Crespo',    'ADC',      1, 21, 'España',      'League of Legends', 1),
('Astrax',      'Tiago',    'Ferreira',  'Support',  1, 25, 'Portugal',    'League of Legends', 1);

-- Valorant (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('PulseX',      'Claudia',  'Santos',    'Duelist',  1, 22, 'España',      'Valorant', 2),
('Spectra',     'Valeria',  'Paz',       'Controller',1, 23, 'Chile',     'Valorant', 2),
('Rayden',      'Camila',   'Costa',     'Initiator',1, 24, 'Brasil',     'Valorant', 2),
('HaloCore',    'André',    'Pereira',   'Sentinel', 1, 25, 'Portugal',   'Valorant', 2),
('Zenflare',    'Nicolás',  'Rivas',     'Flex',     1, 21, 'Uruguay',    'Valorant', 2);

-- Rocket League (3)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('Skyline',     'Alba',     'López',     'Striker',  1, 22, 'España',      'Rocket League', 5),
('TurboShift',  'Bruno',    'Giménez',   'Flex',     1, 23, 'Argentina',   'Rocket League', 5),
('AerialX',     'Beatriz',  'Carvalho',  'Defender', 1, 24, 'Portugal',    'Rocket League', 5);

-- FIFA (1)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('PenaltyKing', 'Elena',    'Martín',    'Player',   1, 24, 'España',      'FIFA', 10);

-- EQUIPO 2: Iron Serpents
-- Juegos: CS2(3), Dota2(4), R6(9), Apex(7)
-- CS2 (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('SteelFang',   'Jonas',    'Schmidt',   'Rifler',   2, 24, 'Alemania',    'CS2', 3),
('ViperCore',   'Greta',    'Becker',    'AWPer',    2, 23, 'Alemania',    'CS2', 3),
('IronSight',   'Tobias',   'Keller',    'Entry',    2, 25, 'Austria',     'CS2', 3),
('GrimLock',    'Maja',     'Kowalski',  'Support',  2, 26, 'Polonia',     'CS2', 3),
('RustStorm',   'Marek',    'Novak',     'IGL',      2, 27, 'Chequia',     'CS2', 3);

-- Dota 2 (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('Serpentia',   'Helena',   'Kraus',     'Carry',    2, 24, 'Alemania',    'Dota 2', 4),
('Obsidian',    'Leonie',   'Müller',    'Mid',      2, 23, 'Alemania',    'Dota 2', 4),
('Gravelord',   'Ivan',     'Petrov',    'Offlane',  2, 26, 'Rusia',       'Dota 2', 4),
('Nightroot',   'Pavel',    'Sidorov',   'Soft Supp',2, 25, 'Ucrania',     'Dota 2', 4),
('IronWisp',    'Milica',   'Jovanovic', 'Hard Supp',2, 27, 'Serbia',      'Dota 2', 4);

-- Rainbow Six Siege (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('BreachX',     'Omar',     'Hassan',    'Entry',    2, 24, 'Marruecos',   'Rainbow Six Siege', 9),
('Lockdown',    'Nadia',    'Karim',     'Flex',     2, 23, 'Egipto',      'Rainbow Six Siege', 9),
('SiegeMind',   'Salma',    'Nasser',    'Support',  2, 25, 'Túnez',       'Rainbow Six Siege', 9),
('DarkRoom',    'Fahim',    'Rahman',    'IGL',      2, 26, 'Bangladés',   'Rainbow Six Siege', 9),
('AnchorX',     'Hassan',   'Ali',       'Anchor',   2, 27, 'Arabia Saudí','Rainbow Six Siege', 9);

-- Apex Legends (3)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('SkyRaptor',   'Emma',     'Walker',    'Fragger',  2, 22, 'Reino Unido', 'Apex Legends', 7),
('PulseWing',   'Logan',    'Reed',      'Support',  2, 23, 'Estados Unidos','Apex Legends', 7),
('GravShift',   'Bianca',   'Silva',     'IGL',      2, 24, 'Brasil',      'Apex Legends', 7);

-- EQUIPO 3: Crimson Horizon
-- Juegos: LoL(1), Valorant(2), OW2(8), Fortnite(6)
-- LoL (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('Crimstar',    'Louise',   'Garnier',   'Top',      3, 24, 'Francia',     'League of Legends', 1),
('RedTide',     'Antoine',  'Mercier',   'Jungle',   3, 23, 'Francia',     'League of Legends', 1),
('HorizonX',    'Amélie',   'Rousseau',  'Mid',      3, 22, 'Bélgica',     'League of Legends', 1),
('Scarlet',     'Claire',   'Lamotte',   'ADC',      3, 25, 'Francia',     'League of Legends', 1),
('Aurorae',     'Nicolas',  'Dubois',    'Support',  3, 26, 'Suiza',       'League of Legends', 1);

-- Valorant (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('CrimShot',    'Chloé',    'Morel',     'Duelist',  3, 22, 'Francia',     'Valorant', 2),
('LineV',       'Manon',    'Girard',    'Controller',3, 23, 'Francia',    'Valorant', 2),
('Sightline',   'Julien',   'Martin',    'Initiator',3, 24, 'Canadá',      'Valorant', 2),
('RedEcho',     'Luca',     'Bianchi',   'Sentinel', 3, 25, 'Italia',      'Valorant', 2),
('Crux',        'Tomás',    'Almeida',   'Flex',     3, 21, 'Portugal',    'Valorant', 2);

-- Overwatch 2 (5)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('Payload',     'Oliver',   'Carter',    'Tank',     3, 23, 'Reino Unido', 'Overwatch 2', 8),
('PulseRay',    'Ethan',    'Walker',    'DPS',      3, 22, 'Irlanda',     'Overwatch 2', 8),
('BarrierX',    'Callum',   'Reeves',    'Tank',     3, 24, 'Reino Unido', 'Overwatch 2', 8),
('Healwave',    'Megan',    'Stevens',   'Support',  3, 25, 'Reino Unido', 'Overwatch 2', 8),
('Zenfield',    'Sean',     'O\'Brien',  'Support',  3, 26, 'Irlanda',     'Overwatch 2', 8);

-- Fortnite (4)
INSERT INTO jugadores (nickname, nombre, apellido, rol, id_equipo, edad, nacionalidad, juego, id_juego) VALUES
('BuildRush',   'Marco',    'Lopes',     'IGL',      3, 21, 'Portugal',    'Fortnite', 6),
('StormDrop',   'Gabriel',  'Silva',     'Fragger',  3, 22, 'Brasil',      'Fortnite', 6),
('SkyEdit',     'Inés',     'Gómez',     'Support',  3, 23, 'España',      'Fortnite', 6),
('ClutchNine',  'Kevin',    'Müller',    'Flex',     3, 24, 'Alemania',    'Fortnite', 6);

-- EQUIPO 4: Eclipse Vanguard

INSERT INTO jugadores VALUES
(NULL,'Zyren','Ingrid','Solberg','Rifler',4,NULL,24,'Noruega','CS2',3),
(NULL,'Krysol','Markus','Nyström','AWPer',4,NULL,23,'Suecia','CS2',3),
(NULL,'Vexlor','Signe','Lindgren','Entry',4,NULL,25,'Dinamarca','CS2',3),
(NULL,'Rhyzen','Jonas','Hedlund','IGL',4,NULL,26,'Suecia','CS2',3),
(NULL,'Averin','Mikkel','Karlsen','Support',4,NULL,22,'Noruega','CS2',3);

INSERT INTO jugadores VALUES
(NULL,'Navor','Elsa','Johansson','Duelist',4,NULL,22,'Suecia','Valorant',2),
(NULL,'Elyndra','Freja','Hansen','Controller',4,NULL,24,'Dinamarca','Valorant',2),
(NULL,'Syrin','Marek','Nowak','Initiator',4,NULL,25,'Polonia','Valorant',2),
(NULL,'Kaelis','Petya','Ivanova','Sentinel',4,NULL,26,'Bulgaria','Valorant',2),
(NULL,'Ravien','Nikolai','Petrov','Flex',4,NULL,23,'Rusia','Valorant',2);

INSERT INTO jugadores VALUES
(NULL,'Veylo','Moa','Lind','Striker',4,NULL,22,'Suecia','Rocket League',5),
(NULL,'Zyphos','Jon','Solheim','Flex',4,NULL,23,'Noruega','Rocket League',5),
(NULL,'Kynor','Ida','Christensen','Defender',4,NULL,24,'Dinamarca','Rocket League',5);

INSERT INTO jugadores VALUES
(NULL,'Navorix','Omar','Al-Farid','Entry',4,NULL,24,'EAU','Rainbow Six Siege',9),
(NULL,'Rhylox','Yasir','Haddad','Flex',4,NULL,23,'Jordania','Rainbow Six Siege',9),
(NULL,'Velkan','Fadi','Naim','Support',4,NULL,25,'Líbano','Rainbow Six Siege',9),
(NULL,'Syrux','Samir','Rahim','IGL',4,NULL,26,'Egipto','Rainbow Six Siege',9),
(NULL,'Nyxara','Salma','Khalil','Anchor',4,NULL,27,'Arabia Saudí','Rainbow Six Siege',9);

INSERT INTO jugadores VALUES
(NULL,'Zyrion','Henrik','Solberg','Top',4,NULL,24,'Noruega','League of Legends',1),
(NULL,'Kavren','Markus','Nyström','Jungle',4,NULL,23,'Suecia','League of Legends',1),
(NULL,'Rhyloxen','Soren','Lindgren','Mid',4,NULL,25,'Dinamarca','League of Legends',1),
(NULL,'Vexlorin','Jonas','Hedlund','ADC',4,NULL,26,'Suecia','League of Legends',1),
(NULL,'Elyndar','Mikkel','Karlsen','Support',4,NULL,22,'Noruega','League of Legends',1);

-- EQUIPO 5: Phantom Titans

INSERT INTO jugadores VALUES
(NULL,'Vexara','Nora','Smith','Top',5,NULL,23,'Irlanda','League of Legends',1),
(NULL,'Nyral','Olivia','Turner','Jungle',5,NULL,22,'Reino Unido','League of Legends',1),
(NULL,'Kairox','Noah','Brown','Mid',5,NULL,24,'Estados Unidos','League of Legends',1),
(NULL,'Zypher','Ava','Clark','ADC',5,NULL,21,'Canadá','League of Legends',1),
(NULL,'Rimor','Aiden','Wilson','Support',5,NULL,25,'Reino Unido','League of Legends',1);

INSERT INTO jugadores VALUES
(NULL,'Solven','Bruna','Oliveira','Carry',5,NULL,23,'Brasil','Dota 2',4),
(NULL,'Kynel','Carlos','Herrera','Mid',5,NULL,24,'Chile','Dota 2',4),
(NULL,'Ravion','Jorge','Ramírez','Offlane',5,NULL,25,'Perú','Dota 2',4),
(NULL,'Elyron','Luisa','García','Soft Support',5,NULL,26,'México','Dota 2',4),
(NULL,'Vornyx','Andrés','Pérez','Hard Support',5,NULL,27,'Colombia','Dota 2',4);

INSERT INTO jugadores VALUES
(NULL,'Zynko','Emi','Tanaka','DPS',5,NULL,23,'Japón','Overwatch 2',8),
(NULL,'Raviel','Haruto','Sato','Tank',5,NULL,22,'Japón','Overwatch 2',8),
(NULL,'Meyla','Aoi','Kobayashi','Support',5,NULL,24,'Japón','Overwatch 2',8),
(NULL,'Kairo','Minho','Park','DPS',5,NULL,25,'Corea del Sur','Overwatch 2',8),
(NULL,'Vexun','Yuna','Kim','Tank',5,NULL,26,'Corea del Sur','Overwatch 2',8);

INSERT INTO jugadores VALUES
(NULL,'Zyral','Diego','Silva','Fragger',5,NULL,22,'Argentina','Apex Legends',7),
(NULL,'Navoru','Matías','Lagos','Support',5,NULL,23,'Chile','Apex Legends',7),
(NULL,'Kelyon','Rafael','Moreira','IGL',5,NULL,24,'Brasil','Apex Legends',7);

-- Valorant (5)
INSERT INTO jugadores VALUES
(NULL,'Vynxar','Fernanda','Monteiro','Duelist',5,NULL,22,'Brasil','Valorant',2),
(NULL,'Zykonos','Juan','Valdez','Controller',5,NULL,23,'México','Valorant',2),
(NULL,'Ravork','Sofía','Núñez','Initiator',5,NULL,24,'Argentina','Valorant',2),
(NULL,'Elynak','Gabriel','Rojas','Sentinel',5,NULL,21,'Chile','Valorant',2),
(NULL,'Kynvael','Larissa','Ferreira','Flex',5,NULL,25,'Brasil','Valorant',2);

-- EQUIPO 6: Royal Hydra

INSERT INTO jugadores VALUES
(NULL,'Zevranis','Martina','Rossi','Top',6,NULL,23,'Italia','League of Legends',1),
(NULL,'Krysolar','Giulia','Bianchi','Jungle',6,NULL,24,'Italia','League of Legends',1),
(NULL,'Rhyvornix','Giovanni','Ricci','Mid',6,NULL,25,'Italia','League of Legends',1),
(NULL,'Vornexis','Paula','Silva','ADC',6,NULL,22,'Brasil','League of Legends',1),
(NULL,'Elyvoro','Miguel','Costa','Support',6,NULL,23,'Portugal','League of Legends',1);

INSERT INTO jugadores VALUES
(NULL,'Zyrux','Martina','Rossi','Duelist',6,NULL,23,'Italia','Valorant',2),
(NULL,'Velion','Luca','Bianchi','Controller',6,NULL,24,'Italia','Valorant',2),
(NULL,'Rhylo','Chiara','Ricci','Initiator',6,NULL,25,'Italia','Valorant',2),
(NULL,'Kynelix','Pedro','Silva','Sentinel',6,NULL,22,'Brasil','Valorant',2),
(NULL,'Averos','Inês','Costa','Flex',6,NULL,23,'Portugal','Valorant',2);

INSERT INTO jugadores VALUES
(NULL,'Nexor','Andreea','Popescu','Rifler',6,NULL,24,'Rumanía','CS2',3),
(NULL,'Zyphor','Viktor','Ivanov','AWPer',6,NULL,25,'Bulgaria','CS2',3),
(NULL,'Ravex','Milos','Petrovic','IGL',6,NULL,26,'Serbia','CS2',3),
(NULL,'Kryven','Petra','Kovacs','Entry',6,NULL,23,'Hungría','CS2',3),
(NULL,'Elyos','Filip','Kral','Support',6,NULL,27,'Chequia','CS2',3);

INSERT INTO jugadores VALUES
(NULL,'Vexium','Sara','Pereira','Striker',6,NULL,22,'Portugal','Rocket League',5),
(NULL,'Rhyzor','Sergio','Alonso','Flex',6,NULL,23,'España','Rocket League',5),
(NULL,'Kynaro','Noa','García','Defender',6,NULL,24,'España','Rocket League',5);

INSERT INTO jugadores VALUES
(NULL,'Zevran','Alessia','Romano','Player',6,NULL,25,'Italia','FIFA',10);

-- EQUIPO 7: Solaris Edge

INSERT INTO jugadores VALUES
(NULL,'Solvar','Leonor','Pereira','Top',7,NULL,23,'Portugal','League of Legends',1),
(NULL,'Raxion','Rui','Cardoso','Jungle',7,NULL,22,'Portugal','League of Legends',1),
(NULL,'Kelyos','Marina','Lopes','Mid',7,NULL,24,'Brasil','League of Legends',1),
(NULL,'Zyrenix','João','Silva','ADC',7,NULL,21,'Portugal','League of Legends',1),
(NULL,'Averon','Marta','Santos','Support',7,NULL,25,'España','League of Legends',1);

INSERT INTO jugadores VALUES
(NULL,'Navoris','Inês','Fernandes','Duelist',7,NULL,22,'Portugal','Valorant',2),
(NULL,'Krysolis','Tiago','Costa','Controller',7,NULL,23,'Portugal','Valorant',2),
(NULL,'Zevlor','Larissa','Gomes','Initiator',7,NULL,24,'Brasil','Valorant',2),
(NULL,'Peomiss','Pedro','Almeida','Sentinel',7,NULL,25,'Portugal','Valorant',2),
(NULL,'Rhyvox','Luis','Martins','Flex',7,NULL,26,'Portugal','Valorant',2);

INSERT INTO jugadores VALUES
(NULL,'Vexlo','Ariadna','Santos','IGL',7,NULL,21,'España','Fortnite',6),
(NULL,'Nyrux','Bruno','Silva','Fragger',7,NULL,22,'Brasil','Fortnite',6),
(NULL,'Xynaro','Matilde','Pires','Support',7,NULL,23,'Portugal','Fortnite',6),
(NULL,'Zyvol','Héctor','Ruiz','Flex',7,NULL,24,'España','Fortnite',6);

INSERT INTO jugadores VALUES
(NULL,'Ravexis','Catalina','Paz','Fragger',7,NULL,22,'Chile','Apex Legends',7),
(NULL,'Elyronis','Matías','Roldán','Support',7,NULL,23,'Argentina','Apex Legends',7),
(NULL,'Kynaris','Julia','Lagos','IGL',7,NULL,24,'España','Apex Legends',7);

-- EQUIPO 8: Quantum Wolves

INSERT INTO jugadores VALUES
(NULL,'Zevrox','Zofia','Nowak','Rifler',8,NULL,24,'Polonia','CS2',3),
(NULL,'Kynexis','Jan','Kowalski','AWPer',8,NULL,23,'Polonia','CS2',3),
(NULL,'Rhyvorn','Maja','Zielinska','Entry',8,NULL,25,'Polonia','CS2',3),
(NULL,'Elyvorn','Tomasz','Lewandowski','IGL',8,NULL,26,'Polonia','CS2',3),
(NULL,'Vexloris','Adam','Wojcik','Support',8,NULL,27,'Polonia','CS2',3);

INSERT INTO jugadores VALUES
(NULL,'Navorin','Mila','Ivanova','Carry',8,NULL,24,'Rusia','Dota 2',4),
(NULL,'Krylos','Sergei','Petrov','Mid',8,NULL,25,'Rusia','Dota 2',4),
(NULL,'Zyvar','Anya','Smirnova','Offlane',8,NULL,26,'Rusia','Dota 2',4),
(NULL,'Rhyson','Nikolai','Sidorov','Soft Support',8,NULL,27,'Rusia','Dota 2',4),
(NULL,'Elyvax','Dmitri','Volkov','Hard Support',8,NULL,28,'Rusia','Dota 2',4);

INSERT INTO jugadores VALUES
(NULL,'Vosneax','Aisha','Rahman','Entry',8,NULL,24,'Pakistán','Rainbow Six Siege',9),
(NULL,'Kryvenix','Imran','Hussain','Flex',8,NULL,23,'India','Rainbow Six Siege',9),
(NULL,'Zevlorin','Sara','Khan','Anchor',8,NULL,25,'Pakistán','Rainbow Six Siege',9),
(NULL,'Rhyvax','Yasir','Ahmed','IGL',8,NULL,26,'Bangladés','Rainbow Six Siege',9),
(NULL,'Elyndaros','Farid','Mahmud','Support',8,NULL,27,'Afganistán','Rainbow Six Siege',9);

INSERT INTO jugadores VALUES
(NULL,'KynaroX','Jin','Park','Tank',8,NULL,23,'Corea del Sur','Overwatch 2',8),
(NULL,'Zevrix','Minji','Choi','DPS',8,NULL,22,'Corea del Sur','Overwatch 2',8),
(NULL,'Rhyzeno','Sora','Kim','Support',8,NULL,24,'Corea del Sur','Overwatch 2',8),
(NULL,'Siyexin','Yui','Sato','Flex',8,NULL,25,'Japón','Overwatch 2',8),
(NULL,'Vexaro','Haruki','Tanaka','Tank',8,NULL,26,'Japón','Overwatch 2',8);

-- EQUIPO 9: Vanguard Esports

INSERT INTO jugadores VALUES
(NULL,'Zevor','Emma','Jansen','Top',9,NULL,23,'Países Bajos','League of Legends',1),
(NULL,'Kirvon','Lars','Visser','Jungle',9,NULL,22,'Países Bajos','League of Legends',1),
(NULL,'Rehlonn','Lotte','Bakker','Mid',9,NULL,24,'Países Bajos','League of Legends',1),
(NULL,'Vexor','Joris','Smit','ADC',9,NULL,21,'Países Bajos','League of Legends',1),
(NULL,'Elynos','Ruben','Dekker','Support',9,NULL,25,'Países Bajos','League of Legends',1);

INSERT INTO jugadores VALUES
(NULL,'Navoren','Sanne','Meijer','Duelist',9,NULL,22,'Países Bajos','Valorant',2),
(NULL,'Keecabis','Sven','Mulder','Controller',9,NULL,23,'Países Bajos','Valorant',2),
(NULL,'Zevlix','Noor','Bos','Initiator',9,NULL,24,'Países Bajos','Valorant',2),
(NULL,'Rhyvos','Niels','Vos','Sentinel',9,NULL,25,'Países Bajos','Valorant',2),
(NULL,'Lixes','Thijs','Koster','Flex',9,NULL,26,'Países Bajos','Valorant',2);

INSERT INTO jugadores VALUES
(NULL,'Vornex','Femke','Kuiper','Striker',9,NULL,22,'Países Bajos','Rocket League',5),
(NULL,'Presolx','Jan','Hoek','Defender',9,NULL,23,'Países Bajos','Rocket League',5),
(NULL,'Zevro','Isa','Post','Flex',9,NULL,24,'Países Bajos','Rocket League',5);

INSERT INTO jugadores VALUES
(NULL,'Hyasen','Mila','van Dijk','Fragger',9,NULL,22,'Países Bajos','Apex Legends',7),
(NULL,'Elyvos','Milan','de Boer','Support',9,NULL,23,'Países Bajos','Apex Legends',7),
(NULL,'Yerazix','Tess','Prins','IGL',9,NULL,24,'Países Bajos','Apex Legends',7);

-- EQUIPO 10: Polaris Gaming

INSERT INTO jugadores VALUES
(NULL,'Zevlorn','Astrid','Nielsen','Top',10,NULL,24,'Dinamarca','League of Legends',1),
(NULL,'Vidoom','Mads','Jensen','Jungle',10,NULL,23,'Dinamarca','League of Legends',1),
(NULL,'Rhyzenor','Freja','Christensen','Mid',10,NULL,25,'Dinamarca','League of Legends',1),
(NULL,'Vexloren','Jonas','Larsen','ADC',10,NULL,22,'Dinamarca','League of Legends',1),
(NULL,'Mourrem','Rasmus','Poulsen','Support',10,NULL,26,'Dinamarca','League of Legends',1);

INSERT INTO jugadores VALUES
(NULL,'Zevrin','Alma','Nielsen','Rifler',10,NULL,24,'Dinamarca','CS2',3),
(NULL,'Krysolin','Mads','Jensen','AWPer',10,NULL,23,'Dinamarca','CS2',3),
(NULL,'Vroneiss','Freja','Christensen','IGL',10,NULL,25,'Dinamarca','CS2',3),
(NULL,'Sannes','Jonas','Larsen','Entry',10,NULL,22,'Dinamarca','CS2',3),
(NULL,'Suralo','Rasmus','Poulsen','Support',10,NULL,26,'Dinamarca','CS2',3);

INSERT INTO jugadores VALUES
(NULL,'Nacoir','Solveig','Olsen','Carry',10,NULL,24,'Noruega','Dota 2',4),
(NULL,'Kryvenor','Henrik','Johansen','Mid',10,NULL,25,'Noruega','Dota 2',4),
(NULL,'Zyvorn','Ingrid','Hansen','Offlane',10,NULL,26,'Noruega','Dota 2',4),
(NULL,'Rhyvaxen','Kristian','Berg','Soft Support',10,NULL,27,'Noruega','Dota 2',4),
(NULL,'Eldaryn','Martin','Dahl','Hard Support',10,NULL,28,'Noruega','Dota 2',4);

INSERT INTO jugadores VALUES
(NULL,'Oskid','Ebba','Lind','Tank',10,NULL,23,'Suecia','Overwatch 2',8),
(NULL,'Zevroth','Erik','Svensson','DPS',10,NULL,22,'Suecia','Overwatch 2',8),
(NULL,'Haclin','Hanna','Karlsson','Support',10,NULL,24,'Suecia','Overwatch 2',8),
(NULL,'Prixers','Nils','Andersson','Flex',10,NULL,25,'Suecia','Overwatch 2',8),
(NULL,'Ilyperx','Lars','Nyström','Tank',10,NULL,26,'Suecia','Overwatch 2',8);

INSERT INTO jugadores VALUES
(NULL,'Zevion','Mille','Hansen','Player',10,NULL,24,'Dinamarca','FIFA',10);

-- Rainbow Six Siege (5)
INSERT INTO jugadores VALUES
(NULL,'Frostlok','Karla','Dahl','Entry',10,NULL,23,'Dinamarca','Rainbow Six Siege',9),
(NULL,'Ironwall','Tobias','Strand','Anchor',10,NULL,24,'Noruega','Rainbow Six Siege',9),
(NULL,'Vexgate','Mille','Lund','Flex',10,NULL,25,'Dinamarca','Rainbow Six Siege',9),
(NULL,'Rhysiege','Joakim','Berg','IGL',10,NULL,22,'Suecia','Rainbow Six Siege',9),
(NULL,'Elybreak','Soren','Holm','Support',10,NULL,26,'Noruega','Rainbow Six Siege',9);

-- Valorant (5)
INSERT INTO jugadores VALUES
(NULL,'Zevnaris','Nanna','Andersen','Duelist',10,NULL,23,'Dinamarca','Valorant',2),
(NULL,'Kryvornis','Viktor','Berg','Controller',10,NULL,24,'Noruega','Valorant',2),
(NULL,'Rhylanis','Elin','Lindberg','Initiator',10,NULL,22,'Suecia','Valorant',2),
(NULL,'Vexnori','Rasmus','Holm','Sentinel',10,NULL,25,'Dinamarca','Valorant',2),
(NULL,'Elykoris','Mikkel','Strand','Flex',10,NULL,23,'Noruega','Valorant',2);

-- Asignación automática de fotos de jugador/jugadora al cargar el SQL
-- Jugadores: jr1..jr4 | Jugadoras: ja1..ja4
UPDATE jugadores
SET foto = CASE
    WHEN LOWER(nombre) IN (
        'lucía','daniela','nerea','claudia','valeria','camila','alba','beatriz','elena','greta','maja','helena',
        'leonie','milica','nadia','salma','emma','bianca','louise','amélie','claire','chloé','manon','megan',
        'inés','ingrid','signe','elsa','freja','petya','moa','ida','nora','olivia','ava','bruna','luisa','emi',
        'aoi','yuna','fernanda','sofía','larissa','martina','giulia','paula','chiara','inês','andreea','petra',
        'sara','alessia','leonor','marina','marta','ariadna','matilde','catalina','julia','zofia','mila','anya',
        'aisha','minji','yui','lotte','sanne','noor','femke','isa','astrid','alma','solveig','ebba','hanna',
        'karla','nanna','elin','mille'
    )
    THEN CONCAT('ja', MOD(id_jugador - 1, 4) + 1, '.png')
    ELSE CONCAT('jr', MOD(id_jugador - 1, 4) + 1, '.png')
END
WHERE id_jugador > 0;


-- PARTIDOS
-- Jugados, pendientes, torneos finalizados/activos

-- Torneo 1: LoL Astral Circuit 2026 (ACTIVO)
-- PARTIDOS TORNEO 1 (8 equipos)
INSERT INTO partidos (id_torneo, id_equipo_local, id_equipo_visitante, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo) VALUES
(1,1,3, '2026-05-11 16:00:00','Cuartos de final',1,0,1),
(1,4,5, '2026-05-11 18:00:00','Cuartos de final',0,1,5),
(1,6,7, '2026-05-12 16:00:00','Cuartos de final',NULL,NULL,NULL),
(1,9,10,'2026-05-12 18:00:00','Cuartos de final',NULL,NULL,NULL),
(1,1,5, '2026-05-14 18:00:00','Semifinal',NULL,NULL,NULL),
(1,NULL,NULL, '2026-05-17 19:00:00','Semifinal',NULL,NULL,NULL),
(1,NULL,NULL, '2026-05-25 20:00:00','Final',NULL,NULL,NULL);


-- Torneo 2: Valorant Eclipse Masters (PENDIENTE, algunos partidos ya jugados)
INSERT INTO partidos (id_torneo, id_equipo_local, id_equipo_visitante, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo) VALUES
(2, 1,  3,  '2026-06-02 16:00:00', 'Cuartos de final', 13, 11, 1),
(2, 4,  6,  '2026-06-02 18:00:00', 'Cuartos de final', NULL, NULL, NULL),
(2, 7,  9,  '2026-06-03 17:00:00', 'Cuartos de final', NULL, NULL, NULL),
(2, 5,  10, '2026-06-03 19:00:00', 'Cuartos de final', NULL, NULL, NULL),
(2, 1,  NULL,  '2026-06-10 20:00:00', 'Semifinal',        NULL, NULL, NULL),
(2, NULL, NULL, '2026-06-10 22:00:00', 'Semifinal',        NULL, NULL, NULL),
(2, NULL, NULL, '2026-06-18 20:00:00', 'Final',            NULL, NULL, NULL);

-- Torneo 3: CS2 Iron Clash 2025 (FINALIZADO)
INSERT INTO partidos (id_torneo, id_equipo_local, id_equipo_visitante, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo) VALUES
(3, 2, 4, '2025-09-07 16:00:00', 'Semifinal', 2, 0, 2),
(3, 8,10, '2025-09-07 19:00:00', 'Semifinal', 1, 2,10),
(3, 2,10, '2025-09-20 20:00:00', 'Final',     2, 1, 2);

-- Torneo 4: Dota 2 Ancients Cup (FINALIZADO)
INSERT INTO partidos (id_torneo, id_equipo_local, id_equipo_visitante, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo) VALUES
(4, 2, 10, '2026-03-12 16:00:00', 'Semifinal', 2, 1, 2),
(4, 5, 8,  '2026-03-12 19:00:00', 'Semifinal', 0, 2, 8),
(4, 2, 8,  '2026-03-22 20:00:00', 'Final',     1, 3, 8);

-- Torneo 5: Rocket League Sky League (ACTIVO)
INSERT INTO partidos (id_torneo, id_equipo_local, id_equipo_visitante, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo) VALUES
(5, 1, 4, '2026-04-02 16:00:00', 'Semifinal', 3, 2, 1),
(5, 6, 9, '2026-04-02 18:00:00', 'Semifinal', NULL, NULL, NULL),
(5, 1, NULL, '2026-04-08 17:00:00', 'Final',     NULL, NULL, NULL);

-- Torneo 6: R6 Global Siege 2026 (PENDIENTE)
INSERT INTO partidos (id_torneo, id_equipo_local, id_equipo_visitante, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo) VALUES
(6, 2, 4, '2026-07-03 16:00:00', 'Semifinal', NULL, NULL, NULL),
(6, 8,10, '2026-07-03 18:00:00', 'Semifinal', NULL, NULL, NULL),
(6, NULL, NULL, '2026-07-15 20:00:00', 'Final',     NULL, NULL, NULL);
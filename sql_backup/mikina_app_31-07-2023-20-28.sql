-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Počítač: localhost:3306
-- Vytvořeno: Pon 31. čec 2023, 18:28
-- Verze serveru: 8.0.30
-- Verze PHP: 8.1.10

SET
SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET
time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáze: `mikina_app`
--

-- --------------------------------------------------------

--
-- Struktura tabulky `authorities`
--

CREATE TABLE `authorities`
(
    `username`  varchar(50) COLLATE utf8mb4_czech_ci NOT NULL,
    `authority` varchar(50) COLLATE utf8mb4_czech_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

--
-- Vypisuji data pro tabulku `authorities`
--

INSERT INTO `authorities` (`username`, `authority`)
VALUES ('admin', 'ROLE_ADMIN'),
       ('admin', 'ROLE_USER'),
       ('ales', 'ROLE_USER');

-- --------------------------------------------------------

--
-- Struktura tabulky `country`
--

CREATE TABLE `country`
(
    `id`           bigint                                NOT NULL,
    `country_name` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
    `version`      int                                   NOT NULL,
    `country_code` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

--
-- Vypisuji data pro tabulku `country`
--

INSERT INTO `country` (`id`, `country_name`, `version`, `country_code`)
VALUES (1, 'Afghánistán', 1, 'AFG'),
       (2, 'Alandy', 1, 'ALA'),
       (3, 'Albánie', 1, 'ALB'),
       (4, 'Alžírsko', 1, 'DZA'),
       (5, 'Americká Samoa', 1, 'ASM'),
       (6, 'Americké Panenské ostrovy', 1, 'VIR'),
       (7, 'Andorra', 1, 'AND'),
       (8, 'Angola', 1, 'AGO'),
       (9, 'Anguilla', 1, 'AIA'),
       (10, 'Antarktida', 1, 'ATA'),
       (11, 'Antigua a Barbuda', 1, 'ATG'),
       (12, 'Argentina', 1, 'ARG'),
       (13, 'Arménie', 1, 'ARM'),
       (14, 'Aruba', 1, 'ABW'),
       (15, 'Austrálie', 1, 'AUS'),
       (16, 'Ázerbájdžán', 1, 'AZE'),
       (17, 'Bahamy', 1, 'BHS'),
       (18, 'Bahrajn', 1, 'BHR'),
       (19, 'Bangladéš', 1, 'BGD'),
       (20, 'Barbados', 1, 'BRB'),
       (21, 'Belgie', 1, 'BEL'),
       (22, 'Belize', 1, 'BLZ'),
       (23, 'Bělorusko', 1, 'BLR'),
       (24, 'Benin', 1, 'BEN'),
       (25, 'Bermudy', 1, 'BMU'),
       (26, 'Bhútán', 1, 'BTN'),
       (27, 'Bolívie', 1, 'BOL'),
       (28, 'Bonaire, Svatý Eustach a Saba', 1, 'BES'),
       (29, 'Bosna a Hercegovina', 1, 'BIH'),
       (30, 'Botswana', 1, 'BWA'),
       (31, 'Bouvetův ostrov', 1, 'BVT'),
       (32, 'Brazílie', 1, 'BRA'),
       (33, 'Britské indickooceánské území', 1, 'IOT'),
       (34, 'Britské Panenské ostrovy', 1, 'VGB'),
       (35, 'Brunej', 1, 'BRN'),
       (36, 'Bulharsko', 1, 'BGR'),
       (37, 'Burkina Faso', 1, 'BFA'),
       (38, 'Burundi', 1, 'BDI'),
       (39, 'Cookovy ostrovy', 1, 'COK'),
       (40, 'Curaçao', 1, 'CUW'),
       (41, 'Čad', 1, 'TCD'),
       (42, 'Černá Hora', 1, 'MNE'),
       (43, 'Česko', 1, 'CZE'),
       (44, 'Čína', 1, 'CHN'),
       (45, 'Dánsko', 1, 'DNK'),
       (46, 'Dominika', 1, 'DMA'),
       (47, 'Dominikánská republika', 1, 'DOM'),
       (48, 'Džibutsko', 1, 'DJI'),
       (49, 'Egypt', 1, 'EGY'),
       (50, 'Ekvádor', 1, 'ECU'),
       (51, 'Eritrea', 1, 'ERI'),
       (52, 'Estonsko', 1, 'EST'),
       (53, 'Etiopie', 1, 'ETH'),
       (54, 'Faerské ostrovy', 1, 'FRO'),
       (55, 'Falklandy (Malvíny)', 1, 'FLK'),
       (56, 'Fidži', 1, 'FJI'),
       (57, 'Filipíny', 1, 'PHL'),
       (58, 'Finsko', 1, 'FIN'),
       (59, 'Francie', 1, 'FRA'),
       (60, 'Francouzská Guyana', 1, 'GUF'),
       (61, 'Francouzská jižní a antarktická území', 1, 'ATF'),
       (62, 'Francouzská Polynésie', 1, 'PYF'),
       (63, 'Gabon', 1, 'GAB'),
       (64, 'Gambie', 1, 'GMB'),
       (65, 'Ghana', 1, 'GHA'),
       (66, 'Gibraltar', 1, 'GIB'),
       (67, 'Grenada', 1, 'GRD'),
       (68, 'Grónsko', 1, 'GRL'),
       (69, 'Gruzie', 1, 'GEO'),
       (70, 'Guadeloupe', 1, 'GLP'),
       (71, 'Guam', 1, 'GUM'),
       (72, 'Guatemala', 1, 'GTM'),
       (73, 'Guernsey', 1, 'GGY'),
       (74, 'Guinea', 1, 'GIN'),
       (75, 'Guinea-Bissau', 1, 'GNB'),
       (76, 'Guyana', 1, 'GUY'),
       (77, 'Haiti', 1, 'HTI'),
       (78, 'Heardův ostrov a MacDonaldovy ostrovy', 1, 'HMD'),
       (79, 'Honduras', 1, 'HND'),
       (80, 'Hongkong', 1, 'HKG'),
       (81, 'Chile', 1, 'CHL'),
       (82, 'Chorvatsko', 1, 'HRV'),
       (83, 'Indie', 1, 'IND'),
       (84, 'Indonésie', 1, 'IDN'),
       (85, 'Irák', 1, 'IRQ'),
       (86, 'Írán', 1, 'IRN'),
       (87, 'Irsko', 1, 'IRL'),
       (88, 'Island', 1, 'ISL'),
       (89, 'Itálie', 1, 'ITA'),
       (90, 'Izrael', 1, 'ISR'),
       (91, 'Jamajka', 1, 'JAM'),
       (92, 'Japonsko', 1, 'JPN'),
       (93, 'Jemen', 1, 'YEM'),
       (94, 'Jersey', 1, 'JEY'),
       (95, 'Jižní Afrika', 1, 'ZAF'),
       (96, 'Jižní Georgie a Jižní Sandwichovy ostrovy', 1, 'SGS'),
       (97, 'Jižní Súdán', 1, 'SSD'),
       (98, 'Jordánsko', 1, 'JOR'),
       (99, 'Kajmanské ostrovy', 1, 'CYM'),
       (100, 'Kambodža', 1, 'KHM'),
       (101, 'Kamerun', 1, 'CMR'),
       (102, 'Kanada', 1, 'CAN'),
       (103, 'Kapverdy', 1, 'CPV'),
       (104, 'Katar', 1, 'QAT'),
       (105, 'Kazachstán', 1, 'KAZ'),
       (106, 'Keňa', 1, 'KEN'),
       (107, 'Kiribati', 1, 'KIR'),
       (108, 'Kokosové (Keelingovy) ostrovy', 1, 'CCK'),
       (109, 'Kolumbie', 1, 'COL'),
       (110, 'Komory', 1, 'COM'),
       (111, 'Konžská demokratická republika', 1, 'COD'),
       (112, 'Konžská republika', 1, 'COG'),
       (113, 'Korejská lidově demokratická republika', 1, 'PRK'),
       (114, 'Korejská republika', 1, 'KOR'),
       (115, 'Kosovo', 1, 'XXK'),
       (116, 'Kostarika', 1, 'CRI'),
       (117, 'Kuba', 1, 'CUB'),
       (118, 'Kuvajt', 1, 'KWT'),
       (119, 'Kypr', 1, 'CYP'),
       (120, 'Kyrgyzstán', 1, 'KGZ'),
       (121, 'Laos', 1, 'LAO'),
       (122, 'Lesotho', 1, 'LSO'),
       (123, 'Libanon', 1, 'LBN'),
       (124, 'Libérie', 1, 'LBR'),
       (125, 'Libye', 1, 'LBY'),
       (126, 'Lichtenštejnsko', 1, 'LIE'),
       (127, 'Litva', 1, 'LTU'),
       (128, 'Lotyšsko', 1, 'LVA'),
       (129, 'Lucembursko', 1, 'LUX'),
       (130, 'Macao', 1, 'MAC'),
       (131, 'Madagaskar', 1, 'MDG'),
       (132, 'Maďarsko', 1, 'HUN'),
       (133, 'Malajsie', 1, 'MYS'),
       (134, 'Malawi', 1, 'MWI'),
       (135, 'Maledivy', 1, 'MDV'),
       (136, 'Mali', 1, 'MLI'),
       (137, 'Malta', 1, 'MLT'),
       (138, 'Man', 1, 'IMN'),
       (139, 'Maroko', 1, 'MAR'),
       (140, 'Marshallovy ostrovy', 1, 'MHL'),
       (141, 'Martinik', 1, 'MTQ'),
       (142, 'Mauricius', 1, 'MUS'),
       (143, 'Mauritánie', 1, 'MRT'),
       (144, 'Mayotte', 1, 'MYT'),
       (145, 'Menší odlehlé ostrovy USA', 1, 'UMI'),
       (146, 'Mexiko', 1, 'MEX'),
       (147, 'Mikronésie', 1, 'FSM'),
       (148, 'Moldavsko', 1, 'MDA'),
       (149, 'Monako', 1, 'MCO'),
       (150, 'Mongolsko', 1, 'MNG'),
       (151, 'Montserrat', 1, 'MSR'),
       (152, 'Mosambik', 1, 'MOZ'),
       (153, 'Myanmar', 1, 'MMR'),
       (154, 'Namibie', 1, 'NAM'),
       (155, 'Nauru', 1, 'NRU'),
       (156, 'Německo', 1, 'DEU'),
       (157, 'Nepál', 1, 'NPL'),
       (158, 'Niger', 1, 'NER'),
       (159, 'Nigérie', 1, 'NGA'),
       (160, 'Nikaragua', 1, 'NIC'),
       (161, 'Niue', 1, 'NIU'),
       (162, 'Nizozemsko', 1, 'NLD'),
       (163, 'Norfolk', 1, 'NFK'),
       (164, 'Norsko', 1, 'NOR'),
       (165, 'Nová Kaledonie', 1, 'NCL'),
       (166, 'Nový Zéland', 1, 'NZL'),
       (167, 'Omán', 1, 'OMN'),
       (168, 'Pákistán', 1, 'PAK'),
       (169, 'Palau', 1, 'PLW'),
       (170, 'Palestina', 1, 'PSE'),
       (171, 'Panama', 1, 'PAN'),
       (172, 'Papua Nová Guinea', 1, 'PNG'),
       (173, 'Paraguay', 1, 'PRY'),
       (174, 'Peru', 1, 'PER'),
       (175, 'Pitcairn', 1, 'PCN'),
       (176, 'Pobřeží slonoviny', 1, 'CIV'),
       (177, 'Polsko', 1, 'POL'),
       (178, 'Portoriko', 1, 'PRI'),
       (179, 'Portugalsko', 1, 'PRT'),
       (180, 'Rakousko', 1, 'AUT'),
       (181, 'Réunion', 1, 'REU'),
       (182, 'Rovníková Guinea', 1, 'GNQ'),
       (183, 'Rumunsko', 1, 'ROU'),
       (184, 'Rusko', 1, 'RUS'),
       (185, 'Rwanda', 1, 'RWA'),
       (186, 'Řecko', 1, 'GRC'),
       (187, 'Saint Pierre a Miquelon', 1, 'SPM'),
       (188, 'Salvador', 1, 'SLV'),
       (189, 'Samoa', 1, 'WSM'),
       (190, 'San Marino', 1, 'SMR'),
       (191, 'Saúdská Arábie', 1, 'SAU'),
       (192, 'Senegal', 1, 'SEN'),
       (193, 'Severní Makedonie', 1, 'MKD'),
       (194, 'Severní Mariany', 1, 'MNP'),
       (195, 'Seychely', 1, 'SYC'),
       (196, 'Sierra Leone', 1, 'SLE'),
       (197, 'Singapur', 1, 'SGP'),
       (198, 'Slovensko', 1, 'SVK'),
       (199, 'Slovinsko', 1, 'SVN'),
       (200, 'Somálsko', 1, 'SOM'),
       (201, 'Spojené arabské emiráty', 1, 'ARE'),
       (202, 'Spojené státy', 1, 'USA'),
       (203, 'Srbsko', 1, 'SRB'),
       (204, 'Středoafrická republika', 1, 'CAF'),
       (205, 'Súdán', 1, 'SDN'),
       (206, 'Surinam', 1, 'SUR'),
       (207, 'Svatá Helena', 1, 'SHN'),
       (208, 'Svatá Lucie', 1, 'LCA'),
       (209, 'Svatý Bartoloměj', 1, 'BLM'),
       (210, 'Svatý Kryštof a Nevis', 1, 'KNA'),
       (211, 'Svatý Martin (FR)', 1, 'MAF'),
       (212, 'Svatý Martin (NL)', 1, 'SXM'),
       (213, 'Svatý Tomáš a Princův ostrov', 1, 'STP'),
       (214, 'Svatý Vincenc a Grenadiny', 1, 'VCT'),
       (215, 'Svazijsko', 1, 'SWZ'),
       (216, 'Sýrie', 1, 'SYR'),
       (217, 'Šalomounovy ostrovy', 1, 'SLB'),
       (218, 'Španělsko', 1, 'ESP'),
       (219, 'Špicberky a Jan Mayen', 1, 'SJM'),
       (220, 'Šrí Lanka', 1, 'LKA'),
       (221, 'Švédsko', 1, 'SWE'),
       (222, 'Švýcarsko', 1, 'CHE'),
       (223, 'Tádžikistán', 1, 'TJK'),
       (224, 'Tanzanie', 1, 'TZA'),
       (225, 'Thajsko', 1, 'THA'),
       (226, 'Tchaj-wan', 1, 'TWN'),
       (227, 'Togo', 1, 'TGO'),
       (228, 'Tokelau', 1, 'TKL'),
       (229, 'Tonga', 1, 'TON'),
       (230, 'Trinidad a Tobago', 1, 'TTO'),
       (231, 'Tunisko', 1, 'TUN'),
       (232, 'Turecko', 1, 'TUR'),
       (233, 'Turkmenistán', 1, 'TKM'),
       (234, 'Turks a Caicos', 1, 'TCA'),
       (235, 'Tuvalu', 1, 'TUV'),
       (236, 'Uganda', 1, 'UGA'),
       (237, 'Ukrajina', 1, 'UKR'),
       (238, 'Uruguay', 1, 'URY'),
       (239, 'Uzbekistán', 1, 'UZB'),
       (240, 'Vánoční ostrov', 1, 'CXR'),
       (241, 'Vanuatu', 1, 'VUT'),
       (242, 'Vatikán', 1, 'VAT'),
       (243, 'Velká Británie', 1, 'GBR'),
       (244, 'Venezuela', 1, 'VEN'),
       (245, 'Vietnam', 1, 'VNM'),
       (246, 'Východní Timor', 1, 'TLS'),
       (247, 'Wallis a Futuna', 1, 'WLF'),
       (248, 'Zambie', 1, 'ZMB'),
       (249, 'Západní Sahara', 1, 'ESH'),
       (250, 'Zimbabwe', 1, 'ZWE');

-- --------------------------------------------------------

--
-- Struktura tabulky `guest`
--

CREATE TABLE `guest`
(
    `id`           bigint NOT NULL,
    `version`      int    NOT NULL,
    `first_name`   varchar(255) COLLATE utf8mb4_czech_ci DEFAULT NULL,
    `country_id`   bigint NOT NULL,
    `status_id`    bigint NOT NULL                       DEFAULT '1',
    `last_name`    varchar(255) COLLATE utf8mb4_czech_ci DEFAULT NULL,
    `birth_date`   date                                  DEFAULT NULL,
    `date_arrived` date                                  DEFAULT NULL,
    `date_left`    date                                  DEFAULT NULL,
    `id_number`    varchar(255) COLLATE utf8mb4_czech_ci DEFAULT NULL,
    `address`      varchar(255) COLLATE utf8mb4_czech_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

--
-- Vypisuji data pro tabulku `guest`
--

INSERT INTO `guest` (`id`, `version`, `first_name`, `country_id`, `status_id`, `last_name`, `birth_date`,
                     `date_arrived`, `date_left`, `id_number`, `address`)
VALUES (1201, 7, 'Pikachu', 43, 1, 'Filson', '2023-06-01', '2023-06-12', '2023-06-22', '20202020', NULL),
       (1454, 2, 'Radek', 43, 1, 'Filsak', '2023-06-12', '2023-08-06', '2023-08-09', '20444222', NULL),
       (1609, 4, 'Michal', 43, 1, 'Novák', '1976-01-10', '2023-07-10', '2023-07-14', '203050865', NULL),
       (1610, 3, 'Peter', 198, 1, 'Popelka', '1983-02-14', '2023-07-16', '2023-07-22', '203070456', NULL),
       (1651, 0, 'Jana', 198, 1, 'Kollerová', '1989-02-01', '2023-07-27', '2023-08-03', '203060789', NULL);

-- --------------------------------------------------------

--
-- Struktura tabulky `idgenerator`
--

CREATE TABLE `idgenerator`
(
    `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

--
-- Vypisuji data pro tabulku `idgenerator`
--

INSERT INTO `idgenerator` (`next_val`)
VALUES (1750);

-- --------------------------------------------------------

--
-- Struktura tabulky `status`
--

CREATE TABLE `status`
(
    `id`      bigint NOT NULL,
    `name`    varchar(255) COLLATE utf8mb4_czech_ci DEFAULT NULL,
    `version` int    NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

--
-- Vypisuji data pro tabulku `status`
--

INSERT INTO `status` (`id`, `name`, `version`)
VALUES (1, 'Imported lead', 0),
       (2, 'Not contacted', 0),
       (3, 'Contacted', 0),
       (4, 'Customer', 0),
       (5, 'Closed (lost)', 0);

-- --------------------------------------------------------

--
-- Struktura tabulky `users`
--

CREATE TABLE `users`
(
    `username` varchar(50) COLLATE utf8mb4_czech_ci  NOT NULL,
    `password` varchar(500) COLLATE utf8mb4_czech_ci NOT NULL,
    `enabled`  tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

--
-- Vypisuji data pro tabulku `users`
--

INSERT INTO `users` (`username`, `password`, `enabled`)
VALUES ('admin', '{bcrypt}$2a$10$qwLpQWt9WZLDvyHvme42l./mWggR2nJli2lUIRQoDlUyvjKpE2XfW', 1),
       ('ales', '{bcrypt}$2a$10$fSCqGAN9WXgLaS1UdG.gkOzEKXxxDtVqouuDQngzQF60SzH7k8sfy', 1);

--
-- Indexy pro exportované tabulky
--

--
-- Indexy pro tabulku `authorities`
--
ALTER TABLE `authorities`
    ADD UNIQUE KEY `ix_auth_username` (`username`,`authority`);

--
-- Indexy pro tabulku `country`
--
ALTER TABLE `country`
    ADD PRIMARY KEY (`id`);

--
-- Indexy pro tabulku `guest`
--
ALTER TABLE `guest`
    ADD PRIMARY KEY (`id`),
  ADD KEY `status_id` (`status_id`),
  ADD KEY `FK5w97l6avtpl8j68u9d4j1ev1o` (`country_id`);

--
-- Indexy pro tabulku `status`
--
ALTER TABLE `status`
    ADD PRIMARY KEY (`id`);

--
-- Indexy pro tabulku `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`username`);

--
-- Omezení pro exportované tabulky
--

--
-- Omezení pro tabulku `authorities`
--
ALTER TABLE `authorities`
    ADD CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`);

--
-- Omezení pro tabulku `guest`
--
ALTER TABLE `guest`
    ADD CONSTRAINT `FK5w97l6avtpl8j68u9d4j1ev1o` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

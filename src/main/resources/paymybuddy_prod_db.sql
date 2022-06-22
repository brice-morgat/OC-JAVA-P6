
START TRANSACTION;
--
-- Base de données : `paymybuddy_prod_db`
--

CREATE DATABASE IF NOT EXISTS `paymybuddy_prod_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `paymybuddy_prod_db`;

-- --------------------------------------------------------

--
-- Structure de la table `contact`
--

DROP TABLE IF EXISTS `contact`;
CREATE TABLE IF NOT EXISTS `contact` (
  `id` bigint NOT NULL,
  `contact_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe07k4jcfdophemi6j1lt84b61` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `contact`
--

INSERT INTO `contact` (`id`, `contact_id`, `user_id`) VALUES
(1, 2, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4);

-- --------------------------------------------------------

--
-- Structure de la table `contact_id_seq`
--

DROP TABLE IF EXISTS `contact_id_seq`;
CREATE TABLE IF NOT EXISTS `contact_id_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `contact_id_seq`
--

INSERT INTO `contact_id_seq` (`next_val`) VALUES
(5);

-- --------------------------------------------------------

--
-- Structure de la table `operation`
--

DROP TABLE IF EXISTS `operation`;
CREATE TABLE IF NOT EXISTS `operation` (
  `id` bigint NOT NULL,
  `amount` float DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `receiver_id` bigint DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `emitter_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfpo0xqx794ih7sf687n86g3rh` (`emitter_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `operation_id_seq`
--

DROP TABLE IF EXISTS `operation_id_seq`;
CREATE TABLE IF NOT EXISTS `operation_id_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `operation_id_seq`
--

INSERT INTO `operation_id_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL,
  `balance` float DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `balance`, `email`, `password`, `surname`) VALUES
(1, 100, 'haley@gmail.com', '$2a$10$YsuvakFOMSYzFQv4QD1swuqI3hw8woV2j0iIKnSTbz8K7kJodQnuq', 'Haley'),
(2, 100, 'clara@gmail.com', '$2a$10$rDWV1mYaiA19y0zKei3CRud3Zl2kE6F/6GSmJbAcTlg1ammkS14PC', 'Clara'),
(3, 100, 'smith@gmail.com', '$2a$10$Hm3AlYP/62U60MQLvmnZMuDQQswIk8K1UYtHnG3CEDZKKfPvbDDJ2', 'Smith'),
(4, 100, 'brice.morgat@gmx.fr', '$2a$10$UxsKx3mg7JsY8OyB4rHu9ueM0evtvJZUBdrr1xF28Dyfmt1m45fRq', 'Brice');

-- --------------------------------------------------------

--
-- Structure de la table `user_id_seq`
--

DROP TABLE IF EXISTS `user_id_seq`;
CREATE TABLE IF NOT EXISTS `user_id_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `user_id_seq`
--

INSERT INTO `user_id_seq` (`next_val`) VALUES
(5);
COMMIT;
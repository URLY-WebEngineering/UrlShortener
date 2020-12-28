CREATE USER 'urly'@'localhost' IDENTIFIED BY 'urly';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON *.* TO 'urly'@'localhost';
# Create DB
CREATE DATABASE IF NOT EXISTS `urly`;
USE `urly`;

CREATE TABLE `click` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `browser` varchar(255) DEFAULT NULL,
                         `country` varchar(255) DEFAULT NULL,
                         `created` date DEFAULT NULL,
                         `hash` varchar(255) NOT NULL,
                         `ip` varchar(255) DEFAULT NULL,
                         `platform` varchar(255) DEFAULT NULL,
                         `referrer` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
);

-- urly.shorturl definition

CREATE TABLE `shorturl` (
                            `hash` varchar(255) NOT NULL,
                            `ip` varchar(255) DEFAULT NULL,
                            `checked` bit(1) DEFAULT NULL,
                            `country` varchar(255) DEFAULT NULL,
                            `created` date DEFAULT NULL,
                            `mode` int(11) DEFAULT NULL,
                            `owner` varchar(255) DEFAULT NULL,
                            `qr` tinyblob,
                            `reachable` bit(1) DEFAULT NULL,
                            `safe` bit(1) DEFAULT NULL,
                            `sponsor` varchar(255) DEFAULT NULL,
                            `target` varchar(255) DEFAULT NULL,
                            `uri` tinyblob,
                            PRIMARY KEY (`hash`)
);
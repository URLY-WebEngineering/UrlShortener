CREATE USER 'urly'@'localhost' IDENTIFIED BY 'urly';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON *.* TO 'urly'@'localhost';
# Create DB
CREATE DATABASE IF NOT EXISTS `urly`;
USE `urly`;
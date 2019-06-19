create database groovierspring;
grant all privileges on groovierspring.* to 'groovierspring'@'localhost' identified by 'groovy';
flush privileges;

DROP TABLE `groovierspring`.`groovy_scripts`;

CREATE TABLE `groovierspring`.`groovy_scripts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `script_name` varchar(255) NOT NULL,
  `script_source` text NOT NULL,
  `last_updated` TIMESTAMP NOT NULL,
  PRIMARY KEY(`id`)
)
CHARACTER SET utf8
COMMENT = 'Contains Groovy scripts that end up as Spring beans!';

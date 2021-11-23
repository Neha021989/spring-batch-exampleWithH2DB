DROP TABLE person IF EXISTS;

CREATE TABLE person  (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);


insert into person(id,first_name,last_name) values (1,'neha','choudhary');
insert into person(id,first_name,last_name) values (2,'ankit','chaudhary');

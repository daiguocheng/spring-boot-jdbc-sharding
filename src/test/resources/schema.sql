CREATE SCHEMA IF NOT EXISTS globe; -- CHARACTER SET utf8 COLLATE utf8_general_ci;
USE globe;
CREATE TABLE IF NOT EXISTS usr_idx (
    mobile BIGINT not null PRIMARY KEY,
    id VARCHAR(40) not null,
    created DATETIME not null
);
CREATE TABLE IF NOT EXISTS usr (
    id VARCHAR(40) not null PRIMARY KEY,
    mobile BIGINT not null,
    name VARCHAR(40) not null,
    password VARCHAR(64) not null,
    updated DATETIME not null
);


CREATE SCHEMA IF NOT EXISTS local0;
USE local0;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;


CREATE SCHEMA IF NOT EXISTS local1;
USE local1;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;

CREATE SCHEMA IF NOT EXISTS local2;
USE local2;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;

CREATE SCHEMA IF NOT EXISTS local3;
USE local3;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;
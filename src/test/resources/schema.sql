--不同的SQL数据库对DDL的支持差异太大，难以移植；以下脚本仅供参考
DROP DATABASE IF EXISTS globe;
CREATE DATABASE globe;
\c globe;
CREATE TABLE IF NOT EXISTS usr_idx (
    mobile BIGINT not null PRIMARY KEY,
    id VARCHAR(40) not null,
    created TIMESTAMP not null
);
CREATE TABLE IF NOT EXISTS usr (
    id VARCHAR(40) not null PRIMARY KEY,
    mobile BIGINT not null,
    name VARCHAR(40) not null,
    password VARCHAR(64) not null,
    updated TIMESTAMP not null
);


DROP DATABASE IF EXISTS local0;
CREATE DATABASE local0;
\c local0;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;


DROP DATABASE IF EXISTS local1;
CREATE DATABASE local1;
\c local1;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;

DROP DATABASE IF EXISTS local2;
CREATE DATABASE local2;
\c local2;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;

DROP DATABASE IF EXISTS local3;
CREATE DATABASE local3;
\c local3;
CREATE TABLE IF NOT EXISTS usr_idx AS SELECT * FROM globe.usr_idx;
CREATE TABLE IF NOT EXISTS usr AS SELECT * FROM globe.usr;
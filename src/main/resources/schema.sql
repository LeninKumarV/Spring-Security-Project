-- create table users(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(500) not null,enabled boolean not null);
-- create table authorities (username varchar_ignorecase(50) not null,authority varchar_ignorecase(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
-- create unique index ix_auth_username on authorities (username,authority);

-- postgre sql connection
DROP TABLE IF EXISTS authorities CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    userId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
    authId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username)
        REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username
    ON authorities (username, authority);


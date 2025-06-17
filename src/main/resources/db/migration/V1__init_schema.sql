-- V1__create_accounts_table.sql
CREATE TABLE accounts
(
    id            UUID PRIMARY KEY,
    owner_name    VARCHAR(255)   NOT NULL,
    email         VARCHAR(255)   NOT NULL UNIQUE,
    date_of_birth DATE,
    address       VARCHAR(500),
    balance       NUMERIC(19, 2) NOT NULL,
    active        BOOLEAN        NOT NULL,
    created_by    VARCHAR(100),
    created_at    TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_by    VARCHAR(100),
    updated_at    TIMESTAMP
);

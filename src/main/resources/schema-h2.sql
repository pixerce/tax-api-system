CREATE SCHEMA IF NOT EXISTS tax;

CREATE TABLE IF NOT EXISTS tax.collection_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL,
    target_year_month VARCHAR,
    status VARCHAR,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    error_message VARCHAR
    );

CREATE TABLE IF NOT EXISTS tax.transaction_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    transaction_type VARCHAR DEFAULT NULL,
    amount INT,
    store_id VARCHAR DEFAULT NOT NULL,
    transaction_date DATE,
    created_at TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tax.store_vat (
    id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL,
    vat INT,
    sales INT,
    purchase INT,
    calculated_at TIMESTAMP,
    target_year_month VARCHAR
);

CREATE TABLE IF NOT EXISTS tax.store (
    id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL
);



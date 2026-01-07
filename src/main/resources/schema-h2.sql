CREATE SCHEMA IF NOT EXISTS tax;

CREATE TABLE IF NOT EXISTS tax.collection_task (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL,
    target_year_month VARCHAR,
    status VARCHAR,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    error_message VARCHAR,

    CONSTRAINT uk_store_year_month UNIQUE (store_id, target_year_month)
    );

CREATE INDEX idx_collection_task_latest
    ON tax.collection_task (store_id, target_year_month, started_at DESC);

CREATE TABLE IF NOT EXISTS tax.transaction_record (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    transaction_type VARCHAR DEFAULT NULL,
    amount INT,
    store_id VARCHAR DEFAULT NOT NULL,
    transaction_date DATE,
    created_at TIMESTAMP
    );

CREATE INDEX idx_transaction_record_stat
    ON tax.transaction_record (store_id, transaction_type, transaction_date);

CREATE TABLE IF NOT EXISTS tax.store_vat (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL,
    vat INT,
    sales INT,
    purchase INT,
    calculated_at TIMESTAMP,
    target_year_month VARCHAR
);

CREATE UNIQUE INDEX uix_store_vat_lookup
    ON tax.store_vat (store_id, target_year_month);

CREATE TABLE IF NOT EXISTS tax.store (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL
);

CREATE UNIQUE INDEX uix_store_store_id
    ON tax.store (store_id);

CREATE TABLE IF NOT EXISTS tax.user_info (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR
);


CREATE TABLE IF NOT EXISTS tax.user_store (
    user_srl BIGINT NOT NULL,
    store_srl BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (user_srl, store_srl),

    CONSTRAINT fk_user_store_user FOREIGN KEY (user_srl) REFERENCES tax.user_info (srl),
    CONSTRAINT fk_user_store_store FOREIGN KEY (store_srl) REFERENCES tax.store (srl)
);
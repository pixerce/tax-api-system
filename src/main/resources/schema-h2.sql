CREATE SCHEMA IF NOT EXISTS tax;

CREATE TABLE IF NOT EXISTS tax.collection_task (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL,
    target_year_month VARCHAR,
    status VARCHAR,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    error_message VARCHAR
    );

CREATE TABLE IF NOT EXISTS tax.transaction_record (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    transaction_type VARCHAR DEFAULT NULL,
    amount INT,
    store_id VARCHAR DEFAULT NOT NULL,
    transaction_date DATE,
    created_at TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tax.store_vat (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL,
    vat INT,
    sales INT,
    purchase INT,
    calculated_at TIMESTAMP,
    target_year_month VARCHAR
);

CREATE TABLE IF NOT EXISTS tax.store (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT ,
    store_id VARCHAR DEFAULT NOT NULL
);

CREATE TABLE IF NOT EXISTS tax.user_info (
    srl BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR NOT NULL
);

-- 3. 사용자-사업장 매핑 테이블 (권한 부여 테이블)
CREATE TABLE IF NOT EXISTS tax.user_store (
    srl BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_srl BIGINT NOT NULL,
    store_srl BIGINT NOT NULL,

    CONSTRAINT fk_user_store_user FOREIGN KEY (user_srl) REFERENCES user_info (srl),
    CONSTRAINT fk_user_store_store FOREIGN KEY (store_srl) REFERENCES store (srl)
);

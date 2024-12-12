CREATE DATABASE statemachine;
USE statemachine;

CREATE TABLE campaigns
(
    campaign_id   BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    campaign_name VARCHAR(255)                          NOT NULL,
    mkt_code      VARCHAR(255)                          NULL,
    mkt_name      VARCHAR(255)                          NOT NULL,
    budget        DECIMAL(38, 2)                        NULL,
    current_state VARCHAR(50) DEFAULT 'DRAFT'           NOT NULL,
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE action_logs
(
    log_id          BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    campaign_id     BIGINT                              NOT NULL,
    previous_state  VARCHAR(50)                         NULL,
    next_state      VARCHAR(50)                         NOT NULL,
    event_triggered VARCHAR(50)                         NOT NULL,
    note            TEXT                                NULL,
    changed_by      VARCHAR(50)                         NOT NULL,
    changed_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT action_logs_ibfk_1
        FOREIGN KEY (campaign_id) REFERENCES campaigns (campaign_id)
            ON DELETE CASCADE
);

CREATE INDEX campaign_id
    ON action_logs (campaign_id);

CREATE TABLE change_logs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT                              NOT NULL,
    actor       VARCHAR(50)                         NULL,
    params      JSON                                NULL,
    state       VARCHAR(50)                         NULL,

    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX campaign_id
    ON change_logs (campaign_id);
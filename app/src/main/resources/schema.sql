DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE url_checks
(
    id          BIGSERIAL PRIMARY KEY,
    url_id      BIGINT,
    status_code INT,
    title       VARCHAR(255),
    h1          VARCHAR(255),
    description TEXT,
    created_at   TIMESTAMP,
    FOREIGN KEY (url_id) REFERENCES urls(id) ON DELETE CASCADE
);


CREATE TABLE tags
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE certificates
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    price NUMERIC(6, 2) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    last_update_date TIMESTAMP NULL
);

CREATE TABLE certificates_tags
(
    certificate_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    FOREIGN KEY (certificate_id) REFERENCES certificates(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

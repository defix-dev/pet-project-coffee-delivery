CREATE TABLE oauth_users (
                             id BIGSERIAL PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             oauth_id VARCHAR(255) NOT NULL,
                             provider VARCHAR(50) NOT NULL,
                             CONSTRAINT fk_oauth_user FOREIGN KEY (user_id) REFERENCES users(id),
                             CONSTRAINT unique_oauth UNIQUE (oauth_id, provider)
);

CREATE TABLE product_sell_requests (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL,
                                  price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
                                  sender_id INT REFERENCES users(id) ON DELETE CASCADE,
                                  status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_update_requests (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
                                       product_id INT REFERENCES products(id) ON DELETE CASCADE,
                                       sender_id INT REFERENCES users(id) ON DELETE CASCADE,
                                       status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

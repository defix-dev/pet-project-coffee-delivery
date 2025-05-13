-- Users table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Roles table
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- Many-to-many between users and roles
CREATE TABLE users_roles (
                             user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                             PRIMARY KEY (user_id, role_id)
);

-- Products table
CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          owner_id INT REFERENCES users(id) ON DELETE SET NULL,
                          price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reviews table
CREATE TABLE reviews (
                         id SERIAL PRIMARY KEY,
                         sender_id INT REFERENCES users(id) ON DELETE CASCADE,
                         product_id INT REFERENCES products(id) ON DELETE CASCADE,
                         text TEXT NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Baskets table with quantity and added_at
CREATE TABLE baskets (
                         id SERIAL PRIMARY KEY,
                         user_id INT REFERENCES users(id) ON DELETE CASCADE,
                         product_id INT REFERENCES products(id) ON DELETE CASCADE,
                         quantity INT NOT NULL CHECK (quantity > 0),
                         added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         UNIQUE (user_id, product_id)
);


DROP TABLE product_sell_requests;
DROP TABLE product_update_requests;

CREATE TABLE product_requests (
                                  id INT PRIMARY KEY,
                                  status VARCHAR(50),
                                  type VARCHAR(50),
                                  submitter_id BIGINT,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_sell_requests (
                                       request_id INT PRIMARY KEY NOT NULL,
                                       name VARCHAR(255) NOT NULL,
                                       price DECIMAL(10, 2) NOT NULL,
                                       FOREIGN KEY (request_id) REFERENCES product_requests(id)
);

CREATE TABLE product_update_requests (
                                         request_id INT PRIMARY KEY NOT NULL,
                                         product_id INT NOT NULL,
                                         name VARCHAR(255),
                                         price DECIMAL(10, 2),
                                         FOREIGN KEY (request_id) REFERENCES product_requests(id),
                                         FOREIGN KEY (product_id) REFERENCES products(id)
);



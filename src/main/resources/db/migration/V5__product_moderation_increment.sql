CREATE SEQUENCE product_requests_id_seq;
ALTER TABLE product_requests ALTER COLUMN id SET DATA TYPE INT;
ALTER TABLE product_requests ALTER COLUMN id SET DEFAULT nextval('product_requests_id_seq');

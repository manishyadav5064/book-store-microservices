-- Create sequence for product IDs
CREATE SEQUENCE IF NOT EXISTS product_id_seq
    INCREMENT BY 1
    MINVALUE 1
    START WITH 1
    CACHE 1;

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('product_id_seq'),

    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,

    description VARCHAR(500),
    image_url VARCHAR(255),

    price NUMERIC(19, 2) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Automatically update 'updated_at' column on update
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_products_updated_at ON products;

CREATE TRIGGER update_products_updated_at
BEFORE UPDATE ON products
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

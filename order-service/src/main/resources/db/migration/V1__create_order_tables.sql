-- ================================
-- SEQUENCES
-- ================================
CREATE SEQUENCE order_id_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE order_item_id_sequence START WITH 1 INCREMENT BY 1;

-- ================================
-- ORDERS TABLE
-- ================================
CREATE TABLE orders (
    id BIGINT PRIMARY KEY DEFAULT nextval('order_id_sequence'),

    order_number VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,

    -- Embedded Customer fields
    customer_name VARCHAR(255),
    customer_email VARCHAR(255),
    customer_phone VARCHAR(255),

    -- Embedded Delivery Address fields
    delivery_address_line1 VARCHAR(255),
    delivery_address_line2 VARCHAR(255),
    delivery_address_city VARCHAR(255),
    delivery_address_state VARCHAR(255),
    delivery_address_zipcode VARCHAR(255),
    delivery_address_country VARCHAR(255),

    status VARCHAR(255),

    comments VARCHAR(500),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- ================================
-- ORDER ITEMS TABLE
-- ================================
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY DEFAULT nextval('order_item_id_sequence'),

    code VARCHAR(100) NOT NULL,
    name VARCHAR(250),
    price NUMERIC(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,

    order_id BIGINT NOT NULL,
    CONSTRAINT fk_order
        FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Index for FK column (as per @Index annotation)
CREATE INDEX idx_order_id ON order_items(order_id);

-- Create sequence for ID generation
CREATE SEQUENCE order_event_id_seq
    INCREMENT BY 1
    START WITH 1
    NO CYCLE;

-- Create table
CREATE TABLE order_events (
    id BIGINT PRIMARY KEY DEFAULT nextval('order_event_id_seq'),
    order_number VARCHAR(255) NOT NULL,
    event_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX idx_order_events_order_number
    ON order_events (order_number);

CREATE INDEX idx_order_events_event_id
    ON order_events (event_id);

CREATE TABLE orders (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   total INTEGER NOT NULL,
   table_id BIGINT,
   payment_id BIGINT,
   status INTEGER,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_orders PRIMARY KEY (id)
);

ALTER TABLE orders ADD CONSTRAINT FK_ORDERS_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payments (id);

ALTER TABLE orders ADD CONSTRAINT FK_ORDERS_ON_TABLE FOREIGN KEY (table_id) REFERENCES tables (id);
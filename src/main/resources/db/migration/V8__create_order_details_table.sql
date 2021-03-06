CREATE TABLE order_details (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   order_id BIGINT,
   product_id BIGINT,
   price INTEGER,
   quantity INTEGER,
   subtotal INTEGER,
   image VARCHAR(255),
   created_at TIMESTAMP WITHOUT TIME ZONE,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_order_details PRIMARY KEY (id)
);

ALTER TABLE order_details ADD CONSTRAINT FK_ORDER_DETAILS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_details ADD CONSTRAINT FK_ORDER_DETAILS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);
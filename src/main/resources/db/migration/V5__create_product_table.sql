CREATE TABLE products (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255) NOT NULL,
   description TEXT,
   volume INTEGER,
   weight INTEGER,
   price INTEGER NOT NULL,
   picture VARCHAR(255),
   deleted BOOLEAN DEFAULT FALSE,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_products PRIMARY KEY (id)
);
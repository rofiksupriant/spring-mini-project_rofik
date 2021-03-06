CREATE TABLE users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(50) NOT NULL,
   username VARCHAR(25) NOT NULL,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(255),
   role INTEGER,
   active BOOLEAN DEFAULT TRUE NOT NULL,
   created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users ADD CONSTRAINT uc_users_username UNIQUE (username);

INSERT INTO users(name, username, password, role) VALUES
('Administrator', 'admin', '$2a$12$6tCUFEczTC/O0x9dMGkV3unOAgutEuk.uXqbcTL9bllOJrkzPYMYS', 0),
('Employee', 'employee', '$2a$12$6tCUFEczTC/O0x9dMGkV3unOAgutEuk.uXqbcTL9bllOJrkzPYMYS', 1);
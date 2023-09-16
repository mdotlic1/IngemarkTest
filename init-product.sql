CREATE TABLE Products (
                         id SERIAL PRIMARY KEY,
                         code CHAR(10) UNIQUE NOT NULL,
                         name TEXT NOT NULL,
                         price_eur DECIMAL(10, 2) CHECK (price_eur >= 0),
                         price_usd DECIMAL(10, 2) CHECK (price_usd >= 0),
                         description TEXT,
                         is_available BOOLEAN NOT NULL
);

CREATE TABLE Users (
                       id SERIAL PRIMARY KEY,
                       username TEXT UNIQUE NOT NULL,
                       password TEXT NOT NULL,
                       roles TEXT NOT NULL
);
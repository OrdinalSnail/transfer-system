CREATE TABLE IF NOT EXISTS account(
    id SERIAL PRIMARY KEY,
    account_number varchar(12) NOT NULL UNIQUE
);
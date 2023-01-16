CREATE TABLE IF NOT EXISTS currency_amount (
    id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL,
    currency varchar(3) NOT NULL,
    amount INTEGER NOT NULL
);

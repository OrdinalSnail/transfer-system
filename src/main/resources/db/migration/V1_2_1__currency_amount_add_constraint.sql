ALTER TABLE currency_amount
ADD CONSTRAINT CURRENCY_AMOUNT_ACCOUNT_ID_ACCOUNT_ID_FK
FOREIGN KEY (account_id) REFERENCES account (id);
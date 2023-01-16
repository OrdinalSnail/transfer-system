INSERT INTO account(id, account_number) VALUES (1, '000142006678');
INSERT INTO account(id, account_number) VALUES (2,'123123123213');
INSERT INTO account(id, account_number) VALUES (3,'908349058349');

SELECT setval('account_id_seq', 4);


INSERT INTO currency_amount(id, account_id, currency, amount) VALUES (1, 1, 'SEK', 1500);
INSERT INTO currency_amount(id, account_id, currency, amount) VALUES (2, 1, 'PLN', 700);
INSERT INTO currency_amount(id, account_id, currency, amount) VALUES (3, 1, 'USD', 10);

INSERT INTO currency_amount(id, account_id, currency, amount) VALUES (4, 2, 'SEK', 80);
INSERT INTO currency_amount(id, account_id, currency, amount) VALUES (5, 2, 'PLN', 200);

INSERT INTO currency_amount(id, account_id, currency, amount) VALUES (6, 3, 'SEK', 8000);
INSERT INTO currency_amount(id, account_id, currency, amount) VALUES (7, 3, 'USD', 25);

SELECT setval('currency_amount_id_seq', 8);
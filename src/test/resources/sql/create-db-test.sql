CREATE TABLE links (id INTEGER NOT NULL AUTO_INCREMENT, clicks INTEGER, fullUrl VARCHAR(555), shortUrl VARCHAR(10));

INSERT INTO links (fullUrl, shortURL, clicks) VALUES ('https://google.com', '12345aS', 10);
INSERT INTO links (fullUrl, shortURL) VALUES ('https://yandex.com', '54321Sa');
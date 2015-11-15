CREATE TABLE Links (id INTEGER NOT NULL AUTO_INCREMENT, clicks INTEGER, fullUrl VARCHAR(255), shortUrl VARCHAR(255));

INSERT INTO Links (fullUrl, shortUrl) VALUES ('https://google.com', '12345aS');
INSERT INTO Links (fullUrl, shortUrl) VALUES ('https://yandex.com', '54321Sa');
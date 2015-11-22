CREATE TABLE Links (id INTEGER NOT NULL AUTO_INCREMENT, clicks INTEGER, fullURL VARCHAR(555), shortURL VARCHAR(10));

INSERT INTO Links (fullURL, shortURL, clicks) VALUES ('https://google.com', '12345aS', 10);
INSERT INTO Links (fullURL, shortURL) VALUES ('https://yandex.com', '54321Sa');
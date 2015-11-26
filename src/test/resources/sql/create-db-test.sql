CREATE TABLE links (id INTEGER NOT NULL AUTO_INCREMENT, clicks INTEGER, fullUrl VARCHAR(555), shortUrl VARCHAR(10), title VARCHAR(255));

INSERT INTO links (fullUrl, shortURL, title, clicks) VALUES ('https://google.com', '12345aS', 'Google', 10);
INSERT INTO links (fullUrl, shortURL, title, clicks) VALUES ('https://yandex.com', '54321Sa', 'Яндекс', 0);
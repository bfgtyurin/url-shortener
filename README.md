You can check deployed app on heroku [https://nebz.herokuapp.com](https://nebz.herokuapp.com)

##### Prerequisites
* JDK 8 or later
* Maven 3.0+
* Node.js for npm
* PostgreSQL

##### Clone
```
git clone https://github.com/<your-username>/url-shortener.git
```
##### Build
```
npm install
mvn package
```

#### Prepare database
Install PostgreSQL.
Create database and create links table:

```
CREATE TABLE links (id INTEGER NOT NULL AUTO_INCREMENT, clicks INTEGER, fullUrl VARCHAR(555), shortUrl VARCHAR(10), title VARCHAR(255));
```

Then set the `DATABASE_URL` environment variable (using the correct values).
The DATABASE_URL follows this naming convention:

```
[database type]://[username]:[password]@[host]:[port]/[database name]
```

* Ex. on Linux/Mac:

        export DATABASE_URL=postgres://foo:foo@localhost:5432/<your-database-name>

* Ex. on Windows:

        set DATABASE_URL=postgres://foo:foo@localhost:5432/<your-database-name>

##### Run
Unix
```
sh target/bin/webapp
```
Windows
```
target/bin/webapp.bat
```

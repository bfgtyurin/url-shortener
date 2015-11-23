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
Create database and create Links table:

```
CREATE TABLE links (id SERIAL PRIMARY KEY NOT NULL, clicks INTEGER, fullURL VARCHAR(555), shortURL VARCHAR(10));
```

Then set the `DATABASE_URL` environment variable (using the correct values):

* On Linux/Mac:

        export DATABASE_URL=postgres://foo:foo@localhost/hellodb

* On Windows:

        set DATABASE_URL=postgres://foo:foo@localhost/hellodb

##### Run
Unix
```
sh target/bin/webapp
```
Windows
```
target/bin/webapp.bat
```
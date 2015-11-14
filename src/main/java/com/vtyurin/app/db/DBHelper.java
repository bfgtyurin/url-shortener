package com.vtyurin.app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {

    private static final String createTable = "CREATE TABLE Links (id INTEGER NOT NULL, fullUrl varchar(255), shortUrl varchar(255))";

    Connection connection;

    public DBHelper() {
        init();
    }

    public static DBHelper getInstance() {
        return new DBHelper();
    }

    private void init() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Throwable firstException = null;
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:mydatabase", "SA", "");
            connection.createStatement().execute(createTable);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Links VALUES (fullUrl='hellodb')");
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            firstException = e;
            throw new RuntimeException("Smth bad happened", e);
        } finally {
            try {
                if(firstException != null) {
                    connection.rollback();
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                if(firstException == null){
                    throw new RuntimeException("Smth bad happened", e);
                }
            }
        }

    }

    public Connection getConnection() {

        return connection;
    }
}

package com.gtmpricingengine.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private final String url;
    public DatabaseManager() {
        this("jdbc:h2:mem:gtm;DB_CLOSE_DELAY=-1");
    }

    public DatabaseManager(String url) {
        this.url = url;
    }

    public Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(url);
    }

    public void initializeDatabase() {

        String sql = """
                CREATE TABLE IF NOT EXISTS products (
                    sku VARCHAR(50) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    rrp DOUBLE NOT NULL,
                    cost DOUBLE NOT NULL
                )
                """;

        try (
                Connection connection = getConnection();
                Statement statement =
                        connection.createStatement()
        ) {

            statement.execute(sql);

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error initializing database",
                    e
            );
        }
    }
}
package com.thogakade.database;

import java.sql.*;

public class DatabaseManager {
    private static final String URL_WITHOUT_DB = "jdbc:mysql://localhost:3306/";
    private static final String URL_WITH_DB = "jdbc:mysql://localhost:3306/thogakade";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL_WITH_DB, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL_WITHOUT_DB, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS thogakade");
            getConnection().createStatement().execute("USE thogakade");

            // Create customers table
            String createCustomersTable =
                    "CREATE TABLE IF NOT EXISTS customers ("
                            + "id VARCHAR(10) PRIMARY KEY, "
                            + "title VARCHAR(10) NOT NULL, "
                            + "name VARCHAR(100) NOT NULL, "
                            + "dob DATE NOT NULL, "
                            + "salary DECIMAL(10,2) NOT NULL, "
                            + "address VARCHAR(255) NOT NULL, "
                            + "city VARCHAR(50) NOT NULL, "
                            + "province VARCHAR(50) NOT NULL, "
                            + "postal_code VARCHAR(10) NOT NULL"
                            + ")";
            getConnection().createStatement().execute(createCustomersTable);

            // Create items table
            String createItemsTable =
                    "CREATE TABLE IF NOT EXISTS items ("
                            + "code VARCHAR(10) PRIMARY KEY, "
                            + "description VARCHAR(255) NOT NULL, "
                            + "pack_size VARCHAR(50) NOT NULL, "
                            + "unit_price DECIMAL(10,2) NOT NULL, "
                            + "qty_on_hand INT NOT NULL"
                            + ")";
            getConnection().createStatement().execute(createItemsTable);

            // Create orders table
            String createOrdersTable =
                    "CREATE TABLE IF NOT EXISTS orders ("
                            + "order_id VARCHAR(10) PRIMARY KEY, "
                            + "order_date DATE NOT NULL, "
                            + "customer_id VARCHAR(10) NOT NULL, "
                            + "total DECIMAL(10,2) NOT NULL, "
                            + "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE"
                            + ")";
            getConnection().createStatement().execute(createOrdersTable);

            // Create order_details table
            String createOrderDetailsTable =
                    "CREATE TABLE IF NOT EXISTS order_details ("
                            + "order_id VARCHAR(10) NOT NULL, "
                            + "item_code VARCHAR(10) NOT NULL, "
                            + "qty INT NOT NULL, "
                            + "unit_price DECIMAL(10,2) NOT NULL, "
                            + "discount DECIMAL(5,2) DEFAULT 0, "
                            + "total DECIMAL(10,2) NOT NULL, "
                            + "PRIMARY KEY (order_id, item_code), "
                            + "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE, "
                            + "FOREIGN KEY (item_code) REFERENCES items(code) ON DELETE CASCADE"
                            + ")";
            getConnection().createStatement().execute(createOrderDetailsTable);

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
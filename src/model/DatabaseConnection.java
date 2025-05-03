// File: model/DatabaseConnection.java
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gym_attendance";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private static Connection connection = null;

    // Singleton pattern to ensure only one connection instance
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                // Set auto-commit to true
                connection.setAutoCommit(true);
                System.out.println("Database connection established successfully");
            } catch (SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed successfully");
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    // Add a method to check connection status
    public static boolean isConnectionValid() {
        if (connection == null) {
            return false;
        }
        try {
            return !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}

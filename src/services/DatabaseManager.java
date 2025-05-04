package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseManager {

    public DatabaseManager() {
        connect();
    }
    public Connection connection;
    private void connect() {
        try {
            String DB_URL = "jdbc:mysql://localhost:3306/my_db";
            String DB_USER = "root";
            String DB_PASSWORD = "Mysql@2908";
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established");

            // Create admins table if it doesn't exist
        } catch (SQLException e) {
            System.err.println("Failed to connect to database");
            e.printStackTrace();
        }
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection");
            e.printStackTrace();
        }
    }

}

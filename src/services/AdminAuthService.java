package services;

import models.Admin;

import java.sql.*;
import java.util.Optional;

public class AdminAuthService {
    private Connection connection;

    public AdminAuthService(DatabaseManager databaseManager) {
        createAdminsTableIfNotExists();
    }


    private void createAdminsTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS admins (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(100) NOT NULL UNIQUE, " +
                            "password VARCHAR(100) NOT NULL, " +
                            "phone VARCHAR(20) NOT NULL, " +
                            "department VARCHAR(50) NOT NULL, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Admins table verified");
        } catch (SQLException e) {
            System.err.println("Error creating admins table");
            e.printStackTrace();
        }
    }

    public Optional<Admin> loginAdmin(String email, String password) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                createAdminsTableIfNotExists();
            }

            // Query to validate admin credentials using email
            String query = "SELECT * FROM admins WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create Admin object from result set
                Admin admin = new Admin(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("department")
                );

                resultSet.close();
                statement.close();
                return Optional.of(admin);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Authentication failed due to database error");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean registerAdmin(String name, String email, String password, String phone) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                createAdminsTableIfNotExists();
            }

            // Check if email already exists
            String checkQuery = "SELECT * FROM admins WHERE email = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, email);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Email already exists
                resultSet.close();
                checkStatement.close();
                return false;
            }

            resultSet.close();
            checkStatement.close();

            // Insert new admin
            String insertQuery = "INSERT INTO admins (name, email, password, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, name);
            insertStatement.setString(2, email);
            insertStatement.setString(3, password);
            insertStatement.setString(4, phone);

            int rowsAffected = insertStatement.executeUpdate();
            insertStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Registration failed due to database error");
            e.printStackTrace();
            return false;
        }
    }

}
package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import models.Book;

public class DatabaseManager {

    public DatabaseManager() {
        createDBIfNotExists();
        connect();
        createBooksTableIfNotExists();
    }

    public Connection connection;

    private void connect() {
        try {
            String DB_URL = "jdbc:mysql://localhost:3306/my_db";
            String DB_USER = "root";
            String DB_PASSWORD = "12345678";
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established");

        } catch (SQLException e) {
            System.err.println("Failed to connect to database");
            e.printStackTrace();
        }
    }

    private void createDBIfNotExists() {
        Connection tempConnection = null;
        Statement statement = null;

        try {
            String SERVER_URL = "jdbc:mysql://localhost:3306/";
            String DB_USER = "root";
            String DB_PASSWORD = "12345678";
            String DB_NAME = "my_db";

            tempConnection = DriverManager.getConnection(SERVER_URL, DB_USER, DB_PASSWORD);
            statement = tempConnection.createStatement();

            String createDBSQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(createDBSQL);
            System.out.println("Database checked/created successfully");

        } catch (SQLException e) {
            System.err.println("Failed to create database");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (tempConnection != null && !tempConnection.isClosed()) {
                    tempConnection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing temporary connection");
                e.printStackTrace();
            }
        }
    }

    public void createBooksTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "description TEXT," +
                    "image_url VARCHAR(255)," +
                    "quantity INT NOT NULL," +
                    "author VARCHAR(255) NOT NULL," +
                    "category VARCHAR(100)," +
                    "publication_year VARCHAR(4)," +
                    "available BOOLEAN NOT NULL" +
                    ")";

            statement.executeUpdate(createTableSQL);
            System.out.println("Books table checked/created successfully");

            java.sql.ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM books");
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                addSampleBooks();
                System.out.println("Sample books added to the books table");
            }

        } catch (SQLException e) {
            System.err.println("Error creating books table");
            e.printStackTrace();
        }
    }

    private void addSampleBooks() throws SQLException {
        String insertSQL = "INSERT INTO books (name, description, image_url, quantity, author, category) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        // Sample book 1
        preparedStatement.setString(1, "To Kill a Mockingbird");
        preparedStatement.setString(2, "A novel about racial injustice and moral growth in the American South.");
        preparedStatement.setString(3, "mockingbird.jpg");
        preparedStatement.setInt(4, 10);
        preparedStatement.setString(5, "Harper Lee");
        preparedStatement.setString(6, "Fiction");
        preparedStatement.executeUpdate();

        // Sample book 2
        preparedStatement.setString(1, "1984");
        preparedStatement.setString(2, "A dystopian novel about totalitarianism and mass surveillance.");
        preparedStatement.setString(3, "1984.jpg");
        preparedStatement.setInt(4, 7);
        preparedStatement.setString(5, "George Orwell");
        preparedStatement.setString(6, "Science Fiction");
        preparedStatement.executeUpdate();

        // Sample book 3
        preparedStatement.setString(1, "The Great Gatsby");
        preparedStatement.setString(2, "A novel depicting the American Dream and the decadence of the Jazz Age.");
        preparedStatement.setString(3, "gatsby.jpg");
        preparedStatement.setInt(4, 5);
        preparedStatement.setString(5, "F. Scott Fitzgerald");
        preparedStatement.setString(6, "Classic");
        preparedStatement.executeUpdate();

        // Sample book 4
        preparedStatement.setString(1, "Pride and Prejudice");
        preparedStatement.setString(2, "A romantic novel of manners that follows the character development of Elizabeth Bennet.");
        preparedStatement.setString(3, "pride.jpg");
        preparedStatement.setInt(4, 8);
        preparedStatement.setString(5, "Jane Austen");
        preparedStatement.setString(6, "Romance");
        preparedStatement.executeUpdate();

        // Sample book 5
        preparedStatement.setString(1, "The Catcher in the Rye");
        preparedStatement.setString(2, "A novel about teenage angst and alienation in post-war America.");
        preparedStatement.setString(3, "catcher.jpg");
        preparedStatement.setInt(4, 6);
        preparedStatement.setString(5, "J.D. Salinger");
        preparedStatement.setString(6, "Coming-of-age");
        preparedStatement.executeUpdate();

        preparedStatement.close();
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
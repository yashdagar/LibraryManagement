package services;

import models.Book;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserAuthService {
    private Connection connection;

    public UserAuthService(DatabaseManager databaseManager) {
        connection=databaseManager.connection;
        createUsersTableIfNotExists();
        createBooksTableIfNotExists();
        createIssueRequestsTableIfNotExists();;
    }


    private void createUsersTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(100) NOT NULL UNIQUE, " +
                            "password VARCHAR(100) NOT NULL, " +
                            "program VARCHAR(100) NOT NULL, " +
                            "sapid INT NOT NULL, " +
                            "yearOfStudy INT NOT NULL, " +
                            "borrowedBooks INT NOT NULL DEFAULT 0, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Users table verified");
        } catch (SQLException e) {
            System.err.println("Error creating users table");
            e.printStackTrace();
        }
    }

    private void createBooksTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS books (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(200) NOT NULL, " +
                            "description TEXT, " +
                            "image_url VARCHAR(255), " +
                            "quantity INT NOT NULL DEFAULT 0, " +
                            "author VARCHAR(200) NOT NULL, " +
                            "category VARCHAR(100) NOT NULL, " +
                            "publication_year VARCHAR(10), " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Books table verified");
        } catch (SQLException e) {
            System.err.println("Error creating books table");
            e.printStackTrace();
        }
    }

    private void createIssueRequestsTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS issue_requests (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "book_id INT NOT NULL, " +
                            "issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "due_date TIMESTAMP, " +
                            "returned BOOLEAN DEFAULT FALSE, " +
                            "FOREIGN KEY (book_id) REFERENCES books(id), " +
                            "FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Issue Requests table verified");
        } catch (SQLException e) {
            System.err.println("Error creating issue requests table");
            e.printStackTrace();
        }
    }

    public Optional<User> loginUser(String email, String password) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                createUsersTableIfNotExists();
                createBooksTableIfNotExists();
                createIssueRequestsTableIfNotExists();;
            }

            // Query to validate user credentials using email instead of username
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            return Optional.of(User.getFromResultSet(resultSet).get());
        } catch (SQLException e) {
            System.err.println("Authentication failed due to database error");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean registerUser(String name, String email, String password, String program, String sapID, String yearOfStudy) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                createUsersTableIfNotExists();
                createBooksTableIfNotExists();
                createIssueRequestsTableIfNotExists();;
            }

            // Check if email already exists
            String checkQuery = "SELECT * FROM users WHERE email = ?";
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

            // Insert new user
            String insertQuery = "INSERT INTO users (name, email, password, program, sapID, yearOfStudy) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, name);
            insertStatement.setString(2, email);
            insertStatement.setString(3, password);
            insertStatement.setString(4, program);
            insertStatement.setString(5, sapID);
            insertStatement.setString(6, yearOfStudy);

            int rowsAffected = insertStatement.executeUpdate();
            insertStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Registration failed due to database error");
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                createUsersTableIfNotExists();
                createBooksTableIfNotExists();
                createIssueRequestsTableIfNotExists();;
            }

            String query = "SELECT * FROM books";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("image_url"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("author"),
                        resultSet.getString("category"),
                        resultSet.getString("publication_year")
                );
                books.add(book);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching books");
            e.printStackTrace();
        }
        return books;
    }

    public boolean borrowBook(int bookId, int userId) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                createUsersTableIfNotExists();
                createBooksTableIfNotExists();
                createIssueRequestsTableIfNotExists();;
            }

            // First check if the book is available
            String checkBookQuery = "SELECT quantity FROM books WHERE id = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkBookQuery);
            checkStatement.setInt(1, bookId);
            ResultSet bookResult = checkStatement.executeQuery();

            if (!bookResult.next() || bookResult.getInt("quantity") <= 0) {
                // Book not found or quantity is 0
                bookResult.close();
                checkStatement.close();
                return false;
            }

            bookResult.close();
            checkStatement.close();

            // Start transaction
            connection.setAutoCommit(false);

            // Update book quantity
            String updateBookQuery = "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
            PreparedStatement updateStatement = connection.prepareStatement(updateBookQuery);
            updateStatement.setInt(1, bookId);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected == 0) {
                // No rows affected - book might not be available
                connection.rollback();
                connection.setAutoCommit(true);
                updateStatement.close();
                return false;
            }

            updateStatement.close();

            // Create issue request - due date set to 14 days from now
            String insertRequestQuery = "INSERT INTO issue_requests (user_id, book_id, due_date) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 14 DAY))";
            PreparedStatement insertStatement = connection.prepareStatement(insertRequestQuery);
            insertStatement.setInt(1, userId);
            insertStatement.setInt(2, bookId);
            rowsAffected = insertStatement.executeUpdate();

            // Update user's borrowed books count
            String updateUserQuery = "UPDATE users SET borrowedBooks = borrowedBooks + 1 WHERE id = ?";
            PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery);
            updateUserStatement.setInt(1, userId);
            updateUserStatement.executeUpdate();
            updateUserStatement.close();

            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);
            insertStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error borrowing book");
            e.printStackTrace();
            try {
                // Rollback transaction on error
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

}
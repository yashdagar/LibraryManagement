package services;

import models.Librarian;
import models.Book;
import models.IssueRequest;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.Optional;

public class LibrarianAuthService {
    private Connection connection;

    public LibrarianAuthService() {
        connect();
    }

    private void connect() {
        try {
            String DB_URL = "jdbc:mysql://localhost:3306/my_db";
            String DB_USER = "root";
            String DB_PASSWORD = "Mysql@2908";
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established");

            // Create tables if they don't exist
            createLibrariansTableIfNotExists();
            createBooksTableIfNotExists();
            createIssueRequestsTableIfNotExists();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database");
            e.printStackTrace();
        }
    }

    private void createIssueRequestsTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS issue_requests (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id VARCHAR(50) NOT NULL, " +
                            "book_id VARCHAR(50) NOT NULL, " +
                            "state VARCHAR(20) NOT NULL, " +  // PENDING, APPROVED, REJECTED, RETURNED
                            "issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "due_date TIMESTAMP, " +
                            "return_date TIMESTAMP)";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Issue Requests table verified");
        } catch (SQLException e) {
            System.err.println("Error creating issue requests table");
            e.printStackTrace();
        }
    }

    private void createLibrariansTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS librarians (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(100) NOT NULL UNIQUE, " +
                            "password VARCHAR(100) NOT NULL, " +
                            "phone VARCHAR(20) NOT NULL, " +
                            "role VARCHAR(50) NOT NULL, " +
                            "years_experience INT NOT NULL, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Librarians table verified");
        } catch (SQLException e) {
            System.err.println("Error creating librarians table");
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

    public Optional<Librarian> loginLibrarian(String email, String password) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                connect();
            }

            // Query to validate librarian credentials using email
            String query = "SELECT * FROM librarians WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create Librarian object from result set
                Librarian librarian = new Librarian(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );

                resultSet.close();
                statement.close();
                return Optional.of(librarian);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Authentication failed due to database error");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean registerLibrarian(String name, String email, String password, String phone) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                connect();
            }

            // Check if email already exists
            String checkQuery = "SELECT * FROM librarians WHERE email = ?";
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

            // Insert new librarian
            String insertQuery = "INSERT INTO librarians (name, email, password, phone) VALUES (?, ?, ?, ?)";
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

    public boolean addBook(Book book) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                connect();
            }

            // Insert new book
            String insertQuery = "INSERT INTO books (name, description, image_url, quantity, author, category, publication_year) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, book.getName());
            insertStatement.setString(2, book.getDescription());
            insertStatement.setString(3, book.getImageUrl());
            insertStatement.setInt(4, book.getQuantity());
            insertStatement.setString(5, book.getAuthor());
            insertStatement.setString(6, book.getCategory());
            insertStatement.setString(7, book.getPublicationYear());

            int rowsAffected = insertStatement.executeUpdate();
            insertStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Failed to add book");
            e.printStackTrace();
            return false;
        }
    }

    public List<IssueRequest> getAllIssueRequests() {
        List<IssueRequest> issueRequests = new ArrayList<>();
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                connect();
            }

            String query = "SELECT * FROM issue_requests ORDER BY issue_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String bookId = resultSet.getString("book_id");
                String userId = resultSet.getString("user_id");
                String stateStr = resultSet.getString("state");

                // Convert string state to enum
                IssueRequest.State state;
                try {
                    state = IssueRequest.State.valueOf(stateStr);
                } catch (IllegalArgumentException e) {
                    // Default to PENDING if state is invalid
                    state = IssueRequest.State.PENDING;
                }

                IssueRequest request = new IssueRequest(bookId, userId, state);
                issueRequests.add(request);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching issue requests");
            e.printStackTrace();
        }
        return issueRequests;
    }

    public boolean updateIssueRequestState(int requestId, IssueRequest.State newState) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                connect();
            }

            // Update issue request state
            String updateQuery = "UPDATE issue_requests SET state = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, newState.toString());
            updateStatement.setInt(2, requestId);

            int rowsAffected = updateStatement.executeUpdate();
            updateStatement.close();

            // If new state is APPROVED, update book quantity
            if (newState == IssueRequest.State.APPROVED) {
                // First get the book ID for this request
                String getBookIdQuery = "SELECT book_id FROM issue_requests WHERE id = ?";
                PreparedStatement getBookIdStatement = connection.prepareStatement(getBookIdQuery);
                getBookIdStatement.setInt(1, requestId);
                ResultSet resultSet = getBookIdStatement.executeQuery();

                if (resultSet.next()) {
                    String bookId = resultSet.getString("book_id");

                    // Decrease the book quantity by 1
                    String updateBookQuery = "UPDATE books SET quantity = quantity - 1 WHERE id = ?";
                    PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
                    updateBookStatement.setString(1, bookId);
                    updateBookStatement.executeUpdate();
                    updateBookStatement.close();
                }

                resultSet.close();
                getBookIdStatement.close();
            }

            // If new state is RETURNED, update book quantity
            if (newState == IssueRequest.State.RETURNED) {
                // First get the book ID for this request
                String getBookIdQuery = "SELECT book_id FROM issue_requests WHERE id = ?";
                PreparedStatement getBookIdStatement = connection.prepareStatement(getBookIdQuery);
                getBookIdStatement.setInt(1, requestId);
                ResultSet resultSet = getBookIdStatement.executeQuery();

                if (resultSet.next()) {
                    String bookId = resultSet.getString("book_id");

                    // Increase the book quantity by 1
                    String updateBookQuery = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
                    PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
                    updateBookStatement.setString(1, bookId);
                    updateBookStatement.executeUpdate();
                    updateBookStatement.close();

                    // Also update the return_date to current timestamp
                    String updateReturnDateQuery = "UPDATE issue_requests SET return_date = CURRENT_TIMESTAMP WHERE id = ?";
                    PreparedStatement updateReturnDateStatement = connection.prepareStatement(updateReturnDateQuery);
                    updateReturnDateStatement.setInt(1, requestId);
                    updateReturnDateStatement.executeUpdate();
                    updateReturnDateStatement.close();
                }

                resultSet.close();
                getBookIdStatement.close();
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update issue request state");
            e.printStackTrace();
            return false;
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
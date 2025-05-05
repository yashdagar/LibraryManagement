package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import models.Book;

public class DatabaseManager {
    // Static variable for the singleton instance
    private static DatabaseManager instance;

    // Connection object made private
    private Connection connection;

    // Private constructor to prevent instantiation outside of this class
    private DatabaseManager() {
        createDBIfNotExists();
        connect();
        createBooksTableIfNotExists();
    }

    // Static method to get the singleton instance
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

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
        String insertSQL = "INSERT INTO books (name, description, image_url, quantity, author, category, publication_year, available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        // Sample book 1
        preparedStatement.setString(1, "To Kill a Mockingbird");
        preparedStatement.setString(2, "A novel about racial injustice and moral growth in the American South.");
        preparedStatement.setString(3, "mockingbird.jpg");
        preparedStatement.setInt(4, 10);
        preparedStatement.setString(5, "Harper Lee");
        preparedStatement.setString(6, "Fiction");
        preparedStatement.setString(7, "1960");
        preparedStatement.setBoolean(8, true);
        preparedStatement.executeUpdate();

        // Sample book 2
        preparedStatement.setString(1, "1984");
        preparedStatement.setString(2, "A dystopian novel about totalitarianism and mass surveillance.");
        preparedStatement.setString(3, "1984.jpg");
        preparedStatement.setInt(4, 7);
        preparedStatement.setString(5, "George Orwell");
        preparedStatement.setString(6, "Science Fiction");
        preparedStatement.setString(7, "1949");
        preparedStatement.setBoolean(8, true);
        preparedStatement.executeUpdate();

        // Sample book 3
        preparedStatement.setString(1, "The Great Gatsby");
        preparedStatement.setString(2, "A novel depicting the American Dream and the decadence of the Jazz Age.");
        preparedStatement.setString(3, "gatsby.jpg");
        preparedStatement.setInt(4, 5);
        preparedStatement.setString(5, "F. Scott Fitzgerald");
        preparedStatement.setString(6, "Classic");
        preparedStatement.setString(7, "1925");
        preparedStatement.setBoolean(8, true);
        preparedStatement.executeUpdate();

        // Sample book 4
        preparedStatement.setString(1, "Pride and Prejudice");
        preparedStatement.setString(2, "A romantic novel of manners that follows the character development of Elizabeth Bennet.");
        preparedStatement.setString(3, "pride.jpg");
        preparedStatement.setInt(4, 8);
        preparedStatement.setString(5, "Jane Austen");
        preparedStatement.setString(6, "Romance");
        preparedStatement.setString(7, "1813");
        preparedStatement.setBoolean(8, true);
        preparedStatement.executeUpdate();

        // Sample book 5
        preparedStatement.setString(1, "The Catcher in the Rye");
        preparedStatement.setString(2, "A novel about teenage angst and alienation in post-war America.");
        preparedStatement.setString(3, "catcher.jpg");
        preparedStatement.setInt(4, 6);
        preparedStatement.setString(5, "J.D. Salinger");
        preparedStatement.setString(6, "Coming-of-age");
        preparedStatement.setString(7, "1951");
        preparedStatement.setBoolean(8, true);
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    // Getter for the connection
    public Connection getConnection() {
        return connection;
    }

    /**
     * Retrieves a student by their ID from the database
     * @param studentId The ID of the student to retrieve
     * @return The student if found, null otherwise
     */
    public models.Student getStudentById(int studentId) {
        try {
            String query = "SELECT * FROM students WHERE student_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentId);

            java.sql.ResultSet resultSet = preparedStatement.executeQuery();
            java.util.Optional<models.Student> studentOpt = models.Student.getFromResultSet(resultSet);

            resultSet.close();
            preparedStatement.close();

            return studentOpt.orElse(null);

        } catch (SQLException e) {
            System.err.println("Error retrieving student with ID: " + studentId);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of all issued books with their details
     * @return List of IssuedBook objects that are currently issued
     */
    public java.util.List<models.IssuedBook> getAllIssuedBooks() {
        java.util.List<models.IssuedBook> issuedBooks = new java.util.ArrayList<>();

        try {
            // SQL join query to get issued books with their book details
            String query =
                    "SELECT ib.id as issue_id, ib.student_id, ib.issue_date, ib.due_date, ib.return_date, ib.fine, " +
                            "b.id as book_id, b.name, b.description, b.image_url, b.quantity, b.author, b.category, b.publication_year, b.available " +
                            "FROM issued_books ib " +
                            "JOIN books b ON ib.book_id = b.id " +
                            "WHERE ib.return_date IS NULL";  // Only get currently issued books (not returned)

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Create Book object
                int bookId = resultSet.getInt("book_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String imageUrl = resultSet.getString("image_url");
                int quantity = resultSet.getInt("quantity");
                String author = resultSet.getString("author");
                String category = resultSet.getString("category");
                String publicationYear = resultSet.getString("publication_year");

                models.Book book = new models.Book(
                        bookId, name, description, imageUrl,
                        quantity, author, category, publicationYear
                );

                // Set availability explicitly since we know these books are issued
                book.setAvailable(resultSet.getBoolean("available"));

                // Create IssuedBook object
                int issueId = resultSet.getInt("issue_id");
                int studentId = resultSet.getInt("student_id");
                java.util.Date issueDate = resultSet.getTimestamp("issue_date");
                java.util.Date dueDate = resultSet.getTimestamp("due_date");

                models.IssuedBook issuedBook = new models.IssuedBook(
                        issueId, book, studentId, issueDate, dueDate
                );

                // Set return date and fine if available
                java.sql.Timestamp returnTimestamp = resultSet.getTimestamp("return_date");
                if (returnTimestamp != null) {
                    issuedBook.setReturnDate(new java.util.Date(returnTimestamp.getTime()));
                }

                issuedBook.setFine(resultSet.getDouble("fine"));

                issuedBooks.add(issuedBook);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            System.err.println("Error retrieving issued books");
            e.printStackTrace();
        }

        return issuedBooks;
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
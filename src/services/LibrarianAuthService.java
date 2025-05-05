package services;

import models.Librarian;
import models.LibraryRequest;

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

            // Create librarians table if it doesn't exist
            createLibrariansTableIfNotExists();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database");
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
                Optional<Librarian> librarian =  Librarian.getFromResultSet(resultSet);

                resultSet.close();
                statement.close();
                return Optional.of(librarian.get());
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
    public LibraryRequest getLibraryRequestByIds(String userId, String bookId) {
        try {
            String sql = "SELECT r.*, u.name as member_name, b.title as book_title " +
                    "FROM library_requests r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN books b ON r.book_id = b.book_id " +
                    "WHERE r.user_id = ? AND r.book_id = ? " +
                    "AND (r.status = 'ISSUED' OR r.status = 'OVERDUE') " +
                    "ORDER BY r.request_date DESC LIMIT 1";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, bookId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return LibraryRequest.getFromResultSet(rs).orElse(null);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error getting library request: " + e.getMessage());
            return null;
        }
    }
    public boolean returnBook(int requestId, int librarianId) {
        try {
            // First get the request
            String sql = "SELECT * FROM library_requests WHERE request_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, requestId);

            ResultSet rs = stmt.executeQuery();
            LibraryRequest request = LibraryRequest.getFromResultSet(rs).orElse(null);

            if (request == null) {
                return false;
            }

            // Process the return
            boolean success = request.returnBook(librarianId);
            if (!success) {
                return false;
            }

            // Update the request in the database
            return updateLibraryRequest(request);
        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a book is currently overdue and update its status
     * @param requestId The ID of the library request to check
     * @return True if the book is overdue, false otherwise
     */
    public boolean checkAndUpdateOverdueStatus(int requestId) {
        try {
            // First get the request
            String sql = "SELECT * FROM library_requests WHERE request_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, requestId);

            ResultSet rs = stmt.executeQuery();
            LibraryRequest request = LibraryRequest.getFromResultSet(rs).orElse(null);

            if (request == null) {
                return false;
            }

            // Check if the book is overdue
            if (request.getStatus() == LibraryRequest.State.ISSUED &&
                    request.getDaysOverdue() > 0) {
                // Update status to OVERDUE
                request.setStatus(LibraryRequest.State.OVERDUE);
                updateLibraryRequest(request);
                return true;
            }

            return false;
        } catch (SQLException e) {
            System.out.println("Error checking overdue status: " + e.getMessage());
            return false;
        }
    }
    public boolean updateLibraryRequest(LibraryRequest request) {
        try {
            String sql = "UPDATE library_requests SET " +
                    "status = ?, librarian_id = ?, processed_date = ?, notes = ? " +
                    "WHERE request_id = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, request.getStatus().toString());

            if (request.getLibrarianId() != null) {
                stmt.setInt(2, request.getLibrarianId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            if (request.getProcessedDate() != null) {
                stmt.setTimestamp(3, new java.sql.Timestamp(request.getProcessedDate().getTime()));
            } else {
                stmt.setNull(3, java.sql.Types.TIMESTAMP);
            }

            stmt.setString(4, request.getNotes());
            stmt.setInt(5, request.getRequestId());

            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating library request: " + e.getMessage());
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
package services;

import models.Librarian;
import models.Book;
import models.IssuedBook;
import models.IssueRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LibrarianAuthService {
    private Connection connection;

    public LibrarianAuthService(DatabaseManager databaseManager) {
        connection = databaseManager.connection;
        createLibrariansTableIfNotExists();
        createIssuedBooksTableIfNotExists();
        createIssueRequestsTableIfNotExists();
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

    private void createIssuedBooksTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS issued_books (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "book_id INT NOT NULL, " +
                            "member_id INT NOT NULL, " +
                            "issue_date DATE NOT NULL, " +
                            "due_date DATE NOT NULL, " +
                            "return_date DATE, " +
                            "fine_amount DECIMAL(10,2) DEFAULT 0.00, " +
                            "status VARCHAR(20) NOT NULL, " + // ISSUED, RETURNED, OVERDUE
                            "issued_by INT NOT NULL, " + // Librarian ID
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Issued books table verified");
        } catch (SQLException e) {
            System.err.println("Error creating issued books table");
            e.printStackTrace();
        }
    }

    private void createIssueRequestsTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL =
                    "CREATE TABLE IF NOT EXISTS issue_requests (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "book_id INT NOT NULL, " +
                            "member_id INT NOT NULL, " +
                            "request_date DATE NOT NULL, " +
                            "status VARCHAR(20) NOT NULL, " + // PENDING, APPROVED, REJECTED
                            "processed_by INT, " + // Librarian ID who processed the request
                            "processed_date DATE, " +
                            "notes VARCHAR(255), " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(createTableSQL);
            statement.close();
            System.out.println("Issue requests table verified");
        } catch (SQLException e) {
            System.err.println("Error creating issue requests table");
            e.printStackTrace();
        }
    }

    public Optional<Librarian> loginLibrarian(String email, String password) {
        try {
            // Check if connection is valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                createLibrariansTableIfNotExists();
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
                createLibrariansTableIfNotExists();
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
            String insertQuery = "INSERT INTO librarians (name, email, password, phone, role, years_experience) VALUES (?, ?, ?, ?, 'Librarian', 0)";
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

    // Get all issued books
    public List<IssuedBook> getAllIssuedBooks() {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        try {
            if (connection == null || connection.isClosed()) {
                createIssuedBooksTableIfNotExists();
            }

            String query = "SELECT ib.*, b.title as book_title, b.author as book_author, " +
                    "m.name as member_name, m.email as member_email " +
                    "FROM issued_books ib " +
                    "JOIN books b ON ib.book_id = b.id " +
                    "JOIN members m ON ib.member_id = m.id " +
                    "WHERE ib.status = 'ISSUED' OR ib.status = 'OVERDUE' " +
                    "ORDER BY ib.issue_date DESC";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                IssuedBook issuedBook = new IssuedBook(
                        resultSet.getInt("id"),
                        resultSet.getInt("book_id"),
                        resultSet.getString("book_title"),
                        resultSet.getString("book_author"),
                        resultSet.getInt("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("member_email"),
                        resultSet.getDate("issue_date"),
                        resultSet.getDate("due_date"),
                        resultSet.getString("status")
                );
                issuedBooks.add(issuedBook);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving issued books");
            e.printStackTrace();
        }
        return issuedBooks;
    }

    // Get all issue requests
    public List<IssueRequest> getAllIssueRequests() {
        List<IssueRequest> issueRequests = new ArrayList<>();
        try {
            if (connection == null || connection.isClosed()) {
                createIssueRequestsTableIfNotExists();
            }

            String query = "SELECT ir.*, b.title as book_title, b.author as book_author, " +
                    "m.name as member_name, m.email as member_email " +
                    "FROM issue_requests ir " +
                    "JOIN books b ON ir.book_id = b.id " +
                    "JOIN members m ON ir.member_id = m.id " +
                    "ORDER BY ir.request_date DESC";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                IssueRequest issueRequest = new IssueRequest(
                        resultSet.getInt("id"),
                        resultSet.getInt("book_id"),
                        resultSet.getString("book_title"),
                        resultSet.getString("book_author"),
                        resultSet.getInt("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("member_email"),
                        resultSet.getDate("request_date"),
                        resultSet.getString("status"),
                        resultSet.getString("notes")
                );
                issueRequests.add(issueRequest);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving issue requests");
            e.printStackTrace();
        }
        return issueRequests;
    }

    // Process issue request (approve or reject)
    public boolean processIssueRequest(int requestId, String status, int librarianId, String notes) {
        try {
            if (connection == null || connection.isClosed()) {
                createIssueRequestsTableIfNotExists();
            }

            // Begin transaction
            connection.setAutoCommit(false);

            // Update request status
            String updateQuery = "UPDATE issue_requests SET status = ?, processed_by = ?, " +
                    "processed_date = CURRENT_DATE, notes = ? WHERE id = ?";

            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, status);
            updateStatement.setInt(2, librarianId);
            updateStatement.setString(3, notes);
            updateStatement.setInt(4, requestId);

            int rowsAffected = updateStatement.executeUpdate();
            updateStatement.close();

            // If approved, create an issued book record
            if (status.equals("APPROVED")) {
                // Get the request details
                String requestQuery = "SELECT * FROM issue_requests WHERE id = ?";
                PreparedStatement requestStatement = connection.prepareStatement(requestQuery);
                requestStatement.setInt(1, requestId);
                ResultSet requestResult = requestStatement.executeQuery();

                if (requestResult.next()) {
                    int bookId = requestResult.getInt("book_id");
                    int memberId = requestResult.getInt("member_id");

                    // Create issued book record
                    String issueQuery = "INSERT INTO issued_books (book_id, member_id, issue_date, due_date, status, issued_by) " +
                            "VALUES (?, ?, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY), 'ISSUED', ?)";

                    PreparedStatement issueStatement = connection.prepareStatement(issueQuery);
                    issueStatement.setInt(1, bookId);
                    issueStatement.setInt(2, memberId);
                    issueStatement.setInt(3, librarianId);

                    issueStatement.executeUpdate();
                    issueStatement.close();

                    // Update book availability
                    String bookUpdateQuery = "UPDATE books SET available_copies = available_copies - 1 WHERE id = ?";
                    PreparedStatement bookUpdateStatement = connection.prepareStatement(bookUpdateQuery);
                    bookUpdateStatement.setInt(1, bookId);
                    bookUpdateStatement.executeUpdate();
                    bookUpdateStatement.close();
                }

                requestResult.close();
                requestStatement.close();
            }

            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);

            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error processing issue request");
            e.printStackTrace();
            return false;
        }
    }

    // Mark a book as returned
    public boolean returnBook(int issuedBookId, int librarianId) {
        try {
            if (connection == null || connection.isClosed()) {
                createIssuedBooksTableIfNotExists();
            }

            // Begin transaction
            connection.setAutoCommit(false);

            // Get the issued book details
            String bookQuery = "SELECT book_id, due_date FROM issued_books WHERE id = ?";
            PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
            bookStatement.setInt(1, issuedBookId);
            ResultSet bookResult = bookStatement.executeQuery();

            if (bookResult.next()) {
                int bookId = bookResult.getInt("book_id");
                Date dueDate = bookResult.getDate("due_date");
                Date currentDate = new Date();

                // Calculate fine if book is returned late
                double fineAmount = 0.0;
                if (currentDate.after(dueDate)) {
                    long diff = currentDate.getTime() - dueDate.getTime();
                    long daysLate = diff / (1000 * 60 * 60 * 24);
                    fineAmount = daysLate * 0.50; // $0.50 per day late
                }

                // Update issued book record
                String updateQuery = "UPDATE issued_books SET return_date = CURRENT_DATE, status = 'RETURNED', " +
                        "fine_amount = ? WHERE id = ?";

                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setDouble(1, fineAmount);
                updateStatement.setInt(2, issuedBookId);

                updateStatement.executeUpdate();
                updateStatement.close();

                // Update book availability
                String bookUpdateQuery = "UPDATE books SET available_copies = available_copies + 1 WHERE id = ?";
                PreparedStatement bookUpdateStatement = connection.prepareStatement(bookUpdateQuery);
                bookUpdateStatement.setInt(1, bookId);
                bookUpdateStatement.executeUpdate();
                bookUpdateStatement.close();

                // Commit transaction
                connection.commit();
                connection.setAutoCommit(true);

                return true;
            }

            bookResult.close();
            bookStatement.close();

            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error processing book return");
            e.printStackTrace();
            return false;
        }
    }

    // Add this new method to the LibrarianAuthService class

    public boolean deleteBook(int bookId, Integer quantity, int librarianId) {
        try {
            if (connection == null || connection.isClosed()) {
                // Reconnect if necessary
                // Assuming a reconnection method or handling this elsewhere
            }

            // Begin transaction
            connection.setAutoCommit(false);

            // First check if the book exists and get its current quantity
            String queryBook = "SELECT * FROM books WHERE id = ?";
            PreparedStatement bookStatement = connection.prepareStatement(queryBook);
            bookStatement.setInt(1, bookId);
            ResultSet bookResult = bookStatement.executeQuery();

            if (bookResult.next()) {
                int currentCopies = bookResult.getInt("total_copies");
                int availableCopies = bookResult.getInt("available_copies");
                String bookTitle = bookResult.getString("title");

                // Determine how many copies to delete
                int copiesToDelete = (quantity == null || quantity >= currentCopies) ?
                        currentCopies : quantity;

                // Calculate remaining copies
                int remainingCopies = currentCopies - copiesToDelete;

                // Check if we are trying to delete more copies than are available (some might be issued)
                if (copiesToDelete > availableCopies) {
                    // Some copies are issued and cannot be deleted
                    System.err.println("Cannot delete all requested copies. Some copies are currently issued.");

                    // Option 1: Fail the operation
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;

                    // Option 2: Only delete available copies (uncomment if this is the preferred behavior)
                    // copiesToDelete = availableCopies;
                    // remainingCopies = currentCopies - availableCopies;
                }

                // Now update or delete the book based on remaining copies
                if (remainingCopies <= 0) {
                    // Delete the book completely if no copies will remain
                    String deleteQuery = "DELETE FROM books WHERE id = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, bookId);
                    deleteStatement.executeUpdate();
                    deleteStatement.close();
                } else {
                    // Update the book quantity
                    String updateQuery = "UPDATE books SET total_copies = ?, available_copies = available_copies - ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, remainingCopies);
                    updateStatement.setInt(2, copiesToDelete);
                    updateStatement.setInt(3, bookId);
                    updateStatement.executeUpdate();
                    updateStatement.close();
                }

                // Log the deletion in a book_deletions table (if you want to keep track)
                // This is optional but recommended for audit purposes
                try {
                    // First check if the table exists, create it if not
                    Statement checkTableStatement = connection.createStatement();
                    String createTableSQL =
                            "CREATE TABLE IF NOT EXISTS book_deletions (" +
                                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                    "book_id INT NOT NULL, " +
                                    "book_title VARCHAR(255) NOT NULL, " +
                                    "quantity_deleted INT NOT NULL, " +
                                    "deleted_by INT NOT NULL, " +
                                    "deletion_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                    checkTableStatement.execute(createTableSQL);
                    checkTableStatement.close();

                    // Now log the deletion
                    String logQuery = "INSERT INTO book_deletions (book_id, book_title, quantity_deleted, deleted_by) VALUES (?, ?, ?, ?)";
                    PreparedStatement logStatement = connection.prepareStatement(logQuery);
                    logStatement.setInt(1, bookId);
                    logStatement.setString(2, bookTitle);
                    logStatement.setInt(3, copiesToDelete);
                    logStatement.setInt(4, librarianId);
                    logStatement.executeUpdate();
                    logStatement.close();
                } catch (SQLException e) {
                    // This is just logging, so if it fails, we don't want to fail the whole transaction
                    System.err.println("Failed to log book deletion");
                    e.printStackTrace();
                }

                // Commit transaction
                connection.commit();
                connection.setAutoCommit(true);
                bookResult.close();
                bookStatement.close();

                return true;
            } else {
                // Book not found
                connection.rollback();
                connection.setAutoCommit(true);
                bookResult.close();
                bookStatement.close();
                return false;
            }
        } catch (SQLException e) {
            try {
                // Rollback in case of any error
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error deleting book");
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
package models;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * A combined class for handling book requests and issues in a library system.
 */
public class LibraryRequest {
    // Core fields from requirements
    private int requestId;
    private int bookId;
    private int userId;
    private State status;
    private Integer librarianId; // Nullable field

    // Additional useful fields
    private String reason;
    private Date requestDate;
    private Date processedDate;
    private Date dueDate;
    private String notes;

    // Additional fields for UI display
    private String memberName;
    private String bookTitle;

    // Constants
    public static final double FINE_RATE_PER_DAY = 0.50; // $0.50 per day

    /**
     * Enum representing the possible states of a library request
     */
    public enum State {
        PENDING,
        APPROVED,
        ISSUED,
        OVERDUE,
        RETURNED,
        REJECTED,
        CANCELLED
    }

    /**
     * Full constructor with all fields
     */
    public LibraryRequest(int requestId, int bookId, int userId, State status, Integer librarianId,
                          String reason, Date requestDate, Date processedDate, Date dueDate,
                          String notes, String memberName, String bookTitle) {
        this.requestId = requestId;
        this.bookId = bookId;
        this.userId = userId;
        this.status = status;
        this.librarianId = librarianId;
        this.reason = reason;
        this.requestDate = requestDate;
        this.processedDate = processedDate;
        this.dueDate = dueDate;
        this.notes = notes;
        this.memberName = memberName;
        this.bookTitle = bookTitle;
    }
    // Additional getters and setters for new fields
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    /**
     * Minimal constructor with just the required fields
     */
    public LibraryRequest(int requestId, int bookId, int userId, State status, Integer librarianId) {
        this(requestId, bookId, userId, status, librarianId,
                null, new Date(), null, null, null, null, null);
    }

    /**
     * Create a new request with default values (PENDING status, current date)
     */
    public static LibraryRequest createNewRequest(int bookId, int userId, String reason) {
        return new LibraryRequest(
                0, // requestId will be assigned by the database
                bookId,
                userId,
                State.PENDING,
                null, // no librarian assigned yet
                reason,
                new Date(), // current date
                null, // not processed yet
                null, // no due date yet
                null, // no notes yet
                null, // member name (will be populated from DB)
                null  // book title (will be populated from DB)
        );
    }

    /**
     * Create a list of LibraryRequest objects from a ResultSet
     */
    public static ArrayList<LibraryRequest> getRequestsFromResultSet(ResultSet rs) {
        ArrayList<LibraryRequest> requests = new ArrayList<>();
        try {
            while (rs.next()) {
                requests.add(createFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing library requests: " + e.getMessage());
        }
        return requests;
    }

    /**
     * Create a single LibraryRequest object from a ResultSet
     */
    public static Optional<LibraryRequest> getFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                return Optional.of(createFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing library request: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Helper method to create a LibraryRequest from the current row in a ResultSet
     */
    private static LibraryRequest createFromResultSet(ResultSet rs) throws SQLException {
        // Get the librarian_id, which may be null
        Integer librarianId = rs.getInt("librarian_id");
        if (rs.wasNull()) {
            librarianId = null;
        }

        // Convert string status to enum
        State status = State.valueOf(rs.getString("status").toUpperCase());

        // Convert date strings to Date objects
        Date requestDate = rs.getTimestamp("request_date");

        Date processedDate = rs.getTimestamp("processed_date");
        if (rs.wasNull()) {
            processedDate = null;
        }

        Date dueDate = rs.getTimestamp("due_date");
        if (rs.wasNull()) {
            dueDate = null;
        }

        // Get member_name and book_title if available
        String memberName = null;
        String bookTitle = null;

        try {
            memberName = rs.getString("member_name");
        } catch (SQLException e) {
            // Column might not exist, ignore
        }

        try {
            bookTitle = rs.getString("book_title");
        } catch (SQLException e) {
            // Column might not exist, ignore
        }

        return new LibraryRequest(
                rs.getInt("request_id"),
                rs.getInt("book_id"),
                rs.getInt("user_id"),
                status,
                librarianId,
                rs.getString("reason"),
                requestDate,
                processedDate,
                dueDate,
                rs.getString("notes"),
                memberName,
                bookTitle
        );
    }

    /**
     * Process a request by updating its status and librarian information
     */
    public void processRequest(Integer librarianId, State newStatus, String notes) {
        this.librarianId = librarianId;
        this.status = newStatus;
        this.notes = notes;
        this.processedDate = new Date();

        // If status is changing to ISSUED, set a due date (default 14 days)
        if (newStatus == State.ISSUED && this.dueDate == null) {
            setDefaultDueDate();
        }
    }

    /**
     * Set a default due date of 14 days from current date
     */
    public void setDefaultDueDate() {
        Date now = new Date();
        long dueTime = now.getTime() + (14 * 24 * 60 * 60 * 1000); // 14 days in milliseconds
        this.dueDate = new Date(dueTime);
    }

    /**
     * Calculate the number of days that a book is overdue
     * @return number of days overdue (0 if not overdue)
     */
    public long getDaysOverdue() {
        if (this.dueDate == null || this.status == State.RETURNED) {
            return 0;
        }

        Date currentDate = new Date();
        long diffInMillies = currentDate.getTime() - this.dueDate.getTime();
        long daysLate = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return Math.max(0, daysLate);
    }

    /**
     * Calculate the fine amount based on days overdue
     * @return fine amount in dollars
     */
    public double calculateFine() {
        return getDaysOverdue() * FINE_RATE_PER_DAY;
    }

    /**
     * Mark the book as returned by the specified librarian
     * @param librarianId the ID of the librarian processing the return
     * @return true if the return was successful
     */
    public boolean returnBook(Integer librarianId) {
        if (this.status != State.ISSUED && this.status != State.OVERDUE) {
            return false;
        }

        this.librarianId = librarianId;
        this.status = State.RETURNED;
        this.processedDate = new Date();
        return true;
    }

    // Getters and setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public State getStatus() {
        return status;
    }

    public void setStatus(State status) {
        this.status = status;
    }

    public Integer getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(Integer librarianId) {
        this.librarianId = librarianId;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "LibraryRequest{" +
                "requestId=" + requestId +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", status=" + status +
                ", librarianId=" + librarianId +
                ", reason='" + reason + '\'' +
                ", requestDate=" + requestDate +
                ", processedDate=" + processedDate +
                ", dueDate=" + dueDate +
                ", notes='" + notes + '\'' +
                ", memberName='" + memberName + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                '}';
    }
}
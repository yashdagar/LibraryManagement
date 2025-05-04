
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class BookRequest {
    public final int requestId, studentId;
    public final String bookTitle, author, reason, requestDate, processedBy, processedDate, status;

    public BookRequest(int requestId, int studentId, String bookTitle, String author, String reason,
                       String requestDate, String processedBy, String processedDate, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.reason = reason;
        this.requestDate = requestDate;
        this.processedBy = processedBy;
        this.processedDate = processedDate;
        this.status = status;
    }

    public static ArrayList<BookRequest> getRequestsFromResultSet(ResultSet rs) {
        ArrayList<BookRequest> requests = new ArrayList<>();
        try {
            while (rs.next()) {
                requests.add(new BookRequest(
                    rs.getInt("request_id"),
                    rs.getInt("student_id"),
                    rs.getString("book_title"),
                    rs.getString("author"),
                    rs.getString("reason"),
                    rs.getString("request_date"),
                    rs.getString("processed_by"),
                    rs.getString("processed_date"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing book requests");
        }
        return requests;
    }

    public static Optional<BookRequest> getFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                return Optional.of(new BookRequest(
                    rs.getInt("request_id"),
                    rs.getInt("student_id"),
                    rs.getString("book_title"),
                    rs.getString("author"),
                    rs.getString("reason"),
                    rs.getString("request_date"),
                    rs.getString("processed_by"),
                    rs.getString("processed_date"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing book request");
        }
        return Optional.empty();
    }
}

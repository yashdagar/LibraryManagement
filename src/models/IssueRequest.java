package models;

import java.util.Date;

public class IssueRequest {
    public int id;
    public int bookId;
    public String bookTitle;
    public String bookAuthor;
    public int memberId;
    public String memberName;
    public String memberEmail;
    public Date requestDate;
    public String status; // PENDING, APPROVED, REJECTED
    public String notes;

    public IssueRequest(int id, int bookId, String bookTitle, String bookAuthor, int memberId,
                        String memberName, String memberEmail, Date requestDate, String status, String notes) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.requestDate = requestDate;
        this.status = status;
        this.notes = notes;
    }

    public enum State {
        PENDING,
        APPROVED,
        REJECTED,
        RETURNED
    }
}
// Option 1: If your IssuedBook model already stores a Book object

// In src/models/IssuedBook.java
package models;

import java.util.Date;

public class IssuedBook {
    private int id;
    private Book book;  // This is what we need - a reference to the Book object
    private int studentId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private double fine;

    // Constructor
    public IssuedBook(int id, Book book, int studentId, Date issueDate, Date dueDate) {
        this.id = id;
        this.book = book;
        this.studentId = studentId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0.0;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public int getStudentId() {
        return studentId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}

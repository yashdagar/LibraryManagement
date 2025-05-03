package models;

public class IssueRequest {
    public String bookId;
    public String userId;
    public State state;

    public IssueRequest(String bookId, String userId, State state) {
        this.bookId = bookId;
        this.userId = userId;
        this.state = state;
    }

    public enum State {
        PENDING,
        APPROVED,
        REJECTED,
        RETURNED
    }
}
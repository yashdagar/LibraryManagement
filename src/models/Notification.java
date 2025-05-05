package models;

import java.util.Date;

/**
 * Model class to represent a notification in the system
 */
public class Notification {
    private int id;
    private String title;
    private String message;
    private Date createdAt;
    private boolean isRead;
    private NotificationType type;
    private int userId;

    public enum NotificationType {
        DUE_DATE_REMINDER,
        FINE_ALERT,
        APPROVAL_STATUS,
        BOOK_RETURN,
        GENERAL
    }

    public Notification(int id, String title, String message, NotificationType type, int userId) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdAt = new Date();
        this.isRead = false;
        this.type = type;
        this.userId = userId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public NotificationType getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }
}
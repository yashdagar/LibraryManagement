package services;



import models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service to handle notification creation and retrieval
 */
public class NotificationService {
    private static NotificationService instance;
    private List<Notification> notifications;
    private int nextId = 1;

    private NotificationService() {
        notifications = new ArrayList<>();
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    // Create methods for different notification types
    public void createDueDateReminder(Student student, IssuedBook issuedBook, int daysRemaining) {
        String title = "Due Date Reminder";
        String message = "Book '" + issuedBook.getBook().getTitle() + "' is due in " + daysRemaining + " days.";

        Notification notification = new Notification(
                nextId++,
                title,
                message,
                Notification.NotificationType.DUE_DATE_REMINDER,
                student.getId()
        );

        notifications.add(notification);
    }

    public void createFineAlert(Student student, IssuedBook issuedBook, double fineAmount) {
        String title = "Fine Alert";
        String message = "You have incurred a fine of $" + fineAmount +
                " for late return of book '" + issuedBook.getBook().getTitle() + "'.";

        Notification notification = new Notification(
                nextId++,
                title,
                message,
                Notification.NotificationType.FINE_ALERT,
                student.getId()
        );

        notifications.add(notification);
    }

    public void createApprovalStatusNotification(User user, String bookTitle, boolean isApproved) {
        String title = "Book Request " + (isApproved ? "Approved" : "Rejected");
        String message = "Your request for book '" + bookTitle + "' has been " +
                (isApproved ? "approved. You can collect it from the library." : "rejected.");

        Notification notification = new Notification(
                nextId++,
                title,
                message,
                Notification.NotificationType.APPROVAL_STATUS,
                user.getId()
        );

        notifications.add(notification);
    }

    public void createBookReturnNotification(Librarian librarian, String studentName, String bookTitle) {
        String title = "Book Returned";
        String message = "Student '" + studentName + "' has returned the book '" + bookTitle + "'.";

        Notification notification = new Notification(
                nextId++,
                title,
                message,
                Notification.NotificationType.BOOK_RETURN,
                librarian.getId()
        );

        notifications.add(notification);
    }

    public void createGeneralNotification(User user, String title, String message) {
        Notification notification = new Notification(
                nextId++,
                title,
                message,
                Notification.NotificationType.GENERAL,
                user.getId()
        );

        notifications.add(notification);
    }

    // Get notifications for a specific user
    public List<Notification> getNotificationsForUser(int userId) {
        return notifications.stream()
                .filter(n -> n.getUserId() == userId)
                .collect(Collectors.toList());
    }

    // Get unread notifications for a specific user
    public List<Notification> getUnreadNotificationsForUser(int userId) {
        return notifications.stream()
                .filter(n -> n.getUserId() == userId && !n.isRead())
                .collect(Collectors.toList());
    }

    // Mark notification as read
    public void markAsRead(int notificationId) {
        notifications.stream()
                .filter(n -> n.getId() == notificationId)
                .findFirst()
                .ifPresent(n -> n.setRead(true));
    }

    // Check for due dates and create reminders
    public void checkAndCreateDueDateReminders() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        List<IssuedBook> issuedBooks = dbManager.getAllIssuedBooks();
        Date currentDate = new Date();

        for (IssuedBook issuedBook : issuedBooks) {
            long diffInMillies = issuedBook.getDueDate().getTime() - currentDate.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diffInDays <= 3 && diffInDays >= 0) {
                Student student = dbManager.getStudentById(issuedBook.getStudentId());
                createDueDateReminder(student, issuedBook, (int)diffInDays);
            }
        }
    }

    // Clear read notifications older than 30 days
    public void clearOldNotifications() {
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);

        notifications.removeIf(n -> n.isRead() && n.getCreatedAt().before(thirtyDaysAgo));
    }
}
package components;

import models.Librarian;
import models.Notification;
import models.Student;
import models.User;
import services.NotificationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationPanel extends RoundedPanel {
    private Student student;
    private User user;
    private Librarian librarian;
    private final JPanel notificationsContainer;
    private final JLabel titleLabel;
    private final JLabel emptyNotificationLabel;
    private final SimpleDateFormat dateFormat;
    private final NotificationService notificationService;
    private final Color UNREAD_BG = new Color(240, 248, 255);
    private final Color READ_BG = Color.WHITE;

    public NotificationPanel(User user) {
        super(15, Color.WHITE, 1, Color.LIGHT_GRAY);
        this.user = user;
        this.notificationService = NotificationService.getInstance();
        this.dateFormat = new SimpleDateFormat("MMM dd, HH:mm");

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshNotifications());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        // Container for notifications with scroll
        notificationsContainer = new JPanel();
        notificationsContainer.setLayout(new BoxLayout(notificationsContainer, BoxLayout.Y_AXIS));
        notificationsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(notificationsContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Empty state label
        emptyNotificationLabel = new JLabel("You have no notifications", SwingConstants.CENTER);
        emptyNotificationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        emptyNotificationLabel.setForeground(Color.GRAY);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshNotifications();
    }
    public NotificationPanel(Librarian librarian) {
        super(15, Color.WHITE, 1, Color.LIGHT_GRAY);
        this.librarian = librarian;
        this.notificationService = NotificationService.getInstance();
        this.dateFormat = new SimpleDateFormat("MMM dd, HH:mm");

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshNotifications());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        // Container for notifications with scroll
        notificationsContainer = new JPanel();
        notificationsContainer.setLayout(new BoxLayout(notificationsContainer, BoxLayout.Y_AXIS));
        notificationsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(notificationsContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Empty state label
        emptyNotificationLabel = new JLabel("You have no notifications", SwingConstants.CENTER);
        emptyNotificationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        emptyNotificationLabel.setForeground(Color.GRAY);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshNotifications();
    }
    public NotificationPanel(Student student) {
        super(15, Color.WHITE, 1, Color.LIGHT_GRAY);
        this.student = student;
        this.notificationService = NotificationService.getInstance();
        this.dateFormat = new SimpleDateFormat("MMM dd, HH:mm");

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshNotifications());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        // Container for notifications with scroll
        notificationsContainer = new JPanel();
        notificationsContainer.setLayout(new BoxLayout(notificationsContainer, BoxLayout.Y_AXIS));
        notificationsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(notificationsContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Empty state label
        emptyNotificationLabel = new JLabel("You have no notifications", SwingConstants.CENTER);
        emptyNotificationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        emptyNotificationLabel.setForeground(Color.GRAY);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshNotifications();
    }


    public void refreshNotifications() {
        notificationsContainer.removeAll();

        List<Notification> notifications = notificationService.getNotificationsForUser(user.getId());
        int unreadCount = (int) notifications.stream().filter(n -> !n.isRead()).count();

        // Update title with unread count
        if (unreadCount > 0) {
            titleLabel.setText("Notifications (" + unreadCount + ")");
        } else {
            titleLabel.setText("Notifications");
        }

        if (notifications.isEmpty()) {
            notificationsContainer.add(emptyNotificationLabel);
        } else {
            // Add notifications in reverse order (newest first)
            for (int i = notifications.size() - 1; i >= 0; i--) {
                Notification notification = notifications.get(i);
                addNotificationItem(notification);
            }
        }

        revalidate();
        repaint();
    }

    private void addNotificationItem(Notification notification) {
        // Container panel for the notification
        RoundedPanel notificationPanel = new RoundedPanel(10, notification.isRead() ? READ_BG : UNREAD_BG, 1, Color.LIGHT_GRAY);
        notificationPanel.setLayout(new BorderLayout(5, 5));
        notificationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        notificationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Icon based on notification type
        Icon icon = getIconForNotificationType(notification.getType());
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(32, 32));

        // Content panel (title, message, time)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel(notification.getTitle());
        titleLabel.setFont(new Font("Segoe UI", notification.isRead() ? Font.PLAIN : Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Message
        JLabel messageLabel = new JLabel("<html><p style='width:300px;'>" + notification.getMessage() + "</p></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Time
        JLabel timeLabel = new JLabel(dateFormat.format(notification.getCreatedAt()));
        timeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(timeLabel);

        // Mark as read button
        JButton markAsReadBtn = new JButton(notification.isRead() ? "Mark Unread" : "Mark as Read");
        markAsReadBtn.setFocusPainted(false);
        markAsReadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (notification.isRead()) {
                    // Toggle the read status in memory
                    notification.setRead(false);
                    // In a real implementation, you would update the database
                    // But since there's no markAsUnread method, we're just toggling in memory
                } else {
                    notificationService.markAsRead(notification.getId());
                }
                refreshNotifications();
            }
        });

        // Add components to notification panel
        notificationPanel.add(iconLabel, BorderLayout.WEST);
        notificationPanel.add(contentPanel, BorderLayout.CENTER);
        notificationPanel.add(markAsReadBtn, BorderLayout.EAST);

        // Add spacing between notifications
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(notificationPanel, BorderLayout.CENTER);
        wrapperPanel.add(Box.createVerticalStrut(8), BorderLayout.SOUTH);

        notificationsContainer.add(wrapperPanel);
    }

    private Icon getIconForNotificationType(Notification.NotificationType type) {
        String iconPath = "/resources/icons/";

        switch (type) {
            case DUE_DATE_REMINDER:
                iconPath += "calendar.png";
                break;
            case FINE_ALERT:
                iconPath += "money.png";
                break;
            case APPROVAL_STATUS:
                iconPath += "approval.png";
                break;
            case BOOK_RETURN:
                iconPath += "return.png";
                break;
            case GENERAL:
            default:
                iconPath += "info.png";
                break;
        }

        // Try to load icon, use default if not found
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource(iconPath));
            // Resize icon if needed
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
        } catch (Exception e) {
            // Use default icon if resource not found
            icon = new ImageIcon(new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB));
        }

        return icon;
    }
}
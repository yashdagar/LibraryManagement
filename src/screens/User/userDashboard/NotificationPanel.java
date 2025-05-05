package screens.User.userDashboard;

import javax.swing.*;
import java.awt.*;

public class NotificationsPanel extends RoundedPanel {
    private final Student student;
    private final NotificationPanel notificationPanel;

    public NotificationsPanel(Student student) {
        super(15, Color.WHITE, Color.LIGHT_GRAY);
        this.student = student;
        setLayout(new BorderLayout(10, 10));

        // Page header
        JLabel headerLabel = new JLabel("My Notifications");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Student notification panel
        notificationPanel = new NotificationPanel(student);

        // Help text
        JPanel helpPanel = new RoundedPanel(10, new Color(240, 255, 240), Color.LIGHT_GRAY);
        helpPanel.setLayout(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel helpLabel = new JLabel("<html><b>Information:</b><br>" +
                "• Due Date Reminders will appear 3 days before a book is due<br>" +
                "• Fine alerts will be sent if you return a book late<br>" +
                "• Book request approval notifications will appear when a librarian processes your request</html>");
        helpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        helpPanel.add(helpLabel, BorderLayout.CENTER);

        // Add to main panel
        add(headerLabel, BorderLayout.NORTH);
        add(notificationPanel, BorderLayout.CENTER);
        add(helpPanel, BorderLayout.SOUTH);
    }

    public void refreshNotifications() {
        notificationPanel.refreshNotifications();
    }
}
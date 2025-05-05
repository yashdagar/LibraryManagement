package screens.Librarian.librarianDashboard;



import components.NotificationPanel;
import components.RoundedPanel;
import models.Librarian;

import javax.swing.*;
import java.awt.*;

public class NotificationsPanel extends RoundedPanel {
    private final Librarian librarian;
    private final NotificationPanel notificationPanel;

    public NotificationsPanel(Librarian librarian) {
        super(15, Color.WHITE, Color.LIGHT_GRAY);
        this.librarian = librarian;
        setLayout(new BorderLayout(10, 10));

        // Page header
        JLabel headerLabel = new JLabel("Notifications");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Librarian notification panel
        notificationPanel = new NotificationPanel(librarian);

        // Help text
        JPanel helpPanel = new RoundedPanel(10, new Color(240, 255, 240), Color.LIGHT_GRAY);
        helpPanel.setLayout(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel helpLabel = new JLabel("<html><b>Information:</b><br>" +
                "• You will be notified when a student returns a book<br>" +
                "• New book requests will appear here for your approval<br>" +
                "• System notifications about book inventory will be displayed here</html>");
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


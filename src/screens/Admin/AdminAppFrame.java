package screens.Admin;

import models.Admin;
import services.AdminAuthService;

import javax.swing.*;
import java.awt.*;

public class AdminAppFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final AdminDashboard adminDashboard;
    final AdminAuthService dbConnection;

    public AdminAppFrame() {
        // Initialize database connection
        dbConnection = new AdminAuthService();

        // Set up the frame
        setTitle("System Administration Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize screens
        AdminLoginPage loginPage = new AdminLoginPage(this);
        adminDashboard = new AdminDashboard(this);

        // Add screens to card layout
        mainPanel.add(loginPage, "LOGIN");
        mainPanel.add(adminDashboard, "DASHBOARD");

        // Set initial screen
        cardLayout.show(mainPanel, "LOGIN");

        // Add main panel to frame
        add(mainPanel);
        setVisible(true);
    }

    public void showLoginPage() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showAdminDashboard(Admin admin) {
        adminDashboard.setAdminInfo(admin);
        cardLayout.show(mainPanel, "DASHBOARD");
        adminDashboard.setAdminInfo(admin);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminAppFrame();
        });
    }
}
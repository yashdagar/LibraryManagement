package screens.User;

import models.User;
import services.UserAuthService;

import javax.swing.*;
import java.awt.*;

public class UserAppFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final UserDashboard userDashboard;
    final UserAuthService dbConnection;
    public User user;

    public UserAppFrame() {
        // Initialize database connection
        dbConnection = new UserAuthService();

        // Set up the frame
        setTitle("User Authentication System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize screens
        UserLoginPage loginPage = new UserLoginPage(this);
        userDashboard = new UserDashboard(this);

        // Add screens to card layout
        mainPanel.add(loginPage, "LOGIN");
        mainPanel.add(userDashboard, "DASHBOARD");

        // Set initial screen
        cardLayout.show(mainPanel, "LOGIN");

        // Add main panel to frame
        add(mainPanel);

        setVisible(true);
    }

    public void showLoginPage() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showDashboard(User user) {
        this.user = user;
        userDashboard.setUserInfo(user);
        cardLayout.show(mainPanel, "DASHBOARD");
        userDashboard.setUserInfo(user);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserAppFrame();
        });
    }
}
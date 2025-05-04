package screens.Librarian;

import main.RoleSelectorFrame;
import models.Librarian;
import services.LibrarianAuthService;
import components.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LibrarianAppFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final LibrarianDashboard librarianDashboard;
    final LibrarianAuthService dbConnection;

    public LibrarianAppFrame() {
        // Initialize database connection
        dbConnection = new LibrarianAuthService();

        // Set up the frame - using similar styling as RoleSelectorFrame
        setTitle("Librarian Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Add window listener to handle when frame is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleLogout();
            }
        });

        // Create content panel with styling
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 250));

        // Add padding around content panel
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(245, 245, 250));
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Header panel
        RoundedPanel headerPanel = new RoundedPanel(15, new Color(240, 240, 240), 1, new Color(250, 250, 252));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Librarian System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(80, 170, 120));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Back to Role Selection");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> handleLogout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Initialize screens
        LibrarianLoginPage loginPage = new LibrarianLoginPage(this);
        librarianDashboard = new LibrarianDashboard(this);

        // Add screens to card layout
        mainPanel.add(loginPage, "LOGIN");
        mainPanel.add(librarianDashboard, "DASHBOARD");

        // Set initial screen
        cardLayout.show(mainPanel, "LOGIN");

        // Add rounded styling to main content area
        RoundedPanel mainContentPanel = new RoundedPanel(20, Color.WHITE, 1, new Color(240, 240, 240));
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Footer
        RoundedPanel footerPanel = new RoundedPanel(15, new Color(240, 240, 240), 1, new Color(250, 250, 252));
        footerPanel.setLayout(new BorderLayout());
        JLabel copyrightLabel = new JLabel("© 2025 Library Management. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(120, 120, 120));
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add content panel to wrapper
        wrapperPanel.add(contentPanel, BorderLayout.CENTER);

        // Add wrapper panel to frame
        add(wrapperPanel);
        setVisible(true);
    }

    public void showLoginPage() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showDashboard(Librarian librarian) {
        librarianDashboard.setLibrarianInfo(librarian);
        cardLayout.show(mainPanel, "DASHBOARD");
        revalidate();
        repaint();
    }

    private void handleLogout() {
        // Close this frame
        dispose();
        // Reopen the role selector
        RoleSelectorFrame.reopenRoleSelector();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LibrarianAppFrame();
        });
    }
}
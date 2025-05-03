package screens.Librarian;

import models.Librarian;
import services.LibrarianAuthService;

import javax.swing.*;
import java.awt.*;

public class LibrarianAppFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final LibrarianDashboard librarianDashboard;
    final LibrarianAuthService dbConnection;

    public LibrarianAppFrame() {
        // Initialize database connection
        dbConnection = new LibrarianAuthService();

        // Set up the frame
        setTitle("Librarian Authentication System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize screens
        LibrarianLoginPage loginPage= new LibrarianLoginPage(this);
        librarianDashboard = new LibrarianDashboard(this);

        // Add screens to card layout
        mainPanel.add(loginPage, "LOGIN");
        mainPanel.add(librarianDashboard, "DASHBOARD");

        // Set initial screen
        cardLayout.show(mainPanel, "LOGIN");

        // Add main panel to frame
        add(mainPanel);
        setVisible(true);
    }

    public void showLoginPage() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showDashboard(Librarian librarian) {
        librarianDashboard.setLibrarianInfo(librarian);
        cardLayout.show(mainPanel, "DASHBOARD");
        librarianDashboard.setLibrarianInfo(librarian);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibrarianAppFrame();
        });
    }
}
package main;

import screens.User.UserAppFrame;
import screens.Admin.AdminAppFrame;
import screens.Librarian.LibrarianAppFrame;
import components.RoundedPanel;
import components.RoundedPanelButton;
import components.SnackBar;
import services.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoleSelectorFrame extends JFrame {

    private final RoundedPanel mainPanel;
    private final JLabel titleLabel;
    private final JLabel subtitleLabel;
    private final JLabel dateTimeLabel;
    private final JPanel contentPanel;
    public DatabaseManager databaseManager;

    public RoleSelectorFrame() {
        // Set up the frame
        databaseManager=new DatabaseManager();
        setTitle("Role Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 250));

        // Header panel with date time
        RoundedPanel headerPanel = new RoundedPanel(15, new Color(240, 240, 240), 1, new Color(250, 250, 252));
        headerPanel.setLayout(new BorderLayout());

        // Current date time
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy • h:mm a");
        String currentDateTime = sdf.format(new Date());
        dateTimeLabel = new JLabel(currentDateTime);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateTimeLabel.setForeground(new Color(100, 100, 100));
        dateTimeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        headerPanel.add(dateTimeLabel, BorderLayout.EAST);
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Main centered panel
        mainPanel = new RoundedPanel(20, new Color(230, 230, 230), 1, Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Title and subtitle with custom styling
        titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitleLabel = new JLabel("Please select your role to continue");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to main panel with spacing
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Create role selection cards panel
        RoundedPanel cardsPanel = new RoundedPanel(15);
        cardsPanel.setLayout(new GridLayout(1, 3, 20, 0));
        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsPanel.setMaximumSize(new Dimension(650, 250));

        // Create role cards
        cardsPanel.add(createRoleCard("User",
                "Access your personal account and manage your profile settings",
                new Color(100, 150, 220),
                e -> showRoleSnackBarAndLaunch("Launching User application...", () -> launchUserApp())));

        cardsPanel.add(createRoleCard("Administrator",
                "Manage system settings, users, and administrative functions",
                new Color(110, 80, 200),
                e -> showRoleSnackBarAndLaunch("Launching Administrator application...", () -> launchAdminApp())));

        cardsPanel.add(createRoleCard("Librarian",
                "Manage library resources, books, and member services",
                new Color(80, 170, 120),
                e -> showRoleSnackBarAndLaunch("Launching Librarian application...", () -> launchLibrarianApp())));

        mainPanel.add(cardsPanel);
        mainPanel.add(Box.createVerticalGlue());

        // Add main panel to content
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Footer
        RoundedPanel footerPanel = new RoundedPanel(15, new Color(240, 240, 240), 1, new Color(250, 250, 252));
        footerPanel.setLayout(new BorderLayout());
        JLabel copyrightLabel = new JLabel("© 2025 System Management. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(120, 120, 120));
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        footerPanel.add(copyrightLabel, BorderLayout.WEST);

        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add padding around content panel
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(245, 245, 250));
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        wrapperPanel.add(contentPanel, BorderLayout.CENTER);

        // Add wrapper panel to frame
        add(wrapperPanel);

        setVisible(true);
    }

    private RoundedPanel createRoleCard(String roleName, String description, Color accentColor, ActionListener action) {
        RoundedPanel card = RoundedPanel.RoundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Role icon placeholder
        JLabel iconLabel = new JLabel("●");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 48));
        iconLabel.setForeground(accentColor);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Role title
        JLabel titleLabel = new JLabel(roleName);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 150px;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add selection button
        RoundedPanelButton selectButton = new RoundedPanelButton("Select", "", accentColor, action);
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with proper spacing
        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(selectButton);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void showRoleSnackBarAndLaunch(String message, Runnable launchAction) {
        // First, delay to simulate processing
        Timer delayTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the snack bar message
                SnackBar.show(contentPanel, message);

                // Delay the launch to allow the snack bar to be visible
                Timer launchTimer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        launchAction.run();
                    }
                });
                launchTimer.setRepeats(false);
                launchTimer.start();
            }
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void launchUserApp() {
        dispose(); // Close the selector
        SwingUtilities.invokeLater(() -> new UserAppFrame(this));
    }

    private void launchAdminApp() {
        dispose(); // Close the selector
        SwingUtilities.invokeLater(() -> new AdminAppFrame(this));
    }

    private void launchLibrarianApp() {
        dispose(); // Close the selector
        SwingUtilities.invokeLater(() -> new LibrarianAppFrame(this));
    }

    public void showMessage(String message) {
        SnackBar.show(contentPanel, message);
    }

    public void showMessage(String message, int durationMs) {
        SnackBar.show(contentPanel, message, durationMs);
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new RoleSelectorFrame());
    }

    public static void reopenRoleSelector() {
        SwingUtilities.invokeLater(() -> new RoleSelectorFrame());
    }

    // For testing the UI standalone
    public static void main1(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new RoleSelectorFrame();
        });
    }
}
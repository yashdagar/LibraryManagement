package screens.Librarian;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import components.RoundedPanel;
import components.RoundedPanelButton;
import components.RoundedTextField;
import models.Librarian;
import services.LibrarianAuthService;

public class LibrarianLoginPage extends JPanel {
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color backgroundColor = new Color(243, 244, 246);
    private final Color textColor = new Color(31, 41, 55);

    private RoundedTextField emailField;
    private RoundedTextField passwordField;
    private RoundedTextField nameField;
    private RoundedTextField phoneField;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final LibrarianAuthService authService;

    public LibrarianLoginPage(LibrarianAppFrame appFrame) {
        appFrame.setTitle("LibraryX - Librarian Portal");
        appFrame.setSize(new Dimension(1000, 600));
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(true);

        // Initialize auth service
        this.authService = new LibrarianAuthService();

        // Create main layout with two panels side by side
        setLayout(new GridLayout(1, 2));

        // Add the panels
        add(createLogoPanel());

        // Create card panel for login and register forms
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Add login and register panels to card layout
        cardPanel.add(createLoginFormPanel(appFrame), "login");
        cardPanel.add(createRegisterFormPanel(appFrame), "register");

        // Start with login panel
        cardLayout.show(cardPanel, "login");

        add(cardPanel);

        setVisible(true);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setBackground(primaryColor);

        // Create vertical panel to hold logo and text
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(primaryColor);

        // Logo
        JLabel logoIcon;
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File("assets/logo.png"));
        } catch (Exception e) {
            System.out.println("Could not find logo image: " + e.getMessage());
        }

        if(bufferedImage != null) {
            ImageIcon image = new ImageIcon(bufferedImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            logoIcon = new JLabel(image);
        } else {
            logoIcon = new JLabel("📚");
            logoIcon.setFont(new Font("Arial", Font.PLAIN, 80));
            logoIcon.setForeground(Color.WHITE);
        }
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // App title
        JLabel titleLabel = new JLabel("LibraryX");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tagline
        JLabel taglineLabel = new JLabel("Staff Portal - Librarian Access");
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        taglineLabel.setForeground(Color.WHITE);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with spacing
        contentPanel.add(logoIcon);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(taglineLabel);

        logoPanel.add(contentPanel);

        return logoPanel;
    }

    private JPanel createLoginFormPanel(LibrarianAppFrame appFrame) {
        // Main container with padding
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(backgroundColor);

        // Login form panel
        RoundedPanel formPanel = new RoundedPanel(16, Color.WHITE, 0, Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        // Set preferred and maximum width for the form panel
        formPanel.setPreferredSize(new Dimension(400, 500));

        // Title
        JLabel titleLabel = new JLabel("Librarian Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign in to access the librarian portal");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(textColor);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = new RoundedTextField(15);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(textColor);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new RoundedTextField(15);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Sign in button
        RoundedPanelButton signInButton = new RoundedPanelButton("Sign In", "", e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter email and password",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Authenticate librarian
            final Optional<Librarian> result = authService.loginLibrarian(email, password);
            if(result.isPresent()){
                // Clear fields and status
                emailField.setText("");
                passwordField.setText("");

                // Show librarian dashboard
                appFrame.showDashboard(result.get());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Make the sign in button left-aligned and full width
        signInButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signInButton.getPreferredSize().height));

        // Register text
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setOpaque(false);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel registerText = new JLabel("New staff member? ");
        registerText.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel registerLink = new JLabel("<HTML><U>Register Here</U></HTML>");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 14));
        registerLink.setForeground(primaryColor);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action to switch to register panel
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(cardPanel, "register");
            }
        });

        registerPanel.add(registerText);
        registerPanel.add(registerLink);

        // Add all components to fields panel
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(emailField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(passwordField);

        // Add components to form panel
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(signInButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(registerPanel);

        mainContainer.add(formPanel);

        return mainContainer;
    }

    private JPanel createRegisterFormPanel(LibrarianAppFrame appFrame) {
        // Main container with padding
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(backgroundColor);

        // Register form panel
        RoundedPanel formPanel = new RoundedPanel(16, Color.WHITE, 0, Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        // Title
        JLabel titleLabel = new JLabel("Librarian Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Create a new librarian account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        // Name field
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setForeground(textColor);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = new RoundedTextField(15);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(textColor);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedTextField registerEmailField = new RoundedTextField(15);
        registerEmailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerEmailField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        registerEmailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(textColor);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedTextField registerPasswordField = new RoundedTextField(15);
        registerPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerPasswordField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        registerPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Phone field
        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setForeground(textColor);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        phoneField = new RoundedTextField(15);
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        phoneField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Register button
        RoundedPanelButton registerButton = new RoundedPanelButton("Register", "", e -> {
            String name = nameField.getText().trim();
            String email = registerEmailField.getText().trim();
            String password = registerPasswordField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Register librarian
            boolean isRegistered = authService.registerLibrarian(name, email, password, phone);

            if (isRegistered) {
                // Clear fields
                nameField.setText("");
                registerEmailField.setText("");
                registerPasswordField.setText("");
                phoneField.setText("");

                JOptionPane.showMessageDialog(this,
                        "Registration successful! Please sign in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "login");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Email might already be in use.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Make the register button left-aligned and full width
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, registerButton.getPreferredSize().height));

        // Sign in text
        JPanel signInPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signInPanel.setOpaque(false);
        signInPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel signInText = new JLabel("Already have an account? ");
        signInText.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel signInLink = new JLabel("<HTML><U>Sign In</U></HTML>");
        signInLink.setFont(new Font("Arial", Font.PLAIN, 14));
        signInLink.setForeground(primaryColor);
        signInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action to switch to log in panel
        signInLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(cardPanel, "login");
            }
        });

        signInPanel.add(signInText);
        signInPanel.add(signInLink);

        // Add all components to fields panel
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(nameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        fieldsPanel.add(emailLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(registerEmailField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(registerPasswordField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        fieldsPanel.add(phoneLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(phoneField);

        // Add components to form panel
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(signInPanel);

        mainContainer.add(formPanel);

        return mainContainer;
    }

    // Clean up resources when no longer needed
    public void cleanup() {
        if (authService != null) {
            authService.closeConnection();
        }
    }
}
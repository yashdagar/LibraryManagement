package screens.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import components.RoundedPanel;
import components.RoundedTextField;
import components.RoundedPanelButton;
import models.User;

public class UserLoginPage extends JPanel {
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color backgroundColor = new Color(243, 244, 246);
    private final Color textColor = new Color(31, 41, 55);

    private RoundedTextField emailField;
    private RoundedTextField passwordField;
    private RoundedTextField nameField;
    private RoundedTextField programField;
    private RoundedTextField sapIDField;
    private JComboBox<String> yearOfStudyField;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public UserLoginPage(UserAppFrame appFrame) {
        appFrame.setTitle("LibraryX - Login");
        appFrame.setSize(new Dimension(1000, 600));
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(true);

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
        JLabel taglineLabel = new JLabel("Your Digital Library Management System");
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

    private JPanel createLoginFormPanel(UserAppFrame appFrame) {
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
        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign in to continue");
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

//        JPanel optionsPanel = new JPanel(new BorderLayout());
//        optionsPanel.setOpaque(false);
//        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//
//        JCheckBox rememberMe = new JCheckBox("Remember Me");
//        rememberMe.setFont(new Font("Arial", Font.PLAIN, 13));
//        rememberMe.setOpaque(false);
//
//        JLabel forgotPassword = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
//        forgotPassword.setFont(new Font("Arial", Font.PLAIN, 13));
//        forgotPassword.setForeground(primaryColor);
//        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//        optionsPanel.add(rememberMe, BorderLayout.WEST);
//        optionsPanel.add(forgotPassword, BorderLayout.EAST);

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

            // Authenticate user
            final Optional<User> result = appFrame.dbConnection.loginUser(email, password);
            if(result.isPresent()){

                // Clear fields and status
                emailField.setText("");
                passwordField.setText("");

                // Show dashboard
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

        // Sign up text
        JPanel signUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpPanel.setOpaque(false);
        signUpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        signUpPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel signUpText = new JLabel("Don't have an account? ");
        signUpText.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel signUpLink = new JLabel("<HTML><U>Sign Up</U></HTML>");
        signUpLink.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpLink.setForeground(primaryColor);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action to switch to register panel
        signUpLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(cardPanel, "register");
            }
        });

        signUpPanel.add(signUpText);
        signUpPanel.add(signUpLink);

        // Add all components to fields panel
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(emailField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(passwordField);
//        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
//        fieldsPanel.add(optionsPanel);

        // Add components to form panel
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(signInButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(signUpPanel);

        mainContainer.add(formPanel);

        return mainContainer;
    }

    private JPanel createRegisterFormPanel(UserAppFrame appFrame) {
        // Main container with padding
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(backgroundColor);

        // Register form panel
        RoundedPanel formPanel = new RoundedPanel(16, Color.WHITE, 0, Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        // Set preferred and maximum width for the form panel
//        formPanel.setPreferredSize(new Dimension(500, 900));

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign up to get started");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldsPanel.setSize(new Dimension(Integer.MAX_VALUE, 900)); // Increased height for more fields

        // Name field
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setForeground(textColor);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = new RoundedTextField(15);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
//        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(textColor);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedTextField registerEmailField = new RoundedTextField(15);
        registerEmailField.setAlignmentX(Component.LEFT_ALIGNMENT);
//        registerEmailField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
//        registerEmailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(textColor);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedTextField registerPasswordField = new RoundedTextField(15);
        registerPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
//        registerPasswordField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
//        registerPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Program field
        JLabel programLabel = new JLabel("Program");
        programLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        programLabel.setForeground(textColor);
        programLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        programField = new RoundedTextField(15);
        programField.setAlignmentX(Component.LEFT_ALIGNMENT);
//        programField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
//        programField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // SAP ID field
        JLabel sapIDLabel = new JLabel("SAP ID");
        sapIDLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sapIDLabel.setForeground(textColor);
        sapIDLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sapIDField = new RoundedTextField(15);
        sapIDField.setAlignmentX(Component.LEFT_ALIGNMENT);
//        sapIDField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
//        sapIDField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Year of Study field
        JLabel yearOfStudyLabel = new JLabel("Year of Study");
        yearOfStudyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        yearOfStudyLabel.setForeground(textColor);
        yearOfStudyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] years = {"1", "2", "3", "4", "5"};
        yearOfStudyField = new JComboBox<>(years);
        yearOfStudyField.setAlignmentX(Component.LEFT_ALIGNMENT);
//        yearOfStudyField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
//        yearOfStudyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        yearOfStudyField.setBackground(Color.WHITE);

        // Sign up button
        RoundedPanelButton signUpButton = new RoundedPanelButton("Sign Up", "", e -> {
            String name = nameField.getText().trim();
            String email = registerEmailField.getText().trim();
            String password = registerPasswordField.getText().trim();
            String program = programField.getText().trim();
            String sapID = sapIDField.getText().trim();
            String yearOfStudy = (String) yearOfStudyField.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                    program.isEmpty() || sapID.isEmpty() || yearOfStudy == null) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Register user with additional fields
            boolean isRegistered = appFrame.dbConnection.registerUser(
                    name, email, password, program, sapID, yearOfStudy);

            if (isRegistered) {
                // Clear fields
                nameField.setText("");
                registerEmailField.setText("");
                registerPasswordField.setText("");
                programField.setText("");
                sapIDField.setText("");
                yearOfStudyField.setSelectedIndex(0);

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

        // Make the signup button left-aligned and full width
        signUpButton.setAlignmentX(Component.LEFT_ALIGNMENT);
//        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signUpButton.getPreferredSize().height));

        // Sign in text
        JPanel signInPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signInPanel.setOpaque(false);
        signInPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        signInPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

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

        fieldsPanel.add(programLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(programField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        fieldsPanel.add(sapIDLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(sapIDField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        fieldsPanel.add(yearOfStudyLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(yearOfStudyField);

        // Add components to form panel
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(signUpButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        formPanel.add(signInPanel);

        mainContainer.add(formPanel);

        return mainContainer;
    }
}
package ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class LoginUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private AnimatedButton loginButton;
    private JLabel statusLabel;

    private HashMap<String, String> users;

    public LoginUI() {
        setTitle("Login System");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new AnimatedButton("Login");
        statusLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Email:", SwingConstants.CENTER));
        add(emailField);
        add(new JLabel("Password:", SwingConstants.CENTER));
        add(passwordField);
        add(loginButton);
        add(statusLabel);

        users = new HashMap<>();
        users.put("admin@example.com", "admin123");
        users.put("user@example.com", "user123");

        loginButton.addActionListener(e -> {
            loginButton.animate();

            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (users.containsKey(email) && users.get(email).equals(password)) {
                statusLabel.setText("Login successful: " + email);
            } else {
                statusLabel.setText("Invalid credentials");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}

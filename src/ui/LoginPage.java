package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*; // Added for backend logic

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton userRadio, adminRadio, librarianRadio;
    private JCheckBox rememberMe;
    private AnimatedButton signInButton;

    public LoginPage() {
        setTitle("Library Management System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background Panel with image and overlay
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon("C:\\Users\\vansh\\OneDrive\\Desktop\\Everything\\College\\SEM4\\OOP_PROJECT\\pexels-artempodrez-7232658.jpg").getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 180), 0, getHeight(), new Color(0, 0, 0, 180)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Login Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setPreferredSize(new Dimension(400, 500));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(33, 47, 61));

        // Username Field
        usernameField = new JTextField("Username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setForeground(Color.GRAY);
        usernameField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Username");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });

        // Password Field
        passwordField = new JPasswordField("Password");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0); // Show placeholder
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('•');
                }
            }
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password");
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });

        // Role Selection
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioPanel.setOpaque(false);
        userRadio = new JRadioButton("User");
        adminRadio = new JRadioButton("Admin");
        librarianRadio = new JRadioButton("Librarian");
        ButtonGroup bg = new ButtonGroup();
        bg.add(userRadio);
        bg.add(adminRadio);
        bg.add(librarianRadio);
        radioPanel.add(userRadio);
        radioPanel.add(adminRadio);
        radioPanel.add(librarianRadio);

        // Remember Me & Forgot Password
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setOpaque(false);
        rememberMe = new JCheckBox("Remember Me");
        rememberMe.setFocusPainted(false);
        rememberMe.setOpaque(false);
        rememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel forgot = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgot.setForeground(Color.BLUE);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lowerPanel.add(rememberMe, BorderLayout.WEST);
        lowerPanel.add(forgot, BorderLayout.EAST);

        // Sign-In Button
        signInButton = new AnimatedButton("Sign In");
        signInButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        signInButton.setPreferredSize(new Dimension(200, 45));
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = String.valueOf(passwordField.getPassword()).trim();
            String role = userRadio.isSelected() ? "User" : adminRadio.isSelected() ? "Admin" :
                    librarianRadio.isSelected() ? "Librarian" : null;

            if (username.equals("Username") || password.equals("Password") || role == null) {
                JOptionPane.showMessageDialog(this, "Please enter all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- BACKEND: JDBC LOGIN LOGIC HERE ---
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                // 1. Load driver (optional for newer JDBC)
                // Class.forName("com.mysql.cj.jdbc.Driver");

                // 2. Connect to database
                // conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_db", "username", "password");

                // 3. Create SQL query
                // String sql = "SELECT * FROM users WHERE USERNAME=username AND PASSWORD=password AND ROLE=role";
                // stmt = conn.prepareStatement(sql);
                // stmt.setString(1, username);
                // stmt.setString(2, password);
                // stmt.setString(3, role);

                // 4. Execute query
                // rs = stmt.executeQuery();
                // if (rs.next()) {
                //     // Successful login - redirect based on role
                //     JOptionPane.showMessageDialog(this, "Login Successful as " + role);
                //
                //     if (role.equals("Admin")) {
                //         // new AdminDashboard().setVisible(true);
                //     } else if (role.equals("User")) {
                //         // new UserDashboard().setVisible(true);
                //     } else if (role.equals("Librarian")) {
                //         // new LibrarianDashboard().setVisible(true);
                //     }
                //
                //     this.dispose(); // Close login page
                // } else {
                //     JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                // }

                // Temporary success until backend is live:
                JOptionPane.showMessageDialog(this, "Login Successful as " + role);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Sign-up Label
        JLabel signUp = new JLabel("Don't have an account? Sign Up");
        signUp.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signUp.setForeground(Color.BLUE);
        signUp.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add components to form panel
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(radioPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(lowerPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(signInButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(signUp);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(formPanel, gbc);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}

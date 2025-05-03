package screens.Librarian;

import models.Librarian;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LibrarianDashboard extends JPanel {
    // Color scheme
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color lightPrimaryColor = new Color(99, 102, 241);
    private final Color backgroundColor = new Color(243, 244, 246);

    private JLabel librarianName;
    private Librarian librarian = new Librarian(
            "Sarah Johnson",
            "sjohnson@libraryx.edu",
            "98765432"
    );

    private JPanel centerPanel;
    private MainPanel mainPanel;
    private CatalogPanel catalogPanel;
    private JLabel timeLabel;
    private Timer timer;

    // Sidebar menu items
    private JPanel[] menuItems;

    public LibrarianDashboard(LibrarianAppFrame appFrame) {
        appFrame.setTitle("LibraryX - Librarian Dashboard");
        appFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        appFrame.setUndecorated(false);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(false);


        appFrame.getContentPane().setBackground(backgroundColor);

        // Create main layout
        setLayout(new BorderLayout());

        centerPanel = new JPanel(new CardLayout());
        mainPanel = new MainPanel(appFrame);
        catalogPanel = new CatalogPanel();

        // Add panels to card layout
        centerPanel.add(mainPanel, "dashboard");
        centerPanel.add(catalogPanel, "catalog");

        // Add components
        add(createSidebar(), BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Create and start the time display timer
        createTimeDisplay();

        setVisible(true);
    }

    private void createTimeDisplay() {
        // Time display in top-right corner
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        timePanel.setBackground(backgroundColor);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(107, 114, 128));
        timePanel.add(timeLabel);

        add(timePanel, BorderLayout.NORTH);

        // Update time every second
        timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm:ss");
            timeLabel.setText(sdf.format(new Date()));
        });
        timer.start();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBackground(primaryColor);

        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        logoPanel.setBackground(primaryColor);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoIcon;
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("assets/logo.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(bufferedImage != null) {
            ImageIcon image = new ImageIcon(bufferedImage.getScaledInstance(32, 32, Image.SCALE_DEFAULT));
            logoIcon = new JLabel(image);
        }
        else{
            logoIcon = new JLabel("📚");
            logoIcon.setFont(new Font("Arial", Font.PLAIN, 24));
            logoIcon.setForeground(Color.WHITE);
        }

        JLabel logoText = new JLabel("LibraryX");
        logoText.setFont(new Font("Arial", Font.BOLD, 20));
        logoText.setForeground(Color.WHITE);

        logoPanel.add(logoIcon);
        logoPanel.add(logoText);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(primaryColor);

        // Create menu items
        String[] menuItemNames = {"Dashboard", "Catalog", "Members", "Add Book", "Circulation", "Reservations", "Returns", "Fines", "Reports"};
        String[] menuIcons = {"🏠", "📚", "👥", "➕", "🔄", "📅", "↩️", "💰", "📊"};
        menuItems = new JPanel[menuItemNames.length];

        for (int i = 0; i < menuItemNames.length; i++) {
            // First menu item (Dashboard) is selected by default
            menuItems[i] = createMenuItem(menuItemNames[i], menuIcons[i], i == 0);
            final int index = i;

            menuItems[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Update UI to show which menu item is selected
                    for (int j = 0; j < menuItems.length; j++) {
                        updateMenuItemStyle(menuItems[j], j == index);
                    }

                    // Handle navigation based on menu selection
                    CardLayout cl = (CardLayout) centerPanel.getLayout();
                    switch (index) {
                        case 0: // Dashboard
                            cl.show(centerPanel, "dashboard");
                            break;
                        case 1: // Catalog
                            cl.show(centerPanel, "catalog");
                            break;
                        case 3: // Add Book
                            showAddBookDialog();
                            break;
                        // Add other menu actions when implemented
                        default:
                            // Show a placeholder for unimplemented screens
                            JOptionPane.showMessageDialog(
                                    LibrarianDashboard.this,
                                    "This feature is coming soon!",
                                    menuItemNames[index],
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            break;
                    }
                }
            });

            menuPanel.add(menuItems[i]);
        }

        // Profile panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(new Color(67, 56, 202)); // Darker indigo
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        userIcon.setForeground(Color.WHITE);

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setBackground(new Color(67, 56, 202));

        librarianName = new JLabel(librarian.name);
        librarianName.setFont(new Font("Arial", Font.BOLD, 14));
        librarianName.setForeground(Color.WHITE);


        userInfo.add(librarianName);


        profilePanel.add(userIcon, BorderLayout.WEST);
        profilePanel.add(spacer(), BorderLayout.CENTER);
        profilePanel.add(userInfo, BorderLayout.EAST);

        // Add all components to sidebar
        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(profilePanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel createMenuItem(String text, String icon, boolean isSelected) {
        JPanel menuItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        if (isSelected) {
            menuItem.setBackground(lightPrimaryColor);
        } else {
            menuItem.setBackground(primaryColor);
        }

        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        iconLabel.setForeground(Color.WHITE);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", isSelected ? Font.BOLD : Font.PLAIN, 14));
        textLabel.setForeground(Color.WHITE);

        menuItem.add(iconLabel);
        menuItem.add(textLabel);

        // Store the text label as a client property so we can update its font
        menuItem.putClientProperty("textLabel", textLabel);

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    menuItem.setBackground(new Color(79, 70, 229, 200));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    menuItem.setBackground(primaryColor);
                }
            }
        });

        return menuItem;
    }

    private void updateMenuItemStyle(JPanel menuItem, boolean isSelected) {
        if (isSelected) {
            menuItem.setBackground(lightPrimaryColor);
            JLabel textLabel = (JLabel) menuItem.getClientProperty("textLabel");
            if (textLabel != null) {
                textLabel.setFont(new Font("Arial", Font.BOLD, 14));
            }
        } else {
            menuItem.setBackground(primaryColor);
            JLabel textLabel = (JLabel) menuItem.getClientProperty("textLabel");
            if (textLabel != null) {
                textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            }
        }
    }

    private Component spacer() {
        return Box.createRigidArea(new Dimension(16, 16));
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        // Author field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Author:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField authorField = new JTextField(20);
        formPanel.add(authorField, gbc);

        // ISBN field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("ISBN:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        JTextField isbnField = new JTextField(20);
        formPanel.add(isbnField, gbc);

        // Publisher field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Publisher:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        JTextField publisherField = new JTextField(20);
        formPanel.add(publisherField, gbc);

        // Year field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Publication Year:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        JTextField yearField = new JTextField(20);
        formPanel.add(yearField, gbc);

        // Category field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        String[] categories = {"Fiction", "Non-Fiction", "Reference", "Textbook", "Magazine", "Other"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        formPanel.add(categoryComboBox, gbc);

        // Copies field
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Number of Copies:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner copiesSpinner = new JSpinner(spinnerModel);
        formPanel.add(copiesSpinner, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton addButton = new JButton("Add Book");
        addButton.setBackground(primaryColor);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            // Here you would add code to save the book to database
            JOptionPane.showMessageDialog(dialog,
                    "Book \"" + titleField.getText() + "\" has been added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void setLibrarianInfo(Librarian librarian) {
        this.librarian = librarian;
        librarianName.setText(librarian.name);
        mainPanel.setLibrarianInfo(librarian);
        revalidate();
        repaint();

    }
}
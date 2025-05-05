package screens.Librarian;

import models.Librarian;
import screens.Librarian.librarianDashboard.CatalogPanel;
import screens.Librarian.librarianDashboard.MainPanel;
import services.LibrarianAuthService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    // Add a librarian ID field since Librarian class doesn't have getId() method
    private int librarianId = 1; // Default ID

    private JPanel centerPanel;
    private MainPanel mainPanel;
    private CatalogPanel catalogPanel;
    private JLabel timeLabel;
    private Timer timer;

    // Add reference to the service
    private LibrarianAuthService librarianService;

    // Sidebar menu items
    private JPanel[] menuItems;

    public LibrarianDashboard(LibrarianAppFrame appFrame) {
        appFrame.setTitle("LibraryX - Librarian Dashboard");
        appFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        appFrame.setUndecorated(false);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(false);

        // Initialize the librarian service
        librarianService = new LibrarianAuthService(appFrame.frame.databaseManager);

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
        String[] menuItemNames = {"Dashboard", "Catalog", "Members", "Add Book", "Delete Book", "Circulation", "Reservations", "Returns", "Fines", "Reports"};
        String[] menuIcons = {"🏠", "📚", "👥", "➕", "🗑️", "🔄", "📅", "↩️", "💰", "📊"};
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
                        case 4: // Delete Book
                            showDeleteBookDialog();
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

    // Data class to represent a book from search results
    private static class BookResult {
        int id;
        String isbn;
        String title;
        String author;
        String category;
        int totalCopies;
        int availableCopies;

        public BookResult(int id, String isbn, String title, String author, String category, int totalCopies, int availableCopies) {
            this.id = id;
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.category = category;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
        }

        // Getters for table display
        public String getIsbn() { return isbn; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getCategory() { return category; }
        public int getTotalCopies() { return totalCopies; }
        public int getAvailableCopies() { return availableCopies; }
    }

    private void showDeleteBookDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Delete Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(700, 400); // Made dialog wider to accommodate additional columns
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Search method selector
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Search by:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        String[] searchMethods = {"ISBN", "Title", "Author"};
        JComboBox<String> searchMethodComboBox = new JComboBox<>(searchMethods);
        formPanel.add(searchMethodComboBox, gbc);

        // Search field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Search:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField searchField = new JTextField(20);
        formPanel.add(searchField, gbc);

        // Search button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 15, 5);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(primaryColor);
        searchButton.setForeground(Color.WHITE);
        formPanel.add(searchButton, gbc);

        // Results section
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Results:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Create table with updated columns
        String[] columnNames = {"ID", "ISBN", "Title", "Author", "Category", "Total Copies", "Available"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        JTable resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        formPanel.add(scrollPane, gbc);

        // Hide the ID column as it's for internal use
        resultsTable.getColumnModel().getColumn(0).setMinWidth(0);
        resultsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        resultsTable.getColumnModel().getColumn(0).setWidth(0);

        // Add quantity panel for deletion
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Copies to delete:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        SpinnerModel deleteSpinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner deleteQuantitySpinner = new JSpinner(deleteSpinnerModel);
        formPanel.add(deleteQuantitySpinner, gbc);

        // List to store book results for later reference
        List<BookResult> bookResults = new ArrayList<>();

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(220, 38, 38)); // Red color for delete
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow >= 0) {
                BookResult selectedBook = bookResults.get(selectedRow);
                int bookId = selectedBook.id;
                String bookTitle = selectedBook.title;
                int quantity = (Integer) deleteQuantitySpinner.getValue();

                // Check if trying to delete more than available
                if (quantity > selectedBook.availableCopies) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Cannot delete " + quantity + " copies. Only " + selectedBook.availableCopies + " copies are available.",
                            "Invalid Quantity",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Confirm deletion
                String confirmMessage;
                if (quantity >= selectedBook.totalCopies) {
                    confirmMessage = "Are you sure you want to delete all copies of \"" + bookTitle + "\"? This will remove the book entirely.";
                } else {
                    confirmMessage = "Are you sure you want to delete " + quantity + " copies of \"" + bookTitle + "\"?";
                }

                int confirm = JOptionPane.showConfirmDialog(
                        dialog,
                        confirmMessage,
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Call service to delete the book - use librarianId instead of librarian.getId()
                    boolean success = librarianService.deleteBook(bookId, quantity, librarianId);

                    if (success) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                quantity >= selectedBook.totalCopies ?
                                        "Book \"" + bookTitle + "\" has been completely removed from the library." :
                                        quantity + " copies of \"" + bookTitle + "\" have been deleted successfully.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Failed to delete book copies. Some copies may be issued or an error occurred.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Please select a book to delete.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add functionality to search button
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter a search term.",
                        "Empty Search",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Clear previous results
            tableModel.setRowCount(0);
            bookResults.clear();

            // Get selected search method
            String searchMethod = (String) searchMethodComboBox.getSelectedItem();

            try {
                // Call search method based on selection
                List<BookResult> results = searchBooks(searchMethod, searchTerm);

                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "No books found matching your search criteria.",
                            "No Results",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    // Populate table with results
                    for (BookResult book : results) {
                        bookResults.add(book);
                        tableModel.addRow(new Object[]{
                                book.id,
                                book.isbn,
                                book.title,
                                book.author,
                                book.category,
                                book.totalCopies,
                                book.availableCopies
                        });
                    }

                    // Update the spinner max based on first result
                    if (!results.isEmpty()) {
                        int maxAvailable = results.get(0).availableCopies;
                        deleteSpinnerModel.setValue(Math.min(1, maxAvailable));
                        ((SpinnerNumberModel) deleteSpinnerModel).setMaximum(maxAvailable);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Error searching for books: " + ex.getMessage(),
                        "Search Error",
                        JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        // Add selection listener to update quantity spinner based on selected book
        resultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = resultsTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < bookResults.size()) {
                    BookResult selectedBook = bookResults.get(selectedRow);
                    int maxAvailable = selectedBook.availableCopies;

                    // Update spinner values
                    deleteQuantitySpinner.setValue(1);
                    ((SpinnerNumberModel) deleteSpinnerModel).setMaximum(maxAvailable);
                }
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Search for books based on the specified criteria
     * @param searchMethod The method to search by (ISBN, Title, Author)
     * @param searchTerm The term to search for
     * @return List of BookResult objects matching the search criteria
     */
    private List<BookResult> searchBooks(String searchMethod, String searchTerm) {
        List<BookResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Get connection from the service
            connection = librarianService.getConnection();

            // Create SQL query based on search method
            String searchColumn;
            switch (searchMethod) {
                case "ISBN":
                    searchColumn = "isbn";
                    break;
                case "Author":
                    searchColumn = "author";
                    break;
                case "Title":
                default:
                    searchColumn = "title";
                    break;
            }

            String query = "SELECT id, isbn, title, author, category, total_copies, available_copies FROM books " +
                    "WHERE " + searchColumn + " LIKE ? ORDER BY title";

            statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchTerm + "%");

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                results.add(new BookResult(
                        resultSet.getInt("id"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("category"),
                        resultSet.getInt("total_copies"),
                        resultSet.getInt("available_copies")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            // Close resultSet and statement, but don't close connection
            // as it's managed by the service
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public void setLibrarianInfo(Librarian librarian) {
        this.librarian = librarian;
        librarianName.setText(librarian.name);
        mainPanel.setLibrarianInfo(librarian);
        revalidate();
        repaint();
    }

    // Helper method to set the librarian ID separately from the Librarian object
    public void setLibrarianId(int id) {
        this.librarianId = id;
    }
}
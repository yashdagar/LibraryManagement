package screens.User.userDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import components.RoundedPanelButton;
import models.Book;
import screens.User.UserAppFrame;
import screens.User.userDashboard.searchBooks.BookGridPanel;
import services.UserAuthService;

public class SearchBooksPanel extends JPanel {
    // Colors
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color secondaryColor = new Color(99, 102, 241);
    private final Color backgroundColor = new Color(243, 244, 246);
    private final Color cardBackground = Color.WHITE;
    private final Color textColor = new Color(31, 41, 55);
    private final Color mutedTextColor = new Color(107, 114, 128);

    // Components
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> sortComboBox;
    private JPanel filtersPanel;
    private JCheckBox availableOnlyCheckbox;
    private List<Book> books; // Book list
    private BookGridPanel bookGridPanel;
    private JToggleButton viewToggleButton;
    private boolean isGridView = true;
    private JPanel listViewPanel;
    private JScrollPane listScrollPane;
    private CardLayout viewCardLayout;
    private JPanel viewSwitchPanel;
    UserAppFrame appFrame;

    // Database connection
    private UserAuthService userService;

    public SearchBooksPanel(UserAppFrame appFrame) {
        this.setLayout(new BorderLayout());
        this.setBackground(backgroundColor);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize database connection
        userService = new UserAuthService(appFrame.frame.databaseManager);
        this.appFrame = appFrame;

        // Create components
        createHeader();
        createSearchBar();
        createFiltersPanel();
        createViewToggle();
        createViewPanels();

        // Fetch books from database
        loadBooksFromDatabase();

        // Perform initial search to populate results
        performSearch();
    }

    private void loadBooksFromDatabase() {
        // Load books from database
        books = userService.getAllBooks();

        // If no books were returned (e.g., database error), initialize with demo data
        if (books == null || books.isEmpty()) {
            books = new ArrayList<>();
            // Adding demo books as fallback
            books.add(new Book(1, "The Great Gatsby", "A novel of the Jazz Age", "gatsby.jpg", 3,
                    "F. Scott Fitzgerald", "Classic", "1925"));
            books.add(new Book(2, "To Kill a Mockingbird", "A novel about racial injustice", "mockingbird.jpg", 0,
                    "Harper Lee", "Fiction", "1960"));
            books.add(new Book(3, "1984", "A dystopian social science fiction novel", "1984.jpg", 2,
                    "George Orwell", "Dystopian", "1949"));
            // More demo books...
        }
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(backgroundColor);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Search Books");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);

        JLabel subtitleLabel = new JLabel("Find books from our extensive library collection");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(mutedTextColor);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(backgroundColor);
        labelPanel.add(titleLabel);
        labelPanel.add(subtitleLabel);

        headerPanel.add(labelPanel, BorderLayout.WEST);
        this.add(headerPanel, BorderLayout.NORTH);
    }

    private void createSearchBar() {
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
        searchBarPanel.setBackground(backgroundColor);
        searchBarPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(primaryColor);
        searchButton.setForeground(Color.BLACK);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setFocusPainted(false);

        searchButton.addActionListener(e -> performSearch());

        searchBarPanel.add(searchField);
        searchBarPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchBarPanel.add(searchButton);

        this.add(searchBarPanel, BorderLayout.NORTH);

        // Position after the header panel
        this.remove(searchBarPanel);  // Remove first to avoid duplicates
        this.add(searchBarPanel, BorderLayout.NORTH);
    }

    private void createFiltersPanel() {
        filtersPanel = new JPanel();
        filtersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filtersPanel.setBackground(backgroundColor);
        filtersPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Categories filter
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        categoryComboBox = new JComboBox<>(new String[]{"All Categories", "Fiction", "Fantasy", "Classic", "Dystopian", "Romance"});
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryComboBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        categoryComboBox.addActionListener(e -> performSearch());

        // Sort by filter
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        sortComboBox = new JComboBox<>(new String[]{"Title (A-Z)", "Title (Z-A)", "Author (A-Z)", "Year (Newest)", "Year (Oldest)"});
        sortComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        sortComboBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sortComboBox.addActionListener(e -> performSearch());

        // Available only checkbox
        availableOnlyCheckbox = new JCheckBox("Available only");
        availableOnlyCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        availableOnlyCheckbox.setBackground(backgroundColor);
        availableOnlyCheckbox.addActionListener(e -> performSearch());

        // Add components to filters panel
        filtersPanel.add(categoryLabel);
        filtersPanel.add(categoryComboBox);
        filtersPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filtersPanel.add(sortLabel);
        filtersPanel.add(sortComboBox);
        filtersPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filtersPanel.add(availableOnlyCheckbox);

        // Add filters panel to layout
        this.add(filtersPanel, BorderLayout.CENTER);

        // Position after the search bar
        this.remove(filtersPanel);  // Remove first to avoid duplicates
        this.add(filtersPanel, BorderLayout.CENTER);
    }

    private void createViewToggle() {
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        togglePanel.setBackground(backgroundColor);

        viewToggleButton = new JToggleButton("List View");
        viewToggleButton.setSelected(false); // Default is grid view
        viewToggleButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewToggleButton.setFocusPainted(false);
        viewToggleButton.addActionListener(e -> {
            isGridView = !viewToggleButton.isSelected();
            viewToggleButton.setText(isGridView ? "List View" : "Grid View");
            viewCardLayout.show(viewSwitchPanel, isGridView ? "GRID" : "LIST");
            performSearch();
        });

        togglePanel.add(viewToggleButton);

        // Add toggle panel to the filters panel
        filtersPanel.add(Box.createHorizontalGlue());
        filtersPanel.add(togglePanel);
    }

    private void createViewPanels() {
        // Create a card layout to switch between views
        viewCardLayout = new CardLayout();
        viewSwitchPanel = new JPanel(viewCardLayout);
        viewSwitchPanel.setBackground(backgroundColor);

        // Grid view panel
        bookGridPanel = new BookGridPanel();

        // List view panel
        listViewPanel = new JPanel();
        listViewPanel.setLayout(new BoxLayout(listViewPanel, BoxLayout.Y_AXIS));
        listViewPanel.setBackground(backgroundColor);

        listScrollPane = new JScrollPane(listViewPanel);
        listScrollPane.setBorder(null);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add both views to card layout
        viewSwitchPanel.add(bookGridPanel, "GRID");
        viewSwitchPanel.add(listScrollPane, "LIST");

        // Show default view (grid)
        viewCardLayout.show(viewSwitchPanel, "GRID");

        // Add to main panel
        this.add(viewSwitchPanel, BorderLayout.CENTER);
    }

    private void performSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        String category = (String) categoryComboBox.getSelectedItem();
        boolean availableOnly = availableOnlyCheckbox.isSelected();

        // Filter books
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            boolean matches = book.getName().toLowerCase().contains(searchTerm)
                    || book.getAuthor().toLowerCase().contains(searchTerm);

            boolean categoryMatches = (category != null && category.equals("All Categories"))
                    || book.getCategory().equals(category);

            boolean availabilityMatches = !availableOnly || book.isAvailable();

            if (matches && categoryMatches && availabilityMatches) {
                filteredBooks.add(book);
            }
        }

        // Sort books based on selected criteria
        sortBooks(filteredBooks);

        // Display results based on current view
        if (isGridView) {
            displayGridView(filteredBooks);
        } else {
            displayListView(filteredBooks);
        }
    }

    private void displayGridView(List<Book> filteredBooks) {
        if (filteredBooks.isEmpty()) {
            bookGridPanel.setBooks(new ArrayList<>());
        } else {
            bookGridPanel.setBooks(filteredBooks);
        }
    }

    private void displayListView(List<Book> filteredBooks) {
        // Clear previous results
        listViewPanel.removeAll();

        if (filteredBooks.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No books found matching your criteria");
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noResultsLabel.setForeground(mutedTextColor);
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel noResultsPanel = new JPanel();
            noResultsPanel.setLayout(new BoxLayout(noResultsPanel, BoxLayout.Y_AXIS));
            noResultsPanel.setBackground(backgroundColor);
            noResultsPanel.add(Box.createVerticalGlue());
            noResultsPanel.add(noResultsLabel);
            noResultsPanel.add(Box.createVerticalGlue());

            listViewPanel.add(noResultsPanel);
        } else {
            // Results count
            JLabel resultsCountLabel = new JLabel("Found " + filteredBooks.size() + " books");
            resultsCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
            resultsCountLabel.setForeground(textColor);
            resultsCountLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));

            JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            countPanel.setBackground(backgroundColor);
            countPanel.add(resultsCountLabel);
            countPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

            listViewPanel.add(countPanel);

            // Add book cards
            for (Book book : filteredBooks) {
                listViewPanel.add(createBookCard(book));
                // Add some spacing between cards
                listViewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        listViewPanel.revalidate();
        listViewPanel.repaint();
    }

    private void sortBooks(List<Book> books) {
        String sortCriteria = (String) sortComboBox.getSelectedItem();

        switch (sortCriteria) {
            case "Title (A-Z)":
                books.sort((b1, b2) -> b1.getName().compareTo(b2.getName()));
                break;
            case "Title (Z-A)":
                books.sort((b1, b2) -> b2.getName().compareTo(b1.getName()));
                break;
            case "Author (A-Z)":
                books.sort((b1, b2) -> b1.getAuthor().compareTo(b2.getAuthor()));
                break;
            case "Year (Newest)":
                books.sort((b1, b2) -> b2.getPublicationYear().compareTo(b1.getPublicationYear()));
                break;
            case "Year (Oldest)":
                books.sort((b1, b2) -> b1.getPublicationYear().compareTo(b2.getPublicationYear()));
                break;
        }
    }

    private JPanel createBookCard(Book book) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(10, 0));
        cardPanel.setBackground(cardBackground);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        // Set a minimum height to ensure buttons are visible
        cardPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 160));
        cardPanel.setMinimumSize(new Dimension(100, 160));

        // Book info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(cardBackground);

        // Title
        JLabel titleLabel = new JLabel(book.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(textColor);

        // Author
        JLabel authorLabel = new JLabel("by " + book.getAuthor());
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        authorLabel.setForeground(mutedTextColor);

        // Details
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        detailsPanel.setBackground(cardBackground);

        JLabel categoryLabel = new JLabel("• Category: " + book.getCategory());
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        categoryLabel.setForeground(mutedTextColor);

        JLabel yearLabel = new JLabel("• Published: " + book.getPublicationYear());
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        yearLabel.setForeground(mutedTextColor);

        JLabel availabilityLabel = new JLabel("• Status: " + (book.isAvailable() ? "Available (" + book.getQuantity() + ")" : "Out of Stock"));
        availabilityLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        availabilityLabel.setForeground(book.isAvailable() ? new Color(34, 197, 94) : new Color(239, 68, 68));

        detailsPanel.add(categoryLabel);
        detailsPanel.add(yearLabel);
        detailsPanel.add(availabilityLabel);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(authorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(detailsPanel);

        // Action buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(2, 1, 0, 10));
        actionPanel.setBackground(cardBackground);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        RoundedPanelButton viewDetailsButton = new RoundedPanelButton("View Details","",e ->JOptionPane.showMessageDialog(
                this,
                "Book Details:\n\n" +
                        "Title: " + book.getName() + "\n" +
                        "Author: " + book.getAuthor() + "\n" +
                        "Category: " + book.getCategory() + "\n" +
                        "Description: " + book.getDescription() + "\n" +
                        "Status: " + (book.isAvailable() ? "Available (" + book.getQuantity() + " in stock)" : "Out of Stock"),
                "Book Details",
                JOptionPane.INFORMATION_MESSAGE ));
        RoundedPanelButton borrowButton = new RoundedPanelButton("Borrow","",e->{if (book.isAvailable()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Would you like to borrow '" + book.getName() + "'?",
                    "Confirm Borrow",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // Call database to update book quantity
                if (userService.borrowBook(book.getId(), appFrame.user.id)) {
                    book.decrementQuantity();
                    JOptionPane.showMessageDialog(
                            this,
                            "You have successfully borrowed '" + book.getName() + "'.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    performSearch(); // Refresh results
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to borrow book. Please try again later.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        });


        // Add buttons to action panel - now in grid layout, one button per row
        actionPanel.add(viewDetailsButton);
        actionPanel.add(borrowButton);

        // Wrap action panel in a container to control its width
        JPanel actionContainer = new JPanel(new BorderLayout());
        actionContainer.setBackground(cardBackground);
        actionContainer.add(actionPanel, BorderLayout.CENTER);
        actionContainer.setPreferredSize(new Dimension(150, 0));

        // Add panels to card
        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(actionContainer, BorderLayout.EAST);

        // Card hover effect
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)));
                cardPanel.setBackground(new Color(249, 250, 251));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)));
                cardPanel.setBackground(cardBackground);
            }
        });

        return cardPanel;
    }
}
package screens.Librarian.librarianDashboard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// BookDetailsPage class to display detailed information about a book
public class BookDetailsPage extends JPanel {
    private final String bookTitle;
    private final String author;
    private final String isbn;
    private final String publisher;
    private final int publicationYear;
    private final String genre;
    private final String description;
    private final int totalCopies;
    private final int availableCopies;
    private final boolean isAvailable;
    private final double rating;

    // Constructor with book details parameters
    public BookDetailsPage(String bookTitle, String author, String isbn, String publisher,
                           int publicationYear, String genre, String description,
                           int totalCopies, int availableCopies, double rating) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.description = description;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.isAvailable = availableCopies > 0;
        this.rating = rating;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        createBookDetailsUI();
    }

    private void createBookDetailsUI() {
        // Header panel with title and back button
        JPanel headerPanel = createHeaderPanel();

        // Main content with book details
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);

        // Left panel for book cover and quick actions
        JPanel leftPanel = createLeftPanel();

        // Right panel for details
        JPanel rightPanel = createRightPanel();

        // Bottom panel for description and reviews
        JPanel bottomPanel = createBottomPanel();

        // Add panels to content panel
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        // Add all components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JButton backButton = new JButton("← Back to Catalog");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to go back to previous screen
                // Would be implemented by the parent container
            }
        });

        JLabel titleLabel = new JLabel(bookTitle);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        // Book cover panel
        JPanel coverPanel = new JPanel();
        coverPanel.setBackground(new Color(220, 220, 220));
        coverPanel.setPreferredSize(new Dimension(200, 300));
        coverPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        coverPanel.setLayout(new BorderLayout());

        JLabel coverLabel = new JLabel("Book Cover");
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverPanel.add(coverLabel, BorderLayout.CENTER);

        // Availability status
        JLabel statusLabel = new JLabel(isAvailable ? "Available" : "Not Available");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusLabel.setForeground(isAvailable ? new Color(46, 125, 50) : new Color(198, 40, 40));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Quick action buttons
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Actions",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)
        ));

        JButton borrowButton = new JButton(isAvailable ? "Borrow Book" : "Reserve Book");
        borrowButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        borrowButton.setFocusPainted(false);
        borrowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        borrowButton.setMaximumSize(new Dimension(180, 30));
        borrowButton.setEnabled(isAvailable);

        JButton wishlistButton = new JButton("Add to Wishlist");
        wishlistButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        wishlistButton.setFocusPainted(false);
        wishlistButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        wishlistButton.setMaximumSize(new Dimension(180, 30));

        JButton shareButton = new JButton("Share");
        shareButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        shareButton.setFocusPainted(false);
        shareButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shareButton.setMaximumSize(new Dimension(180, 30));

        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        actionsPanel.add(borrowButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(wishlistButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(shareButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        leftPanel.add(coverPanel);
        leftPanel.add(statusLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(actionsPanel);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Book Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16)
        ));

        // Book details in a grid layout
        JPanel detailsGrid = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsGrid.setOpaque(false);

        addDetailRow(detailsGrid, "Author:", author);
        addDetailRow(detailsGrid, "ISBN:", isbn);
        addDetailRow(detailsGrid, "Publisher:", publisher);
        addDetailRow(detailsGrid, "Publication Year:", String.valueOf(publicationYear));
        addDetailRow(detailsGrid, "Genre:", genre);
        addDetailRow(detailsGrid, "Total Copies:", String.valueOf(totalCopies));
        addDetailRow(detailsGrid, "Available Copies:", String.valueOf(availableCopies));

        // Rating section
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.setOpaque(false);

        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Display stars for rating
        JPanel starsPanel = createRatingStarsPanel(rating);

        JLabel ratingValueLabel = new JLabel(String.format("%.1f/5.0", rating));
        ratingValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        ratingPanel.add(ratingLabel);
        ratingPanel.add(starsPanel);
        ratingPanel.add(ratingValueLabel);

        rightPanel.add(detailsGrid);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(ratingPanel);

        return rightPanel;
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    private JPanel createRatingStarsPanel(double rating) {
        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        starsPanel.setOpaque(false);

        int fullStars = (int) Math.floor(rating);
        boolean hasHalfStar = (rating - fullStars) >= 0.5;

        // Add full stars
        for (int i = 0; i < fullStars; i++) {
            JLabel starLabel = new JLabel("★");
            starLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            starLabel.setForeground(new Color(255, 193, 7));
            starsPanel.add(starLabel);
        }

        // Add half star if needed
        if (hasHalfStar) {
            JLabel halfStarLabel = new JLabel("★");
            halfStarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            halfStarLabel.setForeground(new Color(255, 224, 130));
            starsPanel.add(halfStarLabel);
            fullStars++;
        }

        // Add empty stars
        for (int i = fullStars + (hasHalfStar ? 0 : 0); i < 5; i++) {
            JLabel emptyStarLabel = new JLabel("☆");
            emptyStarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyStarLabel.setForeground(new Color(180, 180, 180));
            starsPanel.add(emptyStarLabel);
        }

        return starsPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Description panel
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setOpaque(false);
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Book Description",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16)
        ));

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(getWidth(), 150));

        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        // Reviews panel
        JPanel reviewsPanel = createReviewsPanel();

        // Tabbed pane for bottom section
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.addTab("Description", descriptionPanel);
        tabbedPane.addTab("Reviews", reviewsPanel);
        tabbedPane.addTab("Similar Books", createSimilarBooksPanel());

        bottomPanel.add(tabbedPane, BorderLayout.CENTER);

        return bottomPanel;
    }

    private JPanel createReviewsPanel() {
        JPanel reviewsPanel = new JPanel(new BorderLayout());
        reviewsPanel.setOpaque(false);

        // Sample reviews
        String[] reviewers = {"John D.", "Sarah M.", "Robert K."};
        String[] dates = {"12 Mar 2025", "28 Feb 2025", "15 Jan 2025"};
        double[] ratings = {4.5, 5.0, 3.5};
        String[] comments = {
                "This book was exceptional! The character development was superb and the plot kept me engaged throughout.",
                "Absolutely loved this book. One of the author's best works. Couldn't put it down!",
                "Good book but the ending felt a bit rushed. Still enjoyed the overall story and would recommend."
        };

        JPanel reviewsListPanel = new JPanel();
        reviewsListPanel.setLayout(new BoxLayout(reviewsListPanel, BoxLayout.Y_AXIS));
        reviewsListPanel.setOpaque(false);

        for (int i = 0; i < reviewers.length; i++) {
            JPanel reviewCard = createReviewCard(reviewers[i], dates[i], ratings[i], comments[i]);
            reviewsListPanel.add(reviewCard);
            reviewsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(reviewsListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add review button
        JButton addReviewBtn = new JButton("Add Your Review");
        addReviewBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addReviewBtn.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addReviewBtn);

        reviewsPanel.add(scrollPane, BorderLayout.CENTER);
        reviewsPanel.add(buttonPanel, BorderLayout.SOUTH);

        return reviewsPanel;
    }

    private JPanel createReviewCard(String reviewer, String date, double rating, String comment) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(new Color(248, 249, 250));

        // Header with reviewer name, date and rating
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel reviewerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        reviewerPanel.setOpaque(false);

        JLabel reviewerLabel = new JLabel(reviewer);
        reviewerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel dateLabel = new JLabel("- " + date);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(108, 117, 125));

        reviewerPanel.add(reviewerLabel);
        reviewerPanel.add(dateLabel);

        JPanel ratingPanel = createRatingStarsPanel(rating);

        headerPanel.add(reviewerPanel, BorderLayout.WEST);
        headerPanel.add(ratingPanel, BorderLayout.EAST);

        // Comment text
        JTextArea commentArea = new JTextArea(comment);
        commentArea.setWrapStyleWord(true);
        commentArea.setLineWrap(true);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentArea.setEditable(false);
        commentArea.setBackground(card.getBackground());
        commentArea.setBorder(null);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(commentArea, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSimilarBooksPanel() {
        JPanel similarBooksPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        similarBooksPanel.setOpaque(false);
        similarBooksPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Sample similar books
        similarBooksPanel.add(createSimilarBookCard("Pride and Prejudice", "Jane Austen"));
        similarBooksPanel.add(createSimilarBookCard("The Catcher in the Rye", "J.D. Salinger"));
        similarBooksPanel.add(createSimilarBookCard("Lord of the Flies", "William Golding"));

        return similarBooksPanel;
    }

    private JPanel createSimilarBookCard(String title, String author) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(248, 249, 250));

        // Create a placeholder for book cover
        JPanel coverPanel = new JPanel();
        coverPanel.setBackground(new Color(200, 200, 200));
        coverPanel.setPreferredSize(new Dimension(120, 180));
        coverPanel.setMaximumSize(new Dimension(120, 180));
        coverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton viewButton = new JButton("View Details");
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewButton.setFocusPainted(false);
        viewButton.setMaximumSize(new Dimension(120, 30));

        card.add(coverPanel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(authorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(viewButton);

        return card;
    }

    // Static method to demonstrate usage
    public static void main(String[] args) {
        JFrame frame = new JFrame("Book Details Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        BookDetailsPage bookDetailsPage = new BookDetailsPage(
                "To Kill a Mockingbird",
                "Harper Lee",
                "978-0446310789",
                "Grand Central Publishing",
                1960,
                "Classic Fiction",
                "To Kill a Mockingbird is a novel by Harper Lee published in 1960. It was immediately successful, winning the Pulitzer Prize, and has become a classic of modern American literature. The plot and characters are loosely based on the author's observations of her family, her neighbors and an event that occurred near her hometown of Monroeville, Alabama, in 1936, when she was ten. The novel is renowned for its warmth and humor, despite dealing with serious issues of rape and racial inequality.",
                5,
                3,
                4.5
        );

        frame.add(bookDetailsPage);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
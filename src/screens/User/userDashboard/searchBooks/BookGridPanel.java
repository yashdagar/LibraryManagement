package screens.User.userDashboard.searchBooks;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import models.Book;

public class BookGridPanel extends JPanel {
    // Colors
    private final Color backgroundColor = new Color(243, 244, 246);
    private final Color textColor = new Color(31, 41, 55);
    private final Color mutedTextColor = new Color(107, 114, 128);
    private final Color priceColor = new Color(234, 88, 12);

    // Components
    private JPanel booksGrid; // Book list
    private int booksPerRow = 4;

    public BookGridPanel(ArrayList<Book> books) {
        this.setLayout(new BorderLayout());
        this.setBackground(backgroundColor);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize demo books
        initializeDemoBooks();

        // Create header
        createHeader();

        // Create books grid
        createBooksGrid();

        // Display books
        displayBooks();
    }



    private void createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(backgroundColor);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Featured Books");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);

        JLabel subtitleLabel = new JLabel("Explore our collection");
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

    private void createBooksGrid() {
        // Create a panel with a grid layout
        booksGrid = new JPanel();
        booksGrid.setBackground(backgroundColor);

        // Calculate rows needed based on book count and books per row
        int rows = (int) Math.ceil((double) books.size() / booksPerRow);
        booksGrid.setLayout(new GridLayout(rows, booksPerRow, 20, 30));

        JScrollPane scrollPane = new JScrollPane(booksGrid);
        scrollPane.setBorder(null);
        scrollPane.setBackground(backgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void displayBooks() {
        // Add book cards to the grid
        for (Book book : books) {
            booksGrid.add(createBookCard(book));
        }

        // Re-validate and repaint
        booksGrid.revalidate();
        booksGrid.repaint();
    }

    private JPanel createBookCard(Book book) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(0, 5));
        cardPanel.setBackground(backgroundColor);

        // Book image (placeholder)
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Enable antialiasing for smoother edges
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw a rounded rectangle as placeholder for book cover
                int arc = 15; // Rounded corner radius
                g2d.setColor(new Color(209, 213, 219));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

                // Display placeholder text
                g2d.setColor(Color.DARK_GRAY);
                FontMetrics fm = g2d.getFontMetrics();
                String imageName = book.getImageUrl().replace(".jpg", "");
                int textWidth = fm.stringWidth(imageName);
                int textHeight = fm.getHeight();
                g2d.drawString(imageName, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(180, 240);
            }
        };
        imagePanel.setBackground(Color.WHITE);

        // Book title
        JLabel titleLabel = new JLabel(book.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(textColor);

        // Category label
        JLabel categoryLabel = new JLabel("Design Low Book");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryLabel.setForeground(mutedTextColor);

        // Info panel that holds title, category and price
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(backgroundColor);
        infoPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Add components to info panel
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(titleLabel);

        // Add components to card
        cardPanel.add(imagePanel, BorderLayout.CENTER);
        cardPanel.add(infoPanel, BorderLayout.SOUTH);

        return cardPanel;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        displayBooks();
    }

    public void setBooksPerRow(int booksPerRow) {
        this.booksPerRow = booksPerRow;
        // Recalculate the grid layout
        int rows = (int) Math.ceil((double) books.size() / booksPerRow);
        booksGrid.setLayout(new GridLayout(rows, booksPerRow, 20, 30));
        booksGrid.revalidate();
        booksGrid.repaint();
    }
}
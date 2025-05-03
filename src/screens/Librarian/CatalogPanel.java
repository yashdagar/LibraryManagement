package screens.Librarian;

import components.ModernJTable;
import components.RoundedPanelButton;
import components.RoundedTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CatalogPanel extends JPanel {
    // Color scheme
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color backgroundColor = new Color(243, 244, 246);
    private final Color cardColor = Color.WHITE;
    private final Color textColor = new Color(31, 41, 55);

    private ModernJTable booksTable;
    private DefaultTableModel tableModel;
    private RoundedTextField searchField;
    private JComboBox<String> filterComboBox;
    private RoundedPanelButton addBookButton;

    // Sample data for demonstration
    private String[] columnNames = {"Book ID", "Title", "Author", "Category", "Available", "Total"};
    private Object[][] bookData = {
            {"BK001", "Introduction to Java Programming", "John Smith", "Technology", "5", "10"},
            {"BK002", "Data Structures and Algorithms", "Jane Doe", "Technology", "3", "8"},
            {"BK003", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "2", "5"},
            {"BK004", "Pride and Prejudice", "Jane Austen", "Fiction", "4", "6"},
            {"BK005", "Introduction to Psychology", "David Myers", "Psychology", "7", "12"},
            {"BK006", "Principles of Economics", "N. Gregory Mankiw", "Economics", "9", "15"},
            {"BK007", "Introduction to Algorithms", "Thomas H. Cormen", "Technology", "3", "7"},
            {"BK008", "Atomic Habits", "James Clear", "Self-Help", "0", "5"},
            {"BK009", "Educated: A Memoir", "Tara Westover", "Biography", "6", "8"},
            {"BK010", "To Kill a Mockingbird", "Harper Lee", "Fiction", "4", "10"},
            {"BK011", "The Art of Computer Programming", "Donald Knuth", "Technology", "1", "4"},
            {"BK012", "Brief History of Time", "Stephen Hawking", "Science", "3", "6"}
    };

    public CatalogPanel() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create and add the top panel with search and filter
        add(createTopPanel(), BorderLayout.NORTH);

        // Create and add the books table
        add(createBooksTable(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Title and Add Book button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(backgroundColor);

        JLabel titleLabel = new JLabel("Book Catalog");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);

        addBookButton = new RoundedPanelButton("Add New Book","",e ->{});


        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addBookButton, BorderLayout.EAST);

        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(backgroundColor);
        searchPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel searchLabel = new JLabel("Search:");
        searchField = new RoundedTextField(20);

        JLabel filterLabel = new JLabel("Filter by:");
        String[] filterOptions = {"All Categories", "Fiction", "Non-Fiction", "Technology", "Science", "Economics", "Psychology", "Biography", "Self-Help"};
        filterComboBox = new JComboBox<>(filterOptions);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(primaryColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(filterLabel);
        searchPanel.add(filterComboBox);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(searchButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane createBooksTable() {
        // Create table model with data
        tableModel = new DefaultTableModel(bookData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new ModernJTable(tableModel);
        booksTable.setRowHeight(40);
        booksTable.setFont(new Font("Arial", Font.PLAIN, 14));
        booksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setAutoCreateRowSorter(true);

        // Set column widths
        TableColumnModel columnModel = booksTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);    // ID
        columnModel.getColumn(1).setPreferredWidth(250);   // Title
        columnModel.getColumn(2).setPreferredWidth(150);   // Author
        columnModel.getColumn(3).setPreferredWidth(120);   // Category
        columnModel.getColumn(4).setPreferredWidth(80);    // Available
        columnModel.getColumn(5).setPreferredWidth(80);    // Total

        // Add right-click context menu
        JPopupMenu contextMenu = createContextMenu();
        booksTable.setComponentPopupMenu(contextMenu);

        // Add double-click listener
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = booksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String bookId = (String) booksTable.getValueAt(selectedRow, 0);
                        String title = (String) booksTable.getValueAt(selectedRow, 1);
                        showBookDetailsDialog(bookId, title);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));

        return scrollPane;
    }

    private JPopupMenu createContextMenu() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem viewDetailsItem = new JMenuItem("View Details");
        viewDetailsItem.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                String bookId = (String) booksTable.getValueAt(selectedRow, 0);
                String title = (String) booksTable.getValueAt(selectedRow, 1);
                showBookDetailsDialog(bookId, title);
            }
        });

        JMenuItem editItem = new JMenuItem("Edit Book");
        editItem.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                JOptionPane.showMessageDialog(this, "Edit functionality coming soon!", "Edit Book", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem deleteItem = new JMenuItem("Delete Book");
        deleteItem.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this book?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        menu.add(viewDetailsItem);
        menu.add(editItem);
        menu.add(deleteItem);

        return menu;
    }

    private void performSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        String selectedCategory = (String) filterComboBox.getSelectedItem();

        DefaultTableModel newModel = new DefaultTableModel(columnNames, 0);

        for (Object[] row : bookData) {
            String title = ((String) row[1]).toLowerCase();
            String author = ((String) row[2]).toLowerCase();
            String category = (String) row[3];

            boolean categoryMatch = selectedCategory.equals("All Categories") || category.equals(selectedCategory);
            boolean searchMatch = searchTerm.isEmpty() ||
                    title.contains(searchTerm) ||
                    author.contains(searchTerm);

            if (categoryMatch && searchMatch) {
                newModel.addRow(row);
            }
        }

        booksTable.setModel(newModel);

        // Reset column widths
        TableColumnModel columnModel = booksTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);    // ID
        columnModel.getColumn(1).setPreferredWidth(250);   // Title
        columnModel.getColumn(2).setPreferredWidth(150);   // Author
        columnModel.getColumn(3).setPreferredWidth(120);   // Category
        columnModel.getColumn(4).setPreferredWidth(80);    // Available
        columnModel.getColumn(5).setPreferredWidth(80);    // Total
    }

    private void showBookDetailsDialog(String bookId, String title) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Book Details: " + title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 400);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(cardColor);

        // Find the book data
        Object[] bookDetails = null;
        for (Object[] book : bookData) {
            if (book[0].equals(bookId)) {
                bookDetails = book;
                break;
            }
        }

        if (bookDetails != null) {
            // Book title
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Author
            JLabel authorLabel = new JLabel("Author: " + bookDetails[2]);
            authorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Category
            JLabel categoryLabel = new JLabel("Category: " + bookDetails[3]);
            categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Availability
            JLabel availabilityLabel = new JLabel("Available: " + bookDetails[4] + " of " + bookDetails[5]);
            availabilityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            availabilityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Additional sample details
            JLabel isbnLabel = new JLabel("ISBN: 978-3-16-148410-0");
            isbnLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            isbnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel publisherLabel = new JLabel("Publisher: Library Press");
            publisherLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            publisherLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel yearLabel = new JLabel("Publication Year: 2020");
            yearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            yearLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Description
            JLabel descLabel = new JLabel("Description:");
            descLabel.setFont(new Font("Arial", Font.BOLD, 14));
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea descArea = new JTextArea(
                    "This is a sample description for the book. It provides an overview of the content " +
                            "and may include information about the author's approach, the book's significance, " +
                            "and its target audience. In a real application, this would be a full description from the database."
            );
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);
            descArea.setBackground(cardColor);
            descArea.setFont(new Font("Arial", Font.PLAIN, 14));
            descArea.setAlignmentX(Component.LEFT_ALIGNMENT);

            contentPanel.add(titleLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPanel.add(authorLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(categoryLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(availabilityLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(isbnLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(publisherLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(yearLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            contentPanel.add(descLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(descArea);
        }

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(cardColor);

        JButton editButton = new JButton("Edit");
        editButton.setBackground(primaryColor);
        editButton.setForeground(Color.WHITE);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(editButton);
        buttonPanel.add(closeButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}

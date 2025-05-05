//package screens.Librarian.librarianDashboard;
//
//import models.Book;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableRowSorter;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BookManagementPanel extends JPanel {
//    private final Color primaryColor = new Color(79, 70, 229);
//    private final Color backgroundColor = new Color(243, 244, 246);
//    private final Color textColor = new Color(31, 41, 55);
//    private final Color accentColor = new Color(99, 102, 241);
//
//    private JTable booksTable;
//    private DefaultTableModel tableModel;
//    private JTextField searchField;
//    private List<Book> booksList;
//
//    public BookManagementPanel() {
//        setLayout(new BorderLayout());
//        setBackground(backgroundColor);
//
//        // Initialize the list of books (replace with DB call in production)
//        initializeSampleData();
//
//        // Create the header panel
//        JPanel headerPanel = createHeaderPanel();
//        add(headerPanel, BorderLayout.NORTH);
//
//        // Create the table panel
//        JPanel tablePanel = createTablePanel();
//        add(tablePanel, BorderLayout.CENTER);
//
//        // Create the action panel
//        JPanel actionPanel = createActionPanel();
//        add(actionPanel, BorderLayout.SOUTH);
//    }
//
//    private void initializeSampleData() {
//        booksList = new ArrayList<>();
//        // Add some sample books
//        booksList.add(new Book("B001", "To Kill a Mockingbird", "Harper Lee", true));
//        booksList.add(new Book("B002", "1984", "George Orwell", true));
//        booksList.add(new Book("B003", "The Great Gatsby", "F. Scott Fitzgerald", false));
//        booksList.add(new Book("B004", "Pride and Prejudice", "Jane Austen", true));
//        booksList.add(new Book("B005", "The Catcher in the Rye", "J.D. Salinger", false));
//    }
//
//    private JPanel createHeaderPanel() {
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        headerPanel.setBackground(backgroundColor);
//        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
//
//        // Title
//        JLabel titleLabel = new JLabel("Book Management");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setForeground(textColor);
//
//        // Search panel
//        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        searchPanel.setBackground(backgroundColor);
//
//        JLabel searchLabel = new JLabel("Search: ");
//        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        searchField = new JTextField(20);
//        searchField.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                String text = searchField.getText().toLowerCase();
//                filterTable(text);
//            }
//        });
//
//        searchPanel.add(searchLabel);
//        searchPanel.add(searchField);
//
//        headerPanel.add(titleLabel, BorderLayout.WEST);
//        headerPanel.add(searchPanel, BorderLayout.EAST);
//
//        return headerPanel;
//    }
//
//    private JPanel createTablePanel() {
//        JPanel tablePanel = new JPanel(new BorderLayout());
//        tablePanel.setBackground(backgroundColor);
//        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//
//        // Create table model with non-editable cells
//        tableModel = new DefaultTableModel() {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//
//        // Add columns
//        tableModel.addColumn("Book ID");
//        tableModel.addColumn("Title");
//        tableModel.addColumn("Author");
//        tableModel.addColumn("Availability");
//
//        // Add data from book list
//        for (Book book : booksList) {
//            tableModel.addRow(new Object[]{
//                    book.getId(),
//                    book.getTitle(),
//                    book.getAuthor(),
//                    book.isAvailable() ? "Available" : "Checked Out"
//            });
//        }
//
//        // Create table and set properties
//        booksTable = new JTable(tableModel);
//        booksTable.setAutoCreateRowSorter(true);
//        booksTable.setRowHeight(25);
//        booksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
//        booksTable.getTableHeader().setBackground(primaryColor);
//        booksTable.getTableHeader().setForeground(Color.WHITE);
//        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        booksTable.setFont(new Font("Arial", Font.PLAIN, 14));
//
//        // Scroll pane
//        JScrollPane scrollPane = new JScrollPane(booksTable);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        tablePanel.add(scrollPane, BorderLayout.CENTER);
//
//        return tablePanel;
//    }
//
//    private JPanel createActionPanel() {
//        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        actionPanel.setBackground(backgroundColor);
//        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
//
//        // Create buttons
//        JButton addButton = createStyledButton("Add Book", primaryColor);
//        JButton viewButton = createStyledButton("View Details", accentColor);
//        JButton deleteButton = createStyledButton("Delete Book", new Color(220, 38, 38));
//
//        // Add action listeners
//        addButton.addActionListener(e -> showAddBookDialog());
//
//        viewButton.addActionListener(e -> {
//            int selectedRow = booksTable.getSelectedRow();
//            if (selectedRow >= 0) {
//                // Convert row index to model index in case table is sorted
//                int modelRow = booksTable.convertRowIndexToModel(selectedRow);
//                String bookId = (String) tableModel.getValueAt(modelRow, 0);
//                Book selectedBook = findBookById(bookId);
//                if (selectedBook != null) {
//                    showBookDetailsDialog(selectedBook);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Please select a book to view details.",
//                        "No Selection",
//                        JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        deleteButton.addActionListener(e -> {
//            int selectedRow = booksTable.getSelectedRow();
//            if (selectedRow >= 0) {
//                // Convert row index to model index in case table is sorted
//                int modelRow = booksTable.convertRowIndexToModel(selectedRow);
//                String bookId = (String) tableModel.getValueAt(modelRow, 0);
//                Book selectedBook = findBookById(bookId);
//
//                if (selectedBook != null) {
//                    int result = JOptionPane.showConfirmDialog(this,
//                            "Are you sure you want to delete the book: " + selectedBook.getTitle() + "?",
//                            "Confirm Deletion",
//                            JOptionPane.YES_NO_OPTION,
//                            JOptionPane.WARNING_MESSAGE);
//
//                    if (result == JOptionPane.YES_OPTION) {
//                        booksList.remove(selectedBook);
//                        tableModel.removeRow(modelRow);
//                        JOptionPane.showMessageDialog(this,
//                                "Book deleted successfully!",
//                                "Success",
//                                JOptionPane.INFORMATION_MESSAGE);
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Please select a book to delete.",
//                        "No Selection",
//                        JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        actionPanel.add(addButton);
//        actionPanel.add(viewButton);
//        actionPanel.add(deleteButton);
//
//        return actionPanel;
//    }
//
//    private JButton createStyledButton(String text, Color bgColor) {
//        JButton button = new JButton(text);
//        button.setBackground(bgColor);
//        button.setForeground(Color.WHITE);
//        button.setFocusPainted(false);
//        button.setFont(new Font("Arial", Font.BOLD, 14));
//        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        return button;
//    }
//
//    private void filterTable(String text) {
//        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
//        booksTable.setRowSorter(sorter);
//
//        if (text.isEmpty()) {
//            sorter.setRowFilter(null);
//        } else {
//            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
//        }
//    }
//
//    private Book findBookById(String id) {
//        for (Book book : booksList) {
//            if (book.getId() != null && book.getId().equals(id)) {
//                return book;
//            }
//        }
//        return null;
//    }
//
//    private void showAddBookDialog() {
//        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);
//        dialog.setLayout(new BorderLayout());
//        dialog.setSize(500, 400);
//        dialog.setLocationRelativeTo(this);
//
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        formPanel.setBackground(backgroundColor);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(5, 5, 5, 5);
//
//        // Book ID field
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        JLabel idLabel = new JLabel("Book ID:");
//        idLabel.setForeground(textColor);
//        formPanel.add(idLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0;
//        JTextField idField = new JTextField(20);
//        formPanel.add(idField, gbc);
//
//        // Title field
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.weightx = 0.0;
//        JLabel titleLabel = new JLabel("Title:");
//        titleLabel.setForeground(textColor);
//        formPanel.add(titleLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        gbc.weightx = 1.0;
//        JTextField titleField = new JTextField(20);
//        formPanel.add(titleField, gbc);
//
//        // Author field
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.weightx = 0.0;
//        JLabel authorLabel = new JLabel("Author:");
//        authorLabel.setForeground(textColor);
//        formPanel.add(authorLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        gbc.weightx = 1.0;
//        JTextField authorField = new JTextField(20);
//        formPanel.add(authorField, gbc);
//
//        // Availability field
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.weightx = 0.0;
//        JLabel availabilityLabel = new JLabel("Availability:");
//        availabilityLabel.setForeground(textColor);
//        formPanel.add(availabilityLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 3;
//        gbc.weightx = 1.0;
//        String[] availabilityOptions = {"Available", "Checked Out"};
//        JComboBox<String> availabilityComboBox = new JComboBox<>(availabilityOptions);
//        formPanel.add(availabilityComboBox, gbc);
//
//        // Button panel
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttonPanel.setBackground(backgroundColor);
//
//        JButton cancelButton = new JButton("Cancel");
//        cancelButton.addActionListener(e -> dialog.dispose());
//
//        JButton addButton = new JButton("Add Book");
//        addButton.setBackground(primaryColor);
//        addButton.setForeground(Color.WHITE);
//        addButton.addActionListener(e -> {
//            // Validate input
//            if (idField.getText().isEmpty() || titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(dialog,
//                        "Please fill in all fields.",
//                        "Missing Information",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // Check if ID already exists
//            for (Book book : booksList) {
//                if (book.getId() != null && book.getId().equals(idField.getText())) {
//                    JOptionPane.showMessageDialog(dialog,
//                            "A book with this ID already exists.",
//                            "Duplicate ID",
//                            JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//            }
//
//            // Add new book
//            boolean isAvailable = availabilityComboBox.getSelectedItem().equals("Available");
//            Book newBook = new Book(
//                    idField.getText(),
//                    titleField.getText(),
//                    authorField.getText(),
//                    isAvailable
//            );
//
//            booksList.add(newBook);
//            tableModel.addRow(new Object[]{
//                    newBook.getId(),
//                    newBook.getTitle(),
//                    newBook.getAuthor(),
//                    newBook.isAvailable() ? "Available" : "Checked Out"
//            });
//
//            JOptionPane.showMessageDialog(dialog,
//                    "Book \"" + titleField.getText() + "\" has been added successfully!",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE);
//            dialog.dispose();
//        });
//
//        buttonPanel.add(cancelButton);
//        buttonPanel.add(addButton);
//
//        dialog.add(formPanel, BorderLayout.CENTER);
//        dialog.add(buttonPanel, BorderLayout.SOUTH);
//        dialog.setVisible(true);
//    }
//
//    private void showBookDetailsDialog(Book book) {
//        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Book Details", true);
//        dialog.setLayout(new BorderLayout());
//        dialog.setSize(500, 300);
//        dialog.setLocationRelativeTo(this);
//
//        JPanel detailsPanel = new JPanel(new GridBagLayout());
//        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        detailsPanel.setBackground(backgroundColor);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        // Book ID
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        JLabel idLabelTitle = new JLabel("Book ID:");
//        idLabelTitle.setFont(new Font("Arial", Font.BOLD, 14));
//        idLabelTitle.setForeground(textColor);
//        detailsPanel.add(idLabelTitle, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        JLabel idLabel = new JLabel(book.getId());
//        idLabel.setForeground(textColor);
//        detailsPanel.add(idLabel, gbc);
//
//        // Title
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        JLabel titleLabelTitle = new JLabel("Title:");
//        titleLabelTitle.setFont(new Font("Arial", Font.BOLD, 14));
//        titleLabelTitle.setForeground(textColor);
//        detailsPanel.add(titleLabelTitle, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        JLabel titleLabel = new JLabel(book.getTitle());
//        titleLabel.setForeground(textColor);
//        detailsPanel.add(titleLabel, gbc);
//
//        // Author
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        JLabel authorLabelTitle = new JLabel("Author:");
//        authorLabelTitle.setFont(new Font("Arial", Font.BOLD, 14));
//        authorLabelTitle.setForeground(textColor);
//        detailsPanel.add(authorLabelTitle, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        JLabel authorLabel = new JLabel(book.getAuthor());
//        authorLabel.setForeground(textColor);
//        detailsPanel.add(authorLabel, gbc);
//
//        // Availability
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        JLabel availabilityLabelTitle = new JLabel("Availability:");
//        availabilityLabelTitle.setFont(new Font("Arial", Font.BOLD, 14));
//        availabilityLabelTitle.setForeground(textColor);
//        detailsPanel.add(availabilityLabelTitle, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 3;
//        JLabel availabilityLabel = new JLabel(book.isAvailable() ? "Available" : "Checked Out");
//        availabilityLabel.setForeground(book.isAvailable() ? new Color(16, 185, 129) : new Color(239, 68, 68));
//        availabilityLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        detailsPanel.add(availabilityLabel, gbc);
//
//        // Button panel
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttonPanel.setBackground(backgroundColor);
//
//        JButton closeButton = new JButton("Close");
//        closeButton.addActionListener(e -> dialog.dispose());
//
//        JButton toggleAvailabilityButton = new JButton(book.isAvailable() ? "Mark as Checked Out" : "Mark as Available");
//        toggleAvailabilityButton.setBackground(book.isAvailable() ? new Color(239, 68, 68) : new Color(16, 185, 129));
//        toggleAvailabilityButton.setForeground(Color.WHITE);
//        toggleAvailabilityButton.addActionListener(e -> {
//            // Toggle availability
//            book.setAvailable(!book.isAvailable());
//
//            // Update table
//            for (int i = 0; i < tableModel.getRowCount(); i++) {
//                if (tableModel.getValueAt(i, 0).equals(book.getId())) {
//                    tableModel.setValueAt(book.isAvailable() ? "Available" : "Checked Out", i, 3);
//                    break;
//                }
//            }
//
//            // Close dialog
//            dialog.dispose();
//
//            // Show confirmation
//            JOptionPane.showMessageDialog(this,
//                    "Book \"" + book.getTitle() + "\" is now " +
//                            (book.isAvailable() ? "available" : "checked out") + ".",
//                    "Status Updated",
//                    JOptionPane.INFORMATION_MESSAGE);
//        });
//
//        buttonPanel.add(toggleAvailabilityButton);
//        buttonPanel.add(closeButton);
//
//        dialog.add(detailsPanel, BorderLayout.CENTER);
//        dialog.add(buttonPanel, BorderLayout.SOUTH);
//        dialog.setVisible(true);
//    }
//}
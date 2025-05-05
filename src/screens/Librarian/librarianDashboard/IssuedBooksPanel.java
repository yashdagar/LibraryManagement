package screens.Librarian.librarianDashboard;

import models.IssuedBook;
import services.LibrarianAuthService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class IssuedBooksPanel extends JPanel {
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color lightPrimaryColor = new Color(99, 102, 241);
    private final Color backgroundColor = new Color(243, 244, 246);

    private LibrarianAuthService librarianService;
    private JTable issuedBooksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public IssuedBooksPanel(LibrarianAuthService librarianService) {
        this.librarianService = librarianService;
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(backgroundColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Issued Books");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(31, 41, 55));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Search and filter panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(backgroundColor);

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> filterTable());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadIssuedBooks());

        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(refreshButton);

        titlePanel.add(controlPanel, BorderLayout.EAST);

        // Table to display issued books
        String[] columnNames = {"ID", "Book Title", "Author", "Member Name", "Issue Date", "Due Date", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only the Actions column is editable
            }
        };

        issuedBooksTable = new JTable(tableModel);
        issuedBooksTable.setFillsViewportHeight(true);
        issuedBooksTable.setRowHeight(30);
        issuedBooksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        issuedBooksTable.getTableHeader().setBackground(new Color(237, 233, 254));

        // Set up button renderer for the Actions column
        issuedBooksTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        issuedBooksTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox(), this));

        // Set preferred column widths
        issuedBooksTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        issuedBooksTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        issuedBooksTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        issuedBooksTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        issuedBooksTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        issuedBooksTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        issuedBooksTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        issuedBooksTable.getColumnModel().getColumn(7).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(issuedBooksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Statistics panel
        JPanel statsPanel = createStatsPanel();

        // Add all components to the main panel
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);

        // Load data
        loadIssuedBooks();
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setBackground(backgroundColor);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel totalBooksLabel = new JLabel("Total Books Issued: 0");
        totalBooksLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel overdueBooksLabel = new JLabel("Overdue Books: 0");
        overdueBooksLabel.setFont(new Font("Arial", Font.BOLD, 14));
        overdueBooksLabel.setForeground(new Color(220, 38, 38));

        statsPanel.add(totalBooksLabel);
        statsPanel.add(overdueBooksLabel);

        // Store references to update later
        statsPanel.putClientProperty("totalBooksLabel", totalBooksLabel);
        statsPanel.putClientProperty("overdueBooksLabel", overdueBooksLabel);

        return statsPanel;
    }

    public void loadIssuedBooks() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Get issued books from service
        List<IssuedBook> issuedBooks = librarianService.getAllIssuedBooks();

        // Format date for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

        int overdueCount = 0;
        for (IssuedBook book : issuedBooks) {
            // Check if book is overdue
            if (book.status.equals("OVERDUE")) {
                overdueCount++;
            }

            // Create a button for actions
            JButton returnButton = new JButton("Return");

            // Add data to table
            tableModel.addRow(new Object[]{
                    book.id,
                    book.bookTitle,
                    book.bookAuthor,
                    book.memberName,
                    dateFormat.format(book.issueDate),
                    dateFormat.format(book.dueDate),
                    book.status,
                    "Return"  // This will be rendered as a button
            });
        }

        // Update statistics
        JPanel statsPanel = (JPanel) getComponent(2);
        JLabel totalBooksLabel = (JLabel) statsPanel.getClientProperty("totalBooksLabel");
        JLabel overdueBooksLabel = (JLabel) statsPanel.getClientProperty("overdueBooksLabel");

        totalBooksLabel.setText("Total Books Issued: " + issuedBooks.size());
        overdueBooksLabel.setText("Overdue Books: " + overdueCount);

        // Update table
        issuedBooksTable.repaint();
    }

    private void filterTable() {
        String searchTerm = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        issuedBooksTable.setRowSorter(sorter);

        if (searchTerm.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm));
        }
    }

    public void handleReturnBook(int row) {
        int modelRow = issuedBooksTable.convertRowIndexToModel(row);
        int issuedBookId = (int) tableModel.getValueAt(modelRow, 0);
        String bookTitle = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to mark this book as returned?\n" +
                        "Book: " + bookTitle,
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Assume librarian ID is 1 for now, this should be the logged-in librarian's ID
            boolean success = librarianService.returnBook(issuedBookId, 1);

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Book has been marked as returned successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadIssuedBooks(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to process return. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Custom button renderer for table
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setBackground(primaryColor);
            setForeground(Color.WHITE);
            return this;
        }
    }

    // Custom button editor for table
    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private boolean isPushed;
        private final IssuedBooksPanel panel;

        public ButtonEditor(JCheckBox checkBox, IssuedBooksPanel panel) {
            super(checkBox);
            this.panel = panel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = value.toString();
            button.setText(label);
            button.setBackground(primaryColor);
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                panel.handleReturnBook(issuedBooksTable.getSelectedRow());
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
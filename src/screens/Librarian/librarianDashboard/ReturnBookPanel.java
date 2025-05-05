//package screens.Librarian.librarianDashboard;
//
//import components.RoundedPanel;
//import components.RoundedPanelButton;
//import components.RoundedTextField;
//import components.SnackBar;
//import models.IssuedBook;
//import services.LibrarianAuthService;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//public class ReturnBookPanel extends JPanel {
//    private final Color primaryColor = new Color(79, 70, 229);
//    private final Color lightPrimaryColor = new Color(99, 102, 241);
//    private final Color backgroundColor = new Color(243, 244, 246);
//    private final Color successColor = new Color(46, 204, 113);
//    private final Color warningColor = new Color(231, 76, 60);
//
//    private RoundedTextField studentIdField;
//    private RoundedTextField bookIdField;
//    private JLabel daysLateLabel;
//    private JLabel fineAmountLabel;
//    private RoundedPanelButton calculateFineBtn;
//    private RoundedPanelButton confirmReturnBtn;
//    private final double FINE_RATE_PER_DAY = 1.50; // $1.50 per day
//    private LibrarianAuthService librarianService;
//    private JPanel parentPanel;
//
//    public ReturnBookPanel(LibrarianAuthService librarianService) {
//        this.librarianService = librarianService;
//        this.parentPanel = this;
//        setLayout(new BorderLayout());
//        setBackground(backgroundColor);
//        setBorder(new EmptyBorder(20, 20, 20, 20));
//        initComponents();
//    }
//
//    private void initComponents() {
//        // Title Panel
//        JPanel titlePanel = new JPanel(new BorderLayout());
//        titlePanel.setBackground(backgroundColor);
//        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
//
//        JLabel titleLabel = new JLabel("Return Book");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setForeground(new Color(31, 41, 55));
//        titlePanel.add(titleLabel, BorderLayout.WEST);
//
//        // Form Panel
//        JPanel formPanel = new RoundedPanel(15, 15, 15, 15);
//        formPanel.setBackground(Color.WHITE);
//        formPanel.setLayout(new GridBagLayout());
//        ((RoundedPanel)formPanel).setPadding(20, 20, 20, 20);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(10, 10, 10, 10);
//
//        // Student ID
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        JLabel studentIdLabel = new JLabel("Student ID:");
//        studentIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        formPanel.add(studentIdLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0;
//        studentIdField = new RoundedTextField(20);
//        studentIdField.setFont(new Font("Arial", Font.PLAIN, 14));
//        formPanel.add(studentIdField, gbc);
//
//        // Book ID
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.weightx = 0.0;
//        JLabel bookIdLabel = new JLabel("Book ID:");
//        bookIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        formPanel.add(bookIdLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        gbc.weightx = 1.0;
//        bookIdField = new RoundedTextField(20);
//        bookIdField.setFont(new Font("Arial", Font.PLAIN, 14));
//        formPanel.add(bookIdField, gbc);
//
//        // Days Late
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.weightx = 0.0;
//        JLabel daysLateTitleLabel = new JLabel("Days Late:");
//        daysLateTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        formPanel.add(daysLateTitleLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        gbc.weightx = 1.0;
//        daysLateLabel = new JLabel("0");
//        daysLateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        formPanel.add(daysLateLabel, gbc);
//
//        // Fine Amount
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.weightx = 0.0;
//        JLabel fineAmountTitleLabel = new JLabel("Fine Amount:");
//        fineAmountTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        formPanel.add(fineAmountTitleLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridy = 3;
//        gbc.weightx = 1.0;
//        fineAmountLabel = new JLabel("$0.00");
//        fineAmountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//        formPanel.add(fineAmountLabel, gbc);
//
//        // Buttons Panel
//        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        buttonsPanel.setOpaque(false);
//
//        calculateFineBtn = new RoundedPanelButton("Calculate Fine");
//        calculateFineBtn.setBackground(lightPrimaryColor);
//        calculateFineBtn.setForeground(Color.WHITE);
//        calculateFineBtn.setFont(new Font("Arial", Font.BOLD, 14));
//        calculateFineBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                calculateFine();
//            }
//        });
//
//        confirmReturnBtn = new RoundedPanelButton("Confirm Return");
//        confirmReturnBtn.setBackground(successColor);
//        confirmReturnBtn.setForeground(Color.WHITE);
//        confirmReturnBtn.setFont(new Font("Arial", Font.BOLD, 14));
//        confirmReturnBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                confirmReturn();
//            }
//        });
//
//        buttonsPanel.add(calculateFineBtn);
//        buttonsPanel.add(confirmReturnBtn);
//
//        // Add buttons to form panel
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        gbc.gridwidth = 2;
//        gbc.weightx = 1.0;
//        gbc.anchor = GridBagConstraints.CENTER;
//        formPanel.add(buttonsPanel, gbc);
//
//        // Information Panel
//        JPanel infoPanel = new JPanel(new BorderLayout());
//        infoPanel.setBackground(backgroundColor);
//        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
//
//        JLabel infoLabel = new JLabel("<html><b>Note:</b> Fines are calculated at a rate of $" +
//                FINE_RATE_PER_DAY + " per day for overdue books.</html>");
//        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
//        infoPanel.add(infoLabel, BorderLayout.WEST);
//
//        // Add everything to main panel
//        add(titlePanel, BorderLayout.NORTH);
//        add(formPanel, BorderLayout.CENTER);
//        add(infoPanel, BorderLayout.SOUTH);
//    }
//
//    private void calculateFine() {
//        String studentId = studentIdField.getText().trim();
//        String bookId = bookIdField.getText().trim();
//
//        if (studentId.isEmpty() || bookId.isEmpty()) {
//            showMessage("Please enter both Student ID and Book ID", warningColor);
//            return;
//        }
//
//        try {
//            // Get issue details from service
//            IssuedBook issuedBook = librarianService.getIssuedBookByIds(studentId, bookId);
//            if (issuedBook == null) {
//                showMessage("No active loans found for this Student and Book combination", warningColor);
//                return;
//            }
//
//            if (!issuedBook.status.equals("ISSUED") && !issuedBook.status.equals("OVERDUE")) {
//                showMessage("This book has already been returned", warningColor);
//                return;
//            }
//
//            // Calculate days late
//            Date currentDate = new Date();
//            long diffInMillies = currentDate.getTime() - issuedBook.dueDate.getTime();
//            long daysLate = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//
//            if (daysLate < 0) {
//                daysLate = 0;
//            }
//
//            // Update UI
//            daysLateLabel.setText(String.valueOf(daysLate));
//            double fine = daysLate * FINE_RATE_PER_DAY;
//            fineAmountLabel.setText(String.format("$%.2f", fine));
//
//            if (daysLate > 0) {
//                daysLateLabel.setForeground(warningColor);
//                fineAmountLabel.setForeground(warningColor);
//            } else {
//                daysLateLabel.setForeground(Color.BLACK);
//                fineAmountLabel.setForeground(Color.BLACK);
//            }
//
//            showMessage("Fine calculated successfully", primaryColor);
//
//        } catch (Exception ex) {
//            showMessage("Error calculating fine: " + ex.getMessage(), warningColor);
//        }
//    }
//
//    private void confirmReturn() {
//        String studentId = studentIdField.getText().trim();
//        String bookId = bookIdField.getText().trim();
//
//        if (studentId.isEmpty() || bookId.isEmpty()) {
//            showMessage("Please enter both Student ID and Book ID", warningColor);
//            return;
//        }
//
//        try {
//            // First check if the book is actually issued to this student
//            IssuedBook issuedBook = librarianService.getIssuedBookByIds(studentId, bookId);
//            if (issuedBook == null) {
//                showMessage("No active loans found for this Student and Book combination", warningColor);
//                return;
//            }
//
//            if (!issuedBook.status.equals("ISSUED") && !issuedBook.status.equals("OVERDUE")) {
//                showMessage("This book has already been returned", warningColor);
//                return;
//            }
//
//            // Get confirmation from user
//            int confirm = JOptionPane.showConfirmDialog(
//                    this,
//                    "Are you sure you want to mark this book as returned?\n" +
//                            "Book: " + issuedBook.bookTitle + "\n" +
//                            "Student: " + issuedBook.memberName + "\n" +
//                            "Fine Amount: " + fineAmountLabel.getText(),
//                    "Confirm Return",
//                    JOptionPane.YES_NO_OPTION
//            );
//
//            if (confirm == JOptionPane.YES_OPTION) {
//                // Process the return
//                // Assume librarian ID is 1 for now, this should be the logged-in librarian's ID
//                boolean success = librarianService.returnBook(issuedBook.id, 1);
//
//                if (success) {
//                    // Clear fields and reset labels
//                    studentIdField.setText("");
//                    bookIdField.setText("");
//                    daysLateLabel.setText("0");
//                    fineAmountLabel.setText("$0.00");
//                    daysLateLabel.setForeground(Color.BLACK);
//                    fineAmountLabel.setForeground(Color.BLACK);
//
//                    showMessage("Book returned successfully!", successColor);
//                } else {
//                    showMessage("Failed to process return. Please try again.", warningColor);
//                }
//            }
//
//        } catch (Exception ex) {
//            showMessage("Error returning book: " + ex.getMessage(), warningColor);
//        }
//    }
//
//    private void showMessage(String message, Color color) {
//        SnackBar snackBar = new SnackBar(parentPanel, message, 3000);
//        snackBar.setBackground(color);
//        snackBar.show();
//    }
//}
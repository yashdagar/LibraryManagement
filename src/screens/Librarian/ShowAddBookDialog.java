package screens.Librarian;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class ShowAddBookDialog {
    public static  final Color primaryColor = new Color(79, 70, 229);
    public static  final Color lightPrimaryColor = new Color(99, 102, 241);
    public static  final Color secondaryColor = new Color(249, 168, 212);
    public static  final Color accentColor = new Color(110, 231, 183);
    public static  final Color backgroundColor = new Color(243, 244, 246);
    public static  final Color cardColor = Color.WHITE;
    public static  final Color textColor = new Color(31, 41, 55);
    public static  final Color redColor = new Color(239, 68, 68);
    public static  final Color yellowColor = new Color(245, 158, 11);
    public static  final Color greenColor = new Color(16, 185, 129);

    public static void showAddBookDialog(JFrame frame) {
        // Create dialog
        JDialog addBookDialog = new JDialog(frame, "Add New Book", true);
        addBookDialog.setSize(650, 500);
        addBookDialog.setLocationRelativeTo(frame);
        addBookDialog.setLayout(new BorderLayout());
        addBookDialog.getContentPane().setBackground(backgroundColor);

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(backgroundColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JLabel titleLabel = new JLabel("Add New Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(textColor);

        titlePanel.add(titleLabel);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(backgroundColor);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Image panel on the left
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(cardColor);
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true));
        imagePanel.setPreferredSize(new Dimension(200, 300));

        JLabel imageLabel = new JLabel("No Image Selected");
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        imageLabel.setForeground(new Color(107, 114, 128));

        JPanel imageButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imageButtonPanel.setBackground(cardColor);

        JButton uploadButton = new JButton("Upload Image");
        uploadButton.setBackground(primaryColor);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

                int result = fileChooser.showOpenDialog(addBookDialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage originalImage = ImageIO.read(selectedFile);

                        // Resize image to fit the panel
                        Image resizedImage = originalImage.getScaledInstance(180, 250, Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(resizedImage);

                        imageLabel.setIcon(imageIcon);
                        imageLabel.setText(""); // Clear the text
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(addBookDialog,
                                "Error loading image: " + ex.getMessage(),
                                "Image Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        imageButtonPanel.add(uploadButton);

        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(imageButtonPanel, BorderLayout.SOUTH);

        // Form panel on the right
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(backgroundColor);

        // Book Title
        JPanel titleFieldPanel = new JPanel(new BorderLayout());
        titleFieldPanel.setBackground(backgroundColor);
        titleFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel bookTitleLabel = new JLabel("Book Title*");
        bookTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bookTitleLabel.setForeground(textColor);

        JTextField bookTitleField = new JTextField();
        bookTitleField.setFont(new Font("Arial", Font.PLAIN, 14));
        bookTitleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        titleFieldPanel.add(bookTitleLabel, BorderLayout.NORTH);
        titleFieldPanel.add(bookTitleField, BorderLayout.CENTER);

        // Author
        JPanel authorFieldPanel = new JPanel(new BorderLayout());
        authorFieldPanel.setBackground(backgroundColor);
        authorFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel authorLabel = new JLabel("Author*");
        authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        authorLabel.setForeground(textColor);

        JTextField authorField = new JTextField();
        authorField.setFont(new Font("Arial", Font.PLAIN, 14));
        authorField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        authorFieldPanel.add(authorLabel, BorderLayout.NORTH);
        authorFieldPanel.add(authorField, BorderLayout.CENTER);

        // Description
        JPanel descFieldPanel = new JPanel(new BorderLayout());
        descFieldPanel.setBackground(backgroundColor);
        descFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descLabel.setForeground(textColor);

        JTextArea descField = new JTextArea(4, 20);
        descField.setFont(new Font("Arial", Font.PLAIN, 14));
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(descField);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        descFieldPanel.add(descLabel, BorderLayout.NORTH);
        descFieldPanel.add(scrollPane, BorderLayout.CENTER);

        // Quantity
        JPanel qtyFieldPanel = new JPanel(new BorderLayout());
        qtyFieldPanel.setBackground(backgroundColor);
        qtyFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel qtyLabel = new JLabel("Quantity*");
        qtyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        qtyLabel.setForeground(textColor);

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner qtySpinner = new JSpinner(spinnerModel);
        qtySpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        JComponent editor = qtySpinner.getEditor();
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);

        qtyFieldPanel.add(qtyLabel, BorderLayout.NORTH);
        qtyFieldPanel.add(qtySpinner, BorderLayout.CENTER);

        // Category dropdown
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBackground(backgroundColor);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel categoryLabel = new JLabel("Category*");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setForeground(textColor);

        String[] categories = {"Fiction", "Non-Fiction", "Science", "History",
                "Biography", "Children", "Reference", "Other"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        categoryPanel.add(categoryLabel, BorderLayout.NORTH);
        categoryPanel.add(categoryComboBox, BorderLayout.CENTER);

        // Add all components to form panel
        formPanel.add(titleFieldPanel);
        formPanel.add(authorFieldPanel);
        formPanel.add(descFieldPanel);
        formPanel.add(qtyFieldPanel);
        formPanel.add(categoryPanel);

        contentPanel.add(imagePanel, BorderLayout.WEST);
        contentPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(cardColor);
        cancelButton.setForeground(textColor);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> addBookDialog.dispose());

        JButton addButton = new JButton("Add Book");
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.setBackground(primaryColor);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate form
                if (bookTitleField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(addBookDialog,
                            "Please enter a book title",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (authorField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(addBookDialog,
                            "Please enter an author name",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // If all validations pass, show success message and close dialog
                JOptionPane.showMessageDialog(addBookDialog,
                        "Book \"" + bookTitleField.getText() + "\" added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                addBookDialog.dispose();
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(addButton);

        // Add all panels to dialog
        addBookDialog.add(titlePanel, BorderLayout.NORTH);
        addBookDialog.add(contentPanel, BorderLayout.CENTER);
        addBookDialog.add(buttonPanel, BorderLayout.SOUTH);

        addBookDialog.setVisible(true);
    }
}

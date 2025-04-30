package ui;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class UserDashboard extends JFrame {
    // Color scheme
    private Color primaryColor = new Color(79, 70, 229);    // Indigo
    private Color lightPrimaryColor = new Color(99, 102, 241); // Light Indigo
    private Color secondaryColor = new Color(249, 168, 212); // Pink
    private Color accentColor = new Color(110, 231, 183);   // Teal
    private Color backgroundColor = new Color(243, 244, 246); // Light Gray
    private Color cardColor = Color.WHITE;
    private Color textColor = new Color(31, 41, 55);       // Dark Gray

    private JLabel timeLabel;
    private Timer timer;

    public UserDashboard() {
        setTitle("LibraryX - User Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the background color
        getContentPane().setBackground(backgroundColor);

        // Create main layout
        setLayout(new BorderLayout());

        // Add components
        add(createSidebar(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);

        // Initialize and start the timer for the clock
        initializeTimer();

        setVisible(true);
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
        if(getClass().getResource("/icons/book_icon.png") != null) {
            logoIcon = new JLabel(new ImageIcon(getClass().getResource("/icons/book_icon.png")));
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
        String[] menuItems = {"Home", "Search Books", "My Borrowings", "Reading History", "Favorites", "Notifications", "Settings"};
        String[] menuIcons = {"🏠", "🔍", "📚", "⏱️", "⭐", "🔔", "⚙️"};

        for (int i = 0; i < menuItems.length; i++) {
            JPanel menuItem = createMenuItem(menuItems[i], menuIcons[i], i == 0);
            menuPanel.add(menuItem);
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

        JLabel userName = new JLabel("John Doe");
        userName.setFont(new Font("Arial", Font.BOLD, 14));
        userName.setForeground(Color.WHITE);

        JLabel userRole = new JLabel("Student");
        userRole.setFont(new Font("Arial", Font.PLAIN, 12));
        userRole.setForeground(new Color(199, 210, 254)); // Light indigo

        userInfo.add(userName);
        userInfo.add(userRole);

        profilePanel.add(userIcon, BorderLayout.WEST);
        profilePanel.add(userInfo, BorderLayout.CENTER);

        // Add all components to sidebar
        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(profilePanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel createMenuItem(String text, String icon, boolean isSelected) {
        JPanel menuItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
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

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel with search and user info
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(backgroundColor);

        // Search field
        JTextField searchField = new JTextField("Search for books, authors...");
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // User info and time
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(backgroundColor);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(textColor);

        JLabel notificationIcon = new JLabel("🔔");
        notificationIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        notificationIcon.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        userPanel.add(timeLabel);
        userPanel.add(notificationIcon);
        userPanel.add(avatarLabel);

        topPanel.add(searchField, BorderLayout.WEST);
        topPanel.add(userPanel, BorderLayout.EAST);

        // Center panel with stats and books
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(backgroundColor);

        // Stats panels (first row)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(backgroundColor);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        statsPanel.add(createStatCard("Currently Borrowed", "5", "📚", accentColor));
        statsPanel.add(createStatCard("Overdue", "1", "⏰", new Color(252, 165, 165))); // Light red
        statsPanel.add(createStatCard("Reading Streak", "15 days", "🔥", secondaryColor));

        // Recommendations panel
        JPanel recPanel = new JPanel(new BorderLayout());
        recPanel.setBackground(primaryColor);
        recPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        recPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel recContent = new JPanel(new BorderLayout());
        recContent.setBackground(primaryColor);

        JLabel recIcon = new JLabel("✨");
        recIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        recIcon.setForeground(Color.WHITE);

        JPanel recTextPanel = new JPanel(new GridLayout(2, 1));
        recTextPanel.setBackground(primaryColor);

        JLabel recTitle = new JLabel("Recommended Book for You");
        recTitle.setFont(new Font("Arial", Font.BOLD, 16));
        recTitle.setForeground(Color.WHITE);

        JLabel recDesc = new JLabel("\"The Midnight Library\" by Matt Haig");
        recDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        recDesc.setForeground(new Color(199, 210, 254)); // Light indigo

        recTextPanel.add(recTitle);
        recTextPanel.add(recDesc);

        JButton viewButton = new JButton("Check it out");
        viewButton.setBackground(new Color(255, 255, 255, 50));
        viewButton.setForeground(Color.WHITE);
        viewButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        viewButton.setFocusPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        recContent.add(recIcon, BorderLayout.WEST);
        recContent.add(recTextPanel, BorderLayout.CENTER);
        recContent.add(viewButton, BorderLayout.EAST);

        recPanel.add(recContent, BorderLayout.CENTER);

        // Reading activity chart
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(cardColor);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel chartTitle = new JLabel("Your Reading Activity");
        chartTitle.setFont(new Font("Arial", Font.BOLD, 16));

        // Create the dataset and chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5, "Books", "Jan");
        dataset.addValue(7, "Books", "Feb");
        dataset.addValue(3, "Books", "Mar");
        dataset.addValue(5, "Books", "Apr");
        dataset.addValue(8, "Books", "May");
        dataset.addValue(4, "Books", "Jun");

        JFreeChart chart = ChartFactory.createBarChart(
                null,              // chart title
                null,              // domain axis label
                null,              // range axis label
                dataset,           // data
                PlotOrientation.VERTICAL,
                false,             // include legend
                true,
                false
        );

        // Customize the chart
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(229, 231, 235));
        plot.setOutlinePaint(null);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, primaryColor);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        ChartPanel chartComponent = new ChartPanel(chart);
        chartComponent.setPreferredSize(new Dimension(600, 200));

        chartPanel.add(chartTitle, BorderLayout.NORTH);
        chartPanel.add(chartComponent, BorderLayout.CENTER);

        // Recent books panel
        JPanel recentBooksPanel = new JPanel(new BorderLayout());
        recentBooksPanel.setBackground(cardColor);
        recentBooksPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel recentTitle = new JLabel("Recently Borrowed Books");
        recentTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel booksGrid = new JPanel(new GridLayout(1, 3, 15, 0));
        booksGrid.setBackground(cardColor);

        booksGrid.add(createBookCard("To Kill a Mockingbird", "Harper Lee", "Due: May 15, 2025"));
        booksGrid.add(createBookCard("1984", "George Orwell", "Due: May 20, 2025"));
        booksGrid.add(createBookCard("The Great Gatsby", "F. Scott Fitzgerald", "Due: May 22, 2025"));

        recentBooksPanel.add(recentTitle, BorderLayout.NORTH);
        recentBooksPanel.add(booksGrid, BorderLayout.CENTER);

        // Add all components to the center panel
        centerPanel.add(topPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(statsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(recPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(chartPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(recentBooksPanel);

        // Add center panel to main panel
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconPanel.setBackground(cardColor);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        iconLabel.setForeground(color);
        iconPanel.add(iconLabel);

        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setBackground(cardColor);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(textColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(107, 114, 128)); // Gray

        contentPanel.add(valueLabel);
        contentPanel.add(titleLabel);

        card.add(iconPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBookCard(String title, String author, String dueDate) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(backgroundColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel bookIcon = new JLabel("📗");
        bookIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        bookIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel authorLabel = new JLabel("by " + author);
        authorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        authorLabel.setForeground(new Color(107, 114, 128)); // Gray
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dueDateLabel = new JLabel(dueDate);
        dueDateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dueDateLabel.setForeground(new Color(107, 114, 128)); // Gray
        dueDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(bookIcon);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(authorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(dueDateLabel);

        return card;
    }

    private void initializeTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a | MMM dd, yyyy");
                String formattedTime = sdf.format(new Date());
                timeLabel.setText(formattedTime);
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        try {
            // Set look and feel to system
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserDashboard();
            }
        });
    }
}
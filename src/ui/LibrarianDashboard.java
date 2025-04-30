package ui;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
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

public class LibrarianDashboard extends JFrame {
    // Color scheme
    private Color primaryColor = new Color(79, 70, 229);    // Indigo
    private Color lightPrimaryColor = new Color(99, 102, 241); // Light Indigo
    private Color secondaryColor = new Color(249, 168, 212); // Pink
    private Color accentColor = new Color(110, 231, 183);   // Teal
    private Color backgroundColor = new Color(243, 244, 246); // Light Gray
    private Color cardColor = Color.WHITE;
    private Color textColor = new Color(31, 41, 55);       // Dark Gray
    private Color redColor = new Color(239, 68, 68);       // Red
    private Color yellowColor = new Color(245, 158, 11);   // Amber
    private Color greenColor = new Color(16, 185, 129);    // Green

    private JLabel timeLabel;
    private Timer timer;

    public LibrarianDashboard() {
        setTitle("LibraryX - Librarian Dashboard");
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
        String[] menuItems = {"Dashboard", "Circulation", "Catalog", "Members", "Reservations", "Returns", "Fines", "Reports"};
        String[] menuIcons = {"🏠", "🔄", "📚", "👥", "📅", "↩️", "💰", "📊"};

        for (int i = 0; i < menuItems.length; i++) {
            JPanel menuItem = createMenuItem(menuItems[i], menuIcons[i], i == 0);
            menuPanel.add(menuItem);
        }

        // Librarian profile panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(new Color(67, 56, 202)); // Darker indigo
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        userIcon.setForeground(Color.WHITE);

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setBackground(new Color(67, 56, 202));

        JLabel userName = new JLabel("Sarah Johnson");
        userName.setFont(new Font("Arial", Font.BOLD, 14));
        userName.setForeground(Color.WHITE);

        JLabel userRole = new JLabel("Senior Librarian");
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

        // Top panel with search bar and user info
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(backgroundColor);

        // Search field
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(backgroundColor);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(primaryColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchButton.setFocusPainted(false);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // User info and time
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(backgroundColor);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(textColor);

        JButton notificationBtn = new JButton("🔔");
        notificationBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        notificationBtn.setBorderPainted(false);
        notificationBtn.setContentAreaFilled(false);
        notificationBtn.setFocusPainted(false);
        notificationBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton helpBtn = new JButton("❓");
        helpBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        helpBtn.setBorderPainted(false);
        helpBtn.setContentAreaFilled(false);
        helpBtn.setFocusPainted(false);
        helpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.add(timeLabel);
        userPanel.add(notificationBtn);
        userPanel.add(helpBtn);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(userPanel, BorderLayout.EAST);

        // Center panel with stats and tables
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(backgroundColor);

        // Welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(cardColor);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel welcomeTitle = new JLabel("Good Morning, Sarah!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeTitle.setForeground(textColor);

        JLabel welcomeMsg = new JLabel("Today we have 23 scheduled returns and 12 new reservations to process.");
        welcomeMsg.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeMsg.setForeground(new Color(107, 114, 128));

        JPanel welcomeContent = new JPanel(new GridLayout(2, 1));
        welcomeContent.setBackground(cardColor);
        welcomeContent.add(welcomeTitle);
        welcomeContent.add(welcomeMsg);

        welcomePanel.add(welcomeContent, BorderLayout.CENTER);

        // Stats panels (first row)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(backgroundColor);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        statsPanel.add(createStatCard("Today's Checkouts", "24", "📚", accentColor));
        statsPanel.add(createStatCard("Today's Returns", "18", "↩️", greenColor));
        statsPanel.add(createStatCard("Pending Reservations", "12", "⏳", yellowColor));
        statsPanel.add(createStatCard("Overdue Books", "7", "⚠️", redColor));

        // Quick actions panel
        JPanel quickActionsPanel = new JPanel();
        quickActionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        quickActionsPanel.setBackground(backgroundColor);
        quickActionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton[] actionButtons = new JButton[4];
        String[] actionLabels = {"Checkout Book", "Process Return", "Register Member", "Manage Reservations"};

        for (int i = 0; i < actionButtons.length; i++) {
            actionButtons[i] = new JButton(actionLabels[i]);
            actionButtons[i].setBackground(i == 0 ? primaryColor : cardColor);
            actionButtons[i].setForeground(i == 0 ? Color.WHITE : textColor);
            actionButtons[i].setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            actionButtons[i].setFocusPainted(false);
            actionButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            quickActionsPanel.add(actionButtons[i]);
        }

        // Charts and data panel
        JPanel dataPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        dataPanel.setBackground(backgroundColor);

        // Circulation chart
        JPanel chartPanel = createChartPanel();

        // Recent returns table
        JPanel recentReturnsPanel = createRecentReturnsPanel();

        dataPanel.add(chartPanel);
        dataPanel.add(recentReturnsPanel);

        // Recent activity panel
        JPanel recentActivityPanel = createRecentActivityPanel();

        // Add all panels to center panel with vertical spacing
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(welcomePanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(statsPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(quickActionsPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(dataPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(recentActivityPanel);

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStatCard(String title, String value, String icon, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        iconLabel.setForeground(iconColor);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(cardColor);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(textColor);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(107, 114, 128));

        textPanel.add(valueLabel);
        textPanel.add(titleLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("Monthly Circulation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create dataset for chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(210, "Checkouts", "Jan");
        dataset.addValue(240, "Checkouts", "Feb");
        dataset.addValue(260, "Checkouts", "Mar");
        dataset.addValue(180, "Checkouts", "Apr");
        dataset.addValue(200, "Checkouts", "May");
        dataset.addValue(190, "Checkouts", "Jun");

        dataset.addValue(180, "Returns", "Jan");
        dataset.addValue(220, "Returns", "Feb");
        dataset.addValue(230, "Returns", "Mar");
        dataset.addValue(160, "Returns", "Apr");
        dataset.addValue(180, "Returns", "May");
        dataset.addValue(165, "Returns", "Jun");

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                null,          // chart title
                "Month",       // domain axis label
                "Count",       // range axis label
                dataset,       // data
                PlotOrientation.VERTICAL, // orientation
                true,          // include legend
                true,          // tooltips
                false          // URLs
        );

        // Customize chart
        chart.setBackgroundPaint(cardColor);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(229, 231, 235));
        plot.setRangeGridlinePaint(new Color(229, 231, 235));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, primaryColor);
        renderer.setSeriesPaint(1, secondaryColor);
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        chartPanel.setBackground(cardColor);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRecentReturnsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("Recent Returns");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create table
        String[] columnNames = {"Book Title", "Member", "Return Date", "Status"};
        Object[][] data = {
                {"The Great Gatsby", "John Smith", "Today", "On time"},
                {"To Kill a Mockingbird", "Emily Jones", "Today", "On time"},
                {"1984", "Michael Brown", "Yesterday", "Late"},
                {"Pride and Prejudice", "Sarah Williams", "Yesterday", "On time"},
                {"The Hobbit", "David Wilson", "Apr 28", "On time"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setSelectionBackground(new Color(237, 233, 254));

        // Set custom renderer for the status column
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);

                if ("Late".equals(value)) {
                    label.setForeground(redColor);
                } else {
                    label.setForeground(greenColor);
                }

                label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return label;
            }
        });

        // Set header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(new Color(107, 114, 128));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));

        // Set table borders and scrollpane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(cardColor);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(cardColor);

        JButton viewAllButton = new JButton("View All");
        viewAllButton.setForeground(primaryColor);
        viewAllButton.setBackground(cardColor);
        viewAllButton.setBorderPainted(false);
        viewAllButton.setContentAreaFilled(false);
        viewAllButton.setFocusPainted(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(viewAllButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("Recent Activity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(textColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel activitiesPanel = new JPanel();
        activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.Y_AXIS));
        activitiesPanel.setBackground(cardColor);

        // Create activity items
        String[] activityTexts = {
                "<html><b>Sarah Johnson</b> processed a return for <b>The Great Gatsby</b></html>",
                "<html><b>New member registration</b> completed for <b>Robert Clark</b></html>",
                "<html><b>Overdue notice</b> sent to <b>Emily Jones</b> for <b>The Catcher in the Rye</b></html>",
                "<html><b>Book reservation</b> made by <b>Michael Brown</b> for <b>Lord of the Rings</b></html>",
                "<html><b>Fine payment</b> of <b>$2.50</b> received from <b>David Wilson</b></html>"
        };

        String[] activityTimes = {
                "5 minutes ago",
                "20 minutes ago",
                "1 hour ago",
                "2 hours ago",
                "3 hours ago"
        };

        String[] activityIcons = {
                "↩️",
                "👤",
                "⚠️",
                "📅",
                "💰"
        };

        for (int i = 0; i < activityTexts.length; i++) {
            JPanel activityItem = createActivityItem(activityTexts[i], activityTimes[i], activityIcons[i]);
            activitiesPanel.add(activityItem);

            if (i < activityTexts.length - 1) {
                JSeparator separator = new JSeparator();
                separator.setForeground(new Color(229, 231, 235));
                activitiesPanel.add(separator);
            }
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(cardColor);

        JButton viewAllButton = new JButton("View All Activity");
        viewAllButton.setForeground(primaryColor);
        viewAllButton.setBackground(cardColor);
        viewAllButton.setBorderPainted(false);
        viewAllButton.setContentAreaFilled(false);
        viewAllButton.setFocusPainted(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(viewAllButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(activitiesPanel), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createActivityItem(String text, String time, String icon) {
        JPanel activityItem = new JPanel(new BorderLayout(10, 0));
        activityItem.setBackground(cardColor);
        activityItem.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(cardColor);

        JLabel activityText = new JLabel(text);
        activityText.setFont(new Font("Arial", Font.PLAIN, 13));
        activityText.setForeground(textColor);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(156, 163, 175));

        textPanel.add(activityText, BorderLayout.NORTH);
        textPanel.add(timeLabel, BorderLayout.SOUTH);

        activityItem.add(iconLabel, BorderLayout.WEST);
        activityItem.add(textPanel, BorderLayout.CENTER);

        return activityItem;
    }

    private void initializeTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();
        updateTime(); // Set initial time
    }

    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy • h:mm a");
        String formattedDate = dateFormat.format(new Date());
        timeLabel.setText(formattedDate);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibrarianDashboard();
            }
        });
    }
}
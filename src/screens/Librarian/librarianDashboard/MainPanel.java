package screens.Librarian.librarianDashboard;

import components.ModernJTable;
import models.Librarian;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPanel extends JPanel {
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color lightPrimaryColor = new Color(99, 102, 241);
    private final Color secondaryColor = new Color(249, 168, 212);
    private final Color accentColor = new Color(110, 231, 183);
    private final Color backgroundColor = new Color(243, 244, 246);
    private final Color cardColor = Color.WHITE;
    private final Color textColor = new Color(31, 41, 55);
    private final Color redColor = new Color(239, 68, 68);
    private final Color yellowColor = new Color(245, 158, 11);
    private final Color greenColor = new Color(16, 185, 129);

    final JFrame frame;
    final JLabel welcomeTitle;

    private JLabel timeLabel;

    public MainPanel(JFrame frame) {


            this.frame=frame;
            setLayout(new BorderLayout(15, 15));
            setBackground(backgroundColor);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

            initializeTimer();
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

            welcomeTitle = new JLabel("Good Morning,Sarah!");
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

            JButton[] actionButtons = new JButton[5]; // Added one more button for Add Book
            String[] actionLabels = {"Checkout Book", "Process Return", "Register Member", "Manage Reservations", "Add Book"};

            for (int i = 0; i < actionButtons.length; i++) {
                actionButtons[i] = new JButton(actionLabels[i]);
                actionButtons[i].setBackground(i == 0 ? primaryColor : cardColor);
                actionButtons[i].setForeground(i == 0 ? Color.WHITE : textColor);
                actionButtons[i].setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                actionButtons[i].setFocusPainted(false);
                actionButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
                quickActionsPanel.add(actionButtons[i]);

                // Add action listener for the Add Book button
                if (i == 4) {
                    actionButtons[i].addActionListener(e -> ShowAddBookDialog.showAddBookDialog(frame));
                }
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
            add(topPanel, BorderLayout.NORTH);
            add(new JScrollPane(centerPanel), BorderLayout.CENTER);

            setVisible(true);
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

        // Create table model
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{
                        {"The Great Gatsby", "F. Scott Fitzgerald", "10:30 AM", "On Time"},
                        {"Pride and Prejudice", "Jane Austen", "11:45 AM", "Late"},
                        {"To Kill a Mockingbird", "Harper Lee", "01:15 PM", "On Time"},
                        {"1984", "George Orwell", "02:20 PM", "On Time"},
                        {"The Hobbit", "J.R.R. Tolkien", "03:05 PM", "Late"}
                },
                new String[]{
                        "Book Title", "Author", "Return Time", "Status"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        ModernJTable table = new ModernJTable(model);

        // Status column renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(ModernJTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if ("Late".equals(value)) {
                    label.setForeground(redColor);
                } else {
                    label.setForeground(greenColor);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        // Create activity items
        JPanel activitiesPanel = new JPanel();
        activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.Y_AXIS));
        activitiesPanel.setBackground(cardColor);

        String[] activities = {
                "Sarah Johnson processed a return for 'The Great Gatsby'",
                "New member 'David Wilson' registered",
                "Book 'Atomic Habits' by James Clear added to collection (3 copies)",
                "Reservation for 'The Alchemist' approved for member #1024",
                "Late fee of $2.50 collected from member #789",
                "Book 'Data Structures and Algorithms' checked out by member #456"
        };

        String[] times = {
                "5 minutes ago", "15 minutes ago", "30 minutes ago",
                "1 hour ago", "2 hours ago", "3 hours ago"
        };

        for (int i = 0; i < activities.length; i++) {
            panel.add(createActivityItem(activities[i], times[i]), BorderLayout.CENTER);
        }

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(cardColor);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton viewAllButton = new JButton("View All");
        viewAllButton.setBackground(cardColor);
        viewAllButton.setForeground(primaryColor);
        viewAllButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        viewAllButton.setFocusPainted(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        headerPanel.add(viewAllButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Create scrollable activity items
        JPanel activitiesContainer = new JPanel();
        activitiesContainer.setLayout(new BoxLayout(activitiesContainer, BoxLayout.Y_AXIS));
        activitiesContainer.setBackground(cardColor);

        for (int i = 0; i < activities.length; i++) {
            activitiesContainer.add(createActivityItem(activities[i], times[i]));
            if (i < activities.length - 1) {
                JSeparator separator = new JSeparator();
                separator.setForeground(new Color(229, 231, 235));
                activitiesContainer.add(separator);
            }
        }

        JScrollPane scrollPane = new JScrollPane(activitiesContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActivityItem(String activity, String time) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel activityLabel = new JLabel(activity);
        activityLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        activityLabel.setForeground(textColor);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(107, 114, 128));

        panel.add(activityLabel, BorderLayout.CENTER);
        panel.add(timeLabel, BorderLayout.EAST);

        return panel;
    }

    private void initializeTimer() {
        // Update the time every second
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime(); // Initialize with current time
    }

    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy • h:mm a");
        timeLabel.setText(dateFormat.format(new Date()));
    }

    public void setLibrarianInfo(Librarian librarian) {

        welcomeTitle.setText("Welcome, "+librarian.name);
        repaint();

        revalidate();
    }
}


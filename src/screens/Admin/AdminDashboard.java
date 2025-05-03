package screens.Admin;

import components.ModernJTable;
import components.RoundedPanel;
import components.RoundedPanelButton;
import models.Admin;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends JFrame {
    // Color scheme
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

    private JLabel timeLabel;
    private Timer timer;

    public AdminDashboard(AdminAppFrame adminAppFrame) {
        setTitle("LibraryX - Admin Dashboard");
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
        logoText.setFont(new Font("Gilroy", Font.BOLD, 20));
        logoText.setForeground(Color.WHITE);

        logoPanel.add(logoIcon);
        logoPanel.add(logoText);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(primaryColor);

        // Create menu items
        String[] menuItems = {"Dashboard", "Books Management", "User Management", "Staff Management", "Reports", "Inventory", "Settings", "System Logs"};
        String[] menuIcons = {"🏠", "📚", "👥", "👤", "📊", "📦", "⚙️", "📝"};

        for (int i = 0; i < menuItems.length; i++) {
            JPanel menuItem = createMenuItem(menuItems[i], menuIcons[i], i == 0);
            menuPanel.add(menuItem);
        }

        // Admin profile panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(new Color(67, 56, 202)); // Darker indigo
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        userIcon.setForeground(Color.WHITE);

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setBackground(new Color(67, 56, 202));

        JLabel userName = new JLabel("Admin User");
        userName.setFont(new Font("Gilroy", Font.BOLD, 14));
        userName.setForeground(Color.WHITE);

        JLabel userRole = new JLabel("System Administrator");
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
        textLabel.setFont(new Font("Gilroy", isSelected ? Font.BOLD : Font.PLAIN, 14));
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

    JPanel spacer(){
        final JPanel s = new JPanel();
        s.setSize(16, 16);
        s.setBackground(Color.white);
        return s;
    }

    private JPanel createMainPanel() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel with search and user info
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(backgroundColor);

        JLabel dashboardTitle = new JLabel("Admin Dashboard");
        dashboardTitle.setFont(new Font("Gilroy", Font.BOLD, 22));
        dashboardTitle.setForeground(textColor);

        // User info and time
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(backgroundColor);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(textColor);

        RoundedPanelButton notificationBtn = new RoundedPanelButton("🔔");

        RoundedPanelButton profileBtn = new RoundedPanelButton("👤");

        userPanel.add(timeLabel);
        userPanel.add(notificationBtn);
        userPanel.add(profileBtn);

        topPanel.add(dashboardTitle, BorderLayout.WEST);
        topPanel.add(userPanel, BorderLayout.EAST);

        // Center panel with stats and books
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(backgroundColor);

        // Stats panels (first row)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(backgroundColor);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        statsPanel.add(createStatCard("Total Books", "8,542", "📚", accentColor));
        statsPanel.add(createStatCard("Active Users", "2,341", "👥", primaryColor));
        statsPanel.add(createStatCard("Overdue Returns", "57", "⏰", redColor));
        statsPanel.add(createStatCard("New Members", "123", "🆕", greenColor));

        // System status panel
        RoundedPanel statusPanel = new RoundedPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel statusContent = new JPanel();
        statusContent.setBackground(cardColor);

        JLabel statusIcon = new JLabel("🟢");
        statusIcon.setFont(new Font("Arial", Font.PLAIN, 24));

        JPanel statusTextPanel = new JPanel(new GridLayout(2, 1));
        statusTextPanel.setBackground(cardColor);

        JLabel statusTitle = new JLabel("System Status");
        statusTitle.setFont(new Font("Gilroy", Font.BOLD, 16));
        statusTitle.setForeground(textColor);

        JLabel statusDesc = new JLabel("All systems operational - Last checked: 10:25 AM");
        statusDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        statusDesc.setForeground(new Color(107, 114, 128)); // Gray

        statusTextPanel.add(statusTitle);
        statusTextPanel.add(statusDesc);

        RoundedPanelButton viewLogsButton = new RoundedPanelButton("View Logs");

        statusContent.add(statusIcon);
        statusContent.add(spacer());
        statusContent.add(statusTextPanel);
        statusContent.add(spacer());
        statusContent.add(viewLogsButton);

        statusPanel.add(statusContent, BorderLayout.CENTER);

        // Activity charts panel - split into two sections
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsPanel.setBackground(backgroundColor);

        // Books by category chart
        JPanel categoryPanel = new RoundedPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));

        JLabel categoryTitle = new JLabel("Books by Category");
        categoryTitle.setFont(new Font("Gilroy", Font.BOLD, 16));

        // Create the pie chart
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Fiction", 45);
        pieDataset.setValue("Non-Fiction", 30);
        pieDataset.setValue("Academic", 15);
        pieDataset.setValue("Others", 10);

        JFreeChart pieChart = ChartFactory.createPieChart(
                null,              // chart title
                pieDataset,        // dataset
                false,             // include legend
                true,              // tooltips
                false              // URLs
        );

        // Customize the chart
        pieChart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setSectionPaint("Fiction", primaryColor);
        plot.setSectionPaint("Non-Fiction", accentColor);
        plot.setSectionPaint("Academic", secondaryColor);
        plot.setSectionPaint("Others", yellowColor);

        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(300, 200));

        categoryPanel.add(categoryTitle);
        categoryPanel.add(spacer());
        categoryPanel.add(pieChartPanel);

        // Monthly activity chart
        JPanel activityPanel = new RoundedPanel();
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));

        JLabel activityTitle = new JLabel("Monthly Activity");
        activityTitle.setFont(new Font("Gilroy", Font.BOLD, 16));

        // Create the dataset and chart
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        barDataset.addValue(320, "Borrowed", "Jan");
        barDataset.addValue(350, "Borrowed", "Feb");
        barDataset.addValue(300, "Borrowed", "Mar");
        barDataset.addValue(380, "Borrowed", "Apr");
        barDataset.addValue(410, "Borrowed", "May");

        barDataset.addValue(290, "Returned", "Jan");
        barDataset.addValue(330, "Returned", "Feb");
        barDataset.addValue(280, "Returned", "Mar");
        barDataset.addValue(350, "Returned", "Apr");
        barDataset.addValue(390, "Returned", "May");

        JFreeChart barChart = ChartFactory.createBarChart(
                null,              // chart title
                null,              // domain axis label
                null,              // range axis label
                barDataset,        // data
                PlotOrientation.VERTICAL,
                true,              // include legend
                true,
                false
        );

        // Customize the chart
        barChart.setBackgroundPaint(Color.WHITE);
        CategoryPlot barPlot = (CategoryPlot) barChart.getPlot();
        barPlot.setBackgroundPaint(Color.WHITE);
        barPlot.setRangeGridlinePaint(new Color(229, 231, 235));
        barPlot.setOutlinePaint(null);

        BarRenderer renderer = (BarRenderer) barPlot.getRenderer();
        renderer.setSeriesPaint(0, primaryColor);
        renderer.setSeriesPaint(1, accentColor);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        ChartPanel barChartPanel = new ChartPanel(barChart);
        barChartPanel.setPreferredSize(new Dimension(300, 200));

        activityPanel.add(activityTitle);
        activityPanel.add(spacer());
        activityPanel.add(barChartPanel);

        chartsPanel.add(categoryPanel);
        chartsPanel.add(activityPanel);

        // Recent activity table
        JPanel recentPanel = new RoundedPanel();
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));

        JPanel recentHeader = new JPanel(new BorderLayout());
        recentHeader.setBackground(cardColor);

        JLabel recentTitle = new JLabel("Recent System Activity");
        recentTitle.setFont(new Font("Gilroy", Font.BOLD, 16));

        RoundedPanelButton viewAllButton = new RoundedPanelButton("View All");

        recentHeader.add(recentTitle, BorderLayout.WEST);
        recentHeader.add(viewAllButton, BorderLayout.EAST);

        // Create table model
        String[] columnNames = {"Activity", "User", "Book/Resource", "Date & Time", "Status"};
        Object[][] data = {
                {"Book Borrowed", "Alice Johnson", "Harry Potter and the Philosopher's Stone", "Today, 10:23 AM", "Completed"},
                {"New User", "Mark Smith", "User Registration", "Today, 09:45 AM", "Verified"},
                {"Book Returned", "David Wilson", "To Kill a Mockingbird", "Today, 09:30 AM", "Approved"},
                {"Late Return", "Sarah Brown", "1984", "Yesterday, 04:15 PM", "Fined"},
                {"Book Reserved", "Michael Lee", "The Great Gatsby", "Yesterday, 02:20 PM", "Pending"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        ModernJTable table = new ModernJTable(model);
        table.setCornerRadius(8);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;

                if ("Completed".equals(status) || "Approved".equals(status) || "Verified".equals(status)) {
                    c.setForeground(greenColor);
                } else if ("Fined".equals(status)) {
                    c.setForeground(redColor);
                } else {
                    c.setForeground(yellowColor);
                }

                return c;
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(null);

        recentPanel.add(recentHeader, BorderLayout.NORTH);
        recentPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Quick access buttons panel
        JPanel quickActionsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        quickActionsPanel.setBackground(backgroundColor);
        quickActionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        String[] actionTitles = {"Add New Book", "Register User", "Generate Reports", "System Backup"};
        String[] actionIcons = {"📕", "👤", "📊", "💾"};
        Color[] actionColors = {greenColor, primaryColor, yellowColor, accentColor};

        for (int i = 0; i < actionTitles.length; i++) {
            quickActionsPanel.add(createActionButton(actionTitles[i], actionIcons[i], actionColors[i]));
        }

        // Add all components to the center panel
        centerPanel.add(topPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(statsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(statusPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(chartsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(recentPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(quickActionsPanel);

        // Add center panel to main panel
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        RoundedPanel card = RoundedPanel.RoundedCard();
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        iconPanel.setBackground(cardColor);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        iconLabel.setForeground(color);
        iconPanel.add(iconLabel);

        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setBackground(cardColor);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Gilroy", Font.BOLD, 20));
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

    private JPanel createActionButton (String title, String icon, Color color) {
        JPanel button = new RoundedPanelButton(title, icon, color, e -> {
            System.out.println("Button Pressed");
        });
        button.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        iconLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Gilroy", Font.BOLD, 14));
        titleLabel.setForeground(textColor);

        return button;
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

    public void setAdminInfo(Admin admin) {

    }
}
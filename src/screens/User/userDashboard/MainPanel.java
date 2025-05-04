package screens.User.userDashboard;

import components.RoundedPanel;
import components.RoundedPanelButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPanel extends JPanel {

    JLabel timeLabel;

    // Color scheme
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color secondaryColor = new Color(249, 168, 212);
    private final Color accentColor = new Color(110, 231, 183);
    private final Color backgroundColor = new Color(243, 244, 246);
    private final Color cardColor = Color.WHITE;
    private final Color textColor = new Color(31, 41, 55);

    Component spacer(){
        return Box.createRigidArea(new Dimension(16, 16));
    }

    public MainPanel() {
        JPanel mainPanel = new JPanel();
        setLayout(new BorderLayout(16, 12));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel with search and user info
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(backgroundColor);

        initializeTimer();

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
        JPanel recPanel = new RoundedPanel(primaryColor);
        recPanel.setLayout(new BorderLayout());

        JPanel recContent = new JPanel();
        recContent.setLayout(new BoxLayout(recContent, BoxLayout.X_AXIS));
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

        RoundedPanelButton viewButton = new RoundedPanelButton("Check it out", "", Color.black, e -> {});
        viewButton.setPreferredSize(new Dimension(160, 40));

        recContent.add(recIcon);
        recContent.add(spacer());
        recContent.add(recTextPanel);
        recContent.add(Box.createRigidArea(new Dimension(500, 8)));
        recContent.add(viewButton);

        recPanel.add(recContent);

        // Reading activity chart
        JPanel chartPanel = RoundedPanel.RoundedCard();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));

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
        JPanel recentBooksPanel = new RoundedPanel();
        recentBooksPanel.setLayout(new BoxLayout(recentBooksPanel, BoxLayout.Y_AXIS));

        JLabel recentTitle = new JLabel("Recently Borrowed Books");
        recentTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel booksGrid = new JPanel(new GridLayout(1, 3, 16, 0));
        booksGrid.setBackground(cardColor);

        int bookCardWidth = booksGrid.getWidth()/3 - 48;
        booksGrid.add(createBookCard(
                "To Kill a Mockingbird",
                "Harper Lee",
                "https://viewthroughmywindow.com/wp-content/uploads/2019/05/Mocking-Bird.jpg",
                "Due: May 15, 2025"
        ));
        booksGrid.add(createBookCard(
                "1984",
                "George Orwell",
                "https://miro.medium.com/v2/resize:fit:800/1*g8s4n-puPV3y-F2b7ilJ_A.jpeg",
                "Due: May 20, 2025"
        ));
        booksGrid.add(createBookCard(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "https://rukminim2.flixcart.com/image/850/1000/l4fxh8w0/book/z/0/y/the-great-gatsby-a-novel-original-imagfbmgfsan3gjg.jpeg",
                "Due: May 22, 2025"
        ));

        recentBooksPanel.add(recentTitle);
        recentBooksPanel.add(spacer());
        recentBooksPanel.add(booksGrid);

        centerPanel.add(topPanel);
        centerPanel.add(spacer());
        centerPanel.add(statsPanel);
        centerPanel.add(spacer());
        centerPanel.add(recPanel);
        centerPanel.add(spacer());
        centerPanel.add(chartPanel);
        centerPanel.add(spacer());
        centerPanel.add(recentBooksPanel);

        // Add center panel to main panel
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
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

    private JPanel createBookCard(String title, String author, String bookImage, String dueDate) {
        RoundedPanel card = new RoundedPanel(12, new Color(220, 220, 220), 1, new Color(248, 248, 248));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel bookIcon;
        if(bookImage != ""){
            bookIcon = new JLabel("Loading");

            Thread imageLoader = new Thread(() -> {
                try {
                    URL url = new URL(bookImage);
                    BufferedImage image = ImageIO.read(url);
                    if (image != null) {
                        int width = 100;
                        Image scaledImage = image.getScaledInstance(width, width*3/2, Image.SCALE_SMOOTH);
                        Icon icon = new ImageIcon(scaledImage);
                        // Update the JLabel on the Event Dispatch Thread (EDT)
                        SwingUtilities.invokeLater(() -> {
                            bookIcon.setText(""); // Remove "Loading..." text
                            bookIcon.setIcon(icon);
//                            pack(); // Adjust frame size after loading
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            bookIcon.setText("Error: Could not load image.");
                        });
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> {
                        bookIcon.setText("Error: " + e.getMessage());
                    });
                    e.printStackTrace();
                }
            });

            imageLoader.start();
        }else{
            bookIcon = new JLabel("📗");
            bookIcon.setFont(new Font("Arial", Font.PLAIN, 24));
            bookIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

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
        card.add(spacer());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(authorLabel);
        card.add(spacer());
        card.add(dueDateLabel);

        return card;
    }

    private void initializeTimer() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a | MMM dd, yyyy");
            String formattedTime = sdf.format(new Date());
            timeLabel.setText(formattedTime);
        });
        timer.start();
    }
}
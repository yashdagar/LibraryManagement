package screens.User;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import models.User;
import screens.user_dashboard.MainPanel;
import screens.user_dashboard.SearchBooksPanel;

public class UserDashboard extends JPanel {
    // Color scheme
    private final Color primaryColor = new Color(79, 70, 229);
    private final Color lightPrimaryColor = new Color(99, 102, 241);

    private JLabel userName;
    private User user = new User(
            "John Doe",
            "johndoe@gmail.com",
            "12345678",
            "B.Tech",
            500125147,
            2,
            3,
            0
    );

    private JPanel centerPanel;
    private MainPanel mainPanel;
    private SearchBooksPanel searchBooksPanel;

    // Sidebar menu items
    private JPanel[] menuItems;

    public UserDashboard(UserAppFrame appFrame) {
        appFrame.setTitle("LibraryX - User Dashboard");
        setSize(new Dimension(1200, 1000));
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);

        Color backgroundColor = new Color(243, 244, 246);
        appFrame.getContentPane().setBackground(backgroundColor);

        // Create main layout
        setLayout(new BorderLayout());

        centerPanel = new JPanel(new CardLayout());
        mainPanel = new MainPanel();
        searchBooksPanel = new SearchBooksPanel(appFrame);

        // Add panels to card layout
        centerPanel.add(mainPanel, "home");
        centerPanel.add(searchBooksPanel, "searchBooks");

        // Add components
        add(createSidebar(), BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

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
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("assets/logo.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(bufferedImage != null) {
            ImageIcon image = new ImageIcon(bufferedImage.getScaledInstance(32, 32, Image.SCALE_DEFAULT));
            logoIcon = new JLabel(image);
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
        String[] menuItemNames = {"Home", "Search Books", "My Borrowings", "Reading History", "Favorites", "Notifications", "Settings"};
        String[] menuIcons = {"🏠", "🔍", "📚", "⏱", "⭐", "🔔", "⚙"};
        menuItems = new JPanel[menuItemNames.length];

        for (int i = 0; i < menuItemNames.length; i++) {
            // First menu item (Home) is selected by default
            menuItems[i] = createMenuItem(menuItemNames[i], menuIcons[i], i == 0);
            final int index = i;

            menuItems[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Update UI to show which menu item is selected
                    for (int j = 0; j < menuItems.length; j++) {
                        updateMenuItemStyle(menuItems[j], j == index);
                    }

                    // Handle navigation based on menu selection
                    CardLayout cl = (CardLayout) centerPanel.getLayout();
                    switch (index) {
                        case 0: // Home
                            cl.show(centerPanel, "home");
                            break;
                        case 1: // Search Books
                            cl.show(centerPanel, "searchBooks");
                            break;
                        // Add other menu actions when implemented
                        default:
                            // Show a placeholder for unimplemented screens
                            JOptionPane.showMessageDialog(
                                    UserDashboard.this,
                                    "This feature is coming soon!",
                                    menuItemNames[index],
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            break;
                    }
                }
            });

            menuPanel.add(menuItems[i]);
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

        userName = new JLabel("Welcome " + user.name + "!");
        userName.setFont(new Font("Arial", Font.BOLD, 14));
        userName.setForeground(Color.WHITE);

        JLabel userRole = new JLabel("Student");
        userRole.setFont(new Font("Arial", Font.PLAIN, 12));
        userRole.setForeground(new Color(199, 210, 254)); // Light indigo

        userInfo.add(userName);
        userInfo.add(userRole);

        profilePanel.add(userIcon);
        profilePanel.add(spacer());
        profilePanel.add(userInfo);

        // Add all components to sidebar
        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(profilePanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel createMenuItem(String text, String icon, boolean isSelected) {
        JPanel menuItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
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

        // Store the text label as a client property so we can update its font
        menuItem.putClientProperty("textLabel", textLabel);

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

    private void updateMenuItemStyle(JPanel menuItem, boolean isSelected) {
        if (isSelected) {
            menuItem.setBackground(lightPrimaryColor);
            JLabel textLabel = (JLabel) menuItem.getClientProperty("textLabel");
            if (textLabel != null) {
                textLabel.setFont(new Font("Arial", Font.BOLD, 14));
            }
        } else {
            menuItem.setBackground(primaryColor);
            JLabel textLabel = (JLabel) menuItem.getClientProperty("textLabel");
            if (textLabel != null) {
                textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            }
        }
    }

    Component spacer(){
        return Box.createRigidArea(new Dimension(16, 16));
    }

    public void setUserInfo(User user) {
        this.user = user;
        userName.setText(user.name);
        revalidate();
        repaint();
    }
}
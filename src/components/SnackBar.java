package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * A material design-inspired snack bar component for displaying temporary messages
 * in Swing applications.
 */
public class SnackBar extends JPanel {

    private static final int DISPLAY_TIME = 3000; // Display duration in milliseconds
    private static final int FADE_STEP = 10; // Opacity change per step
    private static final int FADE_DELAY = 50; // Delay between opacity changes

    private JLabel messageLabel;
    private Timer displayTimer;
    private Timer fadeTimer;
    private float opacity = 0.9f;
    private JPanel parentPanel;

    /**
     * Creates a new SnackBar instance
     *
     * @param parent The parent container where the snack bar will be displayed
     */
    public SnackBar(JPanel parent) {
        this.parentPanel = parent;

        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Create message label
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(messageLabel, BorderLayout.CENTER);

        // Make the panel initially invisible
        setVisible(false);

        // Initialize timers
        displayTimer = new Timer(DISPLAY_TIME, e -> startFading());
        displayTimer.setRepeats(false);

        fadeTimer = new Timer(FADE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    fadeTimer.stop();
                    setVisible(false);
                    cleanUp();
                }
                repaint();
            }
        });
    }

    /**
     * Shows a message in the snack bar
     *
     * @param message The message to display
     */
    public void showMessage(String message) {
        messageLabel.setText(message);

        // Reset opacity
        opacity = 0.9f;

        // Position the snackbar at the bottom center with some margin
        setPreferredSize(new Dimension(Math.min(500, parentPanel.getWidth() - 40), getPreferredSize().height));

        // Create a wrapper panel for positioning
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(this);

        // Add to parent if not already added
        if (!isVisible()) {
            // Use a layered pane to ensure it appears on top
            JRootPane rootPane = SwingUtilities.getRootPane(parentPanel);
            if (rootPane != null) {
                JLayeredPane layeredPane = rootPane.getLayeredPane();

                // Position the snackbar at the bottom
                Rectangle bounds = parentPanel.getBounds();
                bounds = SwingUtilities.convertRectangle(parentPanel.getParent(), bounds, layeredPane);

                wrapperPanel.setBounds(
                        bounds.x,
                        bounds.y + bounds.height - getPreferredSize().height - 30,
                        bounds.width,
                        getPreferredSize().height + 10
                );

                // Remove any existing snackbars
                Component[] components = layeredPane.getComponents();
                for (Component comp : components) {
                    if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 0 &&
                            ((JPanel) comp).getComponent(0) instanceof SnackBar) {
                        layeredPane.remove(comp);
                    }
                }

                layeredPane.add(wrapperPanel, JLayeredPane.POPUP_LAYER);
                layeredPane.revalidate();
                layeredPane.repaint();
            } else {
                // Fallback to the original method
                parentPanel.add(this, BorderLayout.SOUTH);
                parentPanel.revalidate();
            }
        }

        // Make visible
        setVisible(true);

        // Restart timers
        if (fadeTimer.isRunning()) {
            fadeTimer.stop();
        }
        if (displayTimer.isRunning()) {
            displayTimer.restart();
        } else {
            displayTimer.start();
        }
    }

    /**
     * Starts the fading effect
     */
    private void startFading() {
        fadeTimer.start();
    }

    /**
     * Cleans up the snackbar by removing it from its parent container
     */
    private void cleanUp() {
        // Try to find the wrapper panel
        Container parent = getParent();
        if (parent != null) {
            Container grandParent = parent.getParent();
            if (grandParent instanceof JLayeredPane) {
                grandParent.remove(parent);  // Remove the wrapper panel from layered pane
                grandParent.revalidate();
                grandParent.repaint();
            } else {
                // Regular removal
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        }
    }

    /**
     * Overrides the paintComponent method to create a rounded rectangle shape
     * and apply transparency.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        // Create rounded rectangle
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

        // Fill background
        g2d.setColor(getBackground());
        g2d.fill(roundedRect);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        if (parentPanel != null) {
            int width = Math.min(400, parentPanel.getWidth() - 40);
            Dimension preferredSize = super.getPreferredSize();
            return new Dimension(width, preferredSize.height);
        }
        return super.getPreferredSize();
    }

    /**
     * Utility method to show a snack bar on any container with the specified message
     *
     * @param container Parent container (must be a JPanel)
     * @param message Message to display
     */
    public static void show(JPanel container, String message) {
        SnackBar snackBar = new SnackBar(container);
        snackBar.showMessage(message);
    }

    /**
     * Utility method to show a snack bar on any container with custom display time
     *
     * @param container Parent container (must be a JPanel)
     * @param message Message to display
     * @param durationMs Display duration in milliseconds
     */
    public static void show(JPanel container, String message, int durationMs) {
        SnackBar snackBar = new SnackBar(container);
        snackBar.displayTimer.setInitialDelay(durationMs);
        snackBar.showMessage(message);
    }
}
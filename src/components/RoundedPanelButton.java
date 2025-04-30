package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom button component implemented with JPanel
 * Features rounded corners, black background, and configurable padding
 */
public class RoundedPanelButton extends JPanel {
    private final String text;
    private final String icon;
    private ActionListener onClickListener;
    private Color bgColor = Color.darkGray;
    private final Color textColor = Color.WHITE;
    private int cornerRadius = 15;
    private boolean isHovered = false;
    private boolean isPressed = false;

    /**
     * Creates a custom rounded button with black background and white text
     * @param text The text to display on the button
     */
    public RoundedPanelButton(String text) {
        this(text, "", _ -> {});
    }

    /**
     * Creates a custom rounded button with black background, white text and an action listener
     *
     * @param text    The text to display on the button
     * @param icon
     * @param onClick Action listener called when button is clicked
     */
    public RoundedPanelButton(String text, String icon, ActionListener onClick) {
        this.text = text;
        this.icon = icon;
        this.onClickListener = onClick;


        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        iconLabel.setForeground(Color.white);

        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(new Font("Gilroy", Font.BOLD, 14));
        titleLabel.setForeground(textColor);

        setLayout(new BorderLayout());
        setOpaque(false);

        // Set padding (16px horizontal, 12px vertical)
        setSize(new Dimension(text.length() * 10 + 20 + 32, 20 + 24));

        // Add cursor to indicate clickable
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add mouse listeners for interaction
        addMouseListeners();
    }

    public RoundedPanelButton(String title, String icon, Color color, ActionListener listener) {
        this(title, icon, listener);
        bgColor = color;
    }

    /**
     * Sets the action listener for the button
     * @param listener ActionListener to call when clicked
     */
    public void setOnClickListener(ActionListener listener) {
        this.onClickListener = listener;
    }

    /**
     * Sets the corner radius for the button
     * @param radius The corner radius in pixels
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable antialiasing for smoother corners
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Choose the appropriate color based on button state
        if (isPressed) {
            g2d.setColor(bgColor);
        } else if (isHovered) {
            g2d.setColor(bgColor.darker());
        } else {
            g2d.setColor(bgColor);
        }

        // Draw rounded rectangle as the button background
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Draw the text
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D textBounds = fm.getStringBounds(text, g2d);

        g2d.setColor(textColor);
        g2d.drawString(text,
                (int) (getWidth() - textBounds.getWidth()) / 2,
                (int) ((getHeight() - textBounds.getHeight()) / 2 + fm.getAscent()));

        g2d.dispose();
    }

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickListener != null) {
                    onClickListener.actionPerformed(new ActionEvent(
                            RoundedPanelButton.this, ActionEvent.ACTION_PERFORMED, text));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                if (isHovered) {
                    isHovered = true;
                }
                repaint();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        // Account for padding: 16px horizontal and 12px vertical
        FontMetrics fm = getFontMetrics(getFont());
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        return new Dimension(textWidth + 32, textHeight + 24);
    }

    // Demo usage
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rounded Panel Button Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            RoundedPanelButton button = new RoundedPanelButton("Click Me", "", e -> {
                System.out.println("Button clicked!");
            });

            frame.add(button);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
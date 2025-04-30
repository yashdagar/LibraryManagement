package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private final int cornerRadius;
    private final Color borderColor;
    private final int borderWidth;

    public RoundedPanel() {
        this(12, Color.white, 0);
    }

    public static RoundedPanel RoundedCard(){
        RoundedPanel panel = new RoundedPanel(12, new Color(220, 220, 220), 1);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        return panel;
    }

    public RoundedPanel(int radius) {
        this(radius, Color.white, 0);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
    }

    public RoundedPanel(int radius, Color borderColor, int borderWidth) {
        super();
        this.cornerRadius = radius;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
    }

    public RoundedPanel(int radius, Color borderColor, int borderWidth, int paddingHorizontal, int paddingVertical) {
        super();
        this.cornerRadius = radius;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint shadow effect (slightly larger than panel)
        g2.setColor(new Color(0, 0, 0, 20));  // Very light shadow color
        g2.fill(new RoundRectangle2D.Double(2, 2, getWidth() - 3, getHeight() - 3, 15, 15));

        // Paint rounded background
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 4, getHeight() - 4, 15, 15));

        // Paint border
        g2.setColor(new Color(229, 231, 235));
        g2.setStroke(new BasicStroke(1));
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 4, getHeight() - 4, 15, 15));

        g2.dispose();
    }
}
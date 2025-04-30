package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AnimatedButton extends JButton {
    private Color originalColor;
    private Timer animationTimer;
    private int alpha = 255;

    public AnimatedButton(String text) {
        super(text);
        originalColor = new Color(70, 130, 180);
        setFocusPainted(false);
        setBackground(originalColor);
        setForeground(Color.WHITE);
    }

    public void animate() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        alpha = 255;
        animationTimer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alpha -= 15;
                if (alpha <= 0) {
                    alpha = 255;
                    animationTimer.stop();
                    setBackground(originalColor);
                } else {
                    setBackground(new Color(70, 130, 180, alpha));
                }
                repaint();
            }
        });
        animationTimer.start();
    }
}

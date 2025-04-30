package components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ModernJTable extends JTable {
    // Default colors
    private Color headerBackground = new Color(60, 63, 65);
    private Color headerForeground = new Color(220, 220, 220);
    private Color selectionBackground = new Color(75, 110, 175);
    private Color alternateRowColor = new Color(240, 240, 240);
    private Color gridColor = new Color(210, 210, 210);
    private Color borderColor = new Color(180, 180, 180);
    private int rowHeight = 30;
    private boolean useAlternatingRows = true;
    private int cellHorizontalPadding = 12;
    private int cellVerticalPadding = 8;
    private boolean showHorizontalLines = true;
    private boolean showVerticalLines = false;
    private int cornerRadius = 6;

    public ModernJTable() {
        this(new DefaultTableModel());
    }

    public ModernJTable(TableModel model) {
        super(model);
        initializeTable();
    }

    public ModernJTable(int rows, int columns) {
        super(rows, columns);
        initializeTable();
    }

    public ModernJTable(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
        initializeTable();
    }

    private void initializeTable() {
        // Set basic table properties
        setRowHeight(rowHeight);
        setShowGrid(true);
        setGridColor(gridColor);
        setFillsViewportHeight(true);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure grid lines
        setShowHorizontalLines(showHorizontalLines);
        setShowVerticalLines(showVerticalLines);

        // Create and set custom header renderer
        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new ModernHeaderRenderer());
        header.setReorderingAllowed(true);
        header.setBackground(headerBackground);
        header.setForeground(headerForeground);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
        header.setPreferredSize(new Dimension(0, rowHeight + 5));

        // Enable sorting
        setAutoCreateRowSorter(true);

        // Set custom cell renderer
        setDefaultRenderer(Object.class, new ModernCellRenderer());

        // Add specific renderers for common data types
        setDefaultRenderer(Number.class, new NumberCellRenderer());
        setDefaultRenderer(Boolean.class, new BooleanCellRenderer());

        // Set selection color
        setSelectionBackground(selectionBackground);
        setSelectionForeground(Color.WHITE);

        // Set border
        setBorder(BorderFactory.createLineBorder(borderColor));
    }

    public void setAlternatingRowColors(boolean enabled) {
        this.useAlternatingRows = enabled;
        repaint();
    }

    public void setAlternateRowColor(Color color) {
        this.alternateRowColor = color;
        repaint();
    }

    public void setHeaderBackground(Color color) {
        this.headerBackground = color;
        getTableHeader().setBackground(color);
        repaint();
    }

    public void setHeaderForeground(Color color) {
        this.headerForeground = color;
        getTableHeader().setForeground(color);
        repaint();
    }

    public void setCellPadding(int horizontal, int vertical) {
        this.cellHorizontalPadding = horizontal;
        this.cellVerticalPadding = vertical;
        repaint();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    private class ModernHeaderRenderer extends DefaultTableCellRenderer {
        public ModernHeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            setBackground(headerBackground);
            setForeground(headerForeground);
            setBorder(BorderFactory.createEmptyBorder(0, cellHorizontalPadding, 0, cellHorizontalPadding));

            return this;
        }
    }

    private class ModernCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            setBorder(BorderFactory.createEmptyBorder(
                    cellVerticalPadding, cellHorizontalPadding,
                    cellVerticalPadding, cellHorizontalPadding));

            if (isSelected) {
                setBackground(selectionBackground);
                setForeground(Color.WHITE);
            } else {
                if (useAlternatingRows && row % 2 == 1) {
                    setBackground(alternateRowColor);
                } else {
                    setBackground(Color.WHITE);
                }
                setForeground(Color.BLACK);
            }

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (isOpaque()) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getBackground().equals(selectionBackground)) {
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                } else {
                    g2d.setColor(getBackground());
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                g2d.dispose();
            }

            super.paintComponent(g);
        }
    }

    private class NumberCellRenderer extends ModernCellRenderer {
        public NumberCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    private class BooleanCellRenderer extends JCheckBox implements TableCellRenderer {
        public BooleanCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setBorderPainted(false);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (value instanceof Boolean) {
                setSelected((Boolean) value);
            }

            if (isSelected) {
                setBackground(selectionBackground);
                setForeground(Color.WHITE);
            } else {
                if (useAlternatingRows && row % 2 == 1) {
                    setBackground(alternateRowColor);
                } else {
                    setBackground(Color.WHITE);
                }
                setForeground(Color.BLACK);
            }

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (isOpaque()) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getBackground().equals(selectionBackground)) {
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                } else {
                    g2d.setColor(getBackground());
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                g2d.dispose();
            }

            super.paintComponent(g);
        }
    }
}
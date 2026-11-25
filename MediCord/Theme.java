import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Central place for the MediCord design language:
 * colors, fonts, layout helpers, styled inputs/tables and custom components.
 */
public final class Theme {

    private Theme() {}

    // ======================= Colors =======================
    public static final Color BG        = Color.decode("#F6F7F9");
    public static final Color MAROON    = Color.decode("#651D2A");
    public static final Color MAROON_HV = Color.decode("#792535");
    public static final Color MAROON_AC = Color.decode("#913244");
    public static final Color MAROON_DK = Color.decode("#38121B");
    public static final Color TEXT      = Color.decode("#1F2937");
    public static final Color MUTED     = Color.decode("#667085");
    public static final Color BORDER    = Color.decode("#E4E7EC");
    public static final Color TINT      = Color.decode("#F9EAED");
    public static final Color ZEBRA     = Color.decode("#FAFBFC");
    public static final Color SUCCESS   = Color.decode("#19725A");
    public static final Color DANGER    = Color.decode("#B42318");
    public static final Color LIGHT_GRAY = Color.decode("#F3F5F7");

    // ======================= Fonts =======================
    public static final String FONT = "Segoe UI";
    public static final Font F_BODY  = new Font(FONT, Font.PLAIN, 14);
    public static final Font F_BOLD  = new Font(FONT, Font.BOLD, 14);
    public static final Font F_TITLE = new Font(FONT, Font.BOLD, 30);
    public static final Font F_SUB   = new Font(FONT, Font.PLAIN, 14);
    public static final Font F_SMALL = new Font(FONT, Font.PLAIN, 12);

    /** Applies global UIManager defaults. Call once at startup. */
    public static void install() {
        UIManager.put("Label.font", F_BODY);
        UIManager.put("OptionPane.messageFont", F_BODY);
        UIManager.put("OptionPane.buttonFont", F_BOLD);
        UIManager.put("ComboBox.font", F_BODY);
        UIManager.put("Spinner.font", F_BODY);
        UIManager.put("TextField.font", F_BODY);
        UIManager.put("ScrollBar.thumb", Color.decode("#CBD0D8"));
        UIManager.put("ScrollBar.track", BG);
    }

    // ======================= Layout helpers =======================

    /** Transparent vertical BoxLayout panel. */
    public static JPanel stack() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    /** Left-aligns a component inside a vertical BoxLayout. */
    public static <T extends JComponent> T left(T c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    /** A field with a small caption above it. */
    public static JPanel labeled(String text, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.add(label(text, F_BOLD, TEXT), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));
        return p;
    }

    /** Vertical form made of labeled fields. */
    public static JPanel formPanel(String[] labels, JComponent[] fields) {
        JPanel form = stack();
        for (int i = 0; i < labels.length; i++) {
            form.add(labeled(labels[i], fields[i]));
            form.add(Box.createVerticalStrut(16));
        }
        return form;
    }

    /** Standard page wrapper: title + subtitle on top, body in the center. */
    public static JPanel page(String title, String subtitle, JComponent body) {
        JPanel p = new JPanel(new BorderLayout(0, 26));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(36, 44, 36, 44));

        JPanel h = stack();
        h.add(left(label(title, F_TITLE, TEXT)));
        h.add(Box.createVerticalStrut(7));
        h.add(left(label(subtitle, F_SUB, MUTED)));

        p.add(h, BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    /** Search/filter box on the left, optional action on the right. */
    public static JPanel toolbar(JComponent main, JComponent action) {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setOpaque(false);
        bar.add(main, BorderLayout.CENTER);
        if (action != null) bar.add(action, BorderLayout.EAST);
        return bar;
    }

    /** A white rounded card with standard padding. */
    public static RoundedPanel card() {
        RoundedPanel c = new RoundedPanel(18, Color.WHITE, BORDER);
        c.setBorder(new EmptyBorder(24, 28, 24, 28));
        return c;
    }

    // ======================= Inputs =======================

    public static Border fieldBorder() {
        return new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(11, 14, 11, 14));
    }

    private static void styleField(JTextComponent f) {
        f.setFont(F_BODY);
        f.setForeground(TEXT);
        f.setCaretColor(MAROON);
        f.setBackground(Color.WHITE);
        f.setBorder(fieldBorder());
        f.setPreferredSize(new Dimension(180, 44));
    }

    public static JTextField textField() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    public static HintField searchField(String hint) {
        return new HintField(hint);
    }

    /** Multi-line text input already wrapped in a bordered scroll pane. */
    public static JScrollPane textArea(int rows) {
        JTextArea a = new JTextArea(rows, 20);
        a.setFont(F_BODY);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(10, 12, 10, 12));
        a.setForeground(TEXT);
        a.setCaretColor(MAROON);
        JScrollPane sp = new JScrollPane(a);
        sp.setBorder(new LineBorder(BORDER, 1, true));
        return sp;
    }

    public static JComboBox<String> comboBox(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(F_BODY);
        c.setBackground(Color.WHITE);
        c.setForeground(TEXT);
        c.setPreferredSize(new Dimension(160, 44));
        return c;
    }

    public static JSpinner dateSpinner() {
        JSpinner s = new JSpinner(new SpinnerDateModel());
        s.setEditor(new JSpinner.DateEditor(s, "MMM dd, yyyy"));
        s.setFont(F_BODY);
        s.setBorder(new LineBorder(BORDER, 1, true));
        ((JSpinner.DefaultEditor) s.getEditor()).getTextField().setBorder(new EmptyBorder(11, 10, 11, 10));
        s.setPreferredSize(new Dimension(150, 44));
        return s;
    }

    /** Text field that paints a placeholder while empty. */
    public static class HintField extends JTextField {
        private final String hint;

        public HintField(String hint) {
            this.hint = hint;
            styleField(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                Insets in = getInsets();
                g2.drawString(hint, in.left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        }
    }

    // ======================= Stats =======================

    public static JPanel statBox(String label, String value, String unit) {
        RoundedPanel box = new RoundedPanel(18, Color.WHITE, BORDER);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(20, 24, 20, 24));

        box.add(left(label(label.toUpperCase(), new Font(FONT, Font.BOLD, 12), MUTED)));
        box.add(Box.createVerticalStrut(8));
        box.add(left(label(value, new Font(FONT, Font.BOLD, 36), MAROON)));
        box.add(Box.createVerticalStrut(2));
        box.add(left(label(unit, F_SMALL, MUTED)));
        return box;
    }

    // ======================= Tables =======================

    /** Read-only table model. */
    public static javax.swing.table.DefaultTableModel model(String[] cols, Object[][] rows) {
        return new javax.swing.table.DefaultTableModel(rows, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }

    /** Fully styled table for the given model. */
    public static JTable table(TableModel model) {
        JTable t = new JTable(model);
        styleTable(t);
        styleHeader(t);
        t.setAutoCreateRowSorter(true);
        t.setFillsViewportHeight(true);
        t.setDefaultRenderer(Object.class, new CellRenderer());
        return t;
    }

    public static void styleTable(JTable t) {
        t.setFont(F_BODY);
        t.setForeground(TEXT);
        t.setRowHeight(46);
        t.setShowGrid(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDER);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(TINT);
        t.setSelectionForeground(TEXT);
    }

    public static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(tb, v, sel, foc, r, c);
            setBorder(new EmptyBorder(0, 14, 0, 14));
            setBackground(sel ? TINT : (r % 2 == 0 ? Color.WHITE : ZEBRA));
            setForeground(TEXT);
            return this;
        }
    }

    public static void styleHeader(JTable t) {
        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 46));
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(tb, v, sel, foc, r, c);
                setHorizontalAlignment(LEFT);
                setBackground(Color.WHITE);
                setForeground(TEXT);
                setFont(new Font(FONT, Font.BOLD, 13));
                setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER), new EmptyBorder(0, 14, 0, 14)));
                return this;
            }
        });
    }

    public static JScrollPane scrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER, 1, true));
        sp.getViewport().setBackground(Color.WHITE);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    /** Live-filters a table as the user types in the field. */
    public static void attachFilter(JTable table, JTextField field) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void apply() {
                String q = field.getText().trim();
                sorter.setRowFilter(q.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(q)));
            }
            @Override public void insertUpdate(DocumentEvent e) { apply(); }
            @Override public void removeUpdate(DocumentEvent e) { apply(); }
            @Override public void changedUpdate(DocumentEvent e) { apply(); }
        });
    }

    // ======================= Custom components =======================

    /** Sidebar navigation button with hover and active states. */
    public static class CustomButton extends JButton {
        private boolean active;

        public CustomButton(String text) {
            super(text);
            setFont(F_BOLD);
            setForeground(Color.WHITE);
            setHorizontalAlignment(LEFT);
            setBorder(new EmptyBorder(0, 32, 0, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            Dimension d = new Dimension(276, 52);
            setPreferredSize(d);
            setMinimumSize(d);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        }

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        public boolean isActive() {
            return active;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (active) {
                g2.setColor(MAROON_AC);
                g2.fillRoundRect(12, 4, getWidth() - 24, getHeight() - 8, 12, 12);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(18, (getHeight() - 18) / 2, 3, 18, 3, 3);
            } else if (getModel().isPressed()) {
                g2.setColor(MAROON_AC);
                g2.fillRoundRect(12, 4, getWidth() - 24, getHeight() - 8, 12, 12);
            } else if (getModel().isRollover()) {
                g2.setColor(MAROON_HV);
                g2.fillRoundRect(12, 4, getWidth() - 24, getHeight() - 8, 12, 12);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Primary rounded action button. Pass outline=true for a secondary look. */
    public static class RoundedButton extends JButton {
        private final boolean outline;

        public RoundedButton(String text) {
            this(text, false);
        }

        public RoundedButton(String text, boolean outline) {
            super(text);
            this.outline = outline;
            setFont(F_BOLD);
            setForeground(outline ? MAROON : Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(12, 24, 12, 24));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (outline) {
                g2.setColor(getModel().isRollover() ? TINT : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(MAROON);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            } else {
                Color c = MAROON;
                if (getModel().isPressed()) c = MAROON.darker();
                else if (getModel().isRollover()) c = blend(MAROON, Color.BLACK, 0.08f);
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
            g2.dispose();
            super.paintComponent(g);
        }

        private static Color blend(Color a, Color b, float amount) {
            float keep = 1f - amount;
            return new Color(
                Math.round(a.getRed() * keep + b.getRed() * amount),
                Math.round(a.getGreen() * keep + b.getGreen() * amount),
                Math.round(a.getBlue() * keep + b.getBlue() * amount));
        }
    }

    /** Rounded panel with a soft shadow and optional outline. */
    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;
        private final Color line;

        public RoundedPanel(int radius, Color fill, Color line) {
            this.radius = radius;
            this.fill = fill;
            this.line = line;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(16, 24, 40, 12)); // soft shadow
            g2.fillRoundRect(0, 2, getWidth() - 1, getHeight() - 2, radius, radius);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, radius, radius);
            if (line != null) {
                g2.setColor(line);
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 4, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Maroon diagonal gradient with two soft decorative circles. */
    public static class GradientPanel extends JPanel {
        public GradientPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, MAROON, getWidth(), getHeight(), MAROON_DK));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 12));
            g2.fillOval(-120, getHeight() - 260, 420, 420);
            g2.fillOval(getWidth() - 190, -120, 320, 320);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}

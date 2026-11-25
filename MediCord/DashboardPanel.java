import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shared dashboard shell: maroon sidebar with navigation on the left,
 * switchable content pages on the right. Role dashboards extend this
 * and register their pages with addPage(...).
 */
public abstract class DashboardPanel extends JPanel {

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final JPanel nav = Theme.stack();
    private final Map<String, Theme.CustomButton> buttons = new LinkedHashMap<>();
    private String firstPage;

    protected DashboardPanel(String role, String username, Runnable onLogout) {
        setLayout(new BorderLayout());
        content.setBackground(Theme.BG);

        add(createSidebar(role, username, onLogout), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createSidebar(String role, String username, Runnable onLogout) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Theme.MAROON);
        sidebar.setPreferredSize(new Dimension(276, 0));

        // Brand block
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(34, 28, 28, 20));

        JLabel logo = Theme.label("\u271A MediCord", new Font(Theme.FONT, Font.BOLD, 25), Color.WHITE);
        JLabel sub = Theme.label("Government Health Center", Theme.F_SMALL, new Color(255, 255, 255, 185));

        JLabel roleTag = Theme.label(" " + role.toUpperCase() + " ", new Font(Theme.FONT, Font.BOLD, 11), Color.WHITE);
        roleTag.setOpaque(true);
        roleTag.setBackground(Theme.MAROON_AC);
        roleTag.setBorder(new EmptyBorder(5, 9, 5, 9));

        JLabel user = Theme.label("Signed in as " + username, Theme.F_SMALL, new Color(255, 255, 255, 205));

        brand.add(Theme.left(logo));
        brand.add(Box.createVerticalStrut(4));
        brand.add(Theme.left(sub));
        brand.add(Box.createVerticalStrut(22));
        brand.add(Theme.left(roleTag));
        brand.add(Box.createVerticalStrut(10));
        brand.add(Theme.left(user));

        // Footer with logout
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(8, 0, 22, 0));
        Theme.CustomButton logout = new Theme.CustomButton("Log out");
        logout.addActionListener(e -> onLogout.run());
        footer.add(logout, BorderLayout.CENTER);

        sidebar.add(brand, BorderLayout.NORTH);
        sidebar.add(nav, BorderLayout.CENTER);
        sidebar.add(footer, BorderLayout.SOUTH);
        return sidebar;
    }

    /** Registers a page and adds a matching sidebar button. */
    protected void addPage(String title, JComponent page) {
        content.add(page, title);

        Theme.CustomButton b = new Theme.CustomButton(title);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(e -> showPage(title));
        buttons.put(title, b);
        nav.add(b);
        nav.add(Box.createVerticalStrut(4));

        if (firstPage == null) {
            firstPage = title;
            showPage(title);
        }
    }

    /** Switches the visible page and highlights its sidebar button. */
    protected void showPage(String title) {
        cards.show(content, title);
        buttons.forEach((name, btn) -> btn.setActive(name.equals(title)));
    }
}

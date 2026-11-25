import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.BiConsumer;

/**
 * Login screen: branding on the left, rounded white card on the right.
 * Implemented as a JPanel so Main can swap it with the dashboards.
 */
public class LoginFrame extends JPanel {

    private static final String[] ROLES = {"Admin", "Receptionist", "Doctor"};

    // Dummy credentials per role (replace with real authentication later)
    private static final String[][] CREDENTIALS = {
        {"admin",     "admin123"},
        {"reception", "reception123"},
        {"doctor",    "doctor123"}
    };

    private final BiConsumer<String, String> onSuccess;

    private final JComboBox<String> roleBox = Theme.comboBox(ROLES);
    private final JTextField username = Theme.textField();
    private final JPasswordField password = Theme.passwordField();
    private final JLabel error = Theme.label(" ", Theme.F_SMALL, Theme.DANGER);

    public LoginFrame(BiConsumer<String, String> onSuccess) {
        this.onSuccess = onSuccess;
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.weightx = 1.1;
        add(createLeftSide(), gc);

        gc.gridx = 1;
        gc.weightx = 1.0;
        add(createRightSide(), gc);
    }

    // ---------- Left: branding ----------
    private JPanel createLeftSide() {
        JPanel left = new Theme.GradientPanel();
        left.setLayout(new GridBagLayout());

        JPanel box = Theme.stack();

        JLabel brand = Theme.label("\u271A MEDICORD", new Font(Theme.FONT, Font.BOLD, 15), Color.WHITE);
        JLabel eyebrow = Theme.label("COMMUNITY HEALTH, CONNECTED",
                new Font(Theme.FONT, Font.BOLD, 12), new Color(255, 255, 255, 185));
        JLabel hello = Theme.label("<html>Care that keeps<br>every record close.</html>",
                new Font(Theme.FONT, Font.BOLD, 42), Color.WHITE);
        JLabel sub = Theme.label(
                "<html>Medicord gives your health center one clear place<br>for patients, visits, and clinical records.</html>",
                new Font(Theme.FONT, Font.PLAIN, 15), new Color(255, 255, 255, 205));

        box.add(Theme.left(brand));
        box.add(Box.createVerticalStrut(66));
        box.add(Theme.left(eyebrow));
        box.add(Box.createVerticalStrut(14));
        box.add(Theme.left(hello));
        box.add(Box.createVerticalStrut(18));
        box.add(Theme.left(sub));

        left.add(box);
        return left;
    }

    // ---------- Right: login card ----------
    private JPanel createRightSide() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Theme.LIGHT_GRAY);

        Theme.RoundedPanel card = new Theme.RoundedPanel(22, Color.WHITE, Theme.BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(38, 42, 40, 42));
        card.setPreferredSize(new Dimension(460, 590));

        JPanel form = Theme.stack();

        form.add(Theme.left(Theme.label("Welcome back", Theme.F_TITLE, Theme.TEXT)));
        form.add(Box.createVerticalStrut(4));
        form.add(Theme.left(Theme.label("Sign in to continue to your workspace.", Theme.F_SUB, Theme.MUTED)));
        form.add(Box.createVerticalStrut(30));

        form.add(Theme.labeled("Role", roleBox));
        form.add(Box.createVerticalStrut(16));
        form.add(Theme.labeled("Username", username));
        form.add(Box.createVerticalStrut(16));
        form.add(Theme.labeled("Password", password));
        form.add(Box.createVerticalStrut(10));
        form.add(Theme.left(error));
        form.add(Box.createVerticalStrut(14));

        Theme.RoundedButton login = new Theme.RoundedButton("Log in");
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        login.setPreferredSize(new Dimension(300, 50));
        login.addActionListener(e -> doLogin());
        form.add(Theme.left(login));

        form.add(Box.createVerticalStrut(22));
        form.add(Theme.left(Theme.label(
                "<html>Demo logins: admin / admin123,<br>reception / reception123, doctor / doctor123</html>",
                Theme.F_SMALL, Theme.MUTED)));

        username.addActionListener(e -> doLogin());
        password.addActionListener(e -> doLogin());

        card.add(form, BorderLayout.CENTER);
        right.add(card);
        return right;
    }

    // ---------- Login logic ----------
    private void doLogin() {
        int idx = roleBox.getSelectedIndex();
        String role = ROLES[idx];
        String user = username.getText().trim();
        String pass = new String(password.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            error.setText("Please enter your username and password.");
            return;
        }

        if (CREDENTIALS[idx][0].equalsIgnoreCase(user) && CREDENTIALS[idx][1].equals(pass)) {
            error.setText(" ");
            onSuccess.accept(role, user);
        } else {
            error.setText("Invalid username or password for the " + role + " role.");
            password.setText("");
        }
    }
}

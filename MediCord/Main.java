import javax.swing.*;
import java.awt.*;

/**
 * Application entry point. Owns the window and switches between
 * the login screen and the role-specific dashboards.
 */
public class Main extends JFrame {

    public Main() {
        super("MediCord | Government Health Center");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1320, 820);
        setMinimumSize(new Dimension(1080, 700));
        setLocationRelativeTo(null);
        showLogin();
    }

    /** Shows a fresh login screen (also used after logout). */
    public void showLogin() {
        show(new LoginFrame(this::showDashboard));
    }

    /** Opens the dashboard that matches the logged-in role. */
    public void showDashboard(String role, String username) {
        Runnable logout = this::showLogin;
        JComponent dashboard;
        switch (role) {
            case "Admin":        dashboard = new AdminDashboard(username, logout); break;
            case "Receptionist": dashboard = new ReceptionistDashboard(username, logout); break;
            case "Doctor":       dashboard = new DoctorDashboard(username, logout); break;
            default: throw new IllegalArgumentException("Unknown role: " + role);
        }
        show(dashboard);
    }

    private void show(JComponent view) {
        getContentPane().removeAll();
        getContentPane().add(view, BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();
            new Main().setVisible(true);
        });
    }
}

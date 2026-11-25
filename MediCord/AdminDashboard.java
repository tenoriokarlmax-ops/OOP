import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Admin: system overview, staff accounts, and reports. */
public class AdminDashboard extends DashboardPanel {

    private final DefaultTableModel userModel = Theme.model(
            new String[]{"Full name", "Username", "Role", "Status"},
            new Object[][]{
                {"Maria Santos",    "admin",     "Admin",        "Active"},
                {"Ana Reyes",       "reception", "Receptionist", "Active"},
                {"Dr. Jose Cruz",   "doctor",    "Doctor",       "Active"},
                {"Dr. Liza Garcia", "lgarcia",   "Doctor",       "Active"},
                {"Mark Villanueva", "mvillanue", "Receptionist", "Inactive"}
            });

    public AdminDashboard(String username, Runnable onLogout) {
        super("Admin", username, onLogout);
        addPage("Overview", overviewPage());
        addPage("Staff Accounts", usersPage());
        addPage("Reports", reportsPage());
    }

    private JComponent overviewPage() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 18, 0));
        stats.setOpaque(false);
        stats.add(Theme.statBox("Patients", "1,284", "registered"));
        stats.add(Theme.statBox("Today", "86", "visits"));
        stats.add(Theme.statBox("Doctors", "12", "on duty"));
        stats.add(Theme.statBox("Staff", "27", "accounts"));

        JTable activity = Theme.table(Theme.model(
                new String[]{"Time", "User", "Action"},
                new Object[][]{
                    {"09:42 AM", "reception", "Registered patient Juan Dela Cruz"},
                    {"09:15 AM", "doctor",    "Saved consultation for Maria Lopez"},
                    {"08:58 AM", "admin",     "Created staff account lgarcia"},
                    {"08:30 AM", "reception", "Booked appointment for Pedro Ramos"},
                    {"08:05 AM", "doctor",    "Updated patient record #1021"}
                }));

        JPanel body = new JPanel(new BorderLayout(0, 24));
        body.setOpaque(false);
        body.add(stats, BorderLayout.NORTH);
        body.add(Theme.scrollPane(activity), BorderLayout.CENTER);

        return Theme.page("Overview", "Recent activity across the health center.", body);
    }

    private JComponent usersPage() {
        JTable table = Theme.table(userModel);
        Theme.HintField search = Theme.searchField("Search staff...");
        Theme.attachFilter(table, search);

        Theme.RoundedButton add = new Theme.RoundedButton("+ Add user");
        add.addActionListener(e -> addUser());

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);
        body.add(Theme.toolbar(search, add), BorderLayout.NORTH);
        body.add(Theme.scrollPane(table), BorderLayout.CENTER);

        return Theme.page("Staff Accounts", "Manage who can access MediCord.", body);
    }

    private void addUser() {
        JTextField name = Theme.textField();
        JTextField uname = Theme.textField();
        JComboBox<String> role = Theme.comboBox("Admin", "Receptionist", "Doctor");

        JPanel form = Theme.formPanel(
                new String[]{"Full name", "Username", "Role"},
                new JComponent[]{name, uname, role});
        form.setPreferredSize(new Dimension(360, 280));

        int result = JOptionPane.showConfirmDialog(this, form, "Add user",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (name.getText().isBlank() || uname.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Name and username are required.");
                return;
            }
            userModel.addRow(new Object[]{name.getText().trim(), uname.getText().trim(),
                    role.getSelectedItem(), "Active"});
        }
    }

    private JComponent reportsPage() {
        JPanel stats = new JPanel(new GridLayout(1, 3, 18, 0));
        stats.setOpaque(false);
        stats.add(Theme.statBox("This month", "1,942", "visits"));
        stats.add(Theme.statBox("New patients", "138", "registered"));
        stats.add(Theme.statBox("Avg. wait", "14", "minutes"));

        JTable table = Theme.table(Theme.model(
                new String[]{"Month", "Visits", "New patients", "Follow-ups"},
                new Object[][]{
                    {"September", "1,742", "121", "604"},
                    {"October",   "1,865", "129", "655"},
                    {"November",  "1,942", "138", "702"}
                }));

        JPanel body = new JPanel(new BorderLayout(0, 24));
        body.setOpaque(false);
        body.add(stats, BorderLayout.NORTH);
        body.add(Theme.scrollPane(table), BorderLayout.CENTER);

        return Theme.page("Reports", "Monthly visit summary.", body);
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Receptionist: patient list, patient registration, and appointments. */
public class ReceptionistDashboard extends DashboardPanel {

    private final DefaultTableModel patientModel = Theme.model(
            new String[]{"ID", "Name", "Sex", "Contact", "Registered"},
            new Object[][]{
                {"P-1021", "Maria Lopez",      "Female", "0917 123 4567", "Nov 20, 2026"},
                {"P-1022", "Pedro Ramos",      "Male",   "0918 222 3344", "Nov 21, 2026"},
                {"P-1023", "Juan Dela Cruz",   "Male",   "0920 555 7788", "Nov 24, 2026"},
                {"P-1024", "Carmen Bautista",  "Female", "0915 888 1122", "Nov 25, 2026"}
            });

    public ReceptionistDashboard(String username, Runnable onLogout) {
        super("Receptionist", username, onLogout);
        addPage("Patients", patientsPage());
        addPage("Register Patient", registerPage());
        addPage("Appointments", appointmentsPage());
    }

    private JComponent patientsPage() {
        JTable table = Theme.table(patientModel);
        Theme.HintField search = Theme.searchField("Search patients by name, ID or contact...");
        Theme.attachFilter(table, search);

        Theme.RoundedButton register = new Theme.RoundedButton("+ Register patient");
        register.addActionListener(e -> showPage("Register Patient"));

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);
        body.add(Theme.toolbar(search, register), BorderLayout.NORTH);
        body.add(Theme.scrollPane(table), BorderLayout.CENTER);

        return Theme.page("Patients", "Find and manage registered patients.", body);
    }

    private JComponent registerPage() {
        JTextField name = Theme.textField();
        JSpinner birth = Theme.dateSpinner();
        JComboBox<String> sex = Theme.comboBox("Female", "Male");
        JTextField contact = Theme.textField();
        JTextField address = Theme.textField();
        JTextField guardian = Theme.textField();

        JPanel grid = new JPanel(new GridLayout(0, 2, 22, 18));
        grid.setOpaque(false);
        grid.add(Theme.labeled("Full name", name));
        grid.add(Theme.labeled("Date of birth", birth));
        grid.add(Theme.labeled("Sex", sex));
        grid.add(Theme.labeled("Contact number", contact));
        grid.add(Theme.labeled("Address", address));
        grid.add(Theme.labeled("Guardian / emergency contact", guardian));

        Theme.RoundedButton save = new Theme.RoundedButton("Save patient");
        Theme.RoundedButton clear = new Theme.RoundedButton("Clear", true);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttons.setOpaque(false);
        buttons.add(save);
        buttons.add(clear);

        Theme.RoundedPanel card = Theme.card();
        card.setLayout(new BorderLayout(0, 24));
        card.add(grid, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);

        clear.addActionListener(e -> {
            name.setText("");
            contact.setText("");
            address.setText("");
            guardian.setText("");
        });

        save.addActionListener(e -> {
            if (name.getText().isBlank() || contact.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Name and contact number are required.");
                return;
            }
            String id = "P-" + (1021 + patientModel.getRowCount());
            patientModel.addRow(new Object[]{id, name.getText().trim(), sex.getSelectedItem(),
                    contact.getText().trim(), new SimpleDateFormat("MMM dd, yyyy").format(new Date())});
            JOptionPane.showMessageDialog(this, "Patient registered with ID " + id + ".");
            clear.doClick();
            showPage("Patients");
        });

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(card, BorderLayout.NORTH);

        return Theme.page("Register Patient", "Enter the patient's basic information.", body);
    }

    private JComponent appointmentsPage() {
        JTable table = Theme.table(Theme.model(
                new String[]{"Time", "Patient", "Doctor", "Reason", "Status"},
                new Object[][]{
                    {"09:00 AM", "Maria Lopez",     "Dr. Jose Cruz",   "Prenatal check-up", "Done"},
                    {"09:30 AM", "Pedro Ramos",     "Dr. Liza Garcia", "Blood pressure",    "In consultation"},
                    {"10:00 AM", "Juan Dela Cruz",  "Dr. Jose Cruz",   "Fever and cough",   "Waiting"},
                    {"10:30 AM", "Carmen Bautista", "Dr. Liza Garcia", "Immunization",      "Waiting"}
                }));

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(Theme.scrollPane(table), BorderLayout.CENTER);

        return Theme.page("Appointments", "Today's schedule.", body);
    }
}

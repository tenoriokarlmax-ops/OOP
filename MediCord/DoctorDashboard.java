import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Doctor: today's queue, consultation form, and patient records. */
public class DoctorDashboard extends DashboardPanel {

    private final DefaultTableModel recordModel = Theme.model(
            new String[]{"Date", "Patient", "Diagnosis", "Prescription"},
            new Object[][]{
                {"Nov 20, 2026", "Maria Lopez",     "Normal pregnancy, 28 weeks", "Prenatal vitamins"},
                {"Nov 21, 2026", "Pedro Ramos",     "Hypertension, stage 1",      "Amlodipine 5 mg daily"},
                {"Nov 24, 2026", "Carmen Bautista", "Routine immunization",       "None"}
            });

    public DoctorDashboard(String username, Runnable onLogout) {
        super("Doctor", username, onLogout);
        addPage("Today's Queue", queuePage());
        addPage("Consultation", consultationPage());
        addPage("Patient Records", recordsPage());
    }

    private JComponent queuePage() {
        JPanel stats = new JPanel(new GridLayout(1, 3, 18, 0));
        stats.setOpaque(false);
        stats.add(Theme.statBox("Waiting", "4", "patients"));
        stats.add(Theme.statBox("Seen today", "9", "patients"));
        stats.add(Theme.statBox("Next", "10:00", "Juan Dela Cruz"));

        JTable table = Theme.table(Theme.model(
                new String[]{"Time", "Patient", "Reason", "Status"},
                new Object[][]{
                    {"09:30 AM", "Pedro Ramos",     "Blood pressure",  "In consultation"},
                    {"10:00 AM", "Juan Dela Cruz",  "Fever and cough", "Waiting"},
                    {"10:30 AM", "Carmen Bautista", "Immunization",    "Waiting"},
                    {"11:00 AM", "Elena Torres",    "Follow-up",       "Waiting"}
                }));

        JPanel body = new JPanel(new BorderLayout(0, 24));
        body.setOpaque(false);
        body.add(stats, BorderLayout.NORTH);
        body.add(Theme.scrollPane(table), BorderLayout.CENTER);

        return Theme.page("Today's Queue", "Patients assigned to you today.", body);
    }

    private JComponent consultationPage() {
        JComboBox<String> patient = Theme.comboBox("Pedro Ramos", "Juan Dela Cruz", "Carmen Bautista", "Elena Torres");
        JScrollPane diagnosis = Theme.textArea(4);
        JScrollPane prescription = Theme.textArea(4);

        JPanel form = Theme.stack();
        form.add(Theme.labeled("Patient", patient));
        form.add(Box.createVerticalStrut(16));
        form.add(Theme.labeled("Diagnosis / notes", diagnosis));
        form.add(Box.createVerticalStrut(16));
        form.add(Theme.labeled("Prescription", prescription));
        form.add(Box.createVerticalStrut(20));

        Theme.RoundedButton save = new Theme.RoundedButton("Save consultation");
        form.add(Theme.left(save));

        save.addActionListener(e -> {
            JTextArea d = (JTextArea) diagnosis.getViewport().getView();
            JTextArea p = (JTextArea) prescription.getViewport().getView();
            if (d.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter a diagnosis or notes.");
                return;
            }
            recordModel.insertRow(0, new Object[]{
                new SimpleDateFormat("MMM dd, yyyy").format(new Date()),
                patient.getSelectedItem(),
                d.getText().trim(),
                p.getText().isBlank() ? "None" : p.getText().trim()
            });
            d.setText("");
            p.setText("");
            JOptionPane.showMessageDialog(this, "Consultation saved.");
            showPage("Patient Records");
        });

        Theme.RoundedPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(form, BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(card, BorderLayout.NORTH);

        return Theme.page("Consultation", "Record diagnosis and prescription.", body);
    }

    private JComponent recordsPage() {
        JTable table = Theme.table(recordModel);
        Theme.HintField search = Theme.searchField("Search records...");
        Theme.attachFilter(table, search);

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);
        body.add(Theme.toolbar(search, null), BorderLayout.NORTH);
        body.add(Theme.scrollPane(table), BorderLayout.CENTER);

        return Theme.page("Patient Records", "Past consultations and prescriptions.", body);
    }
}

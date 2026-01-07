package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Clinician;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClinicianPanel extends JPanel {

    private HealthcareController controller;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField clinicianIDField, firstNameField, lastNameField, qualificationField;
    private JTextField specialtyField, gmcNumberField, workplaceField, workplaceTypeField;
    private JTextField employmentStatusField, startDateField, emailField, phoneField;

    public ClinicianPanel(HealthcareController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        refreshData();
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel() {
        String[] columns = {
                "ID", "First Name", "Last Name", "Qualification",
                "Specialty", "GMC#", "Workplace", "Workplace Type",
                "Status", "Start Date", "Email", "Phone"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionBackground(new Color(227, 242, 253));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadSelectedClinician();
            }
        });

        setTableColumnWidths();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Clinicians List"));
        return scrollPane;
    }

    private void setTableColumnWidths() {
        int[] w = {60, 100, 100, 120, 120, 80, 150, 120, 100, 100, 180, 120};
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }

    // ================= FORM =================
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Clinician Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        clinicianIDField = field(); firstNameField = field(); lastNameField = field();
        qualificationField = field(); specialtyField = field(); gmcNumberField = field();
        workplaceField = field(); workplaceTypeField = field(); employmentStatusField = field();
        startDateField = field(); emailField = field(); phoneField = field();

        int row = 0;
        addFormRow(panel, gbc, row++, "Clinician ID", clinicianIDField, "First Name", firstNameField);
        addFormRow(panel, gbc, row++, "Last Name", lastNameField, "Qualification", qualificationField);
        addFormRow(panel, gbc, row++, "Specialty", specialtyField, "GMC Number", gmcNumberField);
        addFormRow(panel, gbc, row++, "Workplace", workplaceField, "Workplace Type", workplaceTypeField);
        addFormRow(panel, gbc, row++, "Employment Status", employmentStatusField, "Start Date", startDateField);
        addFormRow(panel, gbc, row++, "Email", emailField, "Phone", phoneField);

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String label1, JTextField field1,
                            String label2, JTextField field2) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label1), gbc);
        gbc.gridx = 1;
        panel.add(field1, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel(label2), gbc);
        gbc.gridx = 3;
        panel.add(field2, gbc);
    }

    private JTextField field() {
        return new JTextField(12);
    }

    // ================= BUTTONS =================
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");
        JButton refresh = new JButton("Refresh");

        add.addActionListener(e -> { addClinician(); });
        update.addActionListener(e -> { updateClinician(); });
        delete.addActionListener(e -> { deleteClinician(); });
        clear.addActionListener(e -> { clearForm(); });
        refresh.addActionListener(e -> { refreshData(); });

        panel.add(add); panel.add(update); panel.add(delete);
        panel.add(clear); panel.add(refresh);

        return panel;
    }

    // ================= CRUD =================
    private void addClinician() {
        controller.addClinician(createClinicianFromForm());
        refreshData(); clearForm();
    }

    private void updateClinician() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteClinician((String) tableModel.getValueAt(row, 0));
            controller.addClinician(createClinicianFromForm());
            refreshData(); clearForm();
        }
    }

    private void deleteClinician() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteClinician((String) tableModel.getValueAt(row, 0));
            refreshData(); clearForm();
        }
    }

    // ================= DATA =================
    public void refreshData() {
        tableModel.setRowCount(0);
        for (Clinician c : controller.getAllClinicians()) {
            tableModel.addRow(new Object[]{
                    c.getClinicianID(),
                    c.getFirstName(),
                    c.getLastName(),
                    c.getQualification(),
                    c.getSpecialty(),
                    c.getGmcNumber(),
                    c.getWorkplace(),
                    c.getWorkplaceType(),
                    c.getEmploymentStatus(),
                    c.getStartDate(),
                    c.getEmail(),
                    c.getPhone()
            });
        }
    }

    private Clinician createClinicianFromForm() {
        return new Clinician(
                clinicianIDField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                qualificationField.getText(),
                specialtyField.getText(),
                gmcNumberField.getText(),
                workplaceField.getText(),
                workplaceTypeField.getText(),
                employmentStatusField.getText(),
                startDateField.getText(),
                emailField.getText(),
                phoneField.getText()
        );
    }

    private void loadSelectedClinician() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        clinicianIDField.setText((String) tableModel.getValueAt(r, 0));
        firstNameField.setText((String) tableModel.getValueAt(r, 1));
        lastNameField.setText((String) tableModel.getValueAt(r, 2));
        qualificationField.setText((String) tableModel.getValueAt(r, 3));
        specialtyField.setText((String) tableModel.getValueAt(r, 4));
        gmcNumberField.setText((String) tableModel.getValueAt(r, 5));
        workplaceField.setText((String) tableModel.getValueAt(r, 6));
        workplaceTypeField.setText((String) tableModel.getValueAt(r, 7));
        employmentStatusField.setText((String) tableModel.getValueAt(r, 8));
        startDateField.setText((String) tableModel.getValueAt(r, 9));
        emailField.setText((String) tableModel.getValueAt(r, 10));
        phoneField.setText((String) tableModel.getValueAt(r, 11));
    }

    private void clearForm() {
        clinicianIDField.setText(""); firstNameField.setText(""); lastNameField.setText("");
        qualificationField.setText(""); specialtyField.setText(""); gmcNumberField.setText("");
        workplaceField.setText(""); workplaceTypeField.setText(""); employmentStatusField.setText("");
        startDateField.setText(""); emailField.setText(""); phoneField.setText("");
    }
}

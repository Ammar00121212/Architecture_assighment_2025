package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {

    private HealthcareController controller;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField patientIDField, firstNameField, lastNameField, dobField, genderField;
    private JTextField nhsNumberField, emailField, phoneField, addressField, postcodeField;
    private JTextField emergencyContactNameField, emergencyContactPhoneField;
    private JTextField registrationDateField, gpSurgeryField;

    public PatientPanel(HealthcareController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        // ---------- BUTTON BAR ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(new Color(245, 247, 250));

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> addPatient());
        updateBtn.addActionListener(e -> updatePatient());
        deleteBtn.addActionListener(e -> deletePatient());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> refreshData());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        String[] cols = {
                "Patient ID", "First Name", "Last Name", "DOB", "Gender",
                "NHS No", "Email", "Phone", "Address", "Postcode",
                "Emergency Name", "Emergency Phone", "Reg Date", "GP Surgery"
        };

        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setColumnWidths();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPatient();
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Patients"));

        // ---------- FORM ----------
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));

        // ---------- SPLIT PANE (MAIN FIX) ----------
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tableScroll,
                formPanel
        );
        splitPane.setResizeWeight(0.65); // table 65%, form 35%
        splitPane.setDividerSize(6);

        add(splitPane, BorderLayout.CENTER);
    }

    // ---------- FORM PANEL ----------
    private JPanel createFormPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;

        // Column 1
        addField(p, g, 0, r++, "Patient ID", patientIDField = field());
        addField(p, g, 0, r++, "First Name", firstNameField = field());
        addField(p, g, 0, r++, "Last Name", lastNameField = field());
        addField(p, g, 0, r++, "DOB", dobField = field());
        addField(p, g, 0, r++, "Gender", genderField = field());
        addField(p, g, 0, r++, "NHS No", nhsNumberField = field());

        // Column 2
        r = 0;
        addField(p, g, 2, r++, "Email", emailField = field());
        addField(p, g, 2, r++, "Phone", phoneField = field());
        addField(p, g, 2, r++, "Address", addressField = field());
        addField(p, g, 2, r++, "Postcode", postcodeField = field());
        addField(p, g, 2, r++, "Emergency Name", emergencyContactNameField = field());
        addField(p, g, 2, r++, "Emergency Phone", emergencyContactPhoneField = field());

        // Column 3
        r = 0;
        addField(p, g, 4, r++, "Reg Date", registrationDateField = field());
        addField(p, g, 4, r++, "GP Surgery", gpSurgeryField = field());

        return p;
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(180, 26));
        return f;
    }

    private void addField(JPanel p, GridBagConstraints g, int x, int y, String label, JTextField field) {
        g.gridx = x;
        g.gridy = y;
        g.weightx = 0;
        p.add(new JLabel(label), g);

        g.gridx = x + 1;
        g.weightx = 1;
        p.add(field, g);
    }

    // ---------- CRUD ----------
    private void addPatient() {
        controller.addPatient(createPatient());
        refreshData();
        clearForm();
    }

    private void updatePatient() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deletePatient((String) tableModel.getValueAt(row, 0));
            controller.addPatient(createPatient());
            refreshData();
        }
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deletePatient((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Patient createPatient() {
        return new Patient(
                patientIDField.getText(),
                firstNameField.getText(),
                lastNameField.getText(),
                dobField.getText(),
                genderField.getText(),
                nhsNumberField.getText(),
                emailField.getText(),
                phoneField.getText(),
                addressField.getText(),
                postcodeField.getText(),
                emergencyContactNameField.getText(),
                emergencyContactPhoneField.getText(),
                registrationDateField.getText(),
                gpSurgeryField.getText()
        );
    }

    private void loadSelectedPatient() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        patientIDField.setText(tableModel.getValueAt(r, 0).toString());
        firstNameField.setText(tableModel.getValueAt(r, 1).toString());
        lastNameField.setText(tableModel.getValueAt(r, 2).toString());
        dobField.setText(tableModel.getValueAt(r, 3).toString());
        genderField.setText(tableModel.getValueAt(r, 4).toString());
        nhsNumberField.setText(tableModel.getValueAt(r, 5).toString());
        emailField.setText(tableModel.getValueAt(r, 6).toString());
        phoneField.setText(tableModel.getValueAt(r, 7).toString());
        addressField.setText(tableModel.getValueAt(r, 8).toString());
        postcodeField.setText(tableModel.getValueAt(r, 9).toString());
        emergencyContactNameField.setText(tableModel.getValueAt(r,10).toString());
        emergencyContactPhoneField.setText(tableModel.getValueAt(r,11).toString());
        registrationDateField.setText(tableModel.getValueAt(r,12).toString());
        gpSurgeryField.setText(tableModel.getValueAt(r,13).toString());
    }

    private void clearForm() {
        for (Component c : ((JPanel)((JSplitPane)getComponent(1)).getBottomComponent()).getComponents()) {
            if (c instanceof JTextField) ((JTextField)c).setText("");
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Patient> list = controller.getAllPatients();
        for (Patient p : list) {
            tableModel.addRow(new Object[]{
                    p.getPatientID(), p.getFirstName(), p.getLastName(),
                    p.getDateOfBirth(), p.getGender(), p.getNhsNumber(),
                    p.getEmail(), p.getPhone(), p.getAddress(), p.getPostcode(),
                    p.getEmergencyContactName(), p.getEmergencyContactPhone(),
                    p.getRegistrationDate(), p.getGpSurgery()
            });
        }
    }

    private void setColumnWidths() {
        int[] w = {90,100,100,90,70,120,150,110,200,90,140,120,110,140};
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }
}

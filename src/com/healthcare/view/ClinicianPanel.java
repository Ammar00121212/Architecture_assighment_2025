package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Clinician;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClinicianPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField clinicianIDField, firstNameField, lastNameField, qualificationField;
    private JTextField specialtyField, workplaceField, emailField, phoneField;

    public ClinicianPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        String[] columns = {"Clinician ID", "First Name", "Last Name", "Qualification", 
                          "Specialty", "Workplace", "Email", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedClinician();
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);
        add(createButtonPanel(), BorderLayout.NORTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addField(panel, gbc, row++, "Clinician ID:", clinicianIDField = new JTextField(15));
        addField(panel, gbc, row++, "First Name:", firstNameField = new JTextField(15));
        addField(panel, gbc, row++, "Last Name:", lastNameField = new JTextField(15));
        addField(panel, gbc, row++, "Qualification:", qualificationField = new JTextField(15));
        addField(panel, gbc, row++, "Specialty:", specialtyField = new JTextField(15));
        addField(panel, gbc, row++, "Workplace:", workplaceField = new JTextField(15));
        addField(panel, gbc, row++, "Email:", emailField = new JTextField(15));
        addField(panel, gbc, row++, "Phone:", phoneField = new JTextField(15));

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        addButton(panel, "Add", e -> addClinician());
        addButton(panel, "Update", e -> updateClinician());
        addButton(panel, "Delete", e -> deleteClinician());
        addButton(panel, "Clear", e -> clearForm());
        addButton(panel, "Refresh", e -> refreshData());
        return panel;
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void addClinician() {
        try {
            Clinician clinician = createClinicianFromForm();
            if (clinician != null) {
                controller.addClinician(clinician);
                refreshData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Clinician added successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateClinician() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a clinician.");
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteClinician(id);
            controller.addClinician(createClinicianFromForm());
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Clinician updated!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteClinician() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a clinician.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this clinician?", "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.deleteClinician((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Clinician createClinicianFromForm() {
        if (clinicianIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Clinician ID required.");
            return null;
        }
        return new Clinician(clinicianIDField.getText().trim(), firstNameField.getText().trim(),
            lastNameField.getText().trim(), qualificationField.getText().trim(),
            specialtyField.getText().trim(), workplaceField.getText().trim(),
            emailField.getText().trim(), phoneField.getText().trim());
    }

    private void loadSelectedClinician() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            clinicianIDField.setText((String) tableModel.getValueAt(row, 0));
            firstNameField.setText((String) tableModel.getValueAt(row, 1));
            lastNameField.setText((String) tableModel.getValueAt(row, 2));
            qualificationField.setText((String) tableModel.getValueAt(row, 3));
            specialtyField.setText((String) tableModel.getValueAt(row, 4));
            workplaceField.setText((String) tableModel.getValueAt(row, 5));
            emailField.setText((String) tableModel.getValueAt(row, 6));
            phoneField.setText((String) tableModel.getValueAt(row, 7));
        }
    }

    private void clearForm() {
        clinicianIDField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        qualificationField.setText("");
        specialtyField.setText("");
        workplaceField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Clinician c : controller.getAllClinicians()) {
            tableModel.addRow(new Object[]{c.getClinicianID(), c.getFirstName(), c.getLastName(),
                c.getQualification(), c.getSpecialty(), c.getWorkplace(), c.getEmail(), c.getPhone()});
        }
    }
}



package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Prescription;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrescriptionPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField prescriptionIDField, patientIDField, clinicianIDField, medicationField;
    private JTextField dosageField, quantityField, pharmacyField, datePrescribedField;
    private JTextField collectionStatusField, notesField;

    public PrescriptionPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        String[] columns = {"Prescription ID", "Patient ID", "Clinician ID", "Medication", 
                          "Dosage", "Quantity", "Pharmacy", "Date Prescribed", "Collection Status", "Notes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPrescription();
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
        addField(panel, gbc, row++, "Prescription ID:", prescriptionIDField = new JTextField(15));
        addField(panel, gbc, row++, "Patient ID:", patientIDField = new JTextField(15));
        addField(panel, gbc, row++, "Clinician ID:", clinicianIDField = new JTextField(15));
        addField(panel, gbc, row++, "Medication:", medicationField = new JTextField(15));
        addField(panel, gbc, row++, "Dosage:", dosageField = new JTextField(15));
        addField(panel, gbc, row++, "Quantity:", quantityField = new JTextField(15));
        addField(panel, gbc, row++, "Pharmacy:", pharmacyField = new JTextField(15));
        addField(panel, gbc, row++, "Date Prescribed:", datePrescribedField = new JTextField(15));
        addField(panel, gbc, row++, "Collection Status:", collectionStatusField = new JTextField(15));
        addField(panel, gbc, row++, "Notes:", notesField = new JTextField(15));

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
        addButton(panel, "Add", e -> addPrescription());
        addButton(panel, "Update", e -> updatePrescription());
        addButton(panel, "Delete", e -> deletePrescription());
        addButton(panel, "Clear", e -> clearForm());
        addButton(panel, "Refresh", e -> refreshData());
        return panel;
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void addPrescription() {
        try {
            Prescription prescription = createPrescriptionFromForm();
            if (prescription != null) {
                controller.addPrescription(prescription);
                refreshData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Prescription added!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePrescription() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a prescription.");
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deletePrescription(id);
            controller.addPrescription(createPrescriptionFromForm());
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Prescription updated!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePrescription() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a prescription.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this prescription?", "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.deletePrescription((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Prescription createPrescriptionFromForm() {
        if (prescriptionIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prescription ID required.");
            return null;
        }
        return new Prescription(prescriptionIDField.getText().trim(), patientIDField.getText().trim(),
            clinicianIDField.getText().trim(), medicationField.getText().trim(),
            dosageField.getText().trim(), quantityField.getText().trim(), pharmacyField.getText().trim(),
            datePrescribedField.getText().trim(), collectionStatusField.getText().trim(),
            notesField.getText().trim());
    }

    private void loadSelectedPrescription() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            prescriptionIDField.setText((String) tableModel.getValueAt(row, 0));
            patientIDField.setText((String) tableModel.getValueAt(row, 1));
            clinicianIDField.setText((String) tableModel.getValueAt(row, 2));
            medicationField.setText((String) tableModel.getValueAt(row, 3));
            dosageField.setText((String) tableModel.getValueAt(row, 4));
            quantityField.setText((String) tableModel.getValueAt(row, 5));
            pharmacyField.setText((String) tableModel.getValueAt(row, 6));
            datePrescribedField.setText((String) tableModel.getValueAt(row, 7));
            collectionStatusField.setText((String) tableModel.getValueAt(row, 8));
            notesField.setText((String) tableModel.getValueAt(row, 9));
        }
    }

    private void clearForm() {
        prescriptionIDField.setText("");
        patientIDField.setText("");
        clinicianIDField.setText("");
        medicationField.setText("");
        dosageField.setText("");
        quantityField.setText("");
        pharmacyField.setText("");
        datePrescribedField.setText("");
        collectionStatusField.setText("");
        notesField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Prescription p : controller.getAllPrescriptions()) {
            tableModel.addRow(new Object[]{p.getPrescriptionID(), p.getPatientID(), p.getClinicianID(),
                p.getMedication(), p.getDosage(), p.getQuantity(), p.getPharmacy(),
                p.getDatePrescribed(), p.getCollectionStatus(), p.getNotes()});
        }
    }
}



package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Referral;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReferralPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField referralIDField, patientIDField, referringClinicianIDField, receivingClinicianIDField;
    private JTextField referringFacilityField, receivingFacilityField, dateField;
    private JTextField urgencyField, clinicalSummaryField, statusField;

    public ReferralPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] columns = {"Referral ID", "Patient ID", "Referring Clinician", "Receiving Clinician",
                          "Referring Facility", "Receiving Facility", "Date", "Urgency", "Status", "Clinical Summary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setSelectionForeground(Color.BLACK);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedReferral();
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Referrals"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Referral Details"));
        add(formPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        add(buttonPanel, BorderLayout.NORTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addField(panel, gbc, row++, "Referral ID:", referralIDField = new JTextField(15));
        addField(panel, gbc, row++, "Patient ID:", patientIDField = new JTextField(15));
        addField(panel, gbc, row++, "Referring Clinician ID:", referringClinicianIDField = new JTextField(15));
        addField(panel, gbc, row++, "Receiving Clinician ID:", receivingClinicianIDField = new JTextField(15));
        addField(panel, gbc, row++, "Referring Facility:", referringFacilityField = new JTextField(15));
        addField(panel, gbc, row++, "Receiving Facility:", receivingFacilityField = new JTextField(15));
        addField(panel, gbc, row++, "Date:", dateField = new JTextField(15));
        addField(panel, gbc, row++, "Urgency:", urgencyField = new JTextField(15));
        addField(panel, gbc, row++, "Status:", statusField = new JTextField(15));
        addField(panel, gbc, row++, "Clinical Summary:", clinicalSummaryField = new JTextField(30));

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
        addButton(panel, "Add", e -> addReferral());
        addButton(panel, "Update", e -> updateReferral());
        addButton(panel, "Delete", e -> deleteReferral());
        addButton(panel, "Generate File", e -> generateReferralFile());
        addButton(panel, "Clear", e -> clearForm());
        addButton(panel, "Refresh", e -> refreshData());
        return panel;
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void addReferral() {
        try {
            Referral referral = createReferralFromForm();
            if (referral != null) {
                controller.addReferral(referral);
                refreshData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Referral added!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReferral() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a referral.");
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteReferral(id);
            controller.addReferral(createReferralFromForm());
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Referral updated!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteReferral() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a referral.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this referral?", "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.deleteReferral((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private void generateReferralFile() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a referral to generate file.");
            return;
        }

        String referralID = (String) tableModel.getValueAt(row, 0);
        Referral referral = controller.getAllReferrals().stream()
            .filter(r -> r.getReferralID().equals(referralID))
            .findFirst()
            .orElse(null);

        if (referral != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Referral File");
            fileChooser.setSelectedFile(new java.io.File("referral_" + referralID + ".txt"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                controller.generateReferralFile(referral, filePath);
                JOptionPane.showMessageDialog(this, "Referral file generated successfully!");
            }
        }
    }

    private Referral createReferralFromForm() {
        if (referralIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Referral ID required.");
            return null;
        }
        return new Referral(referralIDField.getText().trim(), patientIDField.getText().trim(),
            referringClinicianIDField.getText().trim(), receivingClinicianIDField.getText().trim(),
            referringFacilityField.getText().trim(), receivingFacilityField.getText().trim(),
            dateField.getText().trim(), urgencyField.getText().trim(),
            clinicalSummaryField.getText().trim(), statusField.getText().trim());
    }

    private void loadSelectedReferral() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            referralIDField.setText((String) tableModel.getValueAt(row, 0));
            patientIDField.setText((String) tableModel.getValueAt(row, 1));
            referringClinicianIDField.setText((String) tableModel.getValueAt(row, 2));
            receivingClinicianIDField.setText((String) tableModel.getValueAt(row, 3));
            referringFacilityField.setText((String) tableModel.getValueAt(row, 4));
            receivingFacilityField.setText((String) tableModel.getValueAt(row, 5));
            dateField.setText((String) tableModel.getValueAt(row, 6));
            urgencyField.setText((String) tableModel.getValueAt(row, 7));
            statusField.setText((String) tableModel.getValueAt(row, 8));
            clinicalSummaryField.setText((String) tableModel.getValueAt(row, 9));
        }
    }

    private void clearForm() {
        referralIDField.setText("");
        patientIDField.setText("");
        referringClinicianIDField.setText("");
        receivingClinicianIDField.setText("");
        referringFacilityField.setText("");
        receivingFacilityField.setText("");
        dateField.setText("");
        urgencyField.setText("");
        clinicalSummaryField.setText("");
        statusField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Referral r : controller.getAllReferrals()) {
            tableModel.addRow(new Object[]{r.getReferralID(), r.getPatientID(), r.getReferringClinicianID(),
                r.getReceivingClinicianID(), r.getReferringFacility(), r.getReceivingFacility(),
                r.getDate(), r.getUrgency(), r.getStatus(), r.getClinicalSummary()});
        }
    }
}




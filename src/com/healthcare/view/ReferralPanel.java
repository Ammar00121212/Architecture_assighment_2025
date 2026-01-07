package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Referral;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ReferralPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField referralIDField, patientIDField, referringClinicianIDField, receivingClinicianIDField;
    private JTextField referringFacilityField, receivingFacilityField, dateField, urgencyField;
    private JTextField referralReasonField, clinicalSummaryField, investigationsField;
    private JTextField appointmentIDField, notesField, statusField, createdDateField, lastUpdatedField;

    private JButton addButton, updateButton, deleteButton, generateFileButton;

    public ReferralPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        // ===== Table =====
        String[] columns = {"Referral ID", "Patient ID", "Referring Clinician", "Receiving Clinician",
                "Referring Facility", "Receiving Facility", "Date", "Urgency", "Referral Reason",
                "Clinical Summary", "Investigations", "Appointment ID", "Notes", "Status", "Created", "Last Updated"};

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader headerTable = table.getTableHeader();
        headerTable.setBackground(new Color(236, 239, 241));
        headerTable.setFont(new Font("Segoe UI", Font.BOLD, 13));

        int[] widths = {100, 100, 130, 130, 150, 150, 100, 80, 150, 200, 150, 120, 200, 100, 120, 120};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane tableScroll = new JScrollPane(table);

        // ===== Form =====
        JPanel formPanel = new JPanel(new GridLayout(5, 4, 8, 8));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        referralIDField = field(); patientIDField = field(); referringClinicianIDField = field();
        receivingClinicianIDField = field(); referringFacilityField = field(); receivingFacilityField = field();
        dateField = field(); urgencyField = field(); referralReasonField = field(); clinicalSummaryField = field();
        investigationsField = field(); appointmentIDField = field(); notesField = field(); statusField = field();
        createdDateField = field(); lastUpdatedField = field();

        addToForm(formPanel, "Referral ID", referralIDField);
        addToForm(formPanel, "Patient ID", patientIDField);
        addToForm(formPanel, "Referring Clinician ID", referringClinicianIDField);
        addToForm(formPanel, "Receiving Clinician ID", receivingClinicianIDField);
        addToForm(formPanel, "Referring Facility", referringFacilityField);
        addToForm(formPanel, "Receiving Facility", receivingFacilityField);
        addToForm(formPanel, "Date", dateField);
        addToForm(formPanel, "Urgency", urgencyField);
        addToForm(formPanel, "Status", statusField);
        addToForm(formPanel, "Appointment ID", appointmentIDField);
        addToForm(formPanel, "Created Date", createdDateField);
        addToForm(formPanel, "Last Updated", lastUpdatedField);
        addToForm(formPanel, "Referral Reason", referralReasonField);
        addToForm(formPanel, "Clinical Summary", clinicalSummaryField);
        addToForm(formPanel, "Investigations", investigationsField);
        addToForm(formPanel, "Notes", notesField);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        generateFileButton = new JButton("Generate File");

        addButton.addActionListener(e -> addReferral());
        updateButton.addActionListener(e -> updateReferral());
        deleteButton.addActionListener(e -> deleteReferral());
        generateFileButton.addActionListener(e -> generateReferralFile());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateFileButton);

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.add(formPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        // ===== Split Pane =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, formContainer);
        splitPane.setResizeWeight(0.5); // Table takes half screen
        splitPane.setDividerSize(5);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);

        // ===== Table Selection =====
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                loadSelectedReferral();
        });
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return f;
    }

    private void addToForm(JPanel p, String label, JTextField field) {
        p.add(new JLabel(label));
        p.add(field);
    }

    // ================== CRUD ==================
    private Referral createReferralFromForm() {
        if (referralIDField.getText().trim().isEmpty()) return null;
        return new Referral(referralIDField.getText().trim(), patientIDField.getText().trim(),
                referringClinicianIDField.getText().trim(), receivingClinicianIDField.getText().trim(),
                referringFacilityField.getText().trim(), receivingFacilityField.getText().trim(),
                dateField.getText().trim(), urgencyField.getText().trim(), referralReasonField.getText().trim(),
                clinicalSummaryField.getText().trim(), investigationsField.getText().trim(),
                appointmentIDField.getText().trim(), notesField.getText().trim(), statusField.getText().trim(),
                createdDateField.getText().trim(), lastUpdatedField.getText().trim());
    }

    private void addReferral() {
        Referral r = createReferralFromForm();
        if (r != null) {
            controller.addReferral(r);
            refreshData();
            clearForm();
        }
    }

    private void updateReferral() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteReferral((String) tableModel.getValueAt(row, 0));
            controller.addReferral(createReferralFromForm());
            refreshData();
            clearForm();
        }
    }

    private void deleteReferral() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteReferral((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private void generateReferralFile() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        Referral r = controller.getAllReferrals().stream().filter(ref -> ref.getReferralID().equals(id)).findFirst().orElse(null);
        if (r != null) controller.generateReferralFile(r, "referral_" + id + ".txt");
    }

    private void loadSelectedReferral() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            referralIDField.setText((String) tableModel.getValueAt(r, 0));
            patientIDField.setText((String) tableModel.getValueAt(r, 1));
            referringClinicianIDField.setText((String) tableModel.getValueAt(r, 2));
            receivingClinicianIDField.setText((String) tableModel.getValueAt(r, 3));
            referringFacilityField.setText((String) tableModel.getValueAt(r, 4));
            receivingFacilityField.setText((String) tableModel.getValueAt(r, 5));
            dateField.setText((String) tableModel.getValueAt(r, 6));
            urgencyField.setText((String) tableModel.getValueAt(r, 7));
            referralReasonField.setText((String) tableModel.getValueAt(r, 8));
            clinicalSummaryField.setText((String) tableModel.getValueAt(r, 9));
            investigationsField.setText((String) tableModel.getValueAt(r, 10));
            appointmentIDField.setText((String) tableModel.getValueAt(r, 11));
            notesField.setText((String) tableModel.getValueAt(r, 12));
            statusField.setText((String) tableModel.getValueAt(r, 13));
            createdDateField.setText((String) tableModel.getValueAt(r, 14));
            lastUpdatedField.setText((String) tableModel.getValueAt(r, 15));
        }
    }

    private void clearForm() {
        referralIDField.setText(""); patientIDField.setText(""); referringClinicianIDField.setText("");
        receivingClinicianIDField.setText(""); referringFacilityField.setText(""); receivingFacilityField.setText("");
        dateField.setText(""); urgencyField.setText(""); referralReasonField.setText("");
        clinicalSummaryField.setText(""); investigationsField.setText(""); appointmentIDField.setText("");
        notesField.setText(""); statusField.setText(""); createdDateField.setText(""); lastUpdatedField.setText("");
        table.clearSelection();
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Referral> list = controller.getAllReferrals();
        for (Referral r : list) {
            tableModel.addRow(new Object[]{
                    r.getReferralID(), r.getPatientID(), r.getReferringClinicianID(), r.getReceivingClinicianID(),
                    r.getReferringFacility(), r.getReceivingFacility(), r.getDate(), r.getUrgency(),
                    r.getReferralReason(), r.getClinicalSummary(), r.getRequestedInvestigations(),
                    r.getAppointmentID(), r.getNotes(), r.getStatus(), r.getCreatedDate(), r.getLastUpdated()
            });
        }
    }
}

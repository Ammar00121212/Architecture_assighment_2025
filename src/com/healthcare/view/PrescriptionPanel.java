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

    private JTextField prescriptionIDField, patientIDField, clinicianIDField, appointmentIDField, medicationField;
    private JTextField dosageField, frequencyField, durationDaysField, quantityField, pharmacyField;
    private JTextField datePrescribedField, issueDateField, collectionDateField, collectionStatusField, notesField;

    public PrescriptionPanel(HealthcareController controller) {
        this.controller = controller;
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        // ================= BUTTONS =================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton add = btn("Add");
        JButton update = btn("Update");
        JButton delete = btn("Delete");
        JButton clear = btn("Clear");
        JButton refresh = btn("Refresh");

        add.addActionListener(e -> addPrescription());
        update.addActionListener(e -> updatePrescription());
        delete.addActionListener(e -> deletePrescription());
        clear.addActionListener(e -> clearForm());
        refresh.addActionListener(e -> refreshData());

        buttonPanel.add(add);
        buttonPanel.add(update);
        buttonPanel.add(delete);
        buttonPanel.add(clear);
        buttonPanel.add(refresh);

        add(buttonPanel, BorderLayout.NORTH);

        // ================= TABLE =================
        String[] cols = {
                "Prescription ID", "Patient ID", "Clinician ID", "Appointment ID", "Medication",
                "Dosage", "Frequency", "Duration Days", "Quantity", "Pharmacy",
                "Date Prescribed", "Issue Date", "Collection Date", "Status", "Notes"
        };

        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setColumnWidths();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPrescription();
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Prescriptions"));

        // ================= FORM =================
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Prescription Details"));

        // ================= SPLIT PANE (🔥 MAIN FIX) =================
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tableScroll,
                formPanel
        );
        splitPane.setResizeWeight(0.65); // 65% table, 35% form
        splitPane.setDividerSize(6);

        add(splitPane, BorderLayout.CENTER);
    }

    // ================= FORM =================
    private JPanel createFormPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        prescriptionIDField = field(); patientIDField = field(); clinicianIDField = field();
        appointmentIDField = field(); medicationField = field(); dosageField = field();
        frequencyField = field(); durationDaysField = field(); quantityField = field();
        pharmacyField = field(); datePrescribedField = field(); issueDateField = field();
        collectionDateField = field(); collectionStatusField = field(); notesField = field();

        int r = 0;
        addRow(p, g, r++, "Prescription ID", prescriptionIDField, "Patient ID", patientIDField);
        addRow(p, g, r++, "Clinician ID", clinicianIDField, "Appointment ID", appointmentIDField);
        addRow(p, g, r++, "Medication", medicationField, "Dosage", dosageField);
        addRow(p, g, r++, "Frequency", frequencyField, "Duration Days", durationDaysField);
        addRow(p, g, r++, "Quantity", quantityField, "Pharmacy", pharmacyField);
        addRow(p, g, r++, "Date Prescribed", datePrescribedField, "Issue Date", issueDateField);
        addRow(p, g, r++, "Collection Date", collectionDateField, "Status", collectionStatusField);
        addRow(p, g, r++, "Notes", notesField, "", null);

        return p;
    }

    private void addRow(JPanel p, GridBagConstraints g, int r,
                        String l1, JTextField f1,
                        String l2, JTextField f2) {

        g.gridx = 0; g.gridy = r; g.weightx = 0;
        p.add(new JLabel(l1), g);

        g.gridx = 1; g.weightx = 1;
        p.add(f1, g);

        if (f2 != null) {
            g.gridx = 2; g.weightx = 0;
            p.add(new JLabel(l2), g);

            g.gridx = 3; g.weightx = 1;
            p.add(f2, g);
        }
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(180, 26));
        return f;
    }

    // ================= CRUD =================
    private void addPrescription() {
        controller.addPrescription(createFromForm());
        refreshData();
        clearForm();
    }

    private void updatePrescription() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            controller.deletePrescription(tableModel.getValueAt(r, 0).toString());
            controller.addPrescription(createFromForm());
            refreshData();
        }
    }

    private void deletePrescription() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            controller.deletePrescription(tableModel.getValueAt(r, 0).toString());
            refreshData();
            clearForm();
        }
    }

    private Prescription createFromForm() {
        return new Prescription(
                prescriptionIDField.getText(), patientIDField.getText(),
                clinicianIDField.getText(), appointmentIDField.getText(),
                medicationField.getText(), dosageField.getText(),
                frequencyField.getText(), durationDaysField.getText(),
                quantityField.getText(), pharmacyField.getText(),
                datePrescribedField.getText(), issueDateField.getText(),
                collectionDateField.getText(), collectionStatusField.getText(),
                notesField.getText()
        );
    }

    private void loadSelectedPrescription() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        prescriptionIDField.setText(tableModel.getValueAt(r,0).toString());
        patientIDField.setText(tableModel.getValueAt(r,1).toString());
        clinicianIDField.setText(tableModel.getValueAt(r,2).toString());
        appointmentIDField.setText(tableModel.getValueAt(r,3).toString());
        medicationField.setText(tableModel.getValueAt(r,4).toString());
        dosageField.setText(tableModel.getValueAt(r,5).toString());
        frequencyField.setText(tableModel.getValueAt(r,6).toString());
        durationDaysField.setText(tableModel.getValueAt(r,7).toString());
        quantityField.setText(tableModel.getValueAt(r,8).toString());
        pharmacyField.setText(tableModel.getValueAt(r,9).toString());
        datePrescribedField.setText(tableModel.getValueAt(r,10).toString());
        issueDateField.setText(tableModel.getValueAt(r,11).toString());
        collectionDateField.setText(tableModel.getValueAt(r,12).toString());
        collectionStatusField.setText(tableModel.getValueAt(r,13).toString());
        notesField.setText(tableModel.getValueAt(r,14).toString());
    }

    private void clearForm() {
        for (JTextField f : new JTextField[]{
                prescriptionIDField, patientIDField, clinicianIDField, appointmentIDField,
                medicationField, dosageField, frequencyField, durationDaysField,
                quantityField, pharmacyField, datePrescribedField, issueDateField,
                collectionDateField, collectionStatusField, notesField
        }) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Prescription p : controller.getAllPrescriptions()) {
            tableModel.addRow(new Object[]{
                    p.getPrescriptionID(), p.getPatientID(), p.getClinicianID(),
                    p.getAppointmentID(), p.getMedication(), p.getDosage(),
                    p.getFrequency(), p.getDurationDays(), p.getQuantity(),
                    p.getPharmacy(), p.getDatePrescribed(), p.getIssueDate(),
                    p.getCollectionDate(), p.getCollectionStatus(), p.getNotes()
            });
        }
        setColumnWidths();
    }

    private void setColumnWidths() {
        int[] w = {120,100,100,120,150,100,100,100,80,150,120,100,120,120,200};
        for (int i = 0; i < w.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
    }

    private JButton btn(String t) {
        JButton b = new JButton(t);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 12f));
        return b;
    }
}

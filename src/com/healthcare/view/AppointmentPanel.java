package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Appointment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AppointmentPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField appointmentIDField, patientIDField, clinicianIDField, facilityIDField;
    private JTextField dateField, timeField, statusField, reasonField, notesField;

    public AppointmentPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        String[] columns = {"Appointment ID", "Patient ID", "Clinician ID", "Facility ID", 
                          "Date", "Time", "Status", "Reason", "Notes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedAppointment();
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
        addField(panel, gbc, row++, "Appointment ID:", appointmentIDField = new JTextField(15));
        addField(panel, gbc, row++, "Patient ID:", patientIDField = new JTextField(15));
        addField(panel, gbc, row++, "Clinician ID:", clinicianIDField = new JTextField(15));
        addField(panel, gbc, row++, "Facility ID:", facilityIDField = new JTextField(15));
        addField(panel, gbc, row++, "Date:", dateField = new JTextField(15));
        addField(panel, gbc, row++, "Time:", timeField = new JTextField(15));
        addField(panel, gbc, row++, "Status:", statusField = new JTextField(15));
        addField(panel, gbc, row++, "Reason:", reasonField = new JTextField(15));
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
        addButton(panel, "Add", e -> addAppointment());
        addButton(panel, "Update", e -> updateAppointment());
        addButton(panel, "Delete", e -> deleteAppointment());
        addButton(panel, "Clear", e -> clearForm());
        addButton(panel, "Refresh", e -> refreshData());
        return panel;
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void addAppointment() {
        try {
            Appointment appointment = createAppointmentFromForm();
            if (appointment != null) {
                controller.addAppointment(appointment);
                refreshData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Appointment added!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAppointment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.");
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteAppointment(id);
            controller.addAppointment(createAppointmentFromForm());
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Appointment updated!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this appointment?", "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.deleteAppointment((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Appointment createAppointmentFromForm() {
        if (appointmentIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appointment ID required.");
            return null;
        }
        return new Appointment(appointmentIDField.getText().trim(), patientIDField.getText().trim(),
            clinicianIDField.getText().trim(), facilityIDField.getText().trim(),
            dateField.getText().trim(), timeField.getText().trim(), statusField.getText().trim(),
            reasonField.getText().trim(), notesField.getText().trim());
    }

    private void loadSelectedAppointment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            appointmentIDField.setText((String) tableModel.getValueAt(row, 0));
            patientIDField.setText((String) tableModel.getValueAt(row, 1));
            clinicianIDField.setText((String) tableModel.getValueAt(row, 2));
            facilityIDField.setText((String) tableModel.getValueAt(row, 3));
            dateField.setText((String) tableModel.getValueAt(row, 4));
            timeField.setText((String) tableModel.getValueAt(row, 5));
            statusField.setText((String) tableModel.getValueAt(row, 6));
            reasonField.setText((String) tableModel.getValueAt(row, 7));
            notesField.setText((String) tableModel.getValueAt(row, 8));
        }
    }

    private void clearForm() {
        appointmentIDField.setText("");
        patientIDField.setText("");
        clinicianIDField.setText("");
        facilityIDField.setText("");
        dateField.setText("");
        timeField.setText("");
        statusField.setText("");
        reasonField.setText("");
        notesField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Appointment a : controller.getAllAppointments()) {
            tableModel.addRow(new Object[]{a.getAppointmentID(), a.getPatientID(), a.getClinicianID(),
                a.getFacilityID(), a.getDate(), a.getTime(), a.getStatus(), a.getReason(), a.getNotes()});
        }
    }
}



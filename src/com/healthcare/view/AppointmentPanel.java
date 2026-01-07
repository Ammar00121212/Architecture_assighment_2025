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
    private JTextField dateField, timeField, durationField, typeField, statusField, reasonField, notesField;
    private JTextField createdDateField, lastModifiedField;

    public AppointmentPanel(HealthcareController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        refreshData();
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel() {
        String[] columns = {
                "Appointment ID", "Patient ID", "Clinician ID", "Facility ID",
                "Date", "Time", "Duration", "Type", "Status",
                "Reason", "Notes", "Created", "Last Modified"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);   // ⭐ IMPORTANT
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadSelectedAppointment();
            }
        });

        setTableColumnWidths();

        JScrollPane scrollPane = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        // ⭐ force table height (shows many rows)
        table.setPreferredScrollableViewportSize(
                new Dimension(1100, 350)
        );

        scrollPane.setBorder(BorderFactory.createTitledBorder("Appointments List"));
        return scrollPane;
    }

    private void setTableColumnWidths() {
        int[] w = {130, 100, 100, 100, 100, 80, 80, 100, 100, 160, 220, 120, 120};
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }

    // ================= FORM =================
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        appointmentIDField = field(); patientIDField = field();
        clinicianIDField = field(); facilityIDField = field();
        dateField = field(); timeField = field();
        durationField = field(); typeField = field();
        statusField = field(); reasonField = field();
        notesField = field(); createdDateField = field();
        lastModifiedField = field();

        int r = 0;
        addRow(panel, gbc, r++, "Appointment ID", appointmentIDField, "Patient ID", patientIDField);
        addRow(panel, gbc, r++, "Clinician ID", clinicianIDField, "Facility ID", facilityIDField);
        addRow(panel, gbc, r++, "Date", dateField, "Time", timeField);
        addRow(panel, gbc, r++, "Duration", durationField, "Type", typeField);
        addRow(panel, gbc, r++, "Status", statusField, "Reason", reasonField);
        addRow(panel, gbc, r++, "Created", createdDateField, "Last Modified", lastModifiedField);
        addRow(panel, gbc, r++, "Notes", notesField, "", null);

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String l1, JTextField f1,
                        String l2, JTextField f2) {

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(l1), gbc);

        gbc.gridx = 1;
        panel.add(f1, gbc);

        if (l2 != null && f2 != null) {
            gbc.gridx = 2;
            panel.add(new JLabel(l2), gbc);

            gbc.gridx = 3;
            panel.add(f2, gbc);
        }
    }

    private JTextField field() {
        JTextField f = new JTextField(10);
        f.setPreferredSize(new Dimension(160, 26)); // compact fields
        return f;
    }

    // ================= BUTTONS =================
    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

        JButton add = btn("Add");
        JButton update = btn("Update");
        JButton delete = btn("Delete");
        JButton clear = btn("Clear");
        JButton refresh = btn("Refresh");

        add.addActionListener(e -> addAppointment());
        update.addActionListener(e -> updateAppointment());
        delete.addActionListener(e -> deleteAppointment());
        clear.addActionListener(e -> clearForm());
        refresh.addActionListener(e -> refreshData());

        p.add(add); p.add(update); p.add(delete); p.add(clear); p.add(refresh);
        return p;
    }

    private JButton btn(String t) {
        JButton b = new JButton(t);
        b.setFocusPainted(false);
        return b;
    }

    // ================= CRUD =================
    private void addAppointment() {
        controller.addAppointment(createAppointmentFromForm());
        refreshData();
        clearForm();
    }

    private void updateAppointment() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            controller.deleteAppointment((String) tableModel.getValueAt(r, 0));
            controller.addAppointment(createAppointmentFromForm());
            refreshData();
            clearForm();
        }
    }

    private void deleteAppointment() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            controller.deleteAppointment((String) tableModel.getValueAt(r, 0));
            refreshData();
            clearForm();
        }
    }

    private Appointment createAppointmentFromForm() {
        return new Appointment(
                appointmentIDField.getText(),
                patientIDField.getText(),
                clinicianIDField.getText(),
                facilityIDField.getText(),
                dateField.getText(),
                timeField.getText(),
                durationField.getText(),
                typeField.getText(),
                statusField.getText(),
                reasonField.getText(),
                notesField.getText(),
                createdDateField.getText(),
                lastModifiedField.getText()
        );
    }

    private void loadSelectedAppointment() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        appointmentIDField.setText((String) tableModel.getValueAt(r, 0));
        patientIDField.setText((String) tableModel.getValueAt(r, 1));
        clinicianIDField.setText((String) tableModel.getValueAt(r, 2));
        facilityIDField.setText((String) tableModel.getValueAt(r, 3));
        dateField.setText((String) tableModel.getValueAt(r, 4));
        timeField.setText((String) tableModel.getValueAt(r, 5));
        durationField.setText((String) tableModel.getValueAt(r, 6));
        typeField.setText((String) tableModel.getValueAt(r, 7));
        statusField.setText((String) tableModel.getValueAt(r, 8));
        reasonField.setText((String) tableModel.getValueAt(r, 9));
        notesField.setText((String) tableModel.getValueAt(r, 10));
        createdDateField.setText((String) tableModel.getValueAt(r, 11));
        lastModifiedField.setText((String) tableModel.getValueAt(r, 12));
    }

    private void clearForm() {
        for (JTextField f : new JTextField[]{
                appointmentIDField, patientIDField, clinicianIDField, facilityIDField,
                dateField, timeField, durationField, typeField, statusField,
                reasonField, notesField, createdDateField, lastModifiedField
        }) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Appointment a : controller.getAllAppointments()) {
            tableModel.addRow(new Object[]{
                    a.getAppointmentID(), a.getPatientID(), a.getClinicianID(), a.getFacilityID(),
                    a.getDate(), a.getTime(), a.getDurationMinutes(),
                    a.getAppointmentType(), a.getStatus(), a.getReason(),
                    a.getNotes(), a.getCreatedDate(), a.getLastModified()
            });
        }
    }
}

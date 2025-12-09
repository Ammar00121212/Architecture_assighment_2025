package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Facility;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacilityPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField facilityIDField, nameField, typeField, addressField;
    private JTextField phoneField, emailField, servicesField, capacityField;

    public FacilityPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        String[] columns = {"Facility ID", "Name", "Type", "Address", "Phone", "Email", "Services", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedFacility();
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
        addField(panel, gbc, row++, "Facility ID:", facilityIDField = new JTextField(15));
        addField(panel, gbc, row++, "Name:", nameField = new JTextField(15));
        addField(panel, gbc, row++, "Type:", typeField = new JTextField(15));
        addField(panel, gbc, row++, "Address:", addressField = new JTextField(15));
        addField(panel, gbc, row++, "Phone:", phoneField = new JTextField(15));
        addField(panel, gbc, row++, "Email:", emailField = new JTextField(15));
        addField(panel, gbc, row++, "Services:", servicesField = new JTextField(15));
        addField(panel, gbc, row++, "Capacity:", capacityField = new JTextField(15));

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
        addButton(panel, "Add", e -> addFacility());
        addButton(panel, "Update", e -> updateFacility());
        addButton(panel, "Delete", e -> deleteFacility());
        addButton(panel, "Clear", e -> clearForm());
        addButton(panel, "Refresh", e -> refreshData());
        return panel;
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void addFacility() {
        try {
            Facility facility = createFacilityFromForm();
            if (facility != null) {
                controller.addFacility(facility);
                refreshData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Facility added!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFacility() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a facility.");
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteFacility(id);
            controller.addFacility(createFacilityFromForm());
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Facility updated!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFacility() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a facility.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this facility?", "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.deleteFacility((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Facility createFacilityFromForm() {
        if (facilityIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Facility ID required.");
            return null;
        }
        return new Facility(facilityIDField.getText().trim(), nameField.getText().trim(),
            typeField.getText().trim(), addressField.getText().trim(), phoneField.getText().trim(),
            emailField.getText().trim(), servicesField.getText().trim(), capacityField.getText().trim());
    }

    private void loadSelectedFacility() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            facilityIDField.setText((String) tableModel.getValueAt(row, 0));
            nameField.setText((String) tableModel.getValueAt(row, 1));
            typeField.setText((String) tableModel.getValueAt(row, 2));
            addressField.setText((String) tableModel.getValueAt(row, 3));
            phoneField.setText((String) tableModel.getValueAt(row, 4));
            emailField.setText((String) tableModel.getValueAt(row, 5));
            servicesField.setText((String) tableModel.getValueAt(row, 6));
            capacityField.setText((String) tableModel.getValueAt(row, 7));
        }
    }

    private void clearForm() {
        facilityIDField.setText("");
        nameField.setText("");
        typeField.setText("");
        addressField.setText("");
        phoneField.setText("");
        emailField.setText("");
        servicesField.setText("");
        capacityField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Facility f : controller.getAllFacilities()) {
            tableModel.addRow(new Object[]{f.getFacilityID(), f.getName(), f.getType(), f.getAddress(),
                f.getPhone(), f.getEmail(), f.getServices(), f.getCapacity()});
        }
    }
}


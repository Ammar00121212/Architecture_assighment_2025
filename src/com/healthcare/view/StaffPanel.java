package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Staff;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField staffIDField, firstNameField, lastNameField, roleField;
    private JTextField facilityIDField, emailField, phoneField;

    public StaffPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] columns = {"Staff ID", "First Name", "Last Name", "Role", "Facility ID", "Email", "Phone"};
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
            if (!e.getValueIsAdjusting()) loadSelectedStaff();
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Staff"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Staff Details"));
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
        addField(panel, gbc, row++, "Staff ID:", staffIDField = new JTextField(15));
        addField(panel, gbc, row++, "First Name:", firstNameField = new JTextField(15));
        addField(panel, gbc, row++, "Last Name:", lastNameField = new JTextField(15));
        addField(panel, gbc, row++, "Role:", roleField = new JTextField(15));
        addField(panel, gbc, row++, "Facility ID:", facilityIDField = new JTextField(15));
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
        addButton(panel, "Add", e -> addStaff());
        addButton(panel, "Update", e -> updateStaff());
        addButton(panel, "Delete", e -> deleteStaff());
        addButton(panel, "Clear", e -> clearForm());
        addButton(panel, "Refresh", e -> refreshData());
        return panel;
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void addStaff() {
        try {
            Staff staff = createStaffFromForm();
            if (staff != null) {
                controller.addStaff(staff);
                refreshData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Staff added!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStaff() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a staff member.");
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteStaff(id);
            controller.addStaff(createStaffFromForm());
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Staff updated!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStaff() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a staff member.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this staff member?", "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.deleteStaff((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Staff createStaffFromForm() {
        if (staffIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Staff ID required.");
            return null;
        }
        return new Staff(staffIDField.getText().trim(), firstNameField.getText().trim(),
            lastNameField.getText().trim(), roleField.getText().trim(),
            facilityIDField.getText().trim(), emailField.getText().trim(), phoneField.getText().trim());
    }

    private void loadSelectedStaff() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            staffIDField.setText((String) tableModel.getValueAt(row, 0));
            firstNameField.setText((String) tableModel.getValueAt(row, 1));
            lastNameField.setText((String) tableModel.getValueAt(row, 2));
            roleField.setText((String) tableModel.getValueAt(row, 3));
            facilityIDField.setText((String) tableModel.getValueAt(row, 4));
            emailField.setText((String) tableModel.getValueAt(row, 5));
            phoneField.setText((String) tableModel.getValueAt(row, 6));
        }
    }

    private void clearForm() {
        staffIDField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        roleField.setText("");
        facilityIDField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Staff s : controller.getAllStaff()) {
            tableModel.addRow(new Object[]{s.getStaffID(), s.getFirstName(), s.getLastName(),
                s.getRole(), s.getFacilityID(), s.getEmail(), s.getPhone()});
        }
    }
}




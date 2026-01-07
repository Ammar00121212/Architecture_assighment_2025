package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Facility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FacilityPanel extends JPanel {

    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField facilityIDField, nameField, typeField, addressField, postcodeField;
    private JTextField phoneField, emailField, openingHoursField, managerField, servicesField, capacityField;

    public FacilityPanel(HealthcareController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        refreshData();
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel() {
        String[] columns = {"Facility ID", "Name", "Type", "Address", "Postcode", "Phone", "Email",
                            "Opening Hours", "Manager", "Services", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionBackground(new Color(227, 242, 253));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadSelectedFacility();
            }
        });

        setTableColumnWidths();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Facilities List"));
        return scrollPane;
    }

    private void setTableColumnWidths() {
        int[] w = {100, 150, 100, 200, 100, 120, 150, 150, 120, 200, 80};
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }

    // ================= FORM =================
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Facility Details"));
        panel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        facilityIDField = field(); nameField = field(); typeField = field();
        addressField = field(); postcodeField = field(); phoneField = field();
        emailField = field(); openingHoursField = field(); managerField = field();
        servicesField = field(); capacityField = field();

        int row = 0;
        addFormRow(panel, gbc, row++, "Facility ID", facilityIDField, "Name", nameField);
        addFormRow(panel, gbc, row++, "Type", typeField, "Address", addressField);
        addFormRow(panel, gbc, row++, "Postcode", postcodeField, "Phone", phoneField);
        addFormRow(panel, gbc, row++, "Email", emailField, "Opening Hours", openingHoursField);
        addFormRow(panel, gbc, row++, "Manager", managerField, "Services", servicesField);
        addFormRow(panel, gbc, row++, "Capacity", capacityField, "", null);

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String label1, JTextField field1,
                            String label2, JTextField field2) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label1), gbc);
        gbc.gridx = 1;
        panel.add(field1, gbc);
        if (label2 != null && field2 != null) {
            gbc.gridx = 2;
            panel.add(new JLabel(label2), gbc);
            gbc.gridx = 3;
            panel.add(field2, gbc);
        }
    }

    private JTextField field() {
        JTextField f = new JTextField(12);
        f.setPreferredSize(new Dimension(180, 28));
        return f;
    }

    // ================= BUTTONS =================
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JButton add = createPrimaryButton("Add");
        JButton update = createPrimaryButton("Update");
        JButton delete = createDangerButton("Delete");
        JButton clear = createSecondaryButton("Clear");
        JButton refresh = createSecondaryButton("Refresh");

        add.addActionListener(e -> addFacility());
        update.addActionListener(e -> updateFacility());
        delete.addActionListener(e -> deleteFacility());
        clear.addActionListener(e -> clearForm());
        refresh.addActionListener(e -> refreshData());

        panel.add(add); panel.add(update); panel.add(delete);
        panel.add(clear); panel.add(refresh);

        return panel;
    }

    private JButton createPrimaryButton(String text) { return styleButton(text, new Color(33, 150, 243), Color.WHITE); }
    private JButton createSecondaryButton(String text) { return styleButton(text, new Color(236, 239, 241), new Color(55, 71, 79)); }
    private JButton createDangerButton(String text) { return styleButton(text, new Color(229, 57, 53), Color.WHITE); }

    private JButton styleButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return btn;
    }

    // ================= CRUD =================
    private void addFacility() {
        Facility f = createFacilityFromForm();
        if (f != null) { controller.addFacility(f); refreshData(); clearForm(); }
    }

    private void updateFacility() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteFacility(id);
            controller.addFacility(createFacilityFromForm());
            refreshData();
            clearForm();
        }
    }

    private void deleteFacility() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteFacility((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Facility createFacilityFromForm() {
        return new Facility(
            facilityIDField.getText().trim(),
            nameField.getText().trim(),
            typeField.getText().trim(),
            addressField.getText().trim(),
            postcodeField.getText().trim(),
            phoneField.getText().trim(),
            emailField.getText().trim(),
            openingHoursField.getText().trim(),
            managerField.getText().trim(),
            servicesField.getText().trim(),
            capacityField.getText().trim()
        );
    }

    private void loadSelectedFacility() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        facilityIDField.setText((String) tableModel.getValueAt(r, 0));
        nameField.setText((String) tableModel.getValueAt(r, 1));
        typeField.setText((String) tableModel.getValueAt(r, 2));
        addressField.setText((String) tableModel.getValueAt(r, 3));
        postcodeField.setText((String) tableModel.getValueAt(r, 4));
        phoneField.setText((String) tableModel.getValueAt(r, 5));
        emailField.setText((String) tableModel.getValueAt(r, 6));
        openingHoursField.setText((String) tableModel.getValueAt(r, 7));
        managerField.setText((String) tableModel.getValueAt(r, 8));
        servicesField.setText((String) tableModel.getValueAt(r, 9));
        capacityField.setText((String) tableModel.getValueAt(r, 10));
    }

    private void clearForm() {
        facilityIDField.setText(""); nameField.setText(""); typeField.setText("");
        addressField.setText(""); postcodeField.setText(""); phoneField.setText("");
        emailField.setText(""); openingHoursField.setText(""); managerField.setText("");
        servicesField.setText(""); capacityField.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Facility f : controller.getAllFacilities()) {
            tableModel.addRow(new Object[]{
                f.getFacilityID(), f.getName(), f.getType(), f.getAddress(), f.getPostcode(),
                f.getPhone(), f.getEmail(), f.getOpeningHours(), f.getManagerName(),
                f.getServices(), f.getCapacity()
            });
        }
        setTableColumnWidths();
    }
}

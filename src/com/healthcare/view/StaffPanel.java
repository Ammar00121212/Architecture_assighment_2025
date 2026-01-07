package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Staff;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {

    // Colors
    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color TABLE_HEADER = new Color(236, 239, 241);
    private static final Color TABLE_SELECTION = new Color(227, 242, 253);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);

    private HealthcareController controller;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField staffIDField, firstNameField, lastNameField, roleField, departmentField;
    private JTextField facilityIDField, emailField, phoneField, employmentStatusField;
    private JTextField startDateField, lineManagerField, accessLevelField;

    public StaffPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(BACKGROUND);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        JLabel title = new JLabel("Staff Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ===== Table =====
        String[] columns = {"Staff ID", "First Name", "Last Name", "Role", "Department", "Facility ID",
                "Email", "Phone", "Employment Status", "Start Date", "Line Manager", "Access Level"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setSelectionBackground(TABLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader headerTable = table.getTableHeader();
        headerTable.setBackground(TABLE_HEADER);
        headerTable.setFont(new Font("Segoe UI", Font.BOLD, 13));

        int[] widths = {80, 100, 100, 120, 120, 100, 160, 120, 120, 100, 120, 100};
        for (int i = 0; i < widths.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) loadSelectedStaff();
        });

        JScrollPane tableScroll = new JScrollPane(table);

        // ===== Form =====
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        formPanel.setBackground(CARD_BACKGROUND);

        JPanel grid = new JPanel(new GridLayout(6, 4, 10, 10));
        grid.setBorder(new EmptyBorder(15, 15, 15, 15));

        staffIDField = createField(); firstNameField = createField(); lastNameField = createField();
        roleField = createField(); departmentField = createField(); facilityIDField = createField();
        emailField = createField(); phoneField = createField(); employmentStatusField = createField();
        startDateField = createField(); lineManagerField = createField(); accessLevelField = createField();

        addToGrid(grid, "Staff ID", staffIDField);
        addToGrid(grid, "First Name", firstNameField);
        addToGrid(grid, "Last Name", lastNameField);
        addToGrid(grid, "Role", roleField);
        addToGrid(grid, "Department", departmentField);
        addToGrid(grid, "Facility ID", facilityIDField);
        addToGrid(grid, "Email", emailField);
        addToGrid(grid, "Phone", phoneField);
        addToGrid(grid, "Employment Status", employmentStatusField);
        addToGrid(grid, "Start Date", startDateField);
        addToGrid(grid, "Line Manager", lineManagerField);
        addToGrid(grid, "Access Level", accessLevelField);

        formPanel.add(grid, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> addStaff());
        updateBtn.addActionListener(e -> updateStaff());
        deleteBtn.addActionListener(e -> deleteStaff());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== Split Pane =====
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, formPanel);
        split.setResizeWeight(0.5); // table ~50%, form ~50%
        split.setDividerSize(5);
        split.setOneTouchExpandable(true);

        add(split, BorderLayout.CENTER);
    }

    private JTextField createField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private void addToGrid(JPanel grid, String label, JTextField field) {
        grid.add(new JLabel(label));
        grid.add(field);
    }

    // ================== CRUD ==================
    private Staff createStaffFromForm() {
        return new Staff(
                staffIDField.getText(), firstNameField.getText(), lastNameField.getText(),
                roleField.getText(), departmentField.getText(), facilityIDField.getText(),
                emailField.getText(), phoneField.getText(), employmentStatusField.getText(),
                startDateField.getText(), lineManagerField.getText(), accessLevelField.getText()
        );
    }

    private void addStaff() {
        Staff s = createStaffFromForm();
        controller.addStaff(s);
        refreshData();
        clearForm();
    }

    private void updateStaff() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteStaff((String) tableModel.getValueAt(row, 0));
            controller.addStaff(createStaffFromForm());
            refreshData();
            clearForm();
        }
    }

    private void deleteStaff() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteStaff((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private void loadSelectedStaff() {
        int r = table.getSelectedRow();
        staffIDField.setText((String) tableModel.getValueAt(r, 0));
        firstNameField.setText((String) tableModel.getValueAt(r, 1));
        lastNameField.setText((String) tableModel.getValueAt(r, 2));
        roleField.setText((String) tableModel.getValueAt(r, 3));
        departmentField.setText((String) tableModel.getValueAt(r, 4));
        facilityIDField.setText((String) tableModel.getValueAt(r, 5));
        emailField.setText((String) tableModel.getValueAt(r, 6));
        phoneField.setText((String) tableModel.getValueAt(r, 7));
        employmentStatusField.setText((String) tableModel.getValueAt(r, 8));
        startDateField.setText((String) tableModel.getValueAt(r, 9));
        lineManagerField.setText((String) tableModel.getValueAt(r, 10));
        accessLevelField.setText((String) tableModel.getValueAt(r, 11));
    }

    private void clearForm() {
        staffIDField.setText(""); firstNameField.setText(""); lastNameField.setText("");
        roleField.setText(""); departmentField.setText(""); facilityIDField.setText("");
        emailField.setText(""); phoneField.setText(""); employmentStatusField.setText("");
        startDateField.setText(""); lineManagerField.setText(""); accessLevelField.setText("");
        table.clearSelection();
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Staff> list = controller.getAllStaff();
        for (Staff s : list) {
            tableModel.addRow(new Object[]{
                    s.getStaffID(), s.getFirstName(), s.getLastName(),
                    s.getRole(), s.getDepartment(), s.getFacilityID(),
                    s.getEmail(), s.getPhone(), s.getEmploymentStatus(),
                    s.getStartDate(), s.getLineManager(), s.getAccessLevel()
            });
        }
    }
}

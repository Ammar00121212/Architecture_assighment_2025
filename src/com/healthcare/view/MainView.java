package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import javax.swing.*;
import java.awt.*;

/**
 * Main View class - Main GUI window with tabs for different entities
 */
public class MainView extends JFrame {
    private HealthcareController controller;
    private JTabbedPane tabbedPane;
    private PatientPanel patientPanel;
    private ClinicianPanel clinicianPanel;
    private FacilityPanel facilityPanel;
    private AppointmentPanel appointmentPanel;
    private PrescriptionPanel prescriptionPanel;
    private ReferralPanel referralPanel;
    private StaffPanel staffPanel;

    public MainView(HealthcareController controller) {
        this.controller = controller;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Healthcare Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create menu bar
        createMenuBar();

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create panels for each entity
        patientPanel = new PatientPanel(controller);
        clinicianPanel = new ClinicianPanel(controller);
        facilityPanel = new FacilityPanel(controller);
        appointmentPanel = new AppointmentPanel(controller);
        prescriptionPanel = new PrescriptionPanel(controller);
        referralPanel = new ReferralPanel(controller);
        staffPanel = new StaffPanel(controller);

        // Add panels to tabs
        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Clinicians", clinicianPanel);
        tabbedPane.addTab("Facilities", facilityPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Prescriptions", prescriptionPanel);
        tabbedPane.addTab("Referrals", referralPanel);
        tabbedPane.addTab("Staff", staffPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.add(new JLabel("Ready"));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load Data");
        loadMenuItem.addActionListener(e -> loadData());
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String dataDirectory = fileChooser.getSelectedFile().getAbsolutePath();
            controller.loadData(dataDirectory);
            refreshAllPanels();
            JOptionPane.showMessageDialog(this, "Data loaded successfully!");
        }
    }

    public void refreshAllPanels() {
        patientPanel.refreshData();
        clinicianPanel.refreshData();
        facilityPanel.refreshData();
        appointmentPanel.refreshData();
        prescriptionPanel.refreshData();
        referralPanel.refreshData();
        staffPanel.refreshData();
    }
}


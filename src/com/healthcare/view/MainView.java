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
        // After UI and data are ready, populate all tables once on startup
        refreshAllPanels();
    }

    private void initializeGUI() {
        setTitle("Healthcare Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Use a modern, clean background
        getContentPane().setBackground(new Color(245, 247, 250));

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        header.setBackground(new Color(33, 150, 243));

        JLabel titleLabel = new JLabel("Healthcare Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        header.add(titleLabel, BorderLayout.WEST);

        JLabel subtitleLabel = new JLabel("Patient, Clinician, Facility, Appointment & Referral Management");
        subtitleLabel.setForeground(new Color(225, 240, 255));
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(Font.PLAIN, 13f));
        header.add(subtitleLabel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // Create menu bar
        createMenuBar();

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(tabbedPane.getFont().deriveFont(Font.PLAIN, 14f));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setBackground(new Color(250, 250, 250));
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setForeground(new Color(100, 100, 100));
        statusBar.add(statusLabel);
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

    //load data for csv files
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


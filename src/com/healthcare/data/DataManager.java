package com.healthcare.data;

import com.healthcare.model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Manager class to load and manage all healthcare data
 * Uses BufferedReader for CSV parsing
 */
public class DataManager {
    private List<Patient> patients;
    private List<Clinician> clinicians;
    private List<Facility> facilities;
    private List<Appointment> appointments;
    private List<Prescription> prescriptions;
    private List<Referral> referrals;
    private List<Staff> staff;

    public DataManager() {
        patients = new ArrayList<>();
        clinicians = new ArrayList<>();
        facilities = new ArrayList<>();
        appointments = new ArrayList<>();
        prescriptions = new ArrayList<>();
        referrals = new ArrayList<>();
        staff = new ArrayList<>();
    }

    /**
     * Load all CSV files from the data directory
     */
    public void loadAllData(String dataDirectory) {
        loadPatients(dataDirectory + "/patients.csv");
        loadClinicians(dataDirectory + "/clinicians.csv");
        loadFacilities(dataDirectory + "/facilities.csv");
        loadAppointments(dataDirectory + "/appointments.csv");
        loadPrescriptions(dataDirectory + "/prescriptions.csv");
        loadReferrals(dataDirectory + "/referrals.csv");
        loadStaff(dataDirectory + "/staff.csv");
    }

    /**
     * Parse CSV line handling quoted fields
     */
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString().trim());
        return fields.toArray(new String[0]);
    }

    public void loadPatients(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = parseCSVLine(line);
                if (fields.length >= 10) {
                    Patient patient = new Patient(fields[0], fields[1], fields[2], fields[3],
                            fields[4], fields[5], fields[6], fields[7], fields[8], fields[9]);
                    patients.add(patient);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading patients: " + e.getMessage());
        }
    }

    public void loadClinicians(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = parseCSVLine(line);
                if (fields.length >= 8) {
                    Clinician clinician = new Clinician(fields[0], fields[1], fields[2], fields[3],
                            fields[4], fields[5], fields[6], fields[7]);
                    clinicians.add(clinician);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading clinicians: " + e.getMessage());
        }
    }

    public void loadFacilities(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = parseCSVLine(line);
                if (fields.length >= 8) {
                    Facility facility = new Facility(fields[0], fields[1], fields[2], fields[3],
                            fields[4], fields[5], fields[6], fields[7]);
                    facilities.add(facility);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading facilities: " + e.getMessage());
        }
    }

    public void loadAppointments(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = parseCSVLine(line);
                if (fields.length >= 9) {
                    Appointment appointment = new Appointment(fields[0], fields[1], fields[2], fields[3],
                            fields[4], fields[5], fields[6], fields[7], fields[8]);
                    appointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }

    public void loadPrescriptions(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = parseCSVLine(line);
                if (fields.length >= 10) {
                    Prescription prescription = new Prescription(fields[0], fields[1], fields[2], fields[3],
                            fields[4], fields[5], fields[6], fields[7], fields[8], fields[9]);
                    prescriptions.add(prescription);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading prescriptions: " + e.getMessage());
        }
    }

    public void loadReferrals(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = parseCSVLine(line);
                if (fields.length >= 10) {
                    Referral referral = new Referral(fields[0], fields[1], fields[2], fields[3],
                            fields[4], fields[5], fields[6], fields[7], fields[8], fields[9]);
                    referrals.add(referral);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading referrals: " + e.getMessage());
        }
    }

    public void loadStaff(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = parseCSVLine(line);
                if (fields.length >= 7) {
                    Staff staffMember = new Staff(fields[0], fields[1], fields[2], fields[3],
                            fields[4], fields[5], fields[6]);
                    staff.add(staffMember);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading staff: " + e.getMessage());
        }
    }

    // Getters
    public List<Patient> getPatients() {
        return patients;
    }

    public List<Clinician> getClinicians() {
        return clinicians;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public List<Referral> getReferrals() {
        return referrals;
    }

    public List<Staff> getStaff() {
        return staff;
    }

    // Add methods
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void addClinician(Clinician clinician) {
        clinicians.add(clinician);
    }

    public void addFacility(Facility facility) {
        facilities.add(facility);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public void addReferral(Referral referral) {
        referrals.add(referral);
    }

    public void addStaff(Staff staffMember) {
        staff.add(staffMember);
    }

    // Delete methods
    public boolean deletePatient(String patientID) {
        return patients.removeIf(p -> p.getPatientID().equals(patientID));
    }

    public boolean deleteClinician(String clinicianID) {
        return clinicians.removeIf(c -> c.getClinicianID().equals(clinicianID));
    }

    public boolean deleteFacility(String facilityID) {
        return facilities.removeIf(f -> f.getFacilityID().equals(facilityID));
    }

    public boolean deleteAppointment(String appointmentID) {
        return appointments.removeIf(a -> a.getAppointmentID().equals(appointmentID));
    }

    public boolean deletePrescription(String prescriptionID) {
        return prescriptions.removeIf(p -> p.getPrescriptionID().equals(prescriptionID));
    }

    public boolean deleteReferral(String referralID) {
        return referrals.removeIf(r -> r.getReferralID().equals(referralID));
    }

    public boolean deleteStaff(String staffID) {
        return staff.removeIf(s -> s.getStaffID().equals(staffID));
    }

    // Find methods
    public Patient findPatient(String patientID) {
        return patients.stream()
                .filter(p -> p.getPatientID().equals(patientID))
                .findFirst()
                .orElse(null);
    }

    public Clinician findClinician(String clinicianID) {
        return clinicians.stream()
                .filter(c -> c.getClinicianID().equals(clinicianID))
                .findFirst()
                .orElse(null);
    }

    public Facility findFacility(String facilityID) {
        return facilities.stream()
                .filter(f -> f.getFacilityID().equals(facilityID))
                .findFirst()
                .orElse(null);
    }
}



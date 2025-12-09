package com.healthcare.model;

/**
 * Model class representing a Prescription
 */
public class Prescription {
    private String prescriptionID;
    private String patientID;
    private String clinicianID;
    private String medication;
    private String dosage;
    private String quantity;
    private String pharmacy;
    private String datePrescribed;
    private String collectionStatus;
    private String notes;

    public Prescription() {
    }

    public Prescription(String prescriptionID, String patientID, String clinicianID, String medication,
                        String dosage, String quantity, String pharmacy, String datePrescribed,
                        String collectionStatus, String notes) {
        this.prescriptionID = prescriptionID;
        this.patientID = patientID;
        this.clinicianID = clinicianID;
        this.medication = medication;
        this.dosage = dosage;
        this.quantity = quantity;
        this.pharmacy = pharmacy;
        this.datePrescribed = datePrescribed;
        this.collectionStatus = collectionStatus;
        this.notes = notes;
    }

    // Getters and Setters
    public String getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getClinicianID() {
        return clinicianID;
    }

    public void setClinicianID(String clinicianID) {
        this.clinicianID = clinicianID;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getDatePrescribed() {
        return datePrescribed;
    }

    public void setDatePrescribed(String datePrescribed) {
        this.datePrescribed = datePrescribed;
    }

    public String getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(String collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return prescriptionID + " - " + medication + " (" + datePrescribed + ")";
    }
}



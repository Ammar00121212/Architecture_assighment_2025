package com.healthcare.model;

/**
 * Model class representing a Referral
 */
public class Referral {
    private String referralID;
    private String patientID;
    private String referringClinicianID;
    private String receivingClinicianID;
    private String referringFacility;
    private String receivingFacility;
    private String date;
    private String urgency;
    private String clinicalSummary;
    private String status;

    public Referral() {
    }

    public Referral(String referralID, String patientID, String referringClinicianID,
                    String receivingClinicianID, String referringFacility, String receivingFacility,
                    String date, String urgency, String clinicalSummary, String status) {
        this.referralID = referralID;
        this.patientID = patientID;
        this.referringClinicianID = referringClinicianID;
        this.receivingClinicianID = receivingClinicianID;
        this.referringFacility = referringFacility;
        this.receivingFacility = receivingFacility;
        this.date = date;
        this.urgency = urgency;
        this.clinicalSummary = clinicalSummary;
        this.status = status;
    }

    // Getters and Setters
    public String getReferralID() {
        return referralID;
    }

    public void setReferralID(String referralID) {
        this.referralID = referralID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getReferringClinicianID() {
        return referringClinicianID;
    }

    public void setReferringClinicianID(String referringClinicianID) {
        this.referringClinicianID = referringClinicianID;
    }

    public String getReceivingClinicianID() {
        return receivingClinicianID;
    }

    public void setReceivingClinicianID(String receivingClinicianID) {
        this.receivingClinicianID = receivingClinicianID;
    }

    public String getReferringFacility() {
        return referringFacility;
    }

    public void setReferringFacility(String referringFacility) {
        this.referringFacility = referringFacility;
    }

    public String getReceivingFacility() {
        return receivingFacility;
    }

    public void setReceivingFacility(String receivingFacility) {
        this.receivingFacility = receivingFacility;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return referralID + " - " + date + " (" + urgency + ")";
    }
}



package com.healthcare.model;

/**
 * Model class representing Staff (non-clinical)
 */
public class Staff {
    private String staffID;
    private String firstName;
    private String lastName;
    private String role;
    private String facilityID;
    private String email;
    private String phone;

    public Staff() {
    }

    public Staff(String staffID, String firstName, String lastName, String role,
                 String facilityID, String email, String phone) {
        this.staffID = staffID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.facilityID = facilityID;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " - " + role + " (" + staffID + ")";
    }
}



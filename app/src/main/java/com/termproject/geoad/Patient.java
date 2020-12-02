package com.termproject.geoad;

public class Patient {
    private String address;
    private String caretakerID;
    private String dateOfBirth;
    private String fullName;
    private String password;
    private String patientID;
    private String phone;

    public Patient(String address, String caretakerID, String dateOfBirth, String fullName, String password, String patientID, String phone) {
        this.address = address;
        this.caretakerID = caretakerID;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.password = password;
        this.patientID = patientID;
        this.phone = phone;
    }

    public Patient() {

    }

    //Getter
    public String getAddress() {
        return address;
    }

    public String getCaretakerID() {
        return caretakerID;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getPhone() {
        return phone;
    }

    //Setter
    public void setAddress(String address) {
        this.address = address;
    }

    public void setCaretakerID(String caretakerID) {
        this.caretakerID = caretakerID;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

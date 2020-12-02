package com.termproject.geoad;

//Class to set up a caretaker
public class Caretaker {
    private String caretakerID;
    private String dateOfBirth;
    private String fullName;
    private String password;
    private String phone;

    //Constructor
    public Caretaker(String caretakerID, String dateOfBirth, String fullName, String password, String phone) {
        this.caretakerID = caretakerID;
        this.dateOfBirth = dateOfBirth;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
    }

    //Blank Constructor
    public Caretaker() {

    }

    //Getter
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

    public String getPhone() {
        return phone;
    }

    //Setter
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

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

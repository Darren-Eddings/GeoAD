package com.termproject.geoad;

import androidx.lifecycle.ViewModel;

public class PatientViewModel extends ViewModel {
    private Patient patient;
    private String caretakerNum = "";

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setCaretakerNum(String caretakerNum) {
        this.caretakerNum = caretakerNum;
    }

    public String getCaretakerNum() {
        return caretakerNum;
    }

    public void clearAll() {
        patient = null;
        caretakerNum= "";
    }
}

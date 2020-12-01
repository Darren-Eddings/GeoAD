package com.termproject.geoad;

import androidx.lifecycle.ViewModel;

public class PatientViewModel extends ViewModel {
    private int patientID;

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getPatientID() {
        return patientID;
    }
}

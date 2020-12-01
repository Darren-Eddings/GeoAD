package com.termproject.geoad;

import androidx.lifecycle.ViewModel;

public class PatientViewModel extends ViewModel {
    private Patient patient;

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }
}

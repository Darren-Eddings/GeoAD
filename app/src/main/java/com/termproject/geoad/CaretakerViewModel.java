package com.termproject.geoad;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CaretakerViewModel extends ViewModel {
    private Caretaker caretaker;
    private ArrayList<Patient> patientArrayList;
    private Patient selectedPatient;


    public void setCaretaker(Caretaker caretaker) {
        this.caretaker = caretaker;
    }

    public Caretaker getCaretaker() {
        return caretaker;
    }

    public void setPatientList() {
        patientArrayList = new ArrayList<>();
    }

    public void setPatientList(ArrayList<Patient> patientList) {
        patientArrayList = patientList;
    }

    public ArrayList<Patient> getPatientList() {
        return patientArrayList;
    }

    public void addPatient(Patient patient) {
        patientArrayList.add(patient);
    }

    public void setSelectedPatient(Patient patient) {
        selectedPatient = patient;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void clearAll() {
        caretaker = null;
        patientArrayList = null;
        selectedPatient = null;
    }
}

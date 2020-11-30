package com.termproject.geoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerHomeScreen extends Fragment implements View.OnClickListener{

    private ImageButton patientInformation;
    private ImageButton checkPatientLocation;
    private ImageButton manageGeofence;
    private ImageButton removePatient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_home_screen, container, false);
        patientInformation = view.findViewById(R.id.patientInformationButton);
        checkPatientLocation = view.findViewById(R.id.checkPatientLocationButton);
        manageGeofence = view.findViewById(R.id.manageGeofenceButton);
        removePatient = view.findViewById(R.id.removePatientButton);
        patientInformation.setOnClickListener(this);
        checkPatientLocation.setOnClickListener(this);
        manageGeofence.setOnClickListener(this);
        removePatient.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.patientInformationButton) {
            nextFragment = new CaretakerPatientInformation();
        }
        else if (buttonId == R.id.checkPatientLocationButton) {
            Intent intent = new Intent(getContext(), PatientLocation.class);
            ((MainActivity) getActivity()).startActivity(intent);
        }
        else if (buttonId == R.id.manageGeofenceButton) {
            nextFragment = new CaretakerManageGeofence();
        }
        else if (buttonId == R.id.removePatientButton) {
            nextFragment = new CaretakerRemovePatient();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

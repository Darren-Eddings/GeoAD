package com.termproject.geoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CaretakerHomeScreen extends Fragment implements View.OnClickListener{
    //Sets up the viewModel
    private CaretakerViewModel viewModel;

    //Sets up the ImageButton variable
    private ImageButton patientInformation;
    private ImageButton checkPatientLocation;
    private ImageButton manageGeofence;
    private ImageButton removePatient;
    private ImageButton returnPatientList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Initialize the viewModel to gain access to data shared between fragments
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        // Overwrites the on the text file used to keep track of geofences with the data from the database, or creates a new file if one does not exist.
        File file = new File(getActivity().getFilesDir() + "/CaretakerGeofenceList.txt");
        FileOutputStream fr = null;
        try{
            fr = new FileOutputStream(file, false);
            OutputStreamWriter writer = new OutputStreamWriter(fr);

            if (viewModel.getSelectedPatient().getGeofence() == "") {
                writer.write("");
            }
            else {
                String[] lines = viewModel.getSelectedPatient().getGeofence().split("\n");
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            }

            writer.close();
            fr.close();
        }catch (IOException e) {
            e.printStackTrace();
        }


        View view = inflater.inflate(R.layout.fragment_caretaker_home_screen, container, false);

        patientInformation = view.findViewById(R.id.patientInformationButton);
        checkPatientLocation = view.findViewById(R.id.checkPatientLocationButton);
        manageGeofence = view.findViewById(R.id.manageGeofenceButton);
        removePatient = view.findViewById(R.id.removePatientButton);
        returnPatientList = view.findViewById(R.id.returnToPatientList);

        patientInformation.setOnClickListener(this);
        checkPatientLocation.setOnClickListener(this);
        manageGeofence.setOnClickListener(this);
        removePatient.setOnClickListener(this);
        returnPatientList.setOnClickListener(this);

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
            intent.putExtra("patientName", viewModel.getSelectedPatient().getFullName());
            ((MainActivity) getActivity()).startActivity(intent);
        }
        else if (buttonId == R.id.manageGeofenceButton) {
            nextFragment = new CaretakerManageGeofence();
        }
        else if (buttonId == R.id.removePatientButton) {
            nextFragment = new CaretakerRemovePatient();
        }
        else if (buttonId == R.id.returnToPatientList) {
            nextFragment = new CaretakerPatientList();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

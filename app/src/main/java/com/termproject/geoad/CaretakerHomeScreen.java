/*
 * Written by Kieffer Liestyo
 *
 * Edited and Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment acts as a home screen for the
 * caretaker where they can navigate to all
 * other features of the caretaker side of
 * the app
 */
package com.termproject.geoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerHomeScreen extends Fragment implements View.OnClickListener{

    //initialize ImageButton variables
    private ImageButton patientInformation;
    private ImageButton checkPatientLocation;
    private ImageButton manageGeofence;
    private ImageButton removePatient;
    private ImageButton returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create view
        View view = inflater.inflate(R.layout.fragment_caretaker_home_screen, container, false);

        //connect ImageButton variables with corresponding xml elements
        patientInformation = view.findViewById(R.id.patientInformationButton);
        checkPatientLocation = view.findViewById(R.id.checkPatientLocationButton);
        manageGeofence = view.findViewById(R.id.manageGeofenceButton);
        removePatient = view.findViewById(R.id.removePatientButton);
        returnButton = view.findViewById(R.id.returnToPatientList);

        //sett all of the ImageButtons to listen for clicks
        patientInformation.setOnClickListener(this);
        checkPatientLocation.setOnClickListener(this);
        manageGeofence.setOnClickListener(this);
        removePatient.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {

        //create a temporary fragment and set it to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if user presses the patient info button set nextFragment to CaretakerPatientInformation()
        if (buttonId == R.id.patientInformationButton) {

            nextFragment = new CaretakerPatientInformation();
        }

        //if user presses the patient location button open the map activity
        else if (buttonId == R.id.checkPatientLocationButton) {

            Intent intent = new Intent(getContext(), PatientLocation.class);
            ((MainActivity) getActivity()).startActivity(intent);
        }

        //if user presses the manage geofence button set nextFragment to CaretakerManageGeofence()
        else if (buttonId == R.id.manageGeofenceButton) {

            nextFragment = new CaretakerManageGeofence();
        }

        //if user presses the remove patient button set nextFragment to CaretakerRemovePatient()
        else if (buttonId == R.id.removePatientButton) {

            nextFragment = new CaretakerRemovePatient();
        }

        //if user presses the return button set nextFragment to specified fragment
        else if (buttonId == R.id.returnToPatientList){

            nextFragment = new CaretakerPatientList();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //replace current fragment in the main activity with nextFragment
        try {

            mainActivity.replaceFragments(nextFragment);
        }

        //if nextFragment is null throw an exception
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

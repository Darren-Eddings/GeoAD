/*
 * Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated upon fragment
 * creation
 *
 * Fragment is used to navigate from start up screen to
 * login page of either the patient or caretaker
 */
package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StartScreen extends Fragment implements View.OnClickListener {

    //initialize variable for UI objects
    private Button patientButton;
    private Button caretakerButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create a view to display UI objects
        View view = inflater.inflate(R.layout.fragment_start_screen, container, false);

        //connect variable to objects
        patientButton = view.findViewById(R.id.patientButton);
        caretakerButton = view.findViewById(R.id.caretakerButton);

        //have UI object listen for tap
        patientButton.setOnClickListener(this);
        caretakerButton.setOnClickListener(this);
        return view;
    }

    @Override
    //when an object is tapped
    public void onClick(View v) {

        //create a new fragment and set it to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the object tapped has id patientButton set nextFragment to PatientLogin()
        if (buttonId == R.id.patientButton) {
            nextFragment = new PatientLogin();
        }

        //if the object tapped has id caretakerButton set nextFragment t0 CaretakerLogin()
        else if (buttonId == R.id.caretakerButton) {
            nextFragment = new CaretakerLogin();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //replace current fragment with nextFragment
        try {

            mainActivity.replaceFragments(nextFragment);
        }

        //if nextFragment is null when clicked throw an exception
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

/*
 *Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment is designed to create a page
 * where the caretaker can enter patient
 * info and add them to their patient list
 * or cancel and return to their list
 * without adding the patient
 */
package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerAddPatient extends Fragment implements View.OnClickListener {

    //initialize two ImageButton objects
    private ImageButton add;
    private ImageButton cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create a view to add UI elements to
        View view = inflater.inflate(R.layout.fragment_caretaker_add_patient, container, false);

        //point object variables in the java file to corresponding fields in xml file for the same fragment
        add = view.findViewById(R.id.addButton);
        cancel = view.findViewById(R.id.cancelButton);

        //have both buttons listen for clicks
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {

        //when button is clicked create temporary fragment that is null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the button idea is addButton in the xml file
        if (buttonId == R.id.addButton) {

            //set nextFragment equal to CaretakerPatientLsit()
            nextFragment = new CaretakerPatientList();

            //create fields for a toast
            Context context = getActivity();
            CharSequence text = "Patient Added Successfully!";
            int duration = Toast.LENGTH_SHORT;

            //create and display a toast that confirms the addition of a new patient
            Toast patientAdded = Toast.makeText(context, text, duration);
            patientAdded.show();
        }

        //if the button id is cancelButton in the xml file
        else if (buttonId == R.id.cancelButton) {

            //set nextFragment equal to CaretakerPatientList
            nextFragment = new CaretakerPatientList();
        }

        //replace the current fragment in main activity with nextFragment
        MainActivity mainActivity = (MainActivity) getActivity();

        try {

            mainActivity.replaceFragments(nextFragment);
        }

        //if nextFragment is null throw an exception
        catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}

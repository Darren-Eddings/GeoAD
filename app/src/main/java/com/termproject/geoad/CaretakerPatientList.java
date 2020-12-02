/*
 * Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some Code has been automatically generated
 * upon fragment creation
 *
 * Fragment is responsible for displaying a list
 * of patients and allowing caretaker to select
 * a patient to view or add a new patient
 */
package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerPatientList extends Fragment implements View.OnClickListener {

    //initialize UI object to be displayed on the page
    private ArrayAdapter<CharSequence> patientListAdapter;
    private ListView patientList;
    private ImageButton addPatient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //creates a view to display UI objects
        View view = inflater.inflate(R.layout.fragment_caretaker_patient_list, container, false);

        //point variables to xml files required for showing them on the page
        patientListAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.caretaker_patient_list_dummy, R.layout.listview_entry_color);
        patientList = view.findViewById(R.id.patientList);

        //set ListView as an adapter
        patientList.setAdapter(patientListAdapter);

        //have add patient list listen for click
        patientList.setOnItemClickListener(patientListClick);

        //point to button at an ID and have it listen for clicks
        addPatient = view.findViewById(R.id.addPatientButton);
        addPatient.setOnClickListener(this);

        return view;
    }

    //when an item in the ListView is clicked
    private AdapterView.OnItemClickListener patientListClick = (parent, v, position, id) -> {

        //initialize a temporary fragment
        Fragment nextFragment = new CaretakerHomeScreen();

        //set current fragment in the main activity to nextFragment
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    };

    @Override public void onClick (View v) {

        //when the add patient button is clicked create a temporary fragment and set it to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the button id is correct
        if (buttonId == R.id.addPatientButton) {

            //set the temporary fragment to equal  the CaretakerAddPatient fragment
            nextFragment = new CaretakerAddPatient();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //replace current fragment in the main activity with nextFragment
        try {
            mainActivity.replaceFragments(nextFragment);

        }

        //throw an exception if nextFragment == null on click
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

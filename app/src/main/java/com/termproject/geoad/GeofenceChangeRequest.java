/*
 * Written by Darren Eddings 611442
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Objects in this fragment create a form that
 * the patient can fill out and send to their
 * caretaker to request a change in an already
 * existing geofence
 */
package com.termproject.geoad;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class GeofenceChangeRequest extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //initialize UI object variables
    private ArrayAdapter<CharSequence> typeAdapter;
    private ArrayAdapter<CharSequence> nameAdapter;
    private Spinner typeSpin;
    private Spinner nameSpin;
    private ImageButton submitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create view to display UI objects
        View view = inflater.inflate(R.layout.fragment_geofence_change_request, container, false);

        //point UI object variables to the objects in the xml file by using their ID
        typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_list, R.layout.color_spinner_layout);
        nameAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_list, R.layout.color_spinner_layout);
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_color);
        nameAdapter.setDropDownViewResource(R.layout.spinner_dropdown_color);
        typeSpin = view.findViewById(R.id.typeSpinner);
        nameSpin = view.findViewById(R.id.nameSpinner);

        //add elements to spinner
        typeSpin.setAdapter(typeAdapter);
        nameSpin.setAdapter(nameAdapter);

        //look for selected element from string array
        typeSpin.setOnItemSelectedListener(this);
        nameSpin.setOnItemSelectedListener(this);

        //point the ImageButton to the ImageButton in the xml file
        submitButton = view.findViewById(R.id.submitChangeButton);

        //have button listen for a click
        submitButton.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {

        //create a new temporary fragment and set it to null
        Fragment newFragment = null;
        int buttonID = v.getId();

        //if the button was pressed
        if (buttonID == R.id.submitChangeButton) {

            //set nextFragment to RequestConfirm
            newFragment = new RequestConfirm();
        }

        //replace current fragment in main activity with nextFragment
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(newFragment);
    }

    //when an item selected set the text of the spinner to that item
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    //if nothing is selected do nothing
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
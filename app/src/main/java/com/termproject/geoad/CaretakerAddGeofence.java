package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerAddGeofence extends Fragment implements View.OnClickListener {

    //Variables set up for the class
    private ArrayAdapter<CharSequence> geofenceTypeAdapter;
    private ArrayAdapter<CharSequence> geofenceSizeUnitAdapter;
    private Spinner geofenceType;
    private Spinner geofenceSizeUnit;
    private ImageButton addButton;
    private ImageButton cancelButton;

    @Nullable
    @Override
    //function that is called when the fragment is launched
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Sets up the "view" that the app is going to show
        View view = inflater.inflate(R.layout.fragment_caretaker_add_geofence, container, false);

        //Sets up the variables that are going to fill the ListView objects. The values are obtained from the strings resource file
        geofenceTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_list, R.layout.color_spinner_layout);
        geofenceSizeUnitAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_size_unit_with_select, R.layout.color_spinner_layout);

        //Sets up the spinner, including its color, that will be used to select the values of input
        geofenceTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_color);
        geofenceSizeUnitAdapter.setDropDownViewResource(R.layout.spinner_dropdown_color);

        //Links the variable to the actual view object
        geofenceType = view.findViewById(R.id.typeSpinner);
        geofenceSizeUnit = view.findViewById(R.id.sizeUnitSpinner);

        //Connects the views to the array adapter we have set up previously.
        geofenceType.setAdapter(geofenceTypeAdapter);
        geofenceSizeUnit.setAdapter(geofenceSizeUnitAdapter);

        //Sets up the buttons to be used for app navigation
        addButton = view.findViewById(R.id.addButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        //Adds listeners to detect when the button is clicked and do the corresponding action
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        //Displays the view on the app by returning it
        return view;
    }

    //The function that will be called when the listeners detect that a button has been clicked
    @Override public void onClick (View v) {

        //Initializes the next fragment that will be launched
        Fragment nextFragment = null;

        //Retrieves the id of the button that was clicked
        int buttonId = v.getId();

        //If else statements to navigate to the appropriate fragments depending on the button clicked.
        if (buttonId == R.id.addButton) {
            nextFragment = new CaretakerManageGeofence();

            Context context = getActivity();
            CharSequence text = "New Geofence added successfully!";
            int duration = Toast.LENGTH_SHORT;

            Toast addedGeofence = Toast.makeText(context, text, duration);
            addedGeofence.show();
        }
        else if (buttonId == R.id.cancelButton) {
            nextFragment = new CaretakerManageGeofence();
        }

        //Does the actual replacing of the old fragment for the new one
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CaretakerGeofenceInformation extends Fragment implements View.OnClickListener {
    //Initializes viewModel to share data between fragments
    private CaretakerViewModel viewModel;

    //Sets up the appropriate view objects
    private ArrayAdapter<CharSequence> geofenceInformationFieldsAdapter;
    private ArrayAdapter<CharSequence> geofenceInformationListAdapter;
    private ListView geofenceInformationFields;
    private ListView geofenceInformationList;
    private Button returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Instantiates viewModel to gain access to all existing data
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        //Sets up the view to be displayed on the app
        View view = inflater.inflate(R.layout.fragment_caretaker_geofence_information, container, false);

        //Sets up the list adapters. One filled with values from the string resource file while the other has values to be added
        geofenceInformationFieldsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_information_fields, android.R.layout.simple_list_item_1);
        geofenceInformationListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        //Add values to the second adapter based on several conditions
        geofenceInformationListAdapter.add(viewModel.getGeofence()[0]);
        geofenceInformationListAdapter.add(viewModel.getGeofence()[3] + " m radius");
        switch (viewModel.getGeofence()[4]) {
            case "0":
                geofenceInformationListAdapter.add("Classic");
            break;
            case "1":
                geofenceInformationListAdapter.add("Point-of-Interest");
            break;
            case "2":
                geofenceInformationListAdapter.add("Timed");
            break;
        }
        if(viewModel.getGeofence()[5].equals("-1")) {
            geofenceInformationListAdapter.add("Not Timed");
        }
        else {
            geofenceInformationListAdapter.add(Integer.toString((Integer.parseInt(viewModel.getGeofence()[5])/1000/3600)) + " minutes");
        }

        //Link the variables to the appropriate view objects
        geofenceInformationFields = view.findViewById(R.id.geofenceInformationFields);
        geofenceInformationList = view.findViewById(R.id.geofenceInformationList);

        //Link the viewObjects to the adapters to fill them with values
        geofenceInformationFields.setAdapter(geofenceInformationFieldsAdapter);
        geofenceInformationList.setAdapter(geofenceInformationListAdapter);

        //Link the button to the variable
        returnButton = view.findViewById(R.id.returnButton);

        //Set up a listener for the button
        returnButton.setOnClickListener(this);

        //Returns and displays the view
        return view;
    }

    //Function that gets called when a button is clicked
    @Override public void onClick(View v) {
        //Sets up the next fragment to be navigated to
        Fragment nextFragment = null;

        //Retrieves the id of the button being clicked
        int buttonId = v.getId();

        //If statement only for one button to navigate to the appropriate fragment
        if (buttonId == R.id.returnButton) {
            nextFragment = new CaretakerManageGeofence();
        }

        //Replaces old fragment with the new one
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

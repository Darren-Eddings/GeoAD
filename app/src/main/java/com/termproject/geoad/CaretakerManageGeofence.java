/*
 *Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment displays a page that lists
 * a patient's current geofences and
 * allows caretaker to add a geofence
 * or return back to the homepage
 */
package com.termproject.geoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerManageGeofence extends Fragment implements View.OnClickListener {

    //initialize UI object variables for xml file
    private ArrayAdapter<CharSequence> geofenceListAdapter;
    private ListView geofenceList;
    private ImageButton addGeofenceButton;
    private ImageButton returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create view
        View view = inflater.inflate(R.layout.fragment_caretaker_manage_geofence, container, false);

        //set geofenceListAdapter to an array adapter that takes element from specified array
        geofenceListAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.caretaker_manage_geofence_dummy, R.layout.listview_entry_color);

        //connect variables to corresponding UI components
        geofenceList = view.findViewById(R.id.geofenceList);
        addGeofenceButton = view.findViewById(R.id.addGeofenceButton);
        returnButton = view.findViewById(R.id.returnButton);

        //create adapter for variable
        geofenceList.setAdapter(geofenceListAdapter);

        //have these UI variables listen for a click
        geofenceList.setOnItemClickListener(geofenceListClick);
        addGeofenceButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        return view;
    }

    private AdapterView.OnItemClickListener geofenceListClick = (parent, v, position, id) -> {

        //create a temporary fragment and set its value to specified fragment
        Fragment nextFragment = new CaretakerGeofenceInformation();

        //find mainActivity and replace current fragment with nextFragment
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    };

    @Override public void onClick (View v) {

        //create a temporary fragment and set its value to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the id of the button pressed was the one specified below
        if (buttonId == R.id.addGeofenceButton) {

            //open caretaker map activity
            Intent intent = new Intent(getContext(), CaretakerMapActivity.class);
            ((MainActivity) getActivity()).startActivity(intent);
        }

        //if the id of the button pressed was the one specified below
        else if (buttonId == R.id.returnButton) {

            //set nextFragment to be equal to an already existing fragment
            nextFragment = new CaretakerHomeScreen();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //replace current fragment with nextFragment
        try {
            mainActivity.replaceFragments(nextFragment);
        }

        //if nextFragment is null throw an exception
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}

package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerManageGeofence extends Fragment implements View.OnClickListener {

    private ArrayAdapter<CharSequence> geofenceListAdapter;
    private ListView geofenceList;
    private Button addGeofenceButton;
    private Button returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_manage_geofence, container, false);

        geofenceListAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.caretaker_manage_geofence_dummy, android.R.layout.simple_list_item_1);

        geofenceList = view.findViewById(R.id.geofenceList);

        geofenceList.setAdapter(geofenceListAdapter);

        geofenceList.setOnItemClickListener(geofenceListClick);

        addGeofenceButton = view.findViewById(R.id.addGeofenceButton);
        returnButton = view.findViewById(R.id.returnButton);

        addGeofenceButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        return view;
    }

    private AdapterView.OnItemClickListener geofenceListClick = (parent, v, position, id) -> {
        Fragment nextFragment = new CaretakerGeofenceInformation();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    };

    @Override public void onClick (View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.addGeofenceButton) {
            nextFragment = new CaretakerAddGeofence();
        }
        else if (buttonId == R.id.returnButton) {
            nextFragment = new CaretakerHomeScreen();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}

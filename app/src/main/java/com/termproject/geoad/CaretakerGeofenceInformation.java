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

public class CaretakerGeofenceInformation extends Fragment implements View.OnClickListener {

    private ArrayAdapter<CharSequence> geofenceInformationFieldsAdapter;
    private ArrayAdapter<CharSequence> geofenceInformationListAdapter;
    private ListView geofenceInformationFields;
    private ListView geofenceInformationList;
    private Button editGeofenceButton;
    private Button returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_geofence_information, container, false);

        geofenceInformationFieldsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_information_fields, android.R.layout.simple_list_item_1);
        geofenceInformationListAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_information_list_dummy, android.R.layout.simple_list_item_1);

        geofenceInformationFields = view.findViewById(R.id.geofenceInformationFields);
        geofenceInformationList = view.findViewById(R.id.geofenceInformationList);

        geofenceInformationFields.setAdapter(geofenceInformationFieldsAdapter);
        geofenceInformationList.setAdapter(geofenceInformationListAdapter);

        editGeofenceButton = view.findViewById(R.id.editGeofenceButton);
        returnButton = view.findViewById(R.id.returnButton);

        editGeofenceButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.editGeofenceButton) {
            nextFragment = new CaretakerEditGeofence();
        }
        else if (buttonId == R.id.returnButton) {
            nextFragment = new CaretakerManageGeofence();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

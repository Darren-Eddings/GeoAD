package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerAddGeofence extends Fragment implements View.OnClickListener {

    private ArrayAdapter<CharSequence> geofenceTypeAdapter;
    private ArrayAdapter<CharSequence> geofenceSizeUnitAdapter;
    private Spinner geofenceType;
    private Spinner geofenceSizeUnit;
    private Button addButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_add_geofence, container, false);

        geofenceTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_list, android.R.layout.simple_spinner_dropdown_item);
        geofenceSizeUnitAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_size_unit_with_select, android.R.layout.simple_spinner_dropdown_item);

        geofenceType = view.findViewById(R.id.typeSpinner);
        geofenceSizeUnit = view.findViewById(R.id.sizeUnitSpinner);

        geofenceType.setAdapter(geofenceTypeAdapter);
        geofenceSizeUnit.setAdapter(geofenceSizeUnitAdapter);

        addButton = view.findViewById(R.id.addButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick (View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
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
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

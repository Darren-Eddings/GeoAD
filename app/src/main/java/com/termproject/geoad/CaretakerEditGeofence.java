package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerEditGeofence extends Fragment implements View.OnClickListener {

    private ArrayAdapter<CharSequence> geofenceTypeAdapter;
    private ArrayAdapter<CharSequence> geofenceSizeUnitAdapter;
    private EditText geofenceName;
    private EditText geofenceSize;
    private EditText geofenceAddress;
    private Spinner geofenceType;
    private Spinner geofenceSizeUnit;
    private ImageButton saveChangesButton;
    private ImageButton cancelButton;
    private String[] geofenceInformationDummy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_edit_geofence, container, false);

        geofenceInformationDummy = getActivity().getResources().getStringArray(R.array.geofence_information_list_dummy);

        geofenceName = view.findViewById(R.id.nameTextBox);
        geofenceSize = view.findViewById(R.id.sizeTextBox);
        geofenceAddress = view.findViewById(R.id.addressTextBox);

        geofenceTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_list_without_select, R.layout.color_spinner_layout);
        geofenceSizeUnitAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_size_unit, R.layout.color_spinner_layout);

        geofenceTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_color);
        geofenceSizeUnitAdapter.setDropDownViewResource(R.layout.spinner_dropdown_color);

        geofenceType = view.findViewById(R.id.typeSpinner);
        geofenceSizeUnit = view.findViewById(R.id.sizeUnitSpinner);

        geofenceType.setAdapter(geofenceTypeAdapter);
        geofenceSizeUnit.setAdapter(geofenceSizeUnitAdapter);

        saveChangesButton = view.findViewById(R.id.yesButtonEdit);
        cancelButton = view.findViewById(R.id.noButtonEdit);

        geofenceName.setText(geofenceInformationDummy[0]);
        geofenceSize.setText(geofenceInformationDummy[1]);
        geofenceAddress.setText(geofenceInformationDummy[3]);

        for (int i = 0; i <= 3; i++) {
            if (geofenceTypeAdapter.getItem(i) == geofenceInformationDummy[2]) {
                geofenceType.setSelection(i);
            }
        }

        saveChangesButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick (View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.yesButtonEdit) {
            nextFragment = new CaretakerGeofenceInformation();

            Context context = getActivity();
            CharSequence text = "Changes saved successfully!";
            int duration = Toast.LENGTH_SHORT;

            Toast savedChanges = Toast.makeText(context, text, duration);
            savedChanges.show();
        }
        else if (buttonId == R.id.noButtonEdit) {
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

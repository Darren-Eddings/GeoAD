package com.termproject.geoad;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GeofenceChangeRequest extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ArrayAdapter<CharSequence> typeAdapter;
    private ArrayAdapter<CharSequence> nameAdapter;
    private Spinner typeSpin;
    private Spinner nameSpin;
    private Button submitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_geofence_change_request, container, false);

        typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_list, android.R.layout.simple_spinner_item);
        nameAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_list, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpin = view.findViewById(R.id.typeSpinner);
        nameSpin = view.findViewById(R.id.nameSpinner);
        typeSpin.setAdapter(typeAdapter);
        nameSpin.setAdapter(nameAdapter);
        typeSpin.setOnItemSelectedListener(this);
        nameSpin.setOnItemSelectedListener(this);
        submitButton = view.findViewById(R.id.submitChangeButton);
        submitButton.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {

        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.submitChangeButton) {

            newFragment = new RequestConfirm();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(newFragment);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
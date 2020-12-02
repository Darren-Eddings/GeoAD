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
import androidx.lifecycle.ViewModelProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CaretakerManageGeofence extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private ArrayAdapter<CharSequence> geofenceListAdapter;
    private ListView geofenceList;
    private ImageButton addGeofenceButton;
    private ImageButton returnButton;

    private ArrayList<String[]> geofences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        View view = inflater.inflate(R.layout.fragment_caretaker_manage_geofence, container, false);

        geofenceListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        geofences = new ArrayList<>();

        File file = new File(getActivity().getFilesDir() + "/CaretakerGeofenceList.txt");
        try {
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(isr);
            String newGeofence = "";
            while (reader.ready()) {
                String line = reader.readLine();
                geofences.add(line.split(","));
                newGeofence += line + "\n";
                String geofenceName = line.split(",")[0];
                geofenceListAdapter.add(geofenceName);
            }
            viewModel.getSelectedPatient().setGeofence(newGeofence);
        }catch (IOException e) {
            e.printStackTrace();
        }

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
        String geofenceName = (String) geofenceList.getItemAtPosition(position);
        for (String[] geofence : geofences) {
            if (geofence[0].equals(geofenceName)) {
                viewModel.setGeofence(geofence);
            }
        }

        Fragment nextFragment = new CaretakerGeofenceInformation();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);

    };

    @Override public void onClick (View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.addGeofenceButton) {

            Intent intent = new Intent(getContext(), CaretakerMapActivity.class);
            intent.putExtra("patientName", viewModel.getSelectedPatient().getFullName());
            ((MainActivity) getActivity()).startActivity(intent);

        }
        else if (buttonId == R.id.returnButton) {
            nextFragment = new CaretakerHomeScreen();
            MainActivity mainActivity = (MainActivity) getActivity();
            try {
                mainActivity.replaceFragments(nextFragment);
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        geofenceListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        File file = new File(getActivity().getFilesDir() + "/CaretakerGeofenceList.txt");
        try {
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(isr);
            String newGeofence = "";
            while (reader.ready()) {
                String line = reader.readLine();
                newGeofence += line + "\n";
                String geofenceName = line.split(",")[0];
                geofenceListAdapter.add(geofenceName);
            }
            viewModel.getSelectedPatient().setGeofence(newGeofence);
        }catch (IOException e) {
            e.printStackTrace();
        }
        geofenceList.setAdapter(geofenceListAdapter);
    }
}

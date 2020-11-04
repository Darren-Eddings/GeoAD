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

public class CaretakerPatientList extends Fragment implements View.OnClickListener {

    private ArrayAdapter<String> patientListAdapter;
    private ListView patientList;
    private Button addPatient;
    private final String[] patientListArray = new String[]{"John Doe"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_patient_list, container, false);
        patientListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, patientListArray);
        patientList = view.findViewById(R.id.patientList);
        patientList.setAdapter(patientListAdapter);
        patientList.setOnItemClickListener(patientListClick);
        addPatient = view.findViewById(R.id.addPatientButton);
        addPatient.setOnClickListener(this);

        return view;
    }

    private AdapterView.OnItemClickListener patientListClick = (parent, v, position, id) -> {
        Fragment nextFragment = new CaretakerHomeScreen();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    };

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.addPatientButton) {
            nextFragment = new CaretakerAddPatient();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

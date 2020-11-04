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
    private final String[] array = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_patient_list, container, false);
        patientListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        patientList = view.findViewById(R.id.patientList);
        patientList.setAdapter(patientListAdapter);
        patientList.setOnItemClickListener(patientListClick);
        addPatient = view.findViewById(R.id.addPatientButton);
        addPatient.setOnClickListener(this);

        return view;
    }

    private AdapterView.OnItemClickListener patientListClick = (parent, v, position, id) -> {

    };

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.addPatientButton) {

        }
    }
}

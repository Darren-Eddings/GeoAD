package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerPatientList extends Fragment {

    private ArrayAdapter<String> patientListAdapter;
    private ListView patientList;
    private final String[] array = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_patient_list, container, false);
        patientListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        patientList = view.findViewById(R.id.patientList);
        patientList.setAdapter(patientListAdapter);
        patientList.setOnItemClickListener(patientListClick);

        return view;
    }

    private AdapterView.OnItemClickListener patientListClick = (parent, v, position, id) -> {

    };

}

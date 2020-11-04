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

public class CaretakerPatientInformation extends Fragment implements View.OnClickListener {

    private ArrayAdapter<String> patientInformationFieldsAdapter;
    private ArrayAdapter<String> patientInformationListAdapter;
    private ListView patientInformationFields;
    private ListView patientInformationList;
    private Button returnButton;
    private final String[] patientInformationFieldsArray = new String[]{"Patient ID", "Name", "Phone Number", "Address"};
    private final String[] patientInformationListArray = new String[]{"000000", "John Doe", "111 111 1111", "22500 University Drive, Langley, BC, Canada | V2Y 1Y1"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_patient_information, container, false);
        patientInformationFieldsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, patientInformationFieldsArray);
        patientInformationListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, patientInformationListArray);
        patientInformationFields = view.findViewById(R.id.patientInformationFields);
        patientInformationList = view.findViewById(R.id.patientInformationList);
        patientInformationFields.setAdapter(patientInformationFieldsAdapter);
        patientInformationList.setAdapter(patientInformationListAdapter);
        returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.returnButton) {
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

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

    private ArrayAdapter<CharSequence> patientInformationFieldsAdapter;
    private ArrayAdapter<CharSequence> patientInformationListAdapter;
    private ListView patientInformationFields;
    private ListView patientInformationList;
    private Button returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_patient_information, container, false);

        patientInformationFieldsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.patient_information_fields, android.R.layout.simple_list_item_1);
        patientInformationListAdapter = ArrayAdapter.createFromResource(getActivity(), R. array.patient_information_list_dummy, android.R.layout.simple_list_item_1);

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

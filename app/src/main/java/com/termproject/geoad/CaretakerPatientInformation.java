/*
 * Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment is responsible for allowing a
 * caretaker to view the information of an
 * already existing patent
 */
package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CaretakerPatientInformation extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    //initialize UI object variables
    private ArrayAdapter<CharSequence> patientInformationFieldsAdapter;
    private ArrayAdapter<CharSequence> patientInformationListAdapter;
    private ListView patientInformationFields;
    private ListView patientInformationList;
    private ImageButton returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);


        //create view
        View view = inflater.inflate(R.layout.fragment_caretaker_patient_information, container, false);

        //create array adapters that pull from specified array with a specified layout
        patientInformationFieldsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.patient_information_fields, android.R.layout.simple_list_item_1);

        patientInformationListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        patientInformationListAdapter.add(viewModel.getSelectedPatient().getFullName());
        patientInformationListAdapter.add(viewModel.getSelectedPatient().getDateOfBirth());
        patientInformationListAdapter.add(viewModel.getSelectedPatient().getPhone());
        patientInformationListAdapter.add(viewModel.getSelectedPatient().getAddress());

        //connect UI object variable to corresponding xml component
        patientInformationFields = view.findViewById(R.id.patientInformationFields);
        patientInformationList = view.findViewById(R.id.patientInformationList);
        returnButton = view.findViewById(R.id.returnButton);

        //create the adapters
        patientInformationFields.setAdapter(patientInformationFieldsAdapter);
        patientInformationList.setAdapter(patientInformationListAdapter);

        //set ImageButton object to listen for clicks
        returnButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {

        //create temporary fragment and set it to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the return button is pressed set nextFragment equal to the home screen fragment
        if (buttonId == R.id.returnButton) {

            nextFragment = new CaretakerHomeScreen();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //set the current fragment in mainActivity equal to nextFragment
        try {

            mainActivity.replaceFragments(nextFragment);

        }

        //if nextFragment is null throw an exception
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

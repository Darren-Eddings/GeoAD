package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class NewAccountSuccess extends Fragment implements View.OnClickListener{
    private PatientViewModel viewModel;

    private int patientID;
    private TextView patientId;
    private Button goToHomePage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
        patientID = viewModel.getPatientID();

        View view = inflater.inflate(R.layout.fragment_new_account_success, container, false);

        patientId = view.findViewById(R.id.patientId);
        goToHomePage = view.findViewById(R.id.accountSuccessButton);

        patientId.setText(Integer.toString(patientID));

        goToHomePage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.accountSuccessButton) {

            newFragment = new PatientHomeScreen();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        try {

            mainActivity.replaceFragments(newFragment);

        }catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}
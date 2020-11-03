package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PatientRegisterOrLogin extends Fragment implements View.OnClickListener{

    private Button patientRegisterButton;
    private Button patientLoginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_register_or_login, container, false);
        patientRegisterButton = (Button) view.findViewById(R.id.patientRegisterButton);
        patientLoginButton = (Button) view.findViewById(R.id.patientLoginButton);
        patientRegisterButton.setOnClickListener(this);
        patientLoginButton.setOnClickListener(this);
        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.patientRegisterButton) {

            nextFragment = new PatientRegister();
        }
        else if (buttonId == R.id.patientLoginButton) {

            nextFragment = new PatientLogin();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    }
}

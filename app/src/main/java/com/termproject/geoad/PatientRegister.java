package com.termproject.geoad;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PatientRegister extends Fragment implements View.OnClickListener{

    private EditText newPatientPhone;
    private EditText newPatientPassword;
    private Button submitButton;
    private TextView goToLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_login, container, false);
        newPatientPhone = (EditText) view.findViewById(R.id.newPhoneTextBox);
        newPatientPassword = view.findViewById(R.id.newPasswordText);
        submitButton = (Button) view.findViewById(R.id.accountRegisterButton);
        goToLogin = (TextView) view.findViewById(R.id.linkLogin);
        submitButton.setOnClickListener(this);
        goToLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.linkLogin) {

            newFragment = new PatientLogin();
        } else if (buttonID == R.id.accountRegisterButton) {

            newFragment = new NewAccountSuccess();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(newFragment);
    }
}
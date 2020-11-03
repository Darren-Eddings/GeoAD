package com.termproject.geoad;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PatientLogin extends Fragment implements View.OnClickListener{

    private EditText idTextBox;
    private EditText passwordTextBox;
    private Button loginButton;
    private TextView goToRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_login, container, false);
        idTextBox = (EditText) view.findViewById(R.id.idTextBox);
        passwordTextBox = (EditText) view.findViewById(R.id.passwordTextBox);
        loginButton = (Button) view.findViewById(R.id.patientLoginButton);
        goToRegister = view.findViewById(R.id.linkRegister);
        loginButton.setOnClickListener(this);
        goToRegister.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = null;
        int buttonID = v.getId();
        if (buttonID == R.id.linkRegister) {
            newFragment = new PatientRegister();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(newFragment);
    }
}
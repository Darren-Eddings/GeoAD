package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PatientLogin extends Fragment implements View.OnClickListener{

    private Button loginButton;
    private TextView goToRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_login, container, false);
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
        } else if (buttonID == R.id.patientLoginButton) {
            newFragment = new PatientHomeScreen();

            Context context = getActivity();
            CharSequence text = "Login Successful!";
            int duration = Toast.LENGTH_SHORT;

            Toast loginSuccessful = Toast.makeText(context, text, duration);
            loginSuccessful.show();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(newFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
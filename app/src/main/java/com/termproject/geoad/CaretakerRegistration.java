package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerRegistration extends Fragment implements View.OnClickListener {

    private Button registerButton;
    private TextView loginHere;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_registration, container, false);
        registerButton = view.findViewById(R.id.caretakerRegistrationButton);
        loginHere = view.findViewById(R.id.inkLogin);
        registerButton.setOnClickListener(this);
        loginHere.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.caretakerRegistrationButton) {
            nextFragment = new CaretakerRegistrationSuccess();

            Context context = getActivity();
            CharSequence text = "Registration Successful!";
            int duration = Toast.LENGTH_SHORT;

            Toast registrationSuccessful = Toast.makeText(context, text, duration);
            registrationSuccessful.show();
        }
        else if (buttonId == R.id.inkLogin) {
            nextFragment = new CaretakerLogin();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

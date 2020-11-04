package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerAddPatient extends Fragment implements View.OnClickListener {

    private Button addPatient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_add_patient, container, false);
        addPatient = view.findViewById(R.id.addPatientButton);
        addPatient.setOnClickListener(this);
        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.addPatientButton) {
            nextFragment = new CaretakerPatientList();

            Context context = getActivity();
            CharSequence text = "Patient Added Successfully!";
            int duration = Toast.LENGTH_SHORT;

            Toast patientAdded = Toast.makeText(context, text, duration);
            patientAdded.show();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

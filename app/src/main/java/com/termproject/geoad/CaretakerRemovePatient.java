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

public class CaretakerRemovePatient extends Fragment implements View.OnClickListener {

    private Button yesButton;
    private Button noButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_remove_patient, container, false);
        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.yesButton) {
            nextFragment = new CaretakerPatientList();

            Context context = getActivity();
            CharSequence text = "Patient removed successfully!";
            int duration = Toast.LENGTH_SHORT;

            Toast patientRemoved = Toast.makeText(context, text, duration);
            patientRemoved.show();
        }
        else if (buttonId == R.id.noButton) {
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

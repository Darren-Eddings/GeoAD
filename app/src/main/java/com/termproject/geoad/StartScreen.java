package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StartScreen extends Fragment implements View.OnClickListener {

    private Button patientButton;
    private Button caretakerButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_screen, container, false);
        patientButton = view.findViewById(R.id.patientButton);
        caretakerButton = view.findViewById(R.id.caretakerButton);
        patientButton.setOnClickListener(this);
        caretakerButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.patientButton) {
        }
        else if (buttonId == R.id.caretakerButton) {
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

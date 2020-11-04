package com.termproject.geoad;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PatientHomeScreen extends Fragment implements View.OnClickListener{

    private Button mapButton;
    private Button emergencyButton;
    private Button careButton;
    private Button requestButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        mapButton = (Button) view.findViewById(R.id.mapButton);
        emergencyButton = (Button) view.findViewById(R.id.emergencyButton);
        careButton = (Button) view.findViewById(R.id.careButton);
        requestButton = (Button) view.findViewById(R.id.requestButton);
        mapButton.setOnClickListener(this);
        emergencyButton.setOnClickListener(this);
        careButton.setOnClickListener(this);
        requestButton.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {

        Fragment newFragment = null;
        int buttonId = v.getId();

        if (buttonId == R.id.mapButton){

            newFragment = new MapFragment();

        } else if (buttonId == R.id.emergencyButton) {

            //implement call emergency

        } else if (buttonId == R.id.careButton) {

            //implement call caretaker
        } else if (buttonId == R.id.requestButton){

            newFragment = new RequestChangeOrNew();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        try {

            mainActivity.replaceFragments(newFragment);

        } catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}
package com.termproject.geoad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class PatientHomeScreen extends Fragment implements View.OnClickListener{

    String caretakerNum = "tel:5037268713";
    private ImageButton mapButton;
    private ImageButton emergencyButton;
    private ImageButton careButton;
    private ImageButton requestButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        mapButton = view.findViewById(R.id.mapButton);
        emergencyButton = view.findViewById(R.id.emergencyButton);
        careButton = view.findViewById(R.id.careButton);
        requestButton = view.findViewById(R.id.requestButton);
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

            Intent intent = new Intent(getContext(), MapsActivity.class);
            ((MainActivity) getActivity()).startActivity(intent);

        } else if (buttonId == R.id.emergencyButton) {

            //navigates to CallEmergency fragment
            newFragment = new CallEmergency();

        } else if (buttonId == R.id.careButton) {

            //launch phone app to call caretaker
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(caretakerNum));
            startActivity(intent);

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
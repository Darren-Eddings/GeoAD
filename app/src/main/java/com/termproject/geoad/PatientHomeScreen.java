/*
 * Written by Darren Eddings 611442
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment designed as a home screen that allows
 * a patient to navigate through all the functions of
 * the application that they are allowed to access
 */
package com.termproject.geoad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;


public class PatientHomeScreen extends Fragment implements View.OnClickListener{

    //initializes 4 navigation buttons as well as provides the caretakers phone number
    String caretakerNum = "tel:" + "5037268713";
    private ImageButton mapButton;
    private ImageButton emergencyButton;
    private ImageButton careButton;
    private ImageButton requestButton;
    private ImageButton logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        //point the variables to objects in the xml file
        mapButton = view.findViewById(R.id.mapButton);
        emergencyButton = view.findViewById(R.id.emergencyButton);
        careButton = view.findViewById(R.id.careButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        //set all buttons to listen for clicks
        mapButton.setOnClickListener(this);
        emergencyButton.setOnClickListener(this);
        careButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        return view;
    }

    //when a button is clicked
    public void onClick(View v) {

        //create a temporary new fragment that is null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //determine the ID of the button pressed
        if (buttonId == R.id.mapButton){

            //if map button was pressed open the map activity
            //Intent intent = new Intent(getContext(), MapsActivity.class);
            //((MainActivity) getActivity()).startActivity(intent);

            //sets nextFragment equal to GuideMeHome()
            nextFragment = new GuideMeHome();

        } else if (buttonId == R.id.emergencyButton) {

            //if emergency button was pressed set nextFragment to CallEmergency
            nextFragment = new CallEmergency();

        } else if (buttonId == R.id.careButton) {

            //if caretaker button was pressed call the caretaker
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(caretakerNum));
            startActivity(intent);

        }

        //if patient presses logout button nextFragment sends them to logout confirm
        else if (buttonId == R.id.logoutButton){

            nextFragment = new PatientLogoutConfirm();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        try {

            //replace the current fragment in the mainActivity with nextFragment
            mainActivity.replaceFragments(nextFragment);

        }

        //if nextFragment is null throw an exception
        catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}
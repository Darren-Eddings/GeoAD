/*
 * Written by Darren Eddings 611442
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment is designed to create a form
 * that the patient can fill out to request a
 * new geofence and submit it
 */
package com.termproject.geoad;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class RequestNewGeofence extends Fragment implements View.OnClickListener {

    //initialize variable for ImageButton object
    private ImageButton submitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create a view to display submitButton
        View view = inflater.inflate(R.layout.fragment_request_new_geofence, container, false);

        //point variable to object in xml file
        submitButton = view.findViewById(R.id.submitChangeButton);

        //have submitButton listen for tap
        submitButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        //create a new fragment and point it to null
        Fragment newFragment = null;
        int buttonID = v.getId();

        //if the button clicked has the correct ID
        if (buttonID == R.id.submitChangeButton) {

            //open confirmation page
            newFragment = new RequestConfirm();
        }

        //set the current fragment to nextFragment
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(newFragment);
    }
}
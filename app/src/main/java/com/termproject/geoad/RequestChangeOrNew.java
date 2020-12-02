/*
 * Written by Darren Eddings 611442
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment designed to act as a navigation page
 * that allows patient to select to request either
 * a new geofence or a change in an existing
 * geofence
 */
package com.termproject.geoad;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class RequestChangeOrNew extends Fragment implements View.OnClickListener{

    //initialize ImageButton variables
    private ImageButton changeButton;
    private ImageButton newButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create view to display both ImageButtons
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        //point ImageButtons to the ImageButtons in xml file
        changeButton = view.findViewById(R.id.requestChange);
        newButton = view.findViewById(R.id.requestNew);

        //have both objects listen for clicks
        changeButton.setOnClickListener(this);
        newButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        //create new fragment and set it to null
        Fragment newFragment = null;
        int buttonID = v.getId();

        //if ImageButton ID is requestChange
        if (buttonID == R.id.requestChange) {

            //set newFragment to link to the request change fragment
            newFragment = new GeofenceChangeRequest();
        }

        //if ImageButton ID is requestNew
        else if (buttonID == R.id.requestNew) {

            //set newFragment to link to the request new fragment
            newFragment = new RequestNewGeofence();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        try{

            //replace current fragment with newFragment
            mainActivity.replaceFragments(newFragment);
        }

        //throw an error if newFragment == null
        catch(NullPointerException e) {

            e.printStackTrace();
        }

    }
}
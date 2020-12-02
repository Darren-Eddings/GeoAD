/*
  Written by Darren Eddings 611442

  Some Code was automatically generated
  upon fragment creation

  Fragment designed to provide a page that
  confirms that the request has been submitted
  and navigates them back to the home screen
 */
package com.termproject.geoad;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class RequestConfirm extends Fragment implements View.OnClickListener {

    //initialize ImageButton object
    private ImageButton goToHomePage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create view to display objects in
        View view = inflater.inflate(R.layout.fragment_request_confirm, container, false);

        //point goToHomePage to UI object in xml file
        goToHomePage = view.findViewById(R.id.submitConfirmButton);

        //have object listen for a click
        goToHomePage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        //create new fragment and have it point to null
        Fragment newFragment = null;
        int buttonID = v.getId();

        //if the button ID matches this one set newFragment to an existing fragment
        if (buttonID == R.id.submitConfirmButton) {

            newFragment = new PatientHomeScreen();
        }


        //replace current fragment with nextFragment
        MainActivity mainActivity = (MainActivity) getActivity();
        try {

            mainActivity.replaceFragments(newFragment);

            //if newFragment is null
        } catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}
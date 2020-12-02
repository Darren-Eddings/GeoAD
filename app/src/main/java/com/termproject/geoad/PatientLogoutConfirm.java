/*
 * Written by Darren Eddings
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment acts as a confirmation page
 * to ensure that a patient does not
 * accidentally logout
 */
package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PatientLogoutConfirm extends Fragment implements View.OnClickListener {

    //initialize two ImageButton objects
    private ImageButton yesButtonLogout;
    private ImageButton noButtonLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create a page to display UI objects
        View view = inflater.inflate(R.layout.fragment_patient_logout_confirm, container, false);

        //link ImageButton objects to corresponding objects in xml file
        yesButtonLogout = view.findViewById(R.id.yesButtonLogout);
        noButtonLogout = view.findViewById(R.id.noButtonLogout);

        //have both buttons listen for a click
        yesButtonLogout.setOnClickListener(this);
        noButtonLogout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        //when one of the buttons is clicked create a temporary fragment that is equal to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the user pressed the button with ID == yesButton
        if (buttonId == R.id.yesButtonLogout) {

            //set nextFragment equal to specified fragment
            nextFragment = new StartScreen();
        }

        //if the user pressed the button with ID == noButton
        else if (buttonId == R.id.noButtonLogout) {

            //set nextFragment to equal the specified fragment
            nextFragment = new PatientHomeScreen();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //if nextFragment isn't null replace current fragment in main activity with nextFragment
        try {

            mainActivity.replaceFragments(nextFragment);
        }

        //if nextFragment is null throw an error
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
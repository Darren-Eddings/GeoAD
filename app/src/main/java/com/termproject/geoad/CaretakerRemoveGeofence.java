/*
 * Written by Gideon Woo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Creates a fragment that confirms whether
 * or not a caretaker wishes to remove an
 * existing geofence
 */
package com.termproject.geoad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class CaretakerRemoveGeofence extends Fragment implements View.OnClickListener {

    //initialize UI object variables
    private ImageButton yesButton;
    private ImageButton noButton;

    //initialize ID string variable
    private String fenceID;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create view
        View view = inflater.inflate(R.layout.fragment_caretaker_remove_geofence, container, false);

        //connect variables to existing xml component
        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);

        //have each button listen for a click
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        //set fenceID
        String[] str = new String[1000000];
        int arrayIndex = 0;
        FileInputStream fIn = null;

        //fetch file for an existing geofence
        try {

            fIn = new FileInputStream(new File (getActivity().getFilesDir() + "/CaretakerGeofenceList.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
        }
        //if no such file exists throw an exception
        catch (FileNotFoundException e) {

            FileOutputStream fOut = null;

            try {

                fOut = new FileOutputStream(new File(getActivity().getFilesDir() + "/CaretakerGeofenceList.txt"));
                OutputStreamWriter osw = new OutputStreamWriter(fOut);

                try {
                    osw.write("");
                    osw.close();
                    fOut.close();

                } catch (IOException ioException) {

                    ioException.printStackTrace();
                }
            }

            catch (FileNotFoundException fileNotFoundException) {

                fileNotFoundException.printStackTrace();
            }
        }

        try {

            fIn = new FileInputStream(new File (getActivity().getFilesDir() + "/CaretakerGeofenceList.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(isr);

            while (reader.ready()) {

                String line = reader.readLine();
                String[] splitLine = line.split(",");
                str [arrayIndex] = splitLine[0];
                arrayIndex ++;
            }

            reader.close();
            isr.close();
            fIn.close();
        }

        catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        int index = 0;
        String[] just = new String[arrayIndex];
        while (str [index] != null){
            just[index] = str[index];
            index++;
        }

        Spinner spinner = new Spinner(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                just);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) view.findViewById(R.id.geofenceIDs);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               fenceID = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.yesButton) {

            //remove geofence
            ((CaretakerMapActivity)getContext()).fenceListRemove(fenceID);
            ((CaretakerMapActivity) getContext()).fenceUpdate();
            getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
            nextFragment = new CaretakerManageGeofence();

            //create fields for a toast
            Context context = getActivity();
            CharSequence text = "Geofence removed successfully!";
            int duration = Toast.LENGTH_SHORT;

            //display the toast
            Toast geofenceRemoved = Toast.makeText(context, text, duration);
            geofenceRemoved.show();
        }
        else if (buttonId == R.id.noButton) {

            nextFragment = new CaretakerManageGeofence();
            //getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //replace current fragment with next fragment
        try {

            mainActivity.replaceFragments(nextFragment);
        }

        //if nextFragment is null throw an exception
        catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}

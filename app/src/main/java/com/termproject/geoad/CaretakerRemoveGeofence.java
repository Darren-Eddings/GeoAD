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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        //if no such file exists create one
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
        //Opening the file again
        try {

            fIn = new FileInputStream(new File (getActivity().getFilesDir() + "/CaretakerGeofenceList.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(isr);

            while (reader.ready()) {
                //Reading in each line, saving geofence names
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
        //Converting to array with just the right amount of entries for names
        int index = 0;
        String[] just = new String[arrayIndex];
        while (str [index] != null){
            just[index] = str[index];
            index++;
        }
        //Creating the spinner filled with geofence names from the array
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                R.layout.color_spinner_layout, just);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_color);

        spinner.setAdapter(adapter);
        //Grabbing Geofence name from the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Setting the fenceID to the one wanting to be removed
            fenceID = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

        return view;
    }

    @Override public void onClick(View v) {
        //If the yes button is clicked, remove the geofence chosen from the spinner
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.yesButton) {
            //nextFragment = new CaretakerManageGeofence();
            //Removes the fence from the file calling the function from that activity
            ((CaretakerMapActivity) getContext()).fenceListRemove(fenceID);
            //Updating the drawn geofences with function call
            ((CaretakerMapActivity) getContext()).fenceUpdate();
            //Killing this fragment
            getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
            /**Context context = getActivity();
             CharSequence text = "Geofence removed successfully!";
             int duration = Toast.LENGTH_SHORT;

             Toast geofenceRemoved = Toast.makeText(context, text, duration);
             geofenceRemoved.show();**/
        }
        else if (buttonId == R.id.noButton) {
            //Killing this fragment
            getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
        }
        /**MainActivity mainActivity = (MainActivity) getActivity();
         try {
         mainActivity.replaceFragments(nextFragment);
         }catch (NullPointerException e) {
         e.printStackTrace();
         }**/
    }
}

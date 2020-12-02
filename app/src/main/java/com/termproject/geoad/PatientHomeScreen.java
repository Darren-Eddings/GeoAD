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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class PatientHomeScreen extends Fragment implements View.OnClickListener{
    private PatientViewModel viewModel;

    private FirebaseFirestore db;

    //initializes 4 navigation buttons as well as provides the caretakers phone number
    String caretakerNum = "tel:" + "5037268713";
    private ImageButton mapButton;
    private ImageButton emergencyButton;
    private ImageButton careButton;
    private ImageButton requestButton;
    private ImageButton logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        db = FirebaseFirestore.getInstance();

        File file = new File(getActivity().getFilesDir() + "/CaretakerGeofenceList.txt");
        FileOutputStream fr = null;
        try{
            fr = new FileOutputStream(file, false);
            OutputStreamWriter writer = new OutputStreamWriter(fr);

            if (viewModel.getPatient().getGeofence() == "") {
                writer.write("");
            }
            else {
                String[] lines = viewModel.getPatient().getGeofence().split("\n");
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            }

            writer.close();
            fr.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(new File(getActivity().getFilesDir() + "/MyLocation.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(isr);
            String patientLocation = reader.readLine();

            DocumentReference patientRef = db.collection("patients").document(viewModel.getPatient().getFullName());
            patientRef
                    .update("location", patientLocation)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override public void onSuccess(Void aVoid) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fInSession = null;
        try {
            File fileSession = new File(getActivity().getFilesDir() + "/Session.txt");
            fInSession = new FileInputStream(fileSession);
            InputStreamReader isr = new InputStreamReader(fInSession);
        } catch (FileNotFoundException e) {
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(new File(getActivity().getFilesDir() + "/Session.txt"));
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                try {
                    osw.write("Patient" + "," + viewModel.getPatient().getPatientID() + "," + viewModel.getPatient().getPassword());
                    osw.close();
                    fOut.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }

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
            Intent intent = new Intent(getContext(), GuideMeHome.class);
            ((MainActivity) getActivity()).startActivity(intent);

            //sets nextFragment equal to GuideMeHome()
            //nextFragment = new GuideMeHome();
            //Uri gmmIntentUri = Uri.parse("google.streetview:home");

            //Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            //mapIntent.setPackage("com.google.android.apps.maps");

           // startActivity(mapIntent);

        } else if (buttonId == R.id.emergencyButton) {

            //if emergency button was pressed set nextFragment to CallEmergency
            nextFragment = new CallEmergency();

        } else if (buttonId == R.id.careButton) {

            //if caretaker button was pressed call the caretaker
            if (viewModel.getPatient().getCaretakerID() != "") {
                if (viewModel.getCaretakerNum() == "") {
                    Query caretakerNumQuery = db.collection("caretakers")
                            .whereEqualTo("caretakerID", viewModel.getPatient().getCaretakerID());

                    executeCaretakerNumQuery(caretakerNumQuery, new SimpleCallback<String>() {
                        @Override
                        public void callback(String caretakerNum) {
                            viewModel.setCaretakerNum("tel:" + caretakerNum);

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(viewModel.getCaretakerNum()));
                            startActivity(intent);
                        }
                    });
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(viewModel.getCaretakerNum()));
                    startActivity(intent);
                }
            } else {
                Context context = getActivity();
                CharSequence text = "You don't have a caretaker yet!";
                int duration = Toast.LENGTH_SHORT;

                Toast noCaretaker = Toast.makeText(context, text, duration);
                noCaretaker.show();
            }
        } else if (buttonId == R.id.logoutButton) {
            FileInputStream fIn = null;
            try {
                File file = new File(getActivity().getFilesDir() + "/Session.txt");
                fIn = new FileInputStream(file);
                file.delete();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            viewModel.clearAll();
            nextFragment = new StartScreen();
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

    private void executeCaretakerNumQuery(Query caretakerNumQuery, @NonNull SimpleCallback<String> finishedCallback) {
        caretakerNumQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            String caretakerNum = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                caretakerNum = document.get("phone").toString();
                            }
                            finishedCallback.callback(caretakerNum);
                        }
                    }
                });
    }

    public interface SimpleCallback<T> {
        void callback(T data);
    }
}
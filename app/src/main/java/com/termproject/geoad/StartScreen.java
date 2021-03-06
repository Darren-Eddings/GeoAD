/*
 * Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated upon fragment
 * creation
 *
 * Fragment is used to navigate from start up screen to
 * login page of either the patient or caretaker
 */
package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartScreen extends Fragment implements View.OnClickListener {
    private PatientViewModel viewModelPatient;
    private CaretakerViewModel viewModelCaretaker;

    private FirebaseFirestore db;

    //initialize variable for UI objects
    private Button patientButton;
    private Button caretakerButton;
    private Query session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        FileInputStream fIn = null;
        try {
            File fileSession = new File(getActivity().getFilesDir() + "/Session.txt");
            fIn = new FileInputStream(fileSession);
            InputStreamReader isr = new InputStreamReader(fIn);
            try {
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                String[] credentials = line.split(",");

                if(credentials[0].equals("Patient")) {
                    session = db.collection("patients")
                            .whereEqualTo("patientID", credentials[1])
                            .whereEqualTo("password", credentials[2]);

                    viewModelPatient = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

                    session.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot snapshot = task.getResult();
                                        if (!snapshot.isEmpty()) {
                                            for (QueryDocumentSnapshot document: snapshot) {
                                                Patient patient1 = document.toObject(Patient.class);
                                                viewModelPatient.setPatient(patient1);
                                            }
                                            Context context = getActivity();
                                            CharSequence text = "Login Successful!";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast loginSuccessful = Toast.makeText(context, text, duration);
                                            loginSuccessful.show();

                                            MainActivity mainActivity = (MainActivity) getActivity();
                                            try {
                                                mainActivity.replaceFragments(new PatientHomeScreen());
                                            }catch (NullPointerException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            Context context = getActivity();
                                            CharSequence text = "Session data error...";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast loginFailed = Toast.makeText(context, text, duration);
                                            loginFailed.show();
                                        }
                                    }
                                }
                            });
                }
                else if(credentials[0].equals("Caretaker")) {
                    session = db.collection("caretakers")
                        .whereEqualTo("caretakerID", credentials[1])
                        .whereEqualTo("password", credentials[2]);

                    viewModelCaretaker = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

                    session.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot snapshot = task.getResult();
                                        if (!snapshot.isEmpty()) {
                                            for (QueryDocumentSnapshot document: snapshot) {
                                                Caretaker caretaker1 = document.toObject(Caretaker.class);
                                                viewModelCaretaker.setCaretaker(caretaker1);
                                            }
                                            Context context = getActivity();
                                            CharSequence text = "Login Successful!";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast loginSuccessful = Toast.makeText(context, text, duration);
                                            loginSuccessful.show();

                                            MainActivity mainActivity = (MainActivity) getActivity();
                                            try {
                                                mainActivity.replaceFragments(new CaretakerPatientList());
                                            }catch (NullPointerException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            Context context = getActivity();
                                            CharSequence text = "Session data error...";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast loginFailed = Toast.makeText(context, text, duration);
                                            loginFailed.show();
                                        }
                                    }
                                }
                            });
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        //create a view to display UI objects
        View view = inflater.inflate(R.layout.fragment_start_screen, container, false);

        //connect variable to objects
        patientButton = view.findViewById(R.id.patientButton);
        caretakerButton = view.findViewById(R.id.caretakerButton);

        //have UI object listen for tap
        patientButton.setOnClickListener(this);
        caretakerButton.setOnClickListener(this);
        return view;
    }


    @Override
    //when an object is tapped
    public void onClick(View v) {

        //create a new fragment and set it to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the object tapped has id patientButton set nextFragment to PatientLogin()
        if (buttonId == R.id.patientButton) {
            nextFragment = new PatientLogin();
        }

        //if the object tapped has id caretakerButton set nextFragment t0 CaretakerLogin()
        else if (buttonId == R.id.caretakerButton) {
            nextFragment = new CaretakerLogin();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        //replace current fragment with nextFragment
        try {

            mainActivity.replaceFragments(nextFragment);
        }

        //if nextFragment is null when clicked throw an exception
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

/*
 * Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some Code has been automatically generated
 * upon fragment creation
 *
 * Fragment is responsible for displaying a list
 * of patients and allowing caretaker to select
 * a patient to view or add a new patient
 */
package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CaretakerPatientList extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private FirebaseFirestore db;

    //initialize UI object to be displayed on the page
    private ArrayAdapter<CharSequence> patientListAdapter;

    private ImageButton addPatient;
    private ImageButton logout;
    private ListView patientList;

    private Caretaker caretaker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

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
                    osw.write("Caretaker" + "," + viewModel.getCaretaker().getCaretakerID() + "," + viewModel.getCaretaker().getPassword());
                    osw.close();
                    fOut.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
        //creates a view to display UI objects

        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);
        caretaker = viewModel.getCaretaker();

        Query patientQuery = db.collection("patients")
                .whereEqualTo("caretakerID", caretaker.getCaretakerID());

        View view = inflater.inflate(R.layout.fragment_caretaker_patient_list, container, false);

        //point variables to xml files required for showing them on the page
        patientListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        patientList = view.findViewById(R.id.patientList);
        logout = view.findViewById(R.id.logoutButton);

        //set ListView as an adapter
        patientList.setAdapter(patientListAdapter);

        //have add patient list listen for click
        patientList.setOnItemClickListener(patientListClick);

        //point to button at an ID and have it listen for clicks

        executePatientQuery(patientQuery, new SimpleCallback<ArrayList<Patient>>() {
            @Override
            public void callback(ArrayList<Patient> patientList1) {
                viewModel.setPatientList(patientList1);

                patientListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

                for (Patient patient : viewModel.getPatientList()) {
                    patientListAdapter.add(patient.getFullName());

                    patientList = view.findViewById(R.id.patientList);
                    patientList.setAdapter(patientListAdapter);
                    patientList.setOnItemClickListener(patientListClick);
                }
            }
        });

        addPatient = view.findViewById(R.id.addPatientButton);
        logout = view.findViewById(R.id.logoutButton);

        addPatient.setOnClickListener(this);
        logout.setOnClickListener(this);

        return view;
    }

    //when an item in the ListView is clicked
    private AdapterView.OnItemClickListener patientListClick = (parent, v, position, id) -> {

        //initialize a temporary fragment
        String patientName = (String) patientList.getItemAtPosition(position);
        for (Patient patient : viewModel.getPatientList()) {
            if (patient.getFullName().equals(patientName)) {
                viewModel.setSelectedPatient(patient);
            }
        }

        Fragment nextFragment = new CaretakerHomeScreen();

        //set current fragment in the main activity to nextFragment
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    };

    @Override public void onClick (View v) {

        //when the add patient button is clicked create a temporary fragment and set it to null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //if the button id is correct
        if (buttonId == R.id.addPatientButton) {

            //set the temporary fragment to equal  the CaretakerAddPatient fragment
            nextFragment = new CaretakerAddPatient();
        }
        else if (buttonId == R.id.logoutButton){
            FileInputStream fIn = null;
            try {
                File file = new File(getActivity().getFilesDir() + "/Session.txt");
                fIn = new FileInputStream(file);
                file.delete();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            nextFragment = new StartScreen();
            viewModel.clearAll();
        }
        MainActivity mainActivity = (MainActivity) getActivity();

        //replace current fragment in the main activity with nextFragment
        try {
            mainActivity.replaceFragments(nextFragment);

        }

        //throw an exception if nextFragment == null on click
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void executePatientQuery(Query patientQuery, @NonNull SimpleCallback<ArrayList<Patient>> finishedCallback) {
        patientQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<Patient> temp = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Patient patient = document.toObject(Patient.class);
                                temp.add(patient);
                            }
                            finishedCallback.callback(temp);
                        }
                    }
                });
    }

    public interface SimpleCallback<T> {
        void callback(T data);
    }
}

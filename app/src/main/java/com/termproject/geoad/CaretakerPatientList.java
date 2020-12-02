package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;

public class CaretakerPatientList extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private FirebaseFirestore db;

    private ArrayAdapter<CharSequence> patientListAdapter;
    private ListView patientListView;
    private Button addPatient;
    private Button logout;

    private Caretaker caretaker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);
        caretaker = viewModel.getCaretaker();

        Query patientQuery = db.collection("patients")
                .whereEqualTo("caretakerID", caretaker.getCaretakerID());

        View view = inflater.inflate(R.layout.fragment_caretaker_patient_list, container, false);

        executePatientQuery(patientQuery, new SimpleCallback<ArrayList<Patient>>() {
            @Override
            public void callback(ArrayList<Patient> patientList) {
                viewModel.setPatientList(patientList);

                patientListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

                for (Patient patient : viewModel.getPatientList()) {
                    patientListAdapter.add(patient.getFullName());

                    patientListView = view.findViewById(R.id.patientList);
                    patientListView.setAdapter(patientListAdapter);
                    patientListView.setOnItemClickListener(patientListClick);
                }
            }
        });

        addPatient = view.findViewById(R.id.addPatientButton);
        logout = view.findViewById(R.id.logoutButton);

        addPatient.setOnClickListener(this);
        logout.setOnClickListener(this);

        return view;
    }

    private AdapterView.OnItemClickListener patientListClick = (parent, v, position, id) -> {
        String patientName = (String) patientListView.getItemAtPosition(position);
        for (Patient patient : viewModel.getPatientList()) {
            if (patient.getFullName().equals(patientName)) {
                viewModel.setSelectedPatient(patient);
            }
        }

        Fragment nextFragment = new CaretakerHomeScreen();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    };

    @Override public void onClick (View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.addPatientButton) {
            nextFragment = new CaretakerAddPatient();
        }
        else if (buttonId == R.id.logoutButton){
            nextFragment = new StartScreen();
            viewModel.clearAll();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
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

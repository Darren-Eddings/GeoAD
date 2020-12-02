package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CaretakerAddPatient extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private FirebaseFirestore db;

    private Button add;
    private Button cancel;
    private EditText patientId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_caretaker_add_patient, container, false);

        add = view.findViewById(R.id.addButton);
        cancel = view.findViewById(R.id.cancelButton);
        patientId = view.findViewById(R.id.patientIdTextBox);

        add.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.addButton) {
            Query patientQuery = db.collection("patients")
                    .whereEqualTo("patientID", patientId.getText().toString());

            executePatientQuery(patientQuery, new SimpleCallback() {
                @Override
                public void callback() {

                    MainActivity mainActivity = (MainActivity) getActivity();
                    try {
                        mainActivity.replaceFragments(new CaretakerPatientList());
                    }catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        else if (buttonId == R.id.cancelButton) {
            nextFragment = new CaretakerPatientList();
            MainActivity mainActivity = (MainActivity) getActivity();
            try {
                mainActivity.replaceFragments(nextFragment);
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void executePatientQuery(Query patientQuery, @NonNull SimpleCallback finishedCallback) {
        patientQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (!snapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : snapshot) {
                                    DocumentReference patientRef = db.collection("patients").document(document.get("fullName").toString());
                                    patientRef
                                            .update("caretakerID", viewModel.getCaretaker().getCaretakerID())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override public void onSuccess(Void aVoid) {
                                                    Context context = getActivity();
                                                    CharSequence text = "Patient Added Successfully!";
                                                    int duration = Toast.LENGTH_SHORT;

                                                    Toast patientAdded = Toast.makeText(context, text, duration);
                                                    patientAdded.show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Context context = getActivity();
                                                    CharSequence text = "Patient does not exist!";
                                                    int duration = Toast.LENGTH_SHORT;

                                                    Toast patientAdded = Toast.makeText(context, text, duration);
                                                    patientAdded.show();
                                                }
                                            });
                                }
                                finishedCallback.callback();
                            }
                        }
                    }
                });
    }

    public interface SimpleCallback {
        void callback();
    }
}

/*
 *Written by Kieffer Liestyo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment is designed to create a page
 * where the caretaker can enter patient
 * info and add them to their patient list
 * or cancel and return to their list
 * without adding the patient
 */
package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
    //Initialize the ViewModel to be shared
    private CaretakerViewModel viewModel;

    //Initializes the FireStore database
    private FirebaseFirestore db;

    //initialize two ImageButton objects
    private ImageButton add;
    private ImageButton cancel;
    private EditText patientId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Instantiate the viewModel variable, receiving any data existing in it
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        //Gets a connection to the database
        db = FirebaseFirestore.getInstance();

        //Sets up the view to be displayed on the app
        View view = inflater.inflate(R.layout.fragment_caretaker_add_patient, container, false);

        //Links the variables to the appropriate view objects based on id
        add = view.findViewById(R.id.addButton);
        cancel = view.findViewById(R.id.cancelButton);
        patientId = view.findViewById(R.id.patientIdTextBox);

        //Adds listeners to the two buttons
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //Returns and displays the view
        return view;
    }

    @Override public void onClick(View v) {
        //Initializes the variable to navigate to the next fragment
        Fragment nextFragment = null;

        //Retrieves id of the button that was clicked
        int buttonId = v.getId();

        //If else statements to do commands and navigate to the appropriate fragments based on the button that was clicked
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
        }

        //Replaces old fragment with the new one
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //Callback function to retrieve the data from the database query, making sure that the data is used after it is received
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
                            else {
                                Context context = getActivity();
                                CharSequence text = "Patient does not exist!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast patientAdded = Toast.makeText(context, text, duration);
                                patientAdded.show();
                            }
                        }

                    }
                });
    }

    //Interface to be used with the callback function
    public interface SimpleCallback {
        void callback();
    }
}

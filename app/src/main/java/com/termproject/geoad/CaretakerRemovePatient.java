/*
 * Written by Kieffer Lystoyo
 *
 * Commented by Darren Eddings
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment acts as a confirmation page
 * to ensure that a caretaker does not
 * accidentally remove a patient
 */
package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class CaretakerRemovePatient extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private FirebaseFirestore db;

    //initialize two ImageButton objects
    private ImageButton yesButton;
    private ImageButton noButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        db = FirebaseFirestore.getInstance();

        //create a page to display UI objects

        View view = inflater.inflate(R.layout.fragment_caretaker_remove_patient, container, false);

        //link ImageButton objects to corresponding objects in xml file
        yesButton = view.findViewById(R.id.yesButtonRemove);
        noButton = view.findViewById(R.id.noButtonRemove);

        //have both buttons listen for a click
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        //when one of the buttons is clicked create a temporary fragment that is equal to null
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.yesButtonRemove) {
            DocumentReference patientRef = db.collection("patients").document(viewModel.getSelectedPatient().getFullName());
            patientRef
                    .update("caretakerID", FieldValue.delete())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override public void onSuccess(Void aVoid) {
                            Context context = getActivity();
                            CharSequence text = "Patient removed successfully!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast patientRemoved = Toast.makeText(context, text, duration);
                            patientRemoved.show();

                            MainActivity mainActivity = (MainActivity) getActivity();
                            try {
                                mainActivity.replaceFragments(new CaretakerPatientList());
                            }catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        //if the user pressed the button with ID == noButton
        else if (buttonId == R.id.noButtonRemove) {

            //set nextFragment to equal the CaretakerHomeScreen fragment
            nextFragment = new CaretakerHomeScreen();
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

package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private Button yesButton;
    private Button noButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_caretaker_remove_patient, container, false);

        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);

        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.yesButton) {
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
        else if (buttonId == R.id.noButton) {
            nextFragment = new CaretakerHomeScreen();
            MainActivity mainActivity = (MainActivity) getActivity();
            try {
                mainActivity.replaceFragments(nextFragment);
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }
}

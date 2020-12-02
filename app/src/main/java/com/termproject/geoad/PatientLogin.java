package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PatientLogin extends Fragment implements View.OnClickListener{
    private PatientViewModel viewModel;

    private FirebaseFirestore db;

    private final String TAG = "PatientLogin";

    private Button loginButton;
    private TextView goToRegister;
    private EditText patientId;
    private EditText password;

    private String patientIdValue;
    private String passwordValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_patient_login, container, false);

        loginButton = (Button) view.findViewById(R.id.patientLoginButton);
        goToRegister = view.findViewById(R.id.linkRegister);
        patientId = view.findViewById(R.id.idTextBox);
        password = view.findViewById(R.id.passwordTextBox);

        loginButton.setOnClickListener(this);
        goToRegister.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = null;
        int buttonID = v.getId();
        if (buttonID == R.id.linkRegister) {
            newFragment = new PatientRegister();
            MainActivity mainActivity = (MainActivity) getActivity();
            try {
                mainActivity.replaceFragments(newFragment);
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else if (buttonID == R.id.patientLoginButton) {
            patientIdValue = patientId.getText().toString();
            passwordValue = password.getText().toString();

            Query patient = db.collection("patients")
                    .whereEqualTo("patientID", patientIdValue)
                    .whereEqualTo("password", passwordValue);

            patient.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (!snapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : snapshot) {
                                    Patient patient1 = document.toObject(Patient.class);
                                    viewModel.setPatient(patient1);
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
                                CharSequence text = "Login Failed...";
                                int duration = Toast.LENGTH_SHORT;

                                Toast loginFailed = Toast.makeText(context, text, duration);
                                loginFailed.show();
                            }
                        }
                    });
        }
    }
}
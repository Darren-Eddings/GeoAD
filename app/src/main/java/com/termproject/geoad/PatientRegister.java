/*
 * Written by Darren Eddings 611442
 *
 * Database Impelemtation by Kieffer Liestyo
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment is designed to
 */
package com.termproject.geoad;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Random;

public class PatientRegister extends Fragment implements View.OnClickListener{
    private PatientViewModel viewModel;

    private FirebaseFirestore db;

    private final String TAG = "PatientRegister";

    private DatePickerDialog picker;
    private Button submitButton;
    private TextView goToLogin;
    private EditText fullName;
    private EditText dateOfBirth;
    private EditText phone;
    private EditText password;
    private EditText address;
    private Patient patient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        viewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        View view = inflater.inflate(R.layout.fragment_patient_register, container, false);

        submitButton = view.findViewById(R.id.accountRegisterButton);
        goToLogin = view.findViewById(R.id.linkLogin);
        fullName = view.findViewById(R.id.fullNameTextBox);
        dateOfBirth = view.findViewById(R.id.dateOfBirthTextBox);
        phone = view.findViewById(R.id.newPhoneTextBox);
        password = view.findViewById(R.id.passwordTextBox);
        address = view.findViewById(R.id.addressTextBox);

        submitButton.setOnClickListener(this);
        goToLogin.setOnClickListener(this);

        dateOfBirth.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            picker = new DatePickerDialog(getActivity(),
                    (view1, year1, monthOfYear, dayOfMonth) -> dateOfBirth.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year1), year, month, day);
            picker.show();
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Random rnd = new Random();
        int patientID = 100000 + rnd.nextInt(900000);

        patient = new Patient(address.getText().toString(), null, dateOfBirth.getText().toString(), fullName.getText().toString(), password.getText().toString(), Integer.toString(patientID), phone.getText().toString());

        viewModel.setPatient(patient);

        CollectionReference patients = db.collection("patients");

        patients.document(fullName.getText().toString())
                .set(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Context context = getActivity();
                        CharSequence text = "Registration Successful!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast registrationSuccessful = Toast.makeText(context, text, duration);
                        registrationSuccessful.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Context context = getActivity();
                        CharSequence text = "Error Adding Document!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast registrationFail = Toast.makeText(context, text, duration);
                        registrationFail.show();
                    }
                });

        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.linkLogin) {

            newFragment = new PatientLogin();

        } else if (buttonID == R.id.accountRegisterButton) {

            newFragment = new NewAccountSuccess();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        try {

            mainActivity.replaceFragments(newFragment);

        }catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}
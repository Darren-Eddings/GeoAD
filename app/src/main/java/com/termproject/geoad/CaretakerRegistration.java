package com.termproject.geoad;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CaretakerRegistration extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;

    private static final String TAG = "CaretakerRegistration";

    private DatePickerDialog picker;
    private Button registerButton;
    private TextView loginHere;
    private EditText fullName;
    private EditText dateOfBirth;
    private EditText phone;
    private EditText password;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_caretaker_registration, container, false);

        registerButton = view.findViewById(R.id.caretakerRegistrationButton);
        loginHere = view.findViewById(R.id.inkLogin);
        fullName = view.findViewById(R.id.fullNameTextBox);
        dateOfBirth = view.findViewById(R.id.dateOfBirthTextBox);
        phone = view.findViewById(R.id.phoneTextBox);
        password = view.findViewById(R.id.passwordTextBox);

        registerButton.setOnClickListener(this);
        loginHere.setOnClickListener(this);

        dateOfBirth.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            picker = new DatePickerDialog(getActivity(),
                    (view1, year1, monthOfYear, dayOfMonth) -> dateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        return view;
    }

    @Override public void onClick(View v) {
        CollectionReference caretakers = db.collection("caretakers");

        Map<String, Object> caretaker = new HashMap<>();
        caretaker.put("fullName", fullName.getText().toString());
        caretaker.put("dateOfBirth", dateOfBirth.getText().toString());
        caretaker.put("phone", phone.getText().toString());
        caretaker.put("password", password.getText().toString());

        caretakers.document(fullName.getText().toString())
                .set(caretaker)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "Document saved!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document.", e));

        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.caretakerRegistrationButton) {
            nextFragment = new CaretakerRegistrationSuccess();

            Context context = getActivity();
            CharSequence text = "Registration Successful!";
            int duration = Toast.LENGTH_SHORT;

            Toast registrationSuccessful = Toast.makeText(context, text, duration);
            registrationSuccessful.show();
        }
        else if (buttonId == R.id.inkLogin) {
            nextFragment = new CaretakerLogin();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

package com.termproject.geoad;

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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CaretakerRegistration extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;

    private static final String TAG = "CaretakerRegistration";

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

        return view;
    }

    @Override public void onClick(View v) {
        Map<String, Object> caretaker = new HashMap<>();
        caretaker.put("fullName", fullName.getText());
        caretaker.put("dateOfBirth", dateOfBirth.getText());
        caretaker.put("phone", phone.getText());
        caretaker.put("password", password.getText());

        db.collection("caretakers")
                .add(caretaker)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

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

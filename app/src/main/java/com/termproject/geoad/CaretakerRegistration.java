package com.termproject.geoad;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

public class CaretakerRegistration extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private FirebaseFirestore db;

    private DatePickerDialog picker;
    private Button registerButton;
    private TextView loginHere;
    private EditText fullName;
    private EditText dateOfBirth;
    private EditText phone;
    private EditText password;
    private Caretaker caretaker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

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
                    (view1, year1, monthOfYear, dayOfMonth) -> dateOfBirth.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year1), year, month, day);
            picker.show();
        });

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();

        if (buttonId == R.id.caretakerRegistrationButton) {

            Random rnd = new Random();
            int caretakerID = 100000 + rnd.nextInt(900000);

            caretaker = new Caretaker(Integer.toString(caretakerID), fullName.getText().toString(), dateOfBirth.getText().toString(), password.getText().toString(), phone.getText().toString());

            viewModel.setCaretaker(caretaker);

            CollectionReference caretakers = db.collection("caretakers");
            if (!fullName.getText().toString().isEmpty()) {
                caretakers.document(fullName.getText().toString())
                        .set(caretaker)
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
                nextFragment = new CaretakerRegistrationSuccess();
                MainActivity mainActivity = (MainActivity) getActivity();
                try {
                    mainActivity.replaceFragments(nextFragment);
                }catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            else {
                Context context = getActivity();
                CharSequence text = "Some fields are left blank!!";
                int duration = Toast.LENGTH_SHORT;

                Toast blankError = Toast.makeText(context, text, duration);
                blankError.show();
            }

        }
        else if (buttonId == R.id.inkLogin) {
            nextFragment = new CaretakerLogin();
            MainActivity mainActivity = (MainActivity) getActivity();
            try {
                mainActivity.replaceFragments(nextFragment);
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.termproject.geoad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CaretakerLogin extends Fragment implements View.OnClickListener{
    private CaretakerViewModel viewModel;

    private FirebaseFirestore db;

    private final String TAG = "CaretakerLogin";

    private Button loginButton;
    private TextView registerHere;
    private EditText caretakerId;
    private EditText password;

    private String caretakerIdValue;
    private String passwordValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        View view = inflater.inflate(R.layout.fragment_caretaker_login, container, false);

        loginButton = view.findViewById(R.id.caretakerLoginButton);
        registerHere = view.findViewById(R.id.inkRegister);
        caretakerId = view.findViewById(R.id.caretakerIdTextBox);
        password = view.findViewById(R.id.passwordTextBox);

        loginButton.setOnClickListener(this);
        registerHere.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.caretakerLoginButton) {
            caretakerIdValue =  caretakerId.getText().toString();
            passwordValue = password.getText().toString();

            Query caretaker = db.collection("caretakers")
                    .whereEqualTo("caretakerID", caretakerIdValue)
                    .whereEqualTo("password", passwordValue);

            caretaker.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (!snapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : snapshot) {
                                    Caretaker caretaker1 = document.toObject(Caretaker.class);
                                    viewModel.setCaretaker(caretaker1);
                                }
                                Context context = getActivity();
                                CharSequence text = "Login Successful!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast loginSuccessful = Toast.makeText(context, text, duration);
                                loginSuccessful.show();

                                MainActivity mainActivity = (MainActivity) getActivity();
                                try {
                                    mainActivity.replaceFragments(new CaretakerPatientList());
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
        else if (buttonId == R.id.inkRegister) {
            nextFragment = new CaretakerRegistration();
            MainActivity mainActivity = (MainActivity) getActivity();
            try {
                mainActivity.replaceFragments(nextFragment);
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }
}

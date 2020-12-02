/*
 * Written by Darren Eddings 611442
 *
 * Some code was automatically generated
 * upon fragment creation
 *
 * Fragment designed as a home screen that allows
 * a patient to navigate through all the functions of
 * the application that they are allowed to access
 */
package com.termproject.geoad;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PatientHomeScreen extends Fragment implements View.OnClickListener{
    private PatientViewModel viewModel;

    private FirebaseFirestore db;

    //initializes 4 navigation buttons as well as provides the caretakers phone number
    private ImageButton mapButton;
    private ImageButton emergencyButton;
    private ImageButton careButton;
    private ImageButton requestButton;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

        db = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        //point the variables to objects in the xml file
        mapButton = view.findViewById(R.id.mapButton);
        emergencyButton = view.findViewById(R.id.emergencyButton);
        careButton = view.findViewById(R.id.careButton);
        requestButton = view.findViewById(R.id.requestButton);

        logoutButton = view.findViewById(R.id.logoutButton);

        //set all buttons to listen for clicks
        mapButton.setOnClickListener(this);
        emergencyButton.setOnClickListener(this);
        careButton.setOnClickListener(this);
        requestButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        return view;
    }

    //when a button is clicked
    public void onClick(View v) {

        //create a temporary new fragment that is null
        Fragment nextFragment = null;
        int buttonId = v.getId();

        //determine the ID of the button pressed
        if (buttonId == R.id.mapButton){

            //if map button was pressed open the map activity
            Intent intent = new Intent(getContext(), CaretakerMapActivity.class);
            ((MainActivity) getActivity()).startActivity(intent);

            //sets nextFragment equal to GuideMeHome()
            //nextFragment = new GuideMeHome();

        } else if (buttonId == R.id.emergencyButton) {

            //if emergency button was pressed set nextFragment to CallEmergency
            nextFragment = new CallEmergency();

        } else if (buttonId == R.id.careButton) {

            //if caretaker button was pressed call the caretaker
            if(viewModel.getPatient().getCaretakerID() != "") {
                if(viewModel.getCaretakerNum() == "") {
                    Query caretakerNumQuery = db.collection("caretakers")
                            .whereEqualTo("caretakerID", viewModel.getPatient().getCaretakerID());

                    executeCaretakerNumQuery(caretakerNumQuery, new SimpleCallback<String>() {
                        @Override
                        public void callback(String caretakerNum) {
                            viewModel.setCaretakerNum("tel:" + caretakerNum);

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(viewModel.getCaretakerNum()));
                            startActivity(intent);
                        }
                    });
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(viewModel.getCaretakerNum()));
                    startActivity(intent);
                }
            }
            else {
                Context context = getActivity();
                CharSequence text = "You don't have a caretaker yet!";
                int duration = Toast.LENGTH_SHORT;

                Toast noCaretaker = Toast.makeText(context, text, duration);
                noCaretaker.show();
            }
        } else if (buttonId == R.id.requestButton){

            //if request button was pressed set nextFragment to the requests page
            nextFragment = new RequestChangeOrNew();
        }
        else if (buttonId == R.id.logoutButton) {
            viewModel.clearAll();
            nextFragment = new StartScreen();
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        try {

            //replace the current fragment in the mainActivity with nextFragment
            mainActivity.replaceFragments(nextFragment);

        }

        //if nextFragment is null throw an exception
        catch (NullPointerException e) {

            e.printStackTrace();
        }
    }

    private void executeCaretakerNumQuery(Query caretakerNumQuery, @NonNull SimpleCallback<String> finishedCallback) {
        caretakerNumQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            String caretakerNum = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                caretakerNum = document.get("phone").toString();
                            }
                            finishedCallback.callback(caretakerNum);
                        }
                    }
                });
    }

    public interface SimpleCallback<T> {
        void callback(T data);
    }
}
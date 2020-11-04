package com.termproject.geoad.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.termproject.geoad.CaretakerPatientList;
import com.termproject.geoad.MainActivity;
import com.termproject.geoad.R;

public class CaretakerRegistrationSuccess extends Fragment implements View.OnClickListener {

    private Button continueButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_register_success, container, false);
        continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(this);
        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.continueButton) {
            nextFragment = new CaretakerPatientList();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

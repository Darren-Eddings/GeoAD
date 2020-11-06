package com.termproject.geoad;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class RequestConfirm extends Fragment implements View.OnClickListener {

    private Button goToHomePage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_confirm, container, false);
        goToHomePage = (Button) view.findViewById(R.id.submitConfirmButton);
        goToHomePage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.submitConfirmButton) {

            newFragment = new PatientHomeScreen();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        try {

            mainActivity.replaceFragments(newFragment);

        } catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}
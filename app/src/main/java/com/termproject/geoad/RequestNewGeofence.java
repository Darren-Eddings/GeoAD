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

public class RequestNewGeofence extends Fragment implements View.OnClickListener {

    private Button submitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_geofence_change_request, container, false);
        submitButton = view.findViewById(R.id.submitChangeButton);
        submitButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.submitChangeButton) {

            newFragment = new RequestConfirm();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(newFragment);
    }
}
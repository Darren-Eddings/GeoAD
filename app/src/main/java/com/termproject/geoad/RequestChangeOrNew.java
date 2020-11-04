package com.termproject.geoad;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RequestChangeOrNew extends Fragment implements View.OnClickListener{

    private Button changeButton;
    private Button newButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request, container, false);

        changeButton = (Button) view.findViewById(R.id.requestChange);
        newButton = (Button) view.findViewById(R.id.requestNew);
        changeButton.setOnClickListener(this);
        newButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.requestChange) {

            newFragment = new GeofenceChangeRequest();
        } else if (buttonID == R.id.requestNew) {

            newFragment = new RequestNewGeofence();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(newFragment);
    }
}
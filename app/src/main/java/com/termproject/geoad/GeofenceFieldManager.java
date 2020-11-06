package com.termproject.geoad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.text.NumberFormat;
import java.util.Currency;

public class GeofenceFieldManager extends Fragment implements View.OnClickListener{

    private Slider size;
    private Button close;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geofence_field_manager, container, false);

        close = view.findViewById(R.id.saveButton);
        close.setOnClickListener(this);

        size = view.findViewById(R.id.sizeSlider);
        size.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {

                NumberFormat intFormat = NumberFormat.getIntegerInstance();

                return intFormat.format(value) + 'm';
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.saveButton) {

            newFragment = new MapFragment();

        }

        MainActivity mainActivity = (MainActivity) getActivity();

        try {

            mainActivity.replaceFragments(newFragment);

        }catch (NullPointerException e) {

            e.printStackTrace();
        }
    }
}
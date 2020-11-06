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

public class GeofenceTimedFieldManager extends Fragment implements View.OnClickListener{

    private Slider radius;
    private Slider time;
    private Button save;
    private EditText name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geofence_timed_field_manager, container, false);

        name = view.findViewById(R.id.enterNameTimed);
        save = view.findViewById(R.id.saveTimedButton);
        save.setOnClickListener(this);
        radius = view.findViewById(R.id.sizeSliderTimed);
        time = view.findViewById(R.id.timeSlider);

        radius.setLabelFormatter(new LabelFormatter() {

            @NonNull
            @Override
            public String getFormattedValue(float value) {

                NumberFormat numFormat = NumberFormat.getIntegerInstance();

                return numFormat.format(value) + 'm';
            }
        });

        time.setLabelFormatter(new LabelFormatter() {

            @NonNull
            @Override
            public String getFormattedValue(float value) {

                NumberFormat timeFormat = NumberFormat.getIntegerInstance();

                return timeFormat.format(value) + "hrs";
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = null;
        int buttonID = v.getId();

        if (buttonID == R.id.saveTimedButton) {

            String fenceName = name.getText().toString();
            int size = (int)radius.getValue();
            long duration = (int)time.getValue();
            duration = duration * 3600000;

            ((MapsActivity)getActivity()).setName(fenceName);
            ((MapsActivity)getActivity()).setRadius(size);
            ((MapsActivity)getActivity()).setGeofenceDuration(duration);
            ((MapsActivity)getActivity()).tryAddingGeofence(((MapsActivity)getActivity()).getLocation());
            ((MapsActivity)getActivity()).moveMap(((MapsActivity)getActivity()).getLocation());
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();

        }

        /**MainActivity mainActivity = (MainActivity) getActivity();

        try {

            mainActivity.replaceFragments(newFragment);

        }catch (NullPointerException e) {

            e.printStackTrace();
        }**/
    }
}
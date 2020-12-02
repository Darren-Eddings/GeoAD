/*
 * Written by Darren Eddings and Gideon Woo
 *
 * Commented by Darren Eddings
 *
 * Some Code was automatically generated
 * upon fragment creation
 *
 * Fragment allows caretaker to set the attributes
 * for a regular geofence upon creation
 */
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
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.text.NumberFormat;
import java.util.Currency;

public class GeofenceFieldManager extends Fragment implements View.OnClickListener{

    //initialize variables for UI objects
    private Slider size;
    private ImageButton close;
    private EditText name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create a view for the fragment page
        View view = inflater.inflate(R.layout.fragment_geofence_field_manager, container, false);

        //connect UI object variables to corresponding xml elements by using their ID
        name = view.findViewById(R.id.enterName);
        close = view.findViewById(R.id.saveButton);
        size = view.findViewById(R.id.sizeSlider);

        //have the close button listen for clicks
        close.setOnClickListener(this);

        //format size slider label to have units
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

        //when button is clicked create a new temporary fragment and set it to null
        Fragment newFragment = null;
        int buttonID = v.getId();

        //if the ID matches the button pressed
        if (buttonID == R.id.saveButton) {

            //define the geofence fields
            String fenceName = name.getText().toString();
            int radius = (int)size.getValue();

            //apply the attributes by calling functions from the activity
            ((CaretakerMapActivity)getActivity()).setName(fenceName);
            ((CaretakerMapActivity)getActivity()).setRadius(radius);
            ((CaretakerMapActivity)getActivity()).drawFence(((CaretakerMapActivity)getActivity()).getLocation());
            ((CaretakerMapActivity)getActivity()).addGeofence(((CaretakerMapActivity)getActivity()).getLocation());
            ((CaretakerMapActivity)getActivity()).moveMap(((CaretakerMapActivity)getActivity()).getLocation());
            getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();

        }

        /**MainActivity mainActivity = (MainActivity) getActivity();

        try {

            mainActivity.replaceFragments(newFragment);

        }catch (NullPointerException e) {

            e.printStackTrace();
        }**/
    }
}
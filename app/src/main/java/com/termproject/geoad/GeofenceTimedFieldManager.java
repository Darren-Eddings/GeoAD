/*
 * Written by Darren Eddings and Gideon Woo
 *
 * Commented by Darren Eddings
 *
 * Some Code was automatically generated
 * upon fragment creation
 *
 * Fragment allows caretaker to set the attributes
 * for a timed geofence upon creation
 */
package com.termproject.geoad;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import java.text.NumberFormat;

public class GeofenceTimedFieldManager extends Fragment implements View.OnClickListener{

    //initialize variables for UI objects to appear on the page
    private Slider radius;
    private Slider time;
    private ImageButton save;
    private EditText name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //create a view to display UI objects
        View view = inflater.inflate(R.layout.fragment_geofence_timed_field_manager, container, false);

        //set objects equal to their corresponding xml elements
        name = view.findViewById(R.id.enterNameTimed);
        save = view.findViewById(R.id.saveTimedButton);
        radius = view.findViewById(R.id.sizeSliderTimed);
        time = view.findViewById(R.id.timeSlider);

        //have the ImageButton listen for clicks
        save.setOnClickListener(this);

        //add units to the label for the radius slider
        radius.setLabelFormatter(new LabelFormatter() {

            @NonNull
            @Override
            public String getFormattedValue(float value) {

                NumberFormat numFormat = NumberFormat.getIntegerInstance();

                return numFormat.format(value) + 'm';
            }
        });

        //add units to time slider
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

        //create temporary fragment and set it to null
        Fragment newFragment = null;
        int buttonID = v.getId();

        //if button ID is saveTimedButton
        if (buttonID == R.id.saveTimedButton) {

            //create fields for a timed geofence
            String fenceName = name.getText().toString();
            int size = (int)radius.getValue();
            long duration = (int)time.getValue();
            duration = duration * 3600000;

            //apply field adjustments
            ((CaretakerMapActivity)getActivity()).setName(fenceName);
            ((CaretakerMapActivity)getActivity()).setRadius(size);
            ((CaretakerMapActivity)getActivity()).setGeofenceDuration(duration);
            ((CaretakerMapActivity)getActivity()).drawFence(((CaretakerMapActivity)getActivity()).getLocation());
            ((CaretakerMapActivity)getActivity()).addGeofence(((CaretakerMapActivity)getActivity()).getLocation());
            ((CaretakerMapActivity)getActivity()).moveMap(((CaretakerMapActivity)getActivity()).getLocation());

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
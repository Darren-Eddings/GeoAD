package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CaretakerGeofenceInformation extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private ArrayAdapter<CharSequence> geofenceInformationFieldsAdapter;
    private ArrayAdapter<CharSequence> geofenceInformationListAdapter;
    private ListView geofenceInformationFields;
    private ListView geofenceInformationList;
    private Button returnButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);

        View view = inflater.inflate(R.layout.fragment_caretaker_geofence_information, container, false);

        geofenceInformationFieldsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.geofence_information_fields, android.R.layout.simple_list_item_1);
        geofenceInformationListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        geofenceInformationListAdapter.add(viewModel.getGeofence()[0]);
        geofenceInformationListAdapter.add(viewModel.getGeofence()[3] + " m radius");
        switch (viewModel.getGeofence()[4]) {
            case "0":
                geofenceInformationListAdapter.add("Classic");
            break;
            case "1":
                geofenceInformationListAdapter.add("Point-of-Interest");
            break;
            case "2":
                geofenceInformationListAdapter.add("Timed");
            break;
        }

        if(viewModel.getGeofence()[5].equals("-1")) {
            geofenceInformationListAdapter.add("Not Timed");
        }
        else {
            geofenceInformationListAdapter.add(Integer.toString((Integer.parseInt(viewModel.getGeofence()[5])/1000/3600)) + " minutes");
        }

        geofenceInformationFields = view.findViewById(R.id.geofenceInformationFields);
        geofenceInformationList = view.findViewById(R.id.geofenceInformationList);

        geofenceInformationFields.setAdapter(geofenceInformationFieldsAdapter);
        geofenceInformationList.setAdapter(geofenceInformationListAdapter);

        returnButton = view.findViewById(R.id.returnButton);

        returnButton.setOnClickListener(this);

        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.returnButton) {
            nextFragment = new CaretakerManageGeofence();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

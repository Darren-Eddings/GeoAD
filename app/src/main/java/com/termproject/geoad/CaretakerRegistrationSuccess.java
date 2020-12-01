package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CaretakerRegistrationSuccess extends Fragment implements View.OnClickListener {
    private CaretakerViewModel viewModel;

    private Caretaker caretaker;
    private TextView caretakerId;
    private Button continueButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(CaretakerViewModel.class);
        caretaker = viewModel.getCaretaker();

        View view = inflater.inflate(R.layout.fragment_caretaker_register_success, container, false);

        caretakerId = view.findViewById(R.id.caretakerId);
        continueButton = view.findViewById(R.id.continueButton);

        caretakerId.setText(caretaker.getCaretakerID());

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

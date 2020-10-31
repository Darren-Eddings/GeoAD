package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerLogin extends Fragment implements View.OnClickListener{

    private Button loginButton;
    private TextView registerHere;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_login, container, false);
        loginButton = view.findViewById(R.id.caretakerLoginButton);
        registerHere = view.findViewById(R.id.inkRegister);
        loginButton.setOnClickListener(this);
        registerHere.setOnClickListener(this);
        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.caretakerLoginButton) {
        }
        else if (buttonId == R.id.inkRegister) {

        }
        MainActivity mainActivity = (MainActivity) getActivity();
        try {
            mainActivity.replaceFragments(nextFragment);
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

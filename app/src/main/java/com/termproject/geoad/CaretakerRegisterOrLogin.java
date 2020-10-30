package com.termproject.geoad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaretakerRegisterOrLogin extends Fragment implements View.OnClickListener{

    private Button registerButton;
    private Button loginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretaker_register_or_login, container, false);
        registerButton = (Button) view.findViewById(R.id.registerButton);
        loginButton = (Button) view.findViewById(R.id.loginButton);
        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        return view;
    }

    @Override public void onClick(View v) {
        Fragment nextFragment = null;
        int buttonId = v.getId();
        if (buttonId == R.id.registerButton) {
        }
        else if (buttonId == R.id.loginButton) {
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragments(nextFragment);
    }
}

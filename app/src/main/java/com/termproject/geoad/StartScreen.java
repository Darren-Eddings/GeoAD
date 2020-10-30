package com.termproject.geoad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

public class StartScreen extends AppCompatActivity {

    private Button patientButton;
    private Button caretakerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        patientButton = findViewById(R.id.patientButton);
        caretakerButton = findViewById(R.id.caretakerButton);

        caretakerButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.caretakerRegisterOrLoginDestination));
    }
}

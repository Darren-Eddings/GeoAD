package com.termproject.geoad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CallEmergency extends Fragment implements View.OnClickListener {

    ImageView phoneButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_emergency, container, false);

        phoneButton = view.findViewById(R.id.phoneImage);

        phoneButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:911"));
        startActivity(intent);
    }
}

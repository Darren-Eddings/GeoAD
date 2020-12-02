package com.termproject.geoad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends FragmentActivity {

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enablePermissions();
        startService();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            StartScreen firstFragment = new StartScreen();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }
    private void enablePermissions(){
        //Checking for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Explain permission reasons, ask for permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    void startService(){
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have permission
            }
            else {
                //No permission
            }
        }
        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
            }
            else {
                //No permission
                Toast.makeText(this, "Background location access required for geofences...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void replaceFragments(Fragment fragment) {
        if(Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
            else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    //Ask for permission
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        }
        else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }
}

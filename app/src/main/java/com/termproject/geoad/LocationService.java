package com.termproject.geoad;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LocationService extends Service {

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                FileInputStream fIn = null;
                try {
                    fIn = new FileInputStream(new File(getFilesDir() + "/MyLocation.txt"));
                    InputStreamReader isr = new InputStreamReader(fIn);
                } catch (FileNotFoundException e) {
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(new File (getFilesDir() + "/MyLocation.txt"));
                        OutputStreamWriter osw = new OutputStreamWriter(fOut);
                        try {
                            osw.write(locationResult.getLastLocation().getLatitude() + "," + locationResult.getLastLocation().getLongitude());
                            osw.close();
                            fOut.close();
                        }
                        catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
}
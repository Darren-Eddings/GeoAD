package com.termproject.geoad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

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
        //Using fusedlocation api for location data
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Setting up what happens when location is received
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //Log results
                Log.d("mylog", "Lat is: " + locationResult.getLastLocation().getLatitude() + ", " + "Lng is: " +
                        locationResult.getLastLocation().getLongitude());
                //Setting intent for broadcast receiver in the future
                Intent intent = new Intent("ACT_LOC");
                intent.putExtra("latitude", locationResult.getLastLocation().getLatitude());
                intent.putExtra("longitude", locationResult.getLastLocation().getLongitude());
                sendBroadcast(intent);

                //Writing location to file to interact with the server later on
                FileInputStream fIn = null;
                //Trying to open file, if one does not exist, create it
                try {
                    fIn = new FileInputStream(new File(getFilesDir() + "/MyLocation.txt"));
                    InputStreamReader isr = new InputStreamReader(fIn);
                } catch (FileNotFoundException e) {
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(new File (getFilesDir() + "/MyLocation.txt"));
                        OutputStreamWriter osw = new OutputStreamWriter(fOut);
                        try {
                            //Writing the location into the created file
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
                //Open the file for writing
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(new File (getFilesDir() + "/MyLocation.txt"));
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);
                    try {
                        //Write in the location and close
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
        };
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    //Interacting with the fusedlocationprovider to retrieve location information at a set interval
    @SuppressLint("MissingPermission")
    private void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
}
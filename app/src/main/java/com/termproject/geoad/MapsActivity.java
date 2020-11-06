package com.termproject.geoad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    private LatLng fenceLoc;

    private int GEOFENCE_RADIUS = 200;

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    private String GEOFENCE_ID = "Placeholder";

    private int geofenceType = -1;

    private long geofenceDuration = Geofence.NEVER_EXPIRE;

    private String[] geofenceTypes = new String[]{
            "Classic",
            "Point-of-Interest",
            "Timed"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng home = new LatLng(49.222152, -123.064591);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 16));

        enableUserLocation();

        mMap.setOnMapLongClickListener(this);
    }

    private void enableUserLocation(){
        //Checking for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have permission
                mMap.setMyLocationEnabled(true);
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

    @Override
    public void onMapLongClick(LatLng latLng) {

        if(Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED){
                userInputGeofenceType();
                fenceLoc = latLng;
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
            userInputGeofenceType();
        }

    }

    public void tryAddingGeofence(LatLng latLng){
        //mMap.clear();
        addGeofence(latLng, GEOFENCE_RADIUS, geofenceType);
        addMarker(latLng);
        //Currently using a fixed radius, will require user input later
        addCircle(latLng, GEOFENCE_RADIUS);
    }

    public void addGeofence(LatLng latLng, int radius, int type){
        int translatedType = 0;
        if (type == 0){
            translatedType = Geofence.GEOFENCE_TRANSITION_EXIT;
            geofenceDuration = Geofence.NEVER_EXPIRE;
        }
        else if (type == 1){
            translatedType = Geofence.GEOFENCE_TRANSITION_ENTER;
            geofenceDuration = Geofence.NEVER_EXPIRE;
        }
        else if (type == 2){
            translatedType = Geofence.GEOFENCE_TRANSITION_EXIT;
        }

        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, translatedType, geofenceDuration);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                        boolean isNew = true;
                        FileInputStream fIn = null;
                        try {
                            fIn = new FileInputStream(new File (getFilesDir() + "/GeofenceList.txt"));
                            InputStreamReader isr = new InputStreamReader(fIn);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            FileOutputStream fOut = null;
                            try {
                                fOut = new FileOutputStream(new File (getFilesDir() + "/GeofenceList.txt"));
                                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                                try {
                                    osw.write("");
                                }
                                catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                            catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }
                        try {
                            fIn = new FileInputStream(new File (getFilesDir() + "/GeofenceList.txt"));
                            InputStreamReader isr = new InputStreamReader(fIn);
                            BufferedReader reader = new BufferedReader(isr);
                            while (reader.ready() && isNew == true){
                                String line = reader.readLine();
                                String[] splitLine = line.split(",");
                                if (GEOFENCE_ID.equals(splitLine[0])){
                                    isNew = false;
                                }
                            }
                            reader.close();
                            isr.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (isNew == true){
                            File file = new File(getFilesDir() + "/GeofenceList.txt");
                            FileOutputStream fr = null;
                            try {
                                fr = new FileOutputStream(file, true);
                                OutputStreamWriter writer = new OutputStreamWriter(fr);
                                try {
                                    writer.write(GEOFENCE_ID + "," + latLng.latitude + "," + latLng.longitude +
                                            "," + GEOFENCE_RADIUS + "," + geofenceType + "," + geofenceDuration + "\n");
                                    writer.close();
                                    fr.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }
    /**@Override
    public void onResume() {
        super.onResume();
        addGeofence(fenceLoc, GEOFENCE_RADIUS);
        tryAddingGeofence(fenceLoc);
    }**/

    private void addMarker (LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, int radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }

    private void userInputGeofenceType(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MapsActivity.this);
        builder.setTitle("Geofence Type ");
        builder.setSingleChoiceItems(geofenceTypes, geofenceType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MapsActivity.this, geofenceTypes[i] + " was chosen", Toast.LENGTH_SHORT).show();
                geofenceType = i;
            }
        });
        builder.setBackground(getResources().getDrawable(R.drawable.alert_dialog_bg, null));
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (geofenceType == 0 || geofenceType == 2) {
                    GeofenceFieldManager fragment = new GeofenceFieldManager();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.map, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(geofenceType == 1){
                    GeofenceTimedFieldManager fragment = new GeofenceTimedFieldManager();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.map, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MapsActivity.this,"Adding Cancelled", Toast.LENGTH_SHORT).show();
                geofenceType = -1;
            }
        });
        builder.show();
    }

    public void setName(String geofenceName){
        GEOFENCE_ID = geofenceName;
    }

    public void setRadius(int radius){
        GEOFENCE_RADIUS = radius;
    }

    public void setGeofenceDuration(long duration){
        geofenceDuration = duration;
    }

    public LatLng getLocation(){
        LatLng temp = fenceLoc;
        return temp;
    }

    public int getGeofenceType(){
        int temp = geofenceType;
        return temp;
    }

    public void moveMap(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

}
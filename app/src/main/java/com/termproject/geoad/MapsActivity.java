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
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "CaretakerMapsActivity";

    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    private LatLng fenceLoc = new LatLng(0,0);

    private float GEOFENCE_RADIUS = 100;

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

        fenceUpdate();

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

    @SuppressLint("MissingPermission")
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
                fenceLoc = latLng;
                userInputGeofenceType();
                //tryAddingGeofence(latLng);
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
            fenceLoc = latLng;
            userInputGeofenceType();
            //tryAddingGeofence(latLng);
        }

    }

    public void drawGeofence(LatLng latLng){
        //mMap.clear();
        //addGeofence(latLng, GEOFENCE_RADIUS);
        addMarker(latLng);
        //Currently using a fixed radius, will require user input later
        addCircle(latLng, GEOFENCE_RADIUS);
    }

    @SuppressLint("MissingPermission")
    public void addGeofence(LatLng latLng){
        int translatedType = 0;
        if (geofenceType == 0){
            translatedType = Geofence.GEOFENCE_TRANSITION_EXIT;
            geofenceDuration = Geofence.NEVER_EXPIRE;
        }
        else if (geofenceType == 1){
            translatedType = Geofence.GEOFENCE_TRANSITION_ENTER;
            geofenceDuration = Geofence.NEVER_EXPIRE;
        }
        else if (geofenceType == 2){
            translatedType = Geofence.GEOFENCE_TRANSITION_EXIT;
        }

        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, GEOFENCE_RADIUS, translatedType, geofenceDuration);
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
                            FileOutputStream fOut = null;
                            try {
                                fOut = new FileOutputStream(new File (getFilesDir() + "/GeofenceList.txt"));
                                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                                try {
                                    osw.write("");
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
                            fIn.close();
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
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            catch (IOException e) {
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
        //geofencingClient.removeGeofences(pendingIntent);
    }

    private void addMarker (LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius){
        if (geofenceType == 0) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(radius);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(64, 255, 0, 0));
            circleOptions.strokeWidth(4);
            mMap.addCircle(circleOptions);
        }
        else if (geofenceType == 1) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(radius);
            circleOptions.strokeColor(Color.argb(255, 0, 0, 255));
            circleOptions.fillColor(Color.argb(64, 0, 0, 255));
            circleOptions.strokeWidth(4);
            mMap.addCircle(circleOptions);
        }
        else if (geofenceType == 2) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(radius);
            circleOptions.strokeColor(Color.argb(255, 0, 255, 0));
            circleOptions.fillColor(Color.argb(64, 0, 255, 0));
            circleOptions.strokeWidth(4);
            mMap.addCircle(circleOptions);
        }
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
                if (geofenceType == 0 || geofenceType == 1) {
                    GeofenceFieldManager fragment = new GeofenceFieldManager();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.map, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(geofenceType == 2){
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
        builder.setNeutralButton("REMOVE GEOFENCE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CaretakerRemoveGeofence fragment = new CaretakerRemoveGeofence();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.map, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
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

    public void fenceUpdate(){
        mMap.clear();
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(new File (getFilesDir() + "/GeofenceList.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
        } catch (FileNotFoundException e) {
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(new File (getFilesDir() + "/GeofenceList.txt"));
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                try {
                    osw.write("");
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
        try {
            fIn = new FileInputStream(new File (getFilesDir() + "/GeofenceList.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(isr);
            while (reader.ready()) {
                String line = reader.readLine();
                String[] splitLine = line.split(",");
                GEOFENCE_ID = splitLine[0];
                fenceLoc = new LatLng (parseDouble(splitLine[1]), parseDouble(splitLine[2]));
                GEOFENCE_RADIUS = (parseFloat(splitLine[3]));
                geofenceType = (parseInt(splitLine[4]));
                geofenceDuration = (parseLong(splitLine[5]));
                drawGeofence(fenceLoc);
            }
            reader.close();
            isr.close();
            fIn.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFence(String fenceID){
        List<String> removeList = new ArrayList<>();
        removeList.add(fenceID);
        geofencingClient.removeGeofences(removeList);
        fenceListRemove(fenceID);
    }

    private void fenceListRemove(String fenceID){
        String[] ids = new String[1000000];
        List<String> lines = new LinkedList<>();
        int arrayIndex = 0;
        int lineIndex = 0;
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(new File (getFilesDir() + "/GeofenceList.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader reader = new BufferedReader(isr);
            while (reader.ready()) {
                String line = reader.readLine();
                String[] splitLine = line.split(",");
                lines.add(line);
                ids [arrayIndex] = splitLine[0];
                arrayIndex ++;
            }
            reader.close();
            isr.close();
            fIn.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!ids[lineIndex].equals(fenceID)){
            lineIndex++;
        }
        lines.remove(lineIndex);

            File file = new File(getFilesDir() + "/GeofenceList.txt");
            FileOutputStream fr = null;
            try {
                fr = new FileOutputStream(file, false);
                OutputStreamWriter writer = new OutputStreamWriter(fr);
                try {
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                    writer.close();
                    fr.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

    }

}
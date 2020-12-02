package com.termproject.geoad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class GuideMeHome extends FragmentActivity implements OnMapReadyCallback{

    //Setting up variables
    private static final String TAG = "CaretakerMapsActivity";

    private GoogleMap mMap;

    private LatLng fenceLoc = new LatLng(0,0);

    private float GEOFENCE_RADIUS = 100;

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    private String GEOFENCE_ID = "Placeholder";

    private int geofenceType = -1;

    private long geofenceDuration = Geofence.NEVER_EXPIRE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Using Trinity Western as default home location, starting the map there
        LatLng home = new LatLng(49.139289, -122.608944);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 16));

        //Function call for permissions and enabling user location on the map
        enableUserLocation();

        //Function call to draw all geofences onto the map
        fenceUpdate();

    }
    private void enableUserLocation(){
        //Checking for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        //No permissions, asking for them
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
    //Checking if people gave permission, if not warn that the app requires them
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have permission
                mMap.setMyLocationEnabled(true);
            }
            //No permissions, asking for them
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

    public void fenceUpdate(){
        //Clearing the map of all geofences
        mMap.clear();
        //Doing file opening, creating if one is not found
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(new File(getFilesDir() + "/GeofenceList.txt"));
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
        //Opening up the file and parsing through it line by line, argument by argument, drawing
        //every fence on the list
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
                //Calling the function to draw the geofence on the map
                drawFence(fenceLoc);
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
    public void drawFence(LatLng latLng){
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
    }

    //Function that adds a marker onto the map
    private void addMarker (LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    //Function that draws a shaded circle onto the map
    private void addCircle(LatLng latLng, float radius){
        //Changing the color of the circle depending on the geofence type
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

}
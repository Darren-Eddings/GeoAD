package com.termproject.geoad;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Double.parseDouble;

public class PatientLocation extends FragmentActivity implements OnMapReadyCallback {
    private FirebaseFirestore db;

    private GoogleMap mMap;
    private LatLng patientLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_patient_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        patientLocationUpdate();

        //Setting up code to run repeatedly at intervals
        Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                //Redrawing marker from text file every 7 seconds
                Log.d("Handlers", "Called on main thread");
                patientLocationUpdate();
                handler.postDelayed(this, 7000);
            }
        };
// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    //Drawing the marker on the map of where the patient is
    public void patientLocationUpdate(){
        //Clear the map of markers
        mMap.clear();
        FileInputStream fIn = null;
        //Try reading the file for patient location, creating the file if it does not exist
        try {
            fIn = new FileInputStream(new File(getFilesDir() + "/PatientLocation.txt"));
            InputStreamReader isr = new InputStreamReader(fIn);
        } catch (FileNotFoundException e) {
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(new File (getFilesDir() + "/PatientLocation.txt"));
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
        //Opens the file, reads in the location data

        DocumentReference patientRef = db.collection("patients").document(getIntent().getStringExtra("patientName"));
        executePatientLocation(patientRef, new SimpleCallback<String>() {
            @Override
            public void callback(String patientLocation) {
                String[] splitLine = patientLocation.split(",");
                patientLoc = new LatLng (parseDouble(splitLine[0]), parseDouble(splitLine[1]));
                //Calls the function to draw the marker where the patient is
                drawMarker(patientLoc);
            }
        });
    }

    //Function responsible for drawing a marker
    private void drawMarker (LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Patient Location");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(patientLoc, 15));
    }

    private void executePatientLocation(DocumentReference patientRef, @NonNull SimpleCallback<String> finishedCallback) {
        patientRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String patientLocation = document.get("location").toString();
                            finishedCallback.callback(patientLocation);
                        }
                    }
                });
    }

    public interface SimpleCallback<T> {
        void callback(T data);
    }
}
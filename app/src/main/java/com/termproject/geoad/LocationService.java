package com.termproject.geoad;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LocationService extends Service {

    private FirebaseFirestore db;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        db = FirebaseFirestore.getInstance();

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
                FileInputStream fis = null;
                try{
                    fis = new FileInputStream(new File(getFilesDir() + "/Session.txt"));
                    InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        BufferedReader reader = new BufferedReader(isr);
                        String line = reader.readLine();
                        String[] credentials = line.split(",");
                        Query session;

                        if(credentials[0].equals("Patient")) {
                            session = db.collection("patients")
                                    .whereEqualTo("patientID", credentials[1])
                                    .whereEqualTo("password", credentials[2]);

                            executePatientQuery(session, new SimpleCallback<String>() {
                                @Override
                                public void callback(String patientName) {
                                    DocumentReference patientRef = db.collection("patients").document(patientName);
                                    patientRef.update("location", locationResult.getLastLocation().getLatitude() + "," + locationResult.getLastLocation().getLongitude());
                                }
                            });
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
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

    private void executePatientQuery(Query patientQuery, @NonNull SimpleCallback<String> finishedCallback) {
        patientQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (!snapshot.isEmpty()) {
                                String patientName = "";
                                for (QueryDocumentSnapshot document: snapshot) {
                                    Patient patient = document.toObject(Patient.class);
                                    patientName = patient.getFullName();
                                }
                                finishedCallback.callback(patientName);
                            }
                        }
                    }
                });
    }

    public interface SimpleCallback<T> {
        void callback(T data);
    }
}
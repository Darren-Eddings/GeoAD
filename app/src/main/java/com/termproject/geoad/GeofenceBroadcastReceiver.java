package com.termproject.geoad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    //This class catches events triggered by geofences
    private static final String TAG = "GeofenceBroadcastReceive";

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();
        //Setting up Variables
        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        //In case there is an error received from geofence API
        if(geofencingEvent.hasError()){
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        //Get the triggering geofences
        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for(Geofence geofence: geofenceList){
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }

        //Gets location where geofence was triggered, might be useful for us later on
        Location location = geofencingEvent.getTriggeringLocation();
        //Finding what type of trigger was triggered
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch(transitionType){

            //sets title and body text of the notification for each type of notification
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "You've Entered a Geofence", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("Geofence Triggered", "You have entered once of your previously established geofences", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL", "", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "You've Left a Geofence", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("Geofence Triggered", "You have left your current geofence. Please re-enter the geofence", MapsActivity.class);
                break;

        }
    }
}
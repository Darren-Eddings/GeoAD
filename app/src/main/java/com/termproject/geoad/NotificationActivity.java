/*package com.termproject.geoad;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotificationActivity extends AppCompatAcitity{

    private static final String CHANNEL_ID = "channel_id01";
    public static final int NOTIFICATION_ID =1;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(savedInsatnceState);
        setContentView(R.layout.activity_main);

        Button showNotificationBtn = findViewById(R.id.showNotificationBtn);
        showNotificationBtn.setOnClickListener((v) -> {

            //click button to show notification
            showNotificationBtn();
        });
    }
    private void showNotification() {
        createNotificationChannel();

        //start this on by tapping notification
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mainPIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_ONE_SHOT);

        // click open app button to open app
        Intent openIntent = new Intent(this, PatientHomeScreenFragment.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent openPIntent = PendingIntent.getActivity(this, 0, openIntent, PendingIntent.FLAG_ONE_SHOT);

        // click dismiss button to open app
        Intent dismissIntent = new Intent(this, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent disMissPIntent = PendingIntent.getActivity(this, 0, dismissIntent, PendingIntent.FLAG_ONE_SHOT);

        //creating notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        //icon
        builder.setSmallIcon(R.drawable.ic_message);
        //title
        builder.setContentTitle("GeoAD");
        //description
        builder.setContentText("The primary user has violated the geofence, open the app for more options");
        //set priority
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //dismiss on tap
        builder.setAutoCancel(true);
        //start intent on notification tap (PatientHomeScreenFragment)
        builder.setContentIntent(mainPIntent);
        //add action buttons to notification
        builder.addAction(R.drawable.ic_open, "Open APP",openPIntent);
        builder.addAction(R.drawable.ic_dismiss, "Dismiss",disMissPIntent);

        //notification manager
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence name = "My Notification";
            String description = "My notification description";

            int impotance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ID,name,impotance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel((notificationChannel));
        }
    }
    public void dismiss() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
*/

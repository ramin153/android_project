package com.example.androidproject.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidproject.App;
import com.example.androidproject.R;
import com.example.androidproject.dataBase.MapLocationData;
import com.example.androidproject.view.HomeActivity;
import com.example.androidproject.view.MapActivity;
import com.mapbox.mapboxsdk.log.LoggerDefinition;

public class MapRechLocationReciver extends BroadcastReceiver {
    public static final String MAPDATA = "mapData";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MAPDATA,"item reach for notification");
        MapLocationData mapLocationData = MapLocationData.convertJsonToObject(intent.getStringExtra(MAPDATA));
        sendNotification(context, mapLocationData);
    }

    private static void sendNotification(Context context, MapLocationData mapLocationData)
    {

        Intent notificationIntent = new Intent(context, MapActivity.class);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24)
                .setContentTitle("you are near")
                .setContentText(mapLocationData.detail)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | PendingIntent.FLAG_UPDATE_CURRENT;
        notificationManager.notify(makeId(mapLocationData.id), notification);
    }
    private static int makeId(int id){return (id<<3)+3;}
}

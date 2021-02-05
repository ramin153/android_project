package com.example.androidproject;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.room.Room;

import com.example.androidproject.dataBase.DataBase;
import com.example.androidproject.service.LocationService;
import com.mapbox.mapboxsdk.Mapbox;

public class App extends Application {
    public static final String SHAREDPREFERENCES = "SharedPreferences";
    public static final String SHAREDPREFERENCES_EMAIL = "SharedPreferences_email";
    public static final String SHAREDPREFERENCES_PASSWORD = "SharedPreferences_password";

    public static final String CHANNEL_1_ID = "notification ";
    public static final String CHANNEL_1_NAME = "todo list reminder";
    public static final String CHANNEL_1_Description = "contain all notification that this app contain";
    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(this, "pk.eyJ1IjoicmFtaW4xNTMiLCJhIjoiY2tqZWJkNDlzMDRkNzJxbzdzaDAxZXY0aSJ9.ePNQFXFOJNKiKlOD_cmrew");
        createNotificationChannels();
        LocationService.startService(getApplicationContext());
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    CHANNEL_1_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(CHANNEL_1_Description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
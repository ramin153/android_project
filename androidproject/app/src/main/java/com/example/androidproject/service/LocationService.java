package com.example.androidproject.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.DynamicsProcessing;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.androidproject.dataBase.DataBase;
import com.example.androidproject.dataBase.MapLocationData;
import com.example.androidproject.map.GPSTracker;
import com.example.androidproject.recyclerview.MapData;

import java.util.Calendar;
import java.util.List;

public class LocationService extends JobIntentService {
    final static String TAG = "LocationService";

    final static String MAP_DATA = "MAP_DATA";
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Context context = this;
        Log.d(TAG, "onHandleWork: ");
        AsyncTask.execute(new Runnable() {
            DataBase dataBase = DataBase.getDataBase(context);
            List<MapLocationData> items = dataBase.mapDao().getAll();
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GPSTracker gpsTracker = new GPSTracker(context);
                            Location location = gpsTracker.location;
                            for (MapLocationData i : items)
                            {
                                Location reach = new Location(LocationManager.GPS_PROVIDER);
                                reach.setLatitude(Double.parseDouble(i.latitude));
                                reach.setLongitude(Double.parseDouble(i.longitude));
                                System.out.println(reach.distanceTo(location));
                                if (reach.distanceTo(location) <= 1000)
                                {
                                    Intent broadCast = new Intent(context,MapRechLocationReciver.class);
                                    broadCast.putExtra(MapRechLocationReciver.MAPDATA,i.convertJson());
                                    context.sendBroadcast(broadCast);
                                    System.out.println("in distance");

                                }
                            }

                            startServiceInside(context);


                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

            }});

    }

    public static void startService(Context context)
    {
        Intent intent = new Intent(context,LocationService.class);

        enqueueWork(context, LocationService.class, getNewId(), intent);
    }

    private static void startServiceInside(Context context)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent broadcast_intent = new Intent(context, AlarmLocation_Reciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, getNewId(),  broadcast_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTime().getTime(), pendingIntent);
    }

    static int getNewId(){return 200;}
    static int getNewId(int id){return id<<3 +2;}
}

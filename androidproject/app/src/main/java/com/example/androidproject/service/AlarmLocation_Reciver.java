package com.example.androidproject.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.androidproject.dataBase.MapLocationData;
import com.example.androidproject.recyclerview.MapData;

public class AlarmLocation_Reciver  extends BroadcastReceiver {
    final static String DATA_MAP = "data_map";

    @Override
    public void onReceive(Context context, Intent intent) {
        MapLocationData mapData = MapLocationData.convertJsonToObject(intent.getStringExtra(DATA_MAP));
        LocationService.startService(context);

    }
}

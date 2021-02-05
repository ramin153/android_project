package com.example.androidproject.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.example.androidproject.dataBase.Note;
import com.example.androidproject.recyclerview.NoteData;

import java.text.ParseException;

public class NotificationService extends JobIntentService {
    private static final String NOTE = "NOTE";

    public static void create(Note note,Context context)
    {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra(NOTE,note.convertJson());
        enqueueWork(context,NotificationService.class,getId(note.getId()),serviceIntent);
    }
    private static int getId(int id)
    {
        return (id<<3) + 1;
    }
    public static void cancel(Context context,int id)
    {
        NotificationBroadcastReceiver.cancel(context,id);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Note note = Note.convertJsonToObject( intent.getStringExtra(NOTE));
        try {
            NotificationBroadcastReceiver.create(getApplicationContext(),note);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

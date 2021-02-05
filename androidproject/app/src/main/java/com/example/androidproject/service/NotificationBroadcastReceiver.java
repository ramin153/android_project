package com.example.androidproject.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidproject.App;
import com.example.androidproject.R;
import com.example.androidproject.dataBase.Note;
import com.example.androidproject.helper.MyToasty;
import com.example.androidproject.recyclerview.NoteData;
import com.example.androidproject.recyclerview.TypeOfNote;
import com.example.androidproject.view.HomeActivity;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static String TITLE = "title";
    public static String ID = "ID";
    public static String EMAIL = "email";
    public static String TEXT = "text";
    public static String LINK = "link";
    public static String TYPE = "type";

    @Override
    public void onReceive(Context context, Intent intent) {
        MyToasty t = new MyToasty(context);
        String text = intent.getStringExtra(TEXT);
        String title = intent.getStringExtra(TITLE);
        String link = intent.getStringExtra(LINK);
        String email = intent.getStringExtra(EMAIL);
        int type = intent.getIntExtra(TYPE,-1);
        int id = intent.getIntExtra(ID,-1);

        if (id == -1)
        {
            System.out.println("bug for finding id");
            return;
        }
        System.out.println(type);
        if(TypeOfNote.action() == type )
        {
            sendNotificationAction(context,title,text,id);
        }else if(TypeOfNote.email() == type)
        {
            sendNotificationEmail(context,title,text,email,id);
        }else if(TypeOfNote.reminder() == type)
        {

            sendNotificationLink(context,title,text,link,id);
        }


    }

    private static int makeId(int id)
    {
        return id<<3;
    }

    private static void sendNotificationEmail(Context context, String title, String message,String email,int id)
    {

        Intent notificationIntent = new Intent(context, HomeActivity.class);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Send email to : "+email)
                .setContentText("title : "+title+"\n body : "+message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | PendingIntent.FLAG_UPDATE_CURRENT;
        notificationManager.notify(makeId(id), notification);
    }

    private static void sendNotificationAction(Context context, String title, String message,int id)
    {

        Intent notificationIntent = new Intent(context, HomeActivity.class);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_clock)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | PendingIntent.FLAG_UPDATE_CURRENT;
        notificationManager.notify(makeId(id), notification);
    }

    private static void sendNotificationLink(Context context, String title, String message,String link,int id)
    {
        if (!link.startsWith("http://") && !link.startsWith("https://"))
            link = "http://" + link;
        System.out.println(link);
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_internet)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | PendingIntent.FLAG_UPDATE_CURRENT;
        notificationManager.notify(makeId(id), notification);
    }


    public static void create(Context context, Note note) throws ParseException {
        create(context,note.getId() , NoteData.convertStringToCalendar(note.date).getTime(), note.text , note.title, note.typeOfNote,note.url,note.address);
    }

    public static void create(Context context,int id,Date date,String text ,String title,int type)
    {
        create(context,id ,date, text , title, type,"","");
    }


    public static void create(Context context,int id,Date date,String text ,String title,int type,String link,String email)
    {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent broadcast_intent = new Intent(context, NotificationBroadcastReceiver.class);
        broadcast_intent.putExtra(TITLE, title);
        broadcast_intent.putExtra(TEXT, text);
        broadcast_intent.putExtra(LINK, link);
        broadcast_intent.putExtra(TYPE, type);
        broadcast_intent.putExtra(ID, id);
        broadcast_intent.putExtra(EMAIL, email);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, makeId(id)+2,  broadcast_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Date current = new Date();


        long triggerAtTime = date.getTime();
        if (date.compareTo(current) > 0)
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pendingIntent);
        }


    }
    public static void cancel(Context context,int id)
    {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent broadcast_intent = new Intent(context, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, makeId(id)+2,  broadcast_intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);

        System.out.println("cancel");

    }
}

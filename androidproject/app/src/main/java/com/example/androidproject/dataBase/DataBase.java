package com.example.androidproject.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.androidproject.recyclerview.NoteData;



@Database(entities = {Note.class,MapLocationData.class}, version = 4)
public abstract class DataBase extends RoomDatabase {
    public abstract NoteDao userDao();
    public abstract MapLocationDao mapDao();
    private static DataBase dataBase = null;
    public static DataBase getDataBase(Context context)
    {
        if (dataBase == null)
        {
            dataBase = Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class, "database-note")
                    .fallbackToDestructiveMigration()
                    .build();


        }
        return dataBase;
    }
}


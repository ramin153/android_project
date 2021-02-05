package com.example.androidproject.dataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MapLocationDao  {

    @Query("SELECT * FROM mapLocationData")
    List<MapLocationData> getAll();

    @Query("SELECT * FROM mapLocationData WHERE id IN (:mapsIds)")
    List<MapLocationData> loadAllByIds(int[] mapsIds);

    @Update
    public void updateNot(MapLocationData note);


    @Insert
    void insertAll(MapLocationData... notes);

    @Delete
    void delete(MapLocationData note);
}

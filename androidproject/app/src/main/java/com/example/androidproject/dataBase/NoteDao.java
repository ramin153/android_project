package com.example.androidproject.dataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface NoteDao  {


    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("SELECT * FROM note WHERE id IN (:userIds)")
    List<Note> loadAllByIds(int[] userIds);

    @Update
    public void updateNot(Note note);


    @Insert
    void insertAll(Note... notes);

    @Delete
    void delete(Note note);

}

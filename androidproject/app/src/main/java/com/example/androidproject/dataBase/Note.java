package com.example.androidproject.dataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.androidproject.recyclerview.NoteData;
import com.example.androidproject.service.NotificationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@Entity(tableName  = "note")
public class Note {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @SerializedName("date")
    @ColumnInfo(name = "date")
    public String date;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    public String title;

    @SerializedName("text")
    @ColumnInfo(name = "text")
    public String text;

    @SerializedName("click")
    @ColumnInfo(name = "click")
    public boolean click;

    @SerializedName("url")
    @ColumnInfo(name = "url")
    public String url;

    @SerializedName("address")
    @ColumnInfo(name = "address")
    public String  address;

    @SerializedName("typeOfNote")
    @ColumnInfo(name = "typeOfNote")
    public int typeOfNote;


    public Note(int id, String date, String title, String text, boolean click, String url, String address, int typeOfNote) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.text = text;
        this.click = click;
        this.url = url;
        this.address = address;
        this.typeOfNote = typeOfNote;
    }

    public static Note noteDataToNote(NoteData item)
    {
       return new Note(item.id,item.stringDate(),item.title,item.text,item.isClick,item.url,item.address,item.typeOfNote.getVal());
    }

    public String convertJson()
    {
        return new Gson().toJson(this);
    }

    public static Note convertJsonToObject(String json)
    {
        return new GsonBuilder().create().fromJson(json,Note.class);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", click=" + click +
                ", url='" + url + '\'' +
                ", location='" + address + '\'' +
                ", typeOfNote=" + typeOfNote +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public boolean isClick() {
        return click;
    }

    public String getUrl() {
        return url;
    }

    public String getLocation() {
        return address;
    }

    public int getTypeOfNote() {
        return typeOfNote;
    }
}

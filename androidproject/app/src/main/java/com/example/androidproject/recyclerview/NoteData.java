package com.example.androidproject.recyclerview;

import android.text.PrecomputedText;

import com.example.androidproject.dataBase.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteData {


//    public NoteData(int id, String title, String text, Calendar date,String location, TypeOfNote typeOfNote) {
//        this(id,title,text,date,false,"",location,typeOfNote);
//    }

    public NoteData(int id, String title, String text, Calendar date, boolean isClick, String url, String address, TypeOfNote typeOfNote) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.isClick = isClick;
        this.url = url;
        this.address = address;
        this.typeOfNote = typeOfNote;
    }
    public NoteData(int id, String title, String text,boolean isClick, Calendar date, TypeOfNote typeOfNote) {
        this(id,title,text,date,isClick,"","",typeOfNote);
    }

    public NoteData(int id, String title, String text, Calendar date,String url, TypeOfNote typeOfNote) {
        this(id,title,text,date,false,url,"",typeOfNote);
    }

    public NoteData(int id, String title, String text, Calendar date, TypeOfNote typeOfNote,String email) {
        this(id,title,text,date,false,"",email,typeOfNote);
    }





    public int id;
    public String title;
    public String text;
    public Calendar date;

    public boolean isClick;

    public String url;
    public String address;

    public TypeOfNote typeOfNote;

    public String stringDate()
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(date.getTime());

    }

    public static Calendar convertStringToCalendar(String date) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(f.parse(date));
        return cal;

    }

    public static NoteData noteToNoteData(Note item) throws Exception
    {
        return new NoteData(item.getId(),item.title,item.text,convertStringToCalendar(item.date),item.click,item.url,item.address,TypeOfNote.convertInt(item.typeOfNote));
    }

    @Override
    public String toString() {
        return "NoteData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", isClick=" + isClick +
                ", url='" + url + '\'' +
                ", location='" + address + '\'' +
                ", typeOfNote=" + typeOfNote +
                '}';
    }
}



package com.example.androidproject.fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidproject.App;
import com.example.androidproject.R;
import com.example.androidproject.dataBase.DataBase;
import com.example.androidproject.dataBase.Note;
import com.example.androidproject.helper.InputFilterMinMax;
import com.example.androidproject.helper.MyToasty;
import com.example.androidproject.recyclerview.MyOnClick;
import com.example.androidproject.recyclerview.NoteData;
import com.example.androidproject.recyclerview.Noteadapter;
import com.example.androidproject.recyclerview.TypeOfNote;
import com.example.androidproject.service.NotificationService;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class DayFragment extends Fragment {

    DataBase dataBase;
    public boolean isAdded = false;
    MyToasty myToasty;
    static String tag = "DAY_FRAGMENT";
    String currentDay;
    MotionLayout main;

    EditText minutes, houre;
    TextView dayTitle;
    ImageView reminderCI, linkCI, addressCI,
            addCloseCI;
    TextView typeCI;

    RecyclerView toDoListRecycler;
    Noteadapter noteadapter;
    List<NoteData> noteDataList;



    //create item
    EditText titleInp, textInp,
            hourInp, minInp,
            linkInp,emailInp;

    Button submitInp;

    public DayFragment(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getCurrentDay() {
        return currentDay;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_day, container, false);
        myToasty = new MyToasty(getContext());
        dataBase = DataBase.getDataBase(getContext());
        findView(v);
        setMaxMinTime();
        setButton();
        setRecyclerView();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void setRecyclerView() {
        setNoteData();


    }

    private void setAdapter() {
        MyOnClick action = new MyOnClick() {
            @Override
            public Object[] click(Object... items) {
                final int pos = (int) items[1];
                final NoteData n = noteDataList.get(pos);
                final boolean[] isDone = {false};
                 new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("if you click it means you have done it")
                        .setConfirmText("Yes")
                         .showCancelButton(true)
                         .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                n.isClick = true;
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        dataBase.userDao().updateNot(Note.noteDataToNote(n));
                                        isDone[0] = true;
                                        NotificationService.cancel(getActivity(),n.id);
                                        sDialog.cancel();
                                        new Handler(Looper.getMainLooper()).post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        noteadapter.notifyItemChanged(pos);
                                                    }
                                                });

                                    }
                                });

                            }
                        }).show();




                return new Object[]{isDone[0]};
            }
        };

        MyOnClick delete = new MyOnClick() {
            @Override
            public Object[]  click(final Object... items) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to delete it?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                final int pos  = (int) items[0];
                                final Note note = Note.noteDataToNote(noteDataList.get(pos));
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        dataBase.userDao().delete(note);
                                        NotificationService.cancel(getActivity(),note.getId());
                                        noteDataList.remove(pos);
                                        sDialog.cancel();
                                        new Handler(Looper.getMainLooper()).post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        noteadapter.notifyItemRemoved(pos);
                                                    }
                                                });

                                    }
                                });

                            }
                        })
                        .setCancelText("No!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();

                return new  Object[1];
            }
        };
        noteadapter = new Noteadapter(getContext(), noteDataList, action, delete);
    }

    private void setNoteData() {
        noteDataList = new ArrayList<>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                List<Note> notes = dataBase.userDao().getAll();
                for(Note i : notes)
                {
                    if (isValidNote(i))
                    {
                        try {

                            noteDataList.add(NoteData.noteToNoteData(i));

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
                setAdapter();
                new Handler(Looper.getMainLooper()).post(
                        new Runnable() {
                            @Override
                            public void run() {
                                toDoListRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                toDoListRecycler.setAdapter(noteadapter);
                            }
                        }
                );


            }
        });
    }

    private void setButton() {
        linkCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeItem(Integer.parseInt(view.getTag().toString()));
            }
        });

        reminderCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeItem(Integer.parseInt(view.getTag().toString()));
            }
        });

        addressCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeItem(Integer.parseInt(view.getTag().toString()));
            }
        });


        addCloseCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDayOpenClose();
            }
        });

        submitInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DayFragment_addNewToDo(view);
            }
        });

    }

    private int iconIcTag;

    private void changeItem(int itemPosition) {
        if (iconIcTag == itemPosition)
            return;
        int start;
        if (Integer.parseInt(reminderCI.getTag().toString()) == iconIcTag) {
            start = R.id.reminder;
        }else if(Integer.parseInt(addressCI.getTag().toString()) == iconIcTag)
        {
            start = R.id.email;
        }else {
            start = R.id.link;
        }
        iconIcTag = itemPosition;
        int end;
        if (Integer.parseInt(reminderCI.getTag().toString()) == iconIcTag) {
            end = R.id.reminder;
            setTypeText("reminder");
        }else if (Integer.parseInt(addressCI.getTag().toString()) == iconIcTag){
            setTypeText("email      ");
            end = R.id.email;
        }
            else {
            end = R.id.link;
            setTypeText("link        ");
        }


        main.setTransition(start, end);
        main.setTransitionDuration(600);
        main.transitionToEnd();


    }

    private void setTypeText(String text) {
        typeCI.setText(text);
    }

    private boolean isAddDayOpen = false;

    private void addDayOpenClose() {

        addDayOpenClose(600);


    }

    private void addDayOpenClose(int time) {

        int item;
        if (Integer.parseInt(reminderCI.getTag().toString()) == iconIcTag) {
            item = R.id.reminder;
        } else if (Integer.parseInt(addressCI.getTag().toString()) == iconIcTag)
        {
            item = R.id.email;
        }
        else {
            item = R.id.link;
        }

        if (isAddDayOpen) {
            main.setTransition(item, R.id.start);
        } else {
            main.setTransition(R.id.start, item);
        }
        main.setTransitionDuration(time);
        main.transitionToEnd();
        isAddDayOpen = !isAddDayOpen;


    }

    private void findView(View v) {
        main = v.findViewById(R.id.dayFragment_main);
        houre = v.findViewById(R.id.dayFragment_addClosetHour);
        minutes = v.findViewById(R.id.dayFragment_addClosetMin);
        reminderCI = v.findViewById(R.id.dayFragment_addCloseReminderIcon);
        iconIcTag = Integer.parseInt(reminderCI.getTag().toString());
        linkCI = v.findViewById(R.id.dayFragment_addCloseLinkIcon);
        addressCI = v.findViewById(R.id.dayFragment_addCloseLocationIcon);
        emailInp =  v.findViewById(R.id.dayFragment_emailAddress);
        typeCI = v.findViewById(R.id.dayFragment_addCloseType);
        addCloseCI = v.findViewById(R.id.dayFragment_addCloseButton);
        toDoListRecycler = v.findViewById(R.id.dayFragment_recyclerView);
        dayTitle = v.findViewById(R.id.dayFragment_dayTitle);
        dayTitle.setText(currentDay);
        linkInp = v.findViewById(R.id.dayFragment_addClosetLink);
        textInp = v.findViewById(R.id.dayFragment_addClosetBody);
        titleInp = v.findViewById(R.id.dayFragment_addClosetTitle);
        hourInp = v.findViewById(R.id.dayFragment_addClosetHour);
        minInp = v.findViewById(R.id.dayFragment_addClosetMin);
        submitInp = v.findViewById(R.id.dayFragment_addClosetSubmtin);
        emailInp  = v.findViewById(R.id.dayFragment_emailAddress);
    }

    private void setMaxMinTime() {
        houre.setFilters(new InputFilter[]{new InputFilterMinMax(0, 23), new InputFilter.LengthFilter(2)});
        minutes.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59), new InputFilter.LengthFilter(2)});
    }

    private void DayFragment_addNewToDo(View view) {
        final String title = titleInp.getText().toString(),
                text = textInp.getText().toString(),
                hour = hourInp.getText().toString(),
                min = minInp.getText().toString(),
                link = linkInp.getText().toString(),
                email = emailInp.getText().toString();

        ArrayList<String> empty = new ArrayList<>();

        if (title.isEmpty())
            empty.add("title");
        if (text.isEmpty())
            empty.add("text");
        if (hour.isEmpty())
            empty.add("hour");
        if (min.isEmpty())
            empty.add("minute");
        if (linkCI.getTag().toString().equals(iconIcTag + "") && link.isEmpty())
            empty.add("link");
        if (addressCI.getTag().toString().equals(iconIcTag + "") && email.isEmpty())
            empty.add("email");

        if (empty.size() != 0) {
            myToasty.error(StringErrorEmpty(empty), true);
            return;
        }
        if (linkCI.getTag().toString().equals(iconIcTag + "") && !Patterns.WEB_URL.matcher(link).matches()) {
            try {
                new URL(link).toURI().getPath();
            } catch (Exception e) {
                myToasty.error("The url is invalid", true);
                return;
            }

        }



        if (addressCI.getTag().toString().equals(iconIcTag + "") && !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            myToasty.error("The email address is invalid", true);
            return;
        }

        final Calendar calendar = setCalender(Integer.parseInt(hour),Integer.parseInt(min));

        final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm EEEE");



        final NoteData noteData;
        if (linkCI.getTag().toString().equals(iconIcTag + "")) {
            noteData = new NoteData(0, title, text, calendar, link, TypeOfNote.REMINDER);
        } else if (addressCI.getTag().toString().equals(iconIcTag + ""))
        {
            noteData = new NoteData(0, title, text, calendar, TypeOfNote.EMAIL,email);
        }
        else
        {
            noteData = new NoteData(0, title, text, false, calendar, TypeOfNote.ACTION);
        }


        new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("do you want to add new one?")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Note note = Note.noteDataToNote(noteData);
                                dataBase.userDao().insertAll(note);
                                List<Note> notes = dataBase.userDao().getAll();
                                Note last = notes.get(notes.size()-1);
                                try {
                                    NotificationService.create(note,getActivity());
                                    System.out.println(isValidNote(last));
                                    if (isValidNote(last))
                                    {

                                        noteDataList.add(NoteData.noteToNoteData(last));
                                        new Handler(Looper.getMainLooper()).post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        noteadapter.notifyItemInserted(noteDataList.size()-1);
                                                    }
                                                }
                                        );


                                    }

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                cleanAddItem();
                                            }
                                        }
                                );
                                sDialog.cancel();


                            }
                        });

                    }
                }).show();

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                Note note = Note.noteDataToNote(noteData);
//                dataBase.userDao().insertAll(note);
//                List<Note> notes = dataBase.userDao().getAll();
//                Note last = notes.get(notes.size()-1);
//                try {
//                    NotificationBroadcastReceiver.create(getContext(),last);
//                    System.out.println(isValidNote(last));
//                    if (isValidNote(last))
//                    {
//
//                        noteDataList.add(NoteData.noteToNoteData(last));
//                        new Handler(Looper.getMainLooper()).post(
//                                new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        noteadapter.notifyItemInserted(noteDataList.size()-1);
//                                    }
//                                }
//                        );
//
//
//                    }
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

        //dataBase.userDao().insertAll(note);
        //sendNotification(getContext(),title,text,link);



    }
    private void cleanAddItem()
    {
        titleInp.setText("");
        textInp.setText("");
        hourInp.setText("");
        minInp.setText("");
        linkInp.setText("");
        emailInp.setText("");
    }
    private Calendar setCalender(int hour, int min) {

        Calendar calendar = Calendar.getInstance();
        int addDay;
        switch (currentDay.toUpperCase()) {
            case "SATURDAY":
                addDay = Calendar.SATURDAY;
                break;
            case "SUNDAY":
                addDay = Calendar.SUNDAY;
                break;
            case "MONDAY":
                addDay = Calendar.MONDAY;
                break;
            case "TUESDAY":
                addDay = Calendar.TUESDAY;
                break;
            case "WEDNESDAY":
                addDay = Calendar.WEDNESDAY;
                break;
            case "THURSDAY":
                addDay = Calendar.THURSDAY;
                break;
            default:
                addDay = Calendar.FRIDAY;
                break;
        }

        if (calendar.get(Calendar.DAY_OF_WEEK) == addDay)
        {


            if (calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE) >= hour*60+min)
                addDay = 7;
            else
                addDay = 0;
        }else
        {

            addDay =  (addDay - calendar.get(Calendar.DAY_OF_WEEK)) %7;
            if (addDay <0)
                addDay+=7;

        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm EEEE");

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.add(Calendar.DATE, addDay);

        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private boolean isValidNote(Note note)
    {
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int addDay;
        switch (currentDay.toUpperCase()) {
            case "SATURDAY":
                addDay = Calendar.SATURDAY;
                break;
            case "SUNDAY":
                addDay = Calendar.SUNDAY;
                break;
            case "MONDAY":
                addDay = Calendar.MONDAY;
                break;
            case "TUESDAY":
                addDay = Calendar.TUESDAY;
                break;
            case "WEDNESDAY":
                addDay = Calendar.WEDNESDAY;
                break;
            case "THURSDAY":
                addDay = Calendar.THURSDAY;
                break;
            default:
                addDay = Calendar.FRIDAY;
                break;
        }




        addDay =  (addDay - begin.get(Calendar.DAY_OF_WEEK)) %7;
        if (addDay <0)
            addDay+=7;

        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.add(Calendar.DATE, addDay);
        begin.set(Calendar.SECOND, 0);

        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.add(Calendar.DATE, addDay);
        end.set(Calendar.SECOND, 59);

        boolean canAdd = false;
        try {
            Calendar time = NoteData.convertStringToCalendar(note.date);
            if (time.compareTo(begin) > 0 && time.compareTo(end) < 0)
                canAdd = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }



        return canAdd;
    }

    private static String StringErrorEmpty(List<String> items) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Plz provide ");
        stringBuilder.append(items.get(0));
        for (int i = 1; i < items.size(); i++) {
            stringBuilder.append(" and ");
            stringBuilder.append(items.get(i));
        }
        return stringBuilder.toString();

    }

    private void sendNotification(Context context, String title, String message,String link)
    {


        if (!link.startsWith("http://") && !link.startsWith("https://"))
            link = "http://" + link;

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

        
        
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


        notificationManager.notify(1, notification);
    }

}
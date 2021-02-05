package com.example.androidproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.androidproject.R;
import com.example.androidproject.fragment.DayFragment;
import com.example.androidproject.recyclerview.DailyAdapter;
import com.example.androidproject.recyclerview.MyOnClick;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    RecyclerView showDays;
    DailyAdapter showDaysAdapter;
    MyOnClick showDaysClick;
    List<String> weekDays = Arrays.asList("SATURDAY","SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY");


    HashMap<String, DayFragment> dayFragmentHashMap;
    DayFragment dayFragmentSelect ;
    FrameLayout fragmentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findView();
        setMyOnClick();
        setHashMap();
        setAdapterShowDay(weekDays.get(getDay()));


    }

    int getDay()
    {
        int day ;
        Calendar cur = Calendar.getInstance();

        switch (cur.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.SATURDAY:
                day = 0;
                break;
            case Calendar.SUNDAY:
                day = 1;
                break;
            case Calendar.MONDAY:
                day = 2;
                break;
            case Calendar.TUESDAY:
                day = 3;
                break;

            case Calendar.WEDNESDAY:
                day = 4;
                break;
            case Calendar.THURSDAY:
                day = 5;
                break;
            default:
                day = 6;

        }
        return day;
    }

  


    private void setFragment(boolean isReplace,String dayName) {
        if (dayFragmentSelect != null && dayFragmentSelect.getCurrentDay().equals(dayName))
            return;


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (dayFragmentSelect != null)
            fragmentTransaction.hide( dayFragmentSelect );
        dayFragmentSelect = dayFragmentHashMap.get(dayName);

        if (isReplace)
        {
            if (dayFragmentSelect.isAdded)
                fragmentTransaction.show( dayFragmentSelect );
            else
                fragmentTransaction.add(fragmentPlace.getId(),dayFragmentSelect);

        }
        else
            fragmentTransaction.add(fragmentPlace.getId(),dayFragmentSelect);
        dayFragmentSelect.isAdded = true;
        fragmentTransaction.commit();
    }




    private void setMyOnClick() {
        showDaysClick = new MyOnClick() {
            @Override
            public Object[] click(Object... items) {
                setFragment(true,(String) items[0]);
                String day = (String) items[0];
                System.out.println(day);
                return new Object[1];
            }
        };
    }

    private void setAdapterShowDay(String day) {

        showDaysAdapter = new DailyAdapter(getApplicationContext(),weekDays,showDaysClick,day);
        showDays.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        showDays.setAdapter(showDaysAdapter);
        setFragment(false,day);

    }

    private void setHashMap() {
        dayFragmentHashMap = new HashMap<>();
        for (String day : weekDays)
        {
            DayFragment today = new DayFragment(day);
            dayFragmentHashMap.put(day,today);
        }
    }



    private void findView() {
        showDays = findViewById(R.id.homeActivity_recyclerview);
        fragmentPlace = findViewById(R.id.homeActivity_fragmentDayPalce);
    }

    public void goToTheSettingFromHome(View view)
    {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    public void fromHomeToMap(View view)
    {
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }


}
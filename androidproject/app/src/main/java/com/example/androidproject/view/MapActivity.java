package com.example.androidproject.view;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;


import com.example.androidproject.R;
import com.example.androidproject.dataBase.DataBase;
import com.example.androidproject.dataBase.MapLocationData;
import com.example.androidproject.dataBase.Note;
import com.example.androidproject.fragment.MapFragment;
import com.example.androidproject.recyclerview.MapAdapter;
import com.example.androidproject.recyclerview.MapData;
import com.example.androidproject.recyclerview.MyOnClick;
import com.example.androidproject.recyclerview.NoteData;
import com.example.androidproject.service.LocationService;
import com.example.androidproject.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MapActivity extends AppCompatActivity {
    com.example.androidproject.fragment.MapFragment mapFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MyOnClick addToDataList;
    DataBase dataBase;

    List<MapData> mapDatas;
    MapAdapter mapAdapter;
    MyOnClick delete;
    RecyclerView mapRecyclerView;
    MyOnClick deleteFromFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        dataBase = DataBase.getDataBase(this);
        mapRecyclerView = findViewById(R.id.MapActivity_recyclerView);
        setData();
        setFragment();
        getDeleteFromFragment();



    }
    private void setFragment()
    {
        addToDataList = new MyOnClick() {
            @Override
            public Object[] click(Object... items) {
                MapLocationData newItem = (MapLocationData) items[0];

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        dataBase.mapDao().insertAll(newItem);
                        List<MapLocationData> maps = dataBase.mapDao().getAll();
                        MapLocationData last = maps.get(maps.size()-1);
                        //
                        //todo
                        //NotificationService.create(note,getActivity());

                        try {
                            mapDatas.add(last.convertToMapData());
                            new Handler(Looper.getMainLooper()).post(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            mapAdapter.notifyItemInserted(mapDatas.size()-1);
                                        }
                                    }
                            );

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });


                return new Object[0];
            }
        };
        mapFragment = new MapFragment(addToDataList);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.MapActivity_fragmentPlace, mapFragment);
        fragmentTransaction.commit();

    }

    private void setAdapter()
    {
        delete = new MyOnClick() {
            @Override
            public Object[] click(Object... items) {
                MapData item = (MapData) items[1];
                int pos =  (int) items[0];

                deleteFromFragment.click(item);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        dataBase.mapDao().delete(item.convertToMapMapLocationData());
                        //todo :
                        //NotificationService.cancel(getActivity(),note.getId())

                        mapDatas.remove(findPosById(item));
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        mapAdapter.notifyItemRemoved(findPosById(item));
                                    }
                                });

                    }
                });



                return new Object[0];
            }
        };
        mapAdapter = new MapAdapter(this,delete,mapDatas);
    }

    private void setData()
    {
        mapDatas = new ArrayList<>();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                List<MapLocationData> mapLocationData = dataBase.mapDao().getAll();
                for(MapLocationData i : mapLocationData)
                {
                    mapDatas.add(i.convertToMapData());


                }
                setAdapter();
                new Handler(Looper.getMainLooper()).post(
                        new Runnable() {
                            @Override
                            public void run() {
                                mapRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                mapRecyclerView.setAdapter(mapAdapter);
                            }
                        }
                );


            }
        });

    }
    public void finishBackToHomeMap(View view)
    {
        finish();
    }

    int findPosById(MapData ii)
    {
        for (int j = 0;j < mapDatas.size();j++)
            if (mapDatas.get(j).id == ii.id )
                return j;
        return 0;
    }

    private void getDeleteFromFragment()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    if (mapFragment != null && mapFragment.getMyDelete()!= null)
                    {
                        deleteFromFragment = mapFragment.getMyDelete();

                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
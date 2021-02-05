package com.example.androidproject.fragment;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.androidproject.R;
import com.example.androidproject.dataBase.DataBase;
import com.example.androidproject.dataBase.MapLocationData;
import com.example.androidproject.dataBase.Note;
import com.example.androidproject.helper.MyToasty;
import com.example.androidproject.map.GPSTracker;
import com.example.androidproject.recyclerview.MapData;
import com.example.androidproject.recyclerview.MyOnClick;
import com.example.androidproject.recyclerview.NoteData;
import com.example.androidproject.service.NotificationService;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;


public class MapFragment extends Fragment {
    MyToasty myToasty;
    MapView mapView;
    DataBase dataBase;
    MyOnClick add;
    MyOnClick delete;

    public MapFragment(MyOnClick add)
    {
        this.add = add;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map, container, false);
        myToasty = new MyToasty(getContext());
        dataBase = DataBase.getDataBase(getContext());
        return setMap(savedInstanceState);
    }

    public MyOnClick getMyDelete()
    {
        return  delete;
    }

    private View setMap(Bundle savedInstanceState) {
        GPSTracker gpsTracker = new GPSTracker(getContext());
        MapboxMapOptions options =  MapboxMapOptions.createFromAttributes(getContext(), null)
                .camera(new CameraPosition.Builder()
                        .target(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()))
                        .zoom(12)
                        .build());;


        final EditText sDialogHelp = new EditText(getContext());



        mapView = new MapView(getContext(), options);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                delete = new MyOnClick() {
                    @Override
                    public Object[] click(Object... items) {
                        MapData deItem  = (MapData)items[0];
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                List < MapLocationData > maps = dataBase.mapDao().getAll();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapboxMap.clear();
                                        for (MapLocationData i : maps) {
                                            if (i.id != deItem.id){
                                                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(i.latitude), Double.parseDouble(i.longitude))).title(i.detail);
                                                mapboxMap.addMarker(markerOptions);
                                            }
                                        }
                                        mapboxMap.addMarker(new MarkerOptions().position(mapboxMap.getCameraPosition().target).title("your location"));

                                    }


                                });
                            }
                        });
                        return new Object[0];
                    }
                };

                AsyncTask.execute(new Runnable() {
                                      @Override
                                      public void run() {
                                          List < MapLocationData > maps = dataBase.mapDao().getAll();
                                          new Handler(Looper.getMainLooper()).post(new Runnable() {
                                              @Override
                                              public void run() {

                                                  for (MapLocationData i : maps) {
                                                      MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(i.latitude), Double.parseDouble(i.longitude))).title(i.detail);
                                                      mapboxMap.addMarker(markerOptions);

                                                  }
                                                  mapboxMap.addMarker(new MarkerOptions().position(mapboxMap.getCameraPosition().target).title("your location"));

                                              }
                                          });
                                      }
                                  });

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {

                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                                .setCustomView(sDialogHelp)
                                .setTitleText("Are you sure?")
                                .setContentText("do you want to add new one?")
                                .setConfirmText("confirm")
                                .showCancelButton(true)
                                .setCancelText("no")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                        removeChild(sDialogHelp);

                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(final SweetAlertDialog sDialog) {


                                        final String input = sDialogHelp.getText().toString();
                                        if (input.isEmpty())
                                        {
                                            myToasty.warning("it's empty",true);
                                        }else
                                        {

                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {

                                                    MapLocationData mapLocationData = convertPoint(point,input);
                                                    System.out.println(mapLocationData);
                                                    add.click(mapLocationData);

                                                    sDialog.cancel();

                                                    new Handler(Looper.getMainLooper()).post(
                                                            new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    mapboxMap.addMarker( new MarkerOptions().position(new LatLng(point.getLatitude(),point.getLongitude())).setTitle(input));
                                                                    sDialogHelp.setText("");
                                                                    removeChild(sDialogHelp);
                                                                }
                                                            }
                                                    );



                                                }
                                            });


                                        }


                                    }
                                });
                        sweetAlertDialog.setCanceledOnTouchOutside(false);
                        sweetAlertDialog.show();


                        return true;
                    }
                });
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                    }
                });
            }
        });




        return mapView;
    }
    public MapLocationData convertPoint( LatLng point,String detail)
    {
        return new MapLocationData(0,String.valueOf( point.getLongitude()),String.valueOf(point.getLatitude()),detail);
    }

    private void removeChild(View view)
    {
        if (view.getParent()!= null)
        {
            ((ViewGroup)view.getParent()).removeView(view);
        }
    }
}
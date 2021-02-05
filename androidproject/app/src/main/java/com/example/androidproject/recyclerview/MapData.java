package com.example.androidproject.recyclerview;

import com.example.androidproject.dataBase.MapLocationData;

public class MapData {

  public int id ;
  public String longitude ;
  public String latitude ;
  public String detail;

    public MapData(int id, String longitude, String latitude, String detail) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.detail = detail;
    }

    public MapLocationData convertToMapMapLocationData()
    {
        return new MapLocationData(id,longitude,latitude,detail);
    }
}

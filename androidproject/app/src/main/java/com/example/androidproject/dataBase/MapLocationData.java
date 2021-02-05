package com.example.androidproject.dataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.androidproject.recyclerview.MapData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@Entity(tableName  = "mapLocationData")

public class MapLocationData {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public int id;


    public MapLocationData(int id, String longitude, String latitude,String detail) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.detail= detail;
    }

    @Override
    public String toString() {
        return "MapLocationData{" +
                "id=" + id +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    public double[] getLongLat()
    {
        return new double[]{Double.parseDouble(longitude),Double.parseDouble(latitude)};
    }

    @SerializedName("longitude")
    @ColumnInfo(name = "longitude")
    public String longitude;

    @SerializedName("latitude")
    @ColumnInfo(name = "latitude")
    public String latitude;

    @SerializedName("detail")
    @ColumnInfo(name = "detail")
    public String detail;

    public MapData convertToMapData()
    {
        return new MapData(id,longitude,latitude,detail);
    }
    public String convertJson()
    {
        return new Gson().toJson(this);
    }
    public static MapLocationData convertJsonToObject(String json)
    {
        return new GsonBuilder().create().fromJson(json,MapLocationData.class);
    }

}

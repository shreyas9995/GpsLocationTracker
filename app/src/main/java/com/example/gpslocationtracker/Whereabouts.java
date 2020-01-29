package com.example.gpslocationtracker;

import java.io.Serializable;

public class Whereabouts implements Serializable {
    int id;
    String latitude;
    String longitude;
    String area;

    public Whereabouts(){

    }
    public Whereabouts(int id,String latitude, String longitude, String  area){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
    }

    public Whereabouts(String latitude, String longitude, String area){
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude(){
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea(){
        return area;
    }

}

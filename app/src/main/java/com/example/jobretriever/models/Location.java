package com.example.jobretriever.models;

import com.google.firebase.firestore.GeoPoint;

@SuppressWarnings("unused")
public class Location extends Entity {
    String name;
    String country;
    GeoPoint geopoint;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
    }
}

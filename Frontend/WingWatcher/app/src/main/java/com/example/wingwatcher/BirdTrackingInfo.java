package com.example.wingwatcher;

public class BirdTrackingInfo {
    private double latitude;
    private double longitude;
    private String date;
    private String time;
    private String birdName;

    public BirdTrackingInfo(double latitude, double longitude, String date, String time, String birdName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.birdName = birdName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImageLink() {
        return birdName;
    }

}


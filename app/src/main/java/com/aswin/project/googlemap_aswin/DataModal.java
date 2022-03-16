package com.aswin.project.googlemap_aswin;


public class DataModal {

    private double latitude;
    private double longitude;
    private String location;
    private String user_name;
    private String status;
    private String message;

    public DataModal(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public DataModal(double latitude, double longitude, String location, String userName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.user_name = userName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String userName) {
        this.user_name = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

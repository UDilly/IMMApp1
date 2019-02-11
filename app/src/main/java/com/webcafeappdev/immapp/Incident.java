package com.webcafeappdev.immapp;

public class Incident {
    private int id;
    private String Name;
    private String Date;
    private String Description;
    private String IncidentClass;
    private String Locations;
    private byte[] imageView;

    public Incident(String Name, String Date,String Description, String IncidentClass, String Locations,  byte[] imageView,int id){
        this.Name= Name;
        this.Date= Date;
        this.Description= Description;
        this.IncidentClass= IncidentClass;
        this.Locations = Locations;
        this.imageView=imageView;
        this.id=id;

    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIncidentClass() {
        return IncidentClass;
    }

    public void setIncidentClass(String incidentClass) {
        IncidentClass = incidentClass;
    }

    public String getLocations() {
        return Locations;
    }

    public void setLocations(String locations) { Locations = locations;}

    public byte[] getImageView() {
        return imageView;
    }

    public void setImageView(byte[] imageView) {
        this.imageView = imageView;
    }
}


package com.example.curatorsttit.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {
    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Date")
    private Date date;

    @SerializedName("Status")
    private String status;

    @SerializedName("Organizer")
    private String organizer;

    @SerializedName("Rezults")
    private String results;

    public Event() {
    }

    public Event(int id, String name, Date date, String status, String organizer, String results) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.organizer = organizer;
        this.results = results;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
